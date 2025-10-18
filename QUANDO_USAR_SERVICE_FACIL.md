# 🎯 QUANDO USAR "SERVICE" NA HEXAGONAL - EXPLICAÇÃO FÁCIL

## 🚨 Regra de Ouro

**Na Hexagonal, você RARAMENTE usa "Service"!**

O UseCase JÁ faz o trabalho do Service tradicional.

---

## 🎭 As 3 Situações para Usar "Service"

### 1️⃣ Domain Service (Lógica entre entidades)
### 2️⃣ Adapter Service (Integração externa)
### 3️⃣ NUNCA entre UseCase e Repository! ❌

---

## 1️⃣ DOMAIN SERVICE - Quando Usar?

### 🤔 Pergunta: "Essa lógica pertence a qual entidade?"

**Se a resposta for: "A NENHUMA!" → Use Domain Service**

### Exemplo Real: Calcular Desconto

```java
// ❌ RUIM: Lógica no UseCase (muito complexa)
class CreateOrderUseCaseImpl {
    public FoodOrder createOrder(FoodOrder order, Customer customer) {
        // ❌ UseCase ficou cheio de lógica de cálculo!
        BigDecimal discount = BigDecimal.ZERO;
        
        if (customer.isVip()) {
            discount = order.getTotalAmount().multiply(new BigDecimal("0.10"));
        } else if (customer.isPremium()) {
            discount = order.getTotalAmount().multiply(new BigDecimal("0.05"));
        }
        
        if (order.getItems().size() > 10) {
            discount = discount.add(order.getTotalAmount().multiply(new BigDecimal("0.03")));
        }
        
        // ... mais 50 linhas de lógica de desconto
        
        order.setTotalAmount(order.getTotalAmount().subtract(discount));
        return repository.save(order);
    }
}
```

```java
// ✅ BOM: Lógica em Domain Service
// DOMAIN LAYER
@Component
class OrderPricingService { // ← Domain Service
    
    public BigDecimal calculateDiscount(FoodOrder order, Customer customer) {
        BigDecimal discount = BigDecimal.ZERO;
        
        // Toda a lógica complexa está AQUI
        discount = discount.add(calculateCustomerDiscount(customer, order));
        discount = discount.add(calculateVolumeDiscount(order));
        discount = discount.add(calculateSeasonalDiscount(order));
        
        return discount;
    }
    
    private BigDecimal calculateCustomerDiscount(Customer customer, FoodOrder order) {
        if (customer.isVip()) {
            return order.getTotalAmount().multiply(new BigDecimal("0.10"));
        } else if (customer.isPremium()) {
            return order.getTotalAmount().multiply(new BigDecimal("0.05"));
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateVolumeDiscount(FoodOrder order) {
        if (order.getItems().size() > 10) {
            return order.getTotalAmount().multiply(new BigDecimal("0.03"));
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateSeasonalDiscount(FoodOrder order) {
        // Lógica de desconto sazonal
        return BigDecimal.ZERO;
    }
}

// APPLICATION LAYER
class CreateOrderUseCaseImpl {
    private final OrderPricingService pricingService; // ← Injeta Domain Service
    private final OrderRepositoryPort repository;
    
    public FoodOrder createOrder(FoodOrder order, Customer customer) {
        // ✅ UseCase só orquestra!
        BigDecimal discount = pricingService.calculateDiscount(order, customer);
        BigDecimal finalPrice = order.getTotalAmount().subtract(discount);
        order.setTotalAmount(finalPrice);
        
        return repository.save(order);
    }
}
```

### 📌 Quando Criar um Domain Service?

✅ **SIM, crie quando:**
- Lógica envolve **2 ou mais entidades** (Order + Customer)
- Lógica é **muito complexa** (muitas regras de cálculo)
- Não faz sentido colocar em **uma entidade específica**

❌ **NÃO, não crie quando:**
- Lógica pertence a **uma entidade** → Coloque na entidade!
- Lógica é **simples** → Deixe no UseCase!

---

## 2️⃣ ADAPTER SERVICE - Quando Usar?

### 🤔 Pergunta: "Preciso integrar com sistema externo?"

