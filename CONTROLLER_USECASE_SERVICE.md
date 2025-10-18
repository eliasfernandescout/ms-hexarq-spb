
# 🚨 CONTROLLER → USECASE → SERVICE? NÃO!

## A Pergunta: "Controller chama UseCase e UseCase chama Service?"

### ❌ NÃO! Na Hexagonal, o UseCase JÁ É o Service!

---

## 📚 Comparação: MVC vs Hexagonal

### 🔵 Arquitetura MVC Tradicional
```
┌──────────────┐
│  Controller  │  (Apresentação - recebe HTTP)
└──────┬───────┘
       ↓
┌──────────────┐
│   Service    │  (Lógica de negócio + orquestração)
└──────┬───────┘
       ↓
┌──────────────┐
│  Repository  │  (Acesso a dados)
└──────────────┘
```

**Código MVC:**
```java
@RestController
class OrderController {
    @Autowired
    private OrderService orderService; // ← Service
    
    @PostMapping
    Order create(@RequestBody Order order) {
        return orderService.createOrder(order);
    }
}

@Service
class OrderService {
    @Autowired
    private OrderRepository orderRepository; // ← Repository
    
    Order createOrder(Order order) {
        // Validações
        validateOrder(order);
        
        // Salvar
        Order saved = orderRepository.save(order);
        
        // Publicar evento
        eventPublisher.publish(saved);
        
        return saved;
    }
}
```

---

### 🟢 Arquitetura Hexagonal
```
┌──────────────┐
│  Controller  │  (Adapter de Entrada)
└──────┬───────┘
       ↓
┌──────────────┐
│   UseCase    │  ← ESTE É O "SERVICE"! 
│              │  (Lógica de negócio + orquestração)
└──────┬───────┘
       ↓
┌──────────────┐
│ RepositoryPort│  (Interface - Output Port)
└──────┬───────┘
       ↓
┌──────────────┐
│  Repository  │  (Adapter de Saída)
│   Adapter    │
└──────────────┘
```

**Código Hexagonal:**
```java
@RestController
class OrderController {
    private final CreateOrderUseCase createOrderUseCase; // ← UseCase (não Service!)
    
    @PostMapping
    OrderResponse create(@RequestBody CreateOrderRequest request) {
        FoodOrder domain = mapper.toDomain(request);
        FoodOrder created = createOrderUseCase.createOrder(domain);
        return mapper.toResponse(created);
    }
}

@Service // ← Anotação do Spring, mas é um UseCase!
class CreateOrderUseCaseImpl implements CreateOrderUseCase {
    private final OrderRepositoryPort repository; // ← Port (interface)
    private final OrderEventPublisherPort eventPublisher;
    
    @Override
    public FoodOrder createOrder(FoodOrder order) {
        // Validações (mesma lógica que estava no Service!)
        validateOrder(order);
        
        // Salvar (mesma lógica que estava no Service!)
        FoodOrder saved = repository.save(order);
        
        // Publicar evento (mesma lógica que estava no Service!)
        eventPublisher.publish(createEvent(saved));
        
        return saved;
    }
}
```

---

## 🎯 Resposta Direta

### ❌ ERRADO: Controller → UseCase → Service → Repository
```java
// ❌ Duplicação! Não faça isso!

@RestController
class OrderController {
    private final OrderUseCase orderUseCase;
}

class OrderUseCaseImpl {
    private final OrderService orderService; // ← Camada extra inútil!
    
    Order create(Order order) {
        return orderService.create(order); // Só repassa!
    }
}

class OrderService {
    private final OrderRepository repository;
    
    Order create(Order order) {
        // Lógica aqui
    }
}
```

### ✅ CORRETO: Controller → UseCase → Ports
```java
// ✅ UseCase JÁ orquestra tudo!

@RestController
class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
}

@Service
class CreateOrderUseCaseImpl implements CreateOrderUseCase {
    private final OrderRepositoryPort repository; // ← Interface!
    private final OrderEventPublisherPort eventPublisher;
    private final NotificationServicePort notificationService;
    
    @Override
    public FoodOrder createOrder(FoodOrder order) {
        // ✅ TODA a orquestração está AQUI!
        // (Isso é o que o "Service" faria no MVC)
        
        // 1. Validar
        validateOrder(order);
        
        // 2. Regras de negócio
        order.confirm();
        
        // 3. Persistir
        FoodOrder saved = repository.save(order);
        
        // 4. Publicar evento
        eventPublisher.publishOrderEvent(createEvent(saved));
        
        // 5. Notificar
        notificationService.sendOrderConfirmation(saved);
        
        return saved;
    }
}
```

