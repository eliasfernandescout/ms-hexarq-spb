# 🔗 RELACIONAMENTOS ENTRE ENTIDADES NA HEXAGONAL

## 🤔 A Pergunta: "E quando uma entidade se relaciona com outras?"

Esta é uma das questões mais importantes e confusas na Hexagonal!

---

## 🎯 A Resposta Direta

**Depende do TIPO de relacionamento:**

1. **Agregação simples** (1 para muitos) → **Dentro da própria entidade**
2. **Relacionamento entre agregados** → **Via Repository (ID)**
3. **Lógica complexa entre entidades** → **Domain Service**

---

## 📚 Tipos de Relacionamentos

### 1️⃣ AGREGAÇÃO SIMPLES (Dentro do Agregado)

**Quando:** Uma entidade "possui" outra (composição/agregação)

**Exemplo:** Order → OrderItems

```java
// ✅ CORRETO: OrderItem está DENTRO de FoodOrder
public class FoodOrder { // ← Aggregate Root
    private String orderId;
    private String customerId;
    private List<OrderItem> items; // ← Agregação!
    private OrderStatus status;
    
    // OrderItems fazem parte do Order
    public void addItem(OrderItem item) {
        validateItem(item);
        this.items.add(item);
        recalculateTotal(); // Lógica dentro do agregado
    }
    
    public void removeItem(String itemId) {
        this.items.removeIf(item -> item.getItemId().equals(itemId));
        recalculateTotal();
    }
}

public class OrderItem { // ← Parte do agregado Order
    private String itemId;
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    
    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
```

**Por que não é Domain Service?**
- OrderItem SÓ existe dentro de um Order
- Não faz sentido OrderItem existir sozinho
- É parte do mesmo Agregado (Aggregate Root)

**Persistência:**
```java
// JPA - Relacionamento direto
@Entity
class OrderEntity {
    @Id
    private String orderId;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItemEntity> items; // ✅ Salvos juntos!
}
```

---

### 2️⃣ RELACIONAMENTO ENTRE AGREGADOS (Via ID)

**Quando:** Entidades de agregados diferentes se relacionam

**Exemplo:** Order → Customer (agregados separados)

```java
// ✅ CORRETO: Order NÃO possui Customer completo, apenas o ID
public class FoodOrder { // ← Aggregate Root 1
    private String orderId;
    private String customerId; // ← Apenas o ID! Não o objeto inteiro
    private String customerName; // ← Dados desnormalizados (opcional)
    private List<OrderItem> items;
    
    // Order não precisa conhecer TODOS os dados do Customer
}

public class Customer { // ← Aggregate Root 2 (separado!)
    private String customerId;
    private String name;
    private String email;
    private CustomerType type; // VIP, PREMIUM, REGULAR
    private List<Address> addresses;
}
```

**❌ ERRADO - Não faça isso:**
```java
// ❌ ERRADO: Order com Customer completo
public class FoodOrder {
    private String orderId;
    private Customer customer; // ❌ Acoplamento forte!
    
    // Problema: Agora Order depende de TUDO do Customer
    // Se Customer mudar, Order também muda
}
```

**✅ CORRETO - Use apenas ID:**
```java
// ✅ CORRETO: Order referencia Customer por ID
public class FoodOrder {
    private String orderId;
    private String customerId; // ✅ Apenas ID
    private String customerName; // ✅ Cache/denormalização
}
```

---

### 3️⃣ LÓGICA COMPLEXA ENTRE AGREGADOS → DOMAIN SERVICE

**Quando:** Precisa de dados de ambos os agregados para uma operação

**Exemplo:** Calcular desconto baseado em Order + Customer

```java
// DOMAIN SERVICE - Orquestra lógica entre agregados
@Component
public class OrderPricingService { // ← Domain Service
    
    public BigDecimal calculateFinalPrice(FoodOrder order, Customer customer) {
        BigDecimal subtotal = order.getTotalAmount();
        
        // Lógica que PRECISA de Order E Customer
        BigDecimal discount = calculateDiscount(order, customer);
        BigDecimal tax = calculateTax(order, customer);
        
        return subtotal.subtract(discount).add(tax);
    }
    
    private BigDecimal calculateDiscount(FoodOrder order, Customer customer) {
        // Lógica complexa envolvendo ambos
        if (customer.getType() == CustomerType.VIP) {
            return order.getTotalAmount().multiply(new BigDecimal("0.15"));
        }
        
        if (customer.getType() == CustomerType.PREMIUM && 
            order.getTotalAmount().compareTo(new BigDecimal("100.00")) > 0) {
            return order.getTotalAmount().multiply(new BigDecimal("0.10"));
        }
        
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateTax(FoodOrder order, Customer customer) {
        // Imposto baseado no endereço do cliente
        Address address = customer.getMainAddress();
        if (address.getState().equals("SP")) {
            return order.getTotalAmount().multiply(new BigDecimal("0.18"));
        }
        return order.getTotalAmount().multiply(new BigDecimal("0.12"));
    }
}

// APPLICATION LAYER - UseCase usa o Domain Service
@Service
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {
    private final OrderRepositoryPort orderRepository;
    private final CustomerRepositoryPort customerRepository; // ← Outro repositório!
    private final OrderPricingService pricingService; // ← Domain Service
    
    @Override
    public FoodOrder createOrder(CreateOrderRequest request) {
        // 1. Buscar Customer (agregado separado)
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new CustomerNotFoundException());
        
        // 2. Criar Order (outro agregado)
        FoodOrder order = new FoodOrder(
            customer.getCustomerId(),
            customer.getName(),
            request.getDeliveryAddress()
        );
        
        request.getItems().forEach(order::addItem);
        
        // 3. Calcular preço final usando Domain Service
        BigDecimal finalPrice = pricingService.calculateFinalPrice(order, customer);
        order.setTotalAmount(finalPrice);
        
        // 4. Salvar Order
        return orderRepository.save(order);
    }
}
```