**Se SIM → Use Adapter Service**

### Exemplo Real: Enviar Email

```java
// APPLICATION LAYER - Output Port (Interface)
public interface NotificationServicePort {
    void sendOrderConfirmation(FoodOrder order);
    void sendOrderStatusUpdate(FoodOrder order);
}

// INFRASTRUCTURE LAYER - Adapter Service
@Service
class NotificationServiceAdapter implements NotificationServicePort {
    
    @Autowired
    private SendGridClient sendGridClient; // API externa
    
    @Autowired
    private TwilioClient twilioClient; // API externa
    
    @Override
    public void sendOrderConfirmation(FoodOrder order) {
        // ✅ Integração com SendGrid
        Email email = Email.builder()
            .to(order.getCustomerEmail())
            .subject("Pedido Confirmado!")
            .body("Seu pedido #" + order.getOrderId() + " foi confirmado")
            .build();
        
        sendGridClient.send(email);
        
        // ✅ Integração com Twilio (SMS)
        SMS sms = SMS.builder()
            .to(order.getCustomerPhone())
            .message("Pedido confirmado!")
            .build();
        
        twilioClient.send(sms);
    }
    
    @Override
    public void sendOrderStatusUpdate(FoodOrder order) {
        // Lógica de notificação
    }
}

// APPLICATION LAYER - UseCase
class UpdateOrderUseCaseImpl {
    private final NotificationServicePort notificationService; // ← Port
    
    public FoodOrder confirmOrder(String orderId) {
        FoodOrder order = findOrder(orderId);
        order.confirm(); // Regra de negócio
        
        FoodOrder saved = repository.save(order);
        
        // ✅ Chama o Adapter Service via Port
        notificationService.sendOrderConfirmation(saved);
        
        return saved;
    }
}
```

### 📌 Quando Criar um Adapter Service?

✅ **SIM, crie quando:**
- Precisa integrar com **API externa** (SendGrid, Twilio)
- Precisa acessar **recursos externos** (filesystem, S3)
- Precisa fazer **HTTP calls** para outros sistemas

❌ **NÃO, não crie quando:**
- É lógica de negócio → Domain Service ou Entity!
- É acesso a banco → Use Repository Adapter!

---

## 3️⃣ ❌ NUNCA: Service Entre UseCase e Repository

### ❌ ERRADO
```java
@RestController
class OrderController {
    private final OrderUseCase useCase;
}

class OrderUseCaseImpl implements OrderUseCase {
    private final OrderService service; // ← ❌ INÚTIL!
    
    Order create(Order order) {
        return service.create(order); // Só repassa!
    }
}

class OrderService {
    private final OrderRepository repository;
    
    Order create(Order order) {
        return repository.save(order);
    }
}
```

### ✅ CORRETO
```java
@RestController
class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
}

class CreateOrderUseCaseImpl implements CreateOrderUseCase {
    private final OrderRepositoryPort repository; // ← Direto!
    
    Order create(Order order) {
        return repository.save(order);
    }
}
```

---

## 📊 Resumo Visual

```
┌─────────────────────────────────────────────────────────┐
│                    CONTROLLER                           │
└─────────────────┬───────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────────────────────┐
│                    USECASE                              │
│  (Orquestra tudo - é o "Service" tradicional)           │
│                                                         │
│  Pode usar:                                             │
│    1️⃣ Domain Service (lógica entre entidades)          │
│    2️⃣ Output Ports (interfaces)                        │
└─────────────────┬───────────────────────────────────────┘
                  ↓
      ┌───────────┴───────────┐
      ↓                       ↓
┌─────────────┐       ┌──────────────────┐
│   DOMAIN    │       │     ADAPTERS     │
│   SERVICE   │       │  (implementam    │
│             │       │   Output Ports)  │
│ Opcional!   │       │                  │
└─────────────┘       └──────────────────┘
```

---

## 🎯 Decisão Rápida: "Onde Colocar Essa Lógica?"

### Fluxograma de Decisão