---

## 📊 Equivalência

| MVC Tradicional | Hexagonal | Função |
|----------------|-----------|--------|
| Controller | Controller (Adapter Input) | Recebe requisição |
| **Service** | **UseCase** | **Orquestração + Lógica** |
| Repository | Repository Adapter | Acesso a dados |

**UseCase = Service!** Não precisa dos dois!

---

## 💡 Quando Usar "Service" na Hexagonal?

### 1️⃣ Domain Services (Opcional)
Quando a lógica **não pertence a uma única entidade**:

```java
// DOMAIN LAYER
class OrderPricingService { // ← Domain Service
    BigDecimal calculateDiscount(FoodOrder order, Customer customer) {
        // Lógica complexa entre Order E Customer
    }
}

// USADO DENTRO do UseCase:
class CreateOrderUseCaseImpl {
    private final OrderPricingService pricingService; // ← Domain Service
    private final OrderRepositoryPort repository; // ← Output Port
    
    public FoodOrder createOrder(FoodOrder order, Customer customer) {
        // UseCase orquestra, Domain Service calcula
        BigDecimal price = pricingService.calculateDiscount(order, customer);
        order.setTotalAmount(price);
        
        return repository.save(order);
    }
}
```

### 2️⃣ Adapter Services (Infraestrutura)
Implementação de Output Ports:

```java
// INFRASTRUCTURE LAYER
@Service
class NotificationServiceAdapter implements NotificationServicePort {
    // Integração com SendGrid
}

// USADO pelo UseCase:
class CreateOrderUseCaseImpl {
    private final NotificationServicePort notificationService; // ← Port
    
    public FoodOrder createOrder(FoodOrder order) {
        FoodOrder saved = repository.save(order);
        notificationService.sendEmail(saved); // ← Chama o Adapter
        return saved;
    }
}
```

---

## 🏗️ Fluxo Completo

### MVC (Controller → Service → Repository)
```
HTTP Request
    ↓
OrderController
    ↓
OrderService ← ORQUESTRA TUDO
    ↓
OrderRepository
    ↓
Database
```

### Hexagonal (Controller → UseCase → Adapters)
```
HTTP Request
    ↓
OrderController (Adapter Input)
    ↓
CreateOrderUseCase ← ORQUESTRA TUDO
    ↓
OrderRepositoryPort (interface)
    ↓
OrderRepositoryAdapter (Adapter Output)
    ↓
Database
```

**Viu? O UseCase faz o mesmo papel do Service!**

---

## ✅ Resumo Final

### Sua Pergunta:
> "Controller chama UseCase e UseCase chama Service?"

### Resposta:
**NÃO!** Na Hexagonal:

1. Controller chama **UseCase**
2. UseCase **JÁ É** o Service (orquestra tudo)
3. UseCase usa **Ports** (interfaces) para acessar infraestrutura
4. Adapters implementam os Ports

### Estrutura Correta:
```
Controller → UseCase (orquestra) → Ports → Adapters
```

### NÃO faça:
```
Controller → UseCase → Service → Ports → Adapters
                      ↑
                   Camada extra inútil!
```

---

## 🎓 Por Que Essa Confusão Acontece?

Porque muitos projetos **misturam** MVC com Hexagonal:

```java
// ❌ Mistura confusa
@RestController
class OrderController {
    @Autowired
    private OrderService orderService; // ← Nome de MVC
    
    @PostMapping
    Order create(@RequestBody OrderDTO dto) {
        return orderService.createOrder(dto); // ← Usa como se fosse Hexagonal
    }
}
```

**Escolha UMA arquitetura:**
- **MVC:** Controller → Service → Repository
- **Hexagonal:** Controller → UseCase → Ports → Adapters

**Não misture os dois!**

---

## 📌 Conclusão

Na Arquitetura Hexagonal:
- ✅ Controller chama UseCase
- ✅ UseCase orquestra TUDO (é o "Service")
- ✅ UseCase usa Ports (interfaces)
- ✅ Adapters implementam Ports
- ❌ NÃO precisa de camada "Service" entre UseCase e Repository

**UseCase na Hexagonal = Service no MVC!**