---

## 🎯 Decisão: Onde Colocar a Lógica?

### Fluxograma

```
Relacionamento entre entidades?
    ↓
┌─────────────────────────────────────────┐
│ Uma entidade "possui" a outra?          │
│ (Order → OrderItems)                    │
└─────────────────────────────────────────┘
    │YES                          │NO
    ↓                             ↓
┌──────────────────┐    ┌─────────────────────────┐
│ AGREGAÇÃO        │    │ São agregados separados?│
│ (Dentro da       │    │ (Order ↔ Customer)      │
│  entidade)       │    └─────────────────────────┘
└──────────────────┘        │YES              │NO
                            ↓                 ↓
                  ┌─────────────────┐  ┌──────────────┐
                  │ Usa apenas ID?  │  │ Reveja design│
                  │                 │  │ (pode estar  │
                  │ - Order tem     │  │  errado)     │
                  │   customerId    │  └──────────────┘
                  │                 │
                  │ + Domain Service│
                  │   se precisar   │
                  │   lógica entre  │
                  │   eles          │
                  └─────────────────┘
```

---

## 📝 Exemplos Práticos Completos

### Exemplo 1: Order com Items (Agregação)

```java
// ✅ CORRETO: Agregado completo
public class FoodOrder {
    private String orderId;
    private List<OrderItem> items = new ArrayList<>(); // ← Dentro!
    
    public void addItem(String productId, String name, int qty, BigDecimal price) {
        OrderItem item = new OrderItem(productId, name, qty, price);
        items.add(item);
        recalculateTotal(); // ✅ Lógica dentro do agregado
    }
    
    private void recalculateTotal() {
        this.totalAmount = items.stream()
            .map(OrderItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

public class OrderItem {
    private String productId;
    private Integer quantity;
    private BigDecimal price;
    
    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}

// UseCase - Simples, tudo dentro do agregado
class CreateOrderUseCaseImpl {
    public FoodOrder createOrder(CreateOrderRequest request) {
        FoodOrder order = new FoodOrder(request.getCustomerId());
        
        // Adiciona items (lógica no agregado)
        request.getItems().forEach(itemReq -> 
            order.addItem(
                itemReq.getProductId(),
                itemReq.getName(),
                itemReq.getQuantity(),
                itemReq.getPrice()
            )
        );
        
        return repository.save(order); // Salva tudo junto
    }
}
```

---

### Exemplo 2: Order referenciando Customer (Por ID)

```java
// ✅ CORRETO: Referência por ID
public class FoodOrder {
    private String orderId;
    private String customerId; // ← Apenas ID!
    private String customerName; // ← Cache/denormalização
    private List<OrderItem> items;
}

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private CustomerType type;
}

// UseCase - Busca Customer quando precisa
class CreateOrderUseCaseImpl {
    private final OrderRepositoryPort orderRepository;
    private final CustomerRepositoryPort customerRepository;
    
    public FoodOrder createOrder(String customerId, List<OrderItemRequest> items) {
        // Busca Customer para validação/lógica
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException());
        
        // Cria Order com apenas o ID do Customer
        FoodOrder order = new FoodOrder(
            customer.getCustomerId(), // ✅ Apenas ID
            customer.getName()        // ✅ Cache
        );
        
        items.forEach(item -> order.addItem(item));
        
        return orderRepository.save(order);
    }
}
```

---

### Exemplo 3: Lógica Complexa entre Order e Customer