```
Tenho uma lógica...
    ↓
┌─────────────────────────────────────┐
│ Pertence a UMA entidade?            │
│ (ex: validar pedido)                │
└─────────────────────────────────────┘
    │YES                      │NO
    ↓                         ↓
┌─────────────┐     ┌─────────────────────────┐
│  ENTIDADE   │     │ Envolve várias entidades?│
│  (Domain)   │     │ (ex: calcular desconto   │
│             │     │  com Order + Customer)   │
└─────────────┘     └─────────────────────────┘
                        │YES            │NO
                        ↓               ↓
                  ┌─────────────┐  ┌──────────────────┐
                  │   DOMAIN    │  │ É integração     │
                  │   SERVICE   │  │ externa?         │
                  └─────────────┘  │ (email, SMS)     │
                                   └──────────────────┘
                                       │YES      │NO
                                       ↓         ↓
                                  ┌─────────┐  ┌────────┐
                                  │ ADAPTER │  │ USECASE│
                                  │ SERVICE │  │        │
                                  └─────────┘  └────────┘
```

---

## 📝 Exemplos Práticos

### Exemplo 1: Validar Pedido

```java
// ✅ CORRETO: Na Entidade (Domain)
class FoodOrder {
    public void confirm() {
        validateCanConfirm(); // ← Aqui!
        this.status = CONFIRMED;
    }
    
    private void validateCanConfirm() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot confirm empty order");
        }
    }
}
```

### Exemplo 2: Calcular Frete (envolve Order + Endereço)

```java
// ✅ CORRETO: Domain Service
@Component
class ShippingCalculatorService { // ← Domain Service
    public BigDecimal calculateShipping(FoodOrder order, Address address) {
        BigDecimal basePrice = new BigDecimal("10.00");
        
        // Lógica complexa envolvendo Order E Address
        if (order.getTotalAmount().compareTo(new BigDecimal("100.00")) > 0) {
            return BigDecimal.ZERO; // Frete grátis
        }
        
        if (address.isRemoteArea()) {
            return basePrice.multiply(new BigDecimal("2.0"));
        }
        
        return basePrice;
    }
}

// Usado no UseCase
class CreateOrderUseCaseImpl {
    private final ShippingCalculatorService shippingService;
    
    public FoodOrder createOrder(FoodOrder order, Address address) {
        BigDecimal shipping = shippingService.calculateShipping(order, address);
        order.setShippingCost(shipping);
        return repository.save(order);
    }
}
```

### Exemplo 3: Enviar Email

```java
// ✅ CORRETO: Adapter Service
@Service
class EmailServiceAdapter implements EmailServicePort {
    @Autowired
    private JavaMailSender mailSender; // Lib externa
    
    public void send(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        // Configurar e enviar
    }
}
```

---

## ✅ Checklist Final

### Você deve criar um Service quando:

- [ ] Lógica envolve **2+ entidades** → Domain Service
- [ ] Lógica de **cálculo complexo** entre entidades → Domain Service
- [ ] Precisa integrar com **API externa** → Adapter Service
- [ ] Precisa acessar **recursos externos** → Adapter Service

### Você NÃO deve criar Service quando:

- [ ] ❌ Lógica pertence a **uma entidade** → Coloque na entidade!
- [ ] ❌ É apenas **orquestração** → Deixe no UseCase!
- [ ] ❌ Entre **UseCase e Repository** → Acoplamento direto via Port!
- [ ] ❌ "Ah, no MVC tinha Service" → Não é justificativa! UseCase = Service!

---

## 🎓 Resumo ULTRA Simples

| Situação | Solução |
|----------|---------|
| Lógica de **1 entidade** | Coloque na **Entidade** |
| Lógica de **2+ entidades** | Crie **Domain Service** |
| **Integração externa** | Crie **Adapter Service** |
| **Orquestração geral** | Coloque no **UseCase** |
| Entre UseCase e Repository | **NUNCA crie Service!** |

---

## 💡 Regra de Ouro Final

**Na dúvida, NÃO crie Service!**

Comece simples:
1. Lógica na **Entidade**
2. Orquestração no **UseCase**
3. Só crie Service se **REALMENTE** necessário

---

**UseCase já é seu "Service"! Use-o! 🎯**

