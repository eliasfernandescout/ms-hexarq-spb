 use# 🎯 EXPLICAÇÃO: UseCase vs Service

## 1️⃣ NotificationServiceAdapter - Para Que Serve?

### O Problema
Sua aplicação precisa enviar notificações (email, SMS, push), mas isso **NÃO é uma regra de negócio** do domínio. É uma **dependência externa**.

### A Solução Hexagonal

```
┌─────────────────────────────────────────────────────────┐
│           APPLICATION (Caso de Uso)                     │
│                                                         │
│  UpdateOrderUseCaseImpl                                 │
│    - confirmOrder()                                     │
│    - deliverOrder()                                     │
│         ↓                                               │
│    precisa notificar cliente (OUTPUT PORT)              │
│         ↓                                               │
│    NotificationServicePort (interface)                  │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│          INFRASTRUCTURE (Adapter)                       │
│                                                         │
│  NotificationServiceAdapter (implementa o Port)         │
│    - sendOrderConfirmation()                            │
│    - sendOrderStatusUpdate()                            │
│         ↓                                               │
│    SendGrid API / Twilio / Firebase                     │
└─────────────────────────────────────────────────────────┘
```

### Exemplo Real

```java
// APPLICATION LAYER
class UpdateOrderUseCaseImpl {
    private final NotificationServicePort notificationService;
    
    public FoodOrder confirmOrder(String orderId) {
        FoodOrder order = findOrder(orderId);
        order.confirm(); // ✅ Regra de negócio no DOMAIN
        
        repository.save(order);
        notificationService.sendOrderConfirmation(order); // ✅ Chama Port
        
        return order;
    }
}

// INFRASTRUCTURE LAYER
@Service
class NotificationServiceAdapter implements NotificationServicePort {
    
    @Autowired
    private SendGridClient sendGridClient; // API externa
    
    @Override
    public void sendOrderConfirmation(FoodOrder order) {
        // Integração com SendGrid
        Email email = Email.builder()
            .to(order.getCustomerEmail())
            .subject("Pedido Confirmado!")
            .body("Seu pedido #" + order.getOrderId() + " foi confirmado")
            .build();
            
        sendGridClient.send(email);
    }
}
```

**Por que não está no Domain?**
- Enviar email NÃO é regra de negócio
- É um EFEITO COLATERAL da confirmação
- Pode ser trocado (SendGrid → AWS SES) sem afetar o domínio

---

## 2️⃣ UseCase vs Service - A Diferença!

### ❌ ERRO COMUM (Arquitetura em Camadas Tradicional)

```java
// ❌ RUIM: Muita confusão de nomenclatura
interface OrderService { }
class OrderServiceImpl implements OrderService { }

interface OrderUseCase { }
class OrderUseCaseImpl implements OrderUseCase { }

// São a MESMA COISA com nomes diferentes! ❌
```

### ✅ ARQUITETURA HEXAGONAL CORRETA

```
┌─────────────────────────────────────────────────────────┐
│                    DOMAIN LAYER                         │
│                                                         │
│  FoodOrder (Entity)                                     │
│    - confirm()                                          │
│    - deliver()                                          │
│                                                         │
│  OrderPricingService (Domain Service) ⭐ OPCIONAL       │
│    - calculateDiscount(FoodOrder)                       │
│    - calculateTaxes(FoodOrder)                          │
│    (Lógica de negócio que não pertence à entidade)     │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│                APPLICATION LAYER                        │
│                                                         │
│  CreateOrderUseCase (Interface - INPUT PORT) ⭐         │
│    - createOrder(FoodOrder)                             │
│                                                         │
│  CreateOrderUseCaseImpl ⭐                              │
│    (Implementação - orquestra o fluxo)                  │
│                                                         │
│  OrderRepositoryPort (Interface - OUTPUT PORT) ⭐       │
│  NotificationServicePort (Interface - OUTPUT PORT) ⭐   │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│              INFRASTRUCTURE LAYER                       │
│                                                         │
│  OrderRepositoryAdapter ⭐                              │
│  NotificationServiceAdapter ⭐                          │
│  (Implementam os OUTPUT PORTS)                          │
└─────────────────────────────────────────────────────────┘
```

### Nomenclatura Correta

| Tipo | Nome | Camada | Função |
|------|------|--------|--------|
| **Interface** | `CreateOrderUseCase` | Application/Port/Input | Define O QUE a app faz |
| **Implementação** | `CreateOrderUseCaseImpl` | Application/Service | COMO a app faz |
| **Interface** | `OrderRepositoryPort` | Application/Port/Output | O que a app PRECISA |
| **Implementação** | `OrderRepositoryAdapter` | Infrastructure | Integração externa |
| **Domain Service** | `OrderPricingService` | Domain | Lógica de negócio complexa |

### Respondendo Sua Pergunta