```java
// DOMAIN SERVICE - Lógica que precisa de ambos
@Component
public class OrderPricingService {
    
    public void applyDiscountsAndTaxes(FoodOrder order, Customer customer) {
        // Desconto baseado no tipo de cliente
        BigDecimal discount = calculateCustomerDiscount(order, customer);
        order.applyDiscount(discount);
        
        // Taxa baseada no endereço
        BigDecimal tax = calculateTax(order, customer.getMainAddress());
        order.applyTax(tax);
    }
    
    private BigDecimal calculateCustomerDiscount(FoodOrder order, Customer customer) {
        return switch (customer.getType()) {
            case VIP -> order.getTotalAmount().multiply(new BigDecimal("0.15"));
            case PREMIUM -> order.getTotalAmount().multiply(new BigDecimal("0.10"));
            case REGULAR -> BigDecimal.ZERO;
        };
    }
    
    private BigDecimal calculateTax(FoodOrder order, Address address) {
        // Taxa varia por estado
        BigDecimal taxRate = getTaxRateForState(address.getState());
        return order.getTotalAmount().multiply(taxRate);
    }
}

// UseCase usa o Domain Service
class CreateOrderUseCaseImpl {
    private final OrderRepositoryPort orderRepository;
    private final CustomerRepositoryPort customerRepository;
    private final OrderPricingService pricingService; // ← Domain Service
    
    public FoodOrder createOrder(CreateOrderRequest request) {
        // Busca Customer
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow();
        
        // Cria Order
        FoodOrder order = new FoodOrder(customer.getCustomerId(), customer.getName());
        request.getItems().forEach(order::addItem);
        
        // ✅ Domain Service calcula descontos e taxas
        pricingService.applyDiscountsAndTaxes(order, customer);
        
        return orderRepository.save(order);
    }
}
```

---

## 🏗️ Estrutura no Projeto

```
domain/
├── model/
│   ├── FoodOrder.java          ← Aggregate Root
│   ├── OrderItem.java          ← Parte do agregado Order
│   ├── Customer.java           ← Aggregate Root separado
│   └── Address.java            ← Parte do agregado Customer
│
└── service/
    ├── OrderPricingService.java    ← Domain Service (Order + Customer)
    └── ShippingService.java        ← Domain Service (Order + Address)

application/
├── port/
│   ├── input/
│   │   └── CreateOrderUseCase.java
│   └── output/
│       ├── OrderRepositoryPort.java      ← Repository do Order
│       └── CustomerRepositoryPort.java   ← Repository do Customer
│
└── service/
    └── CreateOrderUseCaseImpl.java
        ├── Usa OrderRepositoryPort
        ├── Usa CustomerRepositoryPort
        └── Usa OrderPricingService (Domain Service)
```

---

## ✅ Regras de Ouro

### 1. **Agregação (1 para N)** → Dentro da entidade
```java
Order → OrderItems  ✅ (items dentro de Order)
Customer → Addresses ✅ (addresses dentro de Customer)
```

### 2. **Relacionamento entre agregados** → Apenas ID
```java
Order.customerId → Customer  ✅ (apenas ID, não objeto)
Order.productIds → Products  ✅ (lista de IDs)
```

### 3. **Lógica entre agregados** → Domain Service
```java
OrderPricingService(Order, Customer) ✅
ShippingCalculator(Order, Address) ✅
```

---

## 🎓 Checklist de Decisão

Quando encontrar relacionamento entre entidades:

- [ ] **É composição/agregação?** (Item dentro de Order)
  - ✅ Coloque dentro da entidade (Aggregate Root)
  
- [ ] **São agregados separados?** (Order ↔ Customer)
  - ✅ Use apenas ID na referência
  - ✅ Busque via Repository quando precisar
  
- [ ] **Precisa de lógica complexa entre eles?**
  - ✅ Crie Domain Service
  - ✅ Injete no UseCase

---

## 💡 Resumo ULTRA Simples

| Tipo de Relacionamento | Solução | Exemplo |
|------------------------|---------|---------|
| **Agregação** (1→N) | Dentro da entidade | Order → Items |
| **Referência** (N→1) | Apenas ID | Order → customerId |
| **Lógica complexa** | Domain Service | Desconto (Order + Customer) |

---

## 🚨 Armadilhas Comuns

### ❌ ERRO 1: Colocar entidade completa
```java
// ❌ ERRADO
class Order {
    private Customer customer; // ← NÃO!
}
```

### ❌ ERRO 2: Lógica entre agregados na entidade
```java
// ❌ ERRADO
class Order {
    private String customerId;
    
    public BigDecimal calculateDiscount(Customer customer) {
        // ❌ Order não deveria conhecer Customer!
    }
}
```

### ✅ CORRETO: Domain Service
```java
// ✅ CORRETO
class OrderPricingService {
    public BigDecimal calculateDiscount(Order order, Customer customer) {
        // ✅ Service conhece ambos!
    }
}
```

---

**Relacionamentos entre entidades são naturais! Use agregação para composição, IDs para referências, e Domain Services para lógica complexa entre elas.** 🎯