> "Não deveria ter Interface, UseCase, UseCaseImpl, Service e ServiceImpl?"

**NÃO!** Isso seria duplicação. A estrutura correta é:

```java
// ✅ CORRETO: Application Layer

// INPUT PORT (interface)
public interface CreateOrderUseCase {
    FoodOrder createOrder(FoodOrder order);
}

// IMPLEMENTAÇÃO (não precisa chamar "ServiceImpl")
@Service // Anotação do Spring
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {
    
    private final OrderRepositoryPort repository; // OUTPUT PORT
    private final OrderEventPublisherPort eventPublisher; // OUTPUT PORT
    
    @Override
    public FoodOrder createOrder(FoodOrder order) {
        // Orquestra o fluxo
        validateOrder(order);
        FoodOrder saved = repository.save(order);
        eventPublisher.publishOrderEvent(createEvent(saved));
        return saved;
    }
}
```

**NÃO precisa de:**
- ❌ `OrderService` interface
- ❌ `OrderServiceImpl` classe
- ❌ `CreateOrderService` interface
- ❌ `CreateOrderServiceImpl` classe

**Já temos:**
- ✅ `CreateOrderUseCase` (input port)
- ✅ `CreateOrderUseCaseImpl` (implementação)

---

## 3️⃣ Quando Usar "Service"?

### 📌 1. Domain Services (Opcional)

Quando a lógica de negócio **não pertence a uma única entidade**:

```java
// DOMAIN LAYER
@Component
public class OrderPricingService {
    
    public BigDecimal calculateTotalWithDiscount(FoodOrder order, Customer customer) {
        BigDecimal subtotal = order.getTotalAmount();
        
        // Lógica complexa que envolve Order E Customer
        if (customer.isVip()) {
            return subtotal.multiply(new BigDecimal("0.9")); // 10% desconto
        }
        
        if (order.getItems().size() > 10) {
            return subtotal.multiply(new BigDecimal("0.95")); // 5% desconto
        }
        
        return subtotal;
    }
}

// Usado assim:
class CreateOrderUseCaseImpl {
    private final OrderPricingService pricingService; // Domain Service
    
    public FoodOrder createOrder(FoodOrder order, Customer customer) {
        BigDecimal finalPrice = pricingService.calculateTotalWithDiscount(order, customer);
        order.setTotalAmount(finalPrice);
        // ...
    }
}
```

### 📌 2. Adapter Services (Nossa NotificationServiceAdapter)

```java
// INFRASTRUCTURE LAYER
@Service // Anotação do Spring
public class NotificationServiceAdapter implements NotificationServicePort {
    // Integração com serviços externos
}
```

---

## 4️⃣ Estrutura Final Correta

```
application/
├── port/
│   ├── input/                        # O QUE a aplicação FAZ
│   │   ├── CreateOrderUseCase.java   # ✅ Interface
│   │   ├── TrackOrderUseCase.java    # ✅ Interface
│   │   └── UpdateOrderStatusUseCase.java
│   └── output/                       # O QUE a aplicação PRECISA
│       ├── OrderRepositoryPort.java  # ✅ Interface
│       ├── OrderEventPublisherPort.java
│       └── NotificationServicePort.java
└── service/                          # Implementações dos UseCases
    ├── CreateOrderUseCaseImpl.java   # ✅ Implementa CreateOrderUseCase
    ├── TrackOrderUseCaseImpl.java
    └── UpdateOrderStatusUseCaseImpl.java

domain/
├── model/
│   ├── FoodOrder.java                # ✅ Entity rica
│   └── OrderItem.java
└── service/                          # ⭐ OPCIONAL: Domain Services
    └── OrderPricingService.java      # Lógica de negócio complexa

adapter/
└── output/
    └── service/
        └── NotificationServiceAdapter.java  # ✅ Implementa NotificationServicePort
```

---

## 5️⃣ Resumo

### NotificationServiceAdapter
- **O que é:** Adapter que implementa um Output Port
- **Para que serve:** Integrar com serviços externos (email, SMS, push)
- **Por que existe:** Separar infraestrutura do domínio
- **Pode trocar:** SendGrid → AWS SES sem afetar Application

### UseCase vs Service
- **UseCase** (interface) = Port de Entrada (o que a app faz)
- **UseCaseImpl** (classe) = Implementação na Application Layer
- **Service** (Domain) = Lógica de negócio entre entidades (opcional)
- **Service** (Adapter) = Integração com serviços externos

### Não precisa duplicar!
❌ UseCase + Service + UseCaseImpl + ServiceImpl  
✅ UseCase (interface) + UseCaseImpl (implementação)

---

**Em resumo:** Sua arquitetura está correta! Só ajustar a nomenclatura para evitar confusão entre "Service" do domínio e "Service" como adapter de infraestrutura.

