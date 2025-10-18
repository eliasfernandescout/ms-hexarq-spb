# 📊 ESTRUTURA HEXAGONAL - VISUALIZAÇÃO

## Diagrama da Arquitetura

```
┌─────────────────────────────────────────────────────────────────┐
│                     ADAPTERS DE ENTRADA                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │     HTTP     │  │ Message      │  │   Rest API   │         │
│  │ Controllers  │  │ Broker (in)  │  │              │         │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘         │
└─────────┼──────────────────┼──────────────────┼─────────────────┘
          │                  │                  │
          └──────────────────┼──────────────────┘
                             │
┌────────────────────────────┼─────────────────────────────────────┐
│                     PORTAS DE ENTRADA                            │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  CreateOrderUseCase  │  TrackOrderUseCase  │  Update...│    │
│  └─────────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────────┘
                             │
┌────────────────────────────┼─────────────────────────────────────┐
│                    CAMADA DE APLICAÇÃO                           │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  CreateOrderUseCaseImpl  │  TrackOrderUseCaseImpl  │...│    │
│  └─────────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────────┘
                             │
┌────────────────────────────┼─────────────────────────────────────┐
│                      DOMÍNIO (CORE)                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐             │
│  │  FoodOrder  │  │  OrderItem  │  │ OrderEvent  │             │
│  │   (RICO)    │  │             │  │             │             │
│  └─────────────┘  └─────────────┘  └─────────────┘             │
│                                                                  │
│  Regras de Negócio:                                             │
│  • confirm() → Confirmar pedido                                 │
│  • prepare() → Preparar pedido                                  │
│  • deliver() → Entregar pedido                                  │
│  • cancel() → Cancelar pedido                                   │
│  • addItem() → Adicionar item com validação                     │
│  • recalculateTotal() → Recalcular total                        │
└──────────────────────────────────────────────────────────────────┘
                             │
┌────────────────────────────┼─────────────────────────────────────┐
│                    PORTAS DE SAÍDA                               │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  OrderRepositoryPort  │  EventPublisherPort  │  Notif...│    │
│  └─────────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────────┘
                             │
          ┌──────────────────┼──────────────────┐
          │                  │                  │
┌─────────┴───────┐  ┌───────┴────────┐  ┌─────┴──────────┐
│  ADAPTERS DE SAÍDA                                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐ │
│  │   JPA    │  │  Kafka   │  │   SMTP   │  │  Others  │ │
│  │   SQL    │  │  Broker  │  │   Email  │  │          │ │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘ │
└───────────────────────────────────────────────────────────┘
```

## Fluxo de uma Requisição

```
1. Cliente → HTTP POST /api/orders
            ↓
2. OrderController (Adapter Input REST)
            ↓
3. OrderRestMapper.toDomain() → FoodOrder
            ↓
4. CreateOrderUseCaseImpl (Application)
            ↓
5. FoodOrder.addItem() → Validações de negócio
            ↓
6. OrderRepositoryPort.save() (Output Port)
            ↓
7. OrderRepositoryAdapter (Adapter Output)
            ↓
8. OrderPersistenceMapper.toEntity()
            ↓
9. JpaOrderRepository.save() → H2 Database
            ↓
10. KafkaOrderEventPublisher → Kafka Topic
            ↓
11. NotificationServiceAdapter → Email/SMS
            ↓
12. OrderRestMapper.toResponse() → JSON
            ↓
13. Cliente ← HTTP 201 Created
```

## Dependências entre Camadas

```
┌─────────────────────────────────────────────────┐
│  REGRA: Dependências apontam PARA DENTRO       │
└─────────────────────────────────────────────────┘

Adapters (Input)  →  Ports (Input)  →  Application
                                             ↓
                                         DOMAIN
                                             ↑
Adapters (Output) →  Ports (Output) ←  Application

✅ Domain NÃO conhece nada fora dele
✅ Application conhece apenas Domain
✅ Adapters conhecem Ports e Application
✅ Ports são interfaces (contratos)
```

## Diferença: Input Port vs Output Port

### Input Ports (Casos de Uso)
- **O QUE** a aplicação FAZ
- Implementados pela camada de Aplicação
- Chamados pelos Adapters de Entrada
- Exemplo: CreateOrderUseCase, TrackOrderUseCase

### Output Ports (Dependências Externas)
- **O QUE** a aplicação PRECISA
- Implementados pelos Adapters de Saída
- Chamados pela camada de Aplicação
- Exemplo: OrderRepositoryPort, EventPublisherPort

```
INPUT PORT:  REST Controller → CreateOrderUseCase (interface)
                                       ↓
                               CreateOrderUseCaseImpl (implementação)

OUTPUT PORT: CreateOrderUseCaseImpl → OrderRepositoryPort (interface)
                                              ↓
                                      OrderRepositoryAdapter (implementação)
```

## Por que FoodOrder é Domínio Rico?

### ❌ Modelo Anêmico (Anti-pattern)
```java
class Order {
    private String status;
    // apenas getters/setters
}

// Lógica no Service (fora do domínio)
class OrderService {
    void confirm(Order order) {
        order.setStatus("CONFIRMED");
    }
}
```

### ✅ Modelo Rico (Hexagonal)
```java
class FoodOrder {
    private OrderStatus status;
    
    // Regras de negócio DENTRO do domínio
    public void confirm() {
        validateCanConfirm();
        this.status = OrderStatus.CONFIRMED;
    }
    
    private void validateCanConfirm() {
        if (items.isEmpty()) {
            throw new IllegalStateException("...");
        }
    }
}
```

## Onde Ficam DTO e Mapper?

### DTOs (adapter/input/rest/dto)
- **CreateOrderRequest** → DTO de entrada (REST)
- **OrderResponse** → DTO de saída (REST)
- **OrderEntity** → DTO de persistência (JPA)

### Mappers
- **OrderRestMapper** → Converte DTO ↔ Domain (REST)
- **OrderPersistenceMapper** → Converte Entity ↔ Domain (JPA)

```
REST DTO → OrderRestMapper → FoodOrder (Domain)
                                  ↓
                        OrderPersistenceMapper
                                  ↓
                            OrderEntity (JPA)
```

## Persistência em REST Controller?

**NÃO!** O Controller NÃO deve ter Repository.

```
❌ ERRADO:
OrderController → JpaOrderRepository (acoplamento direto)

✅ CORRETO:
OrderController → CreateOrderUseCase (port) → CreateOrderUseCaseImpl
                                                      ↓
                                              OrderRepositoryPort
                                                      ↓
                                           OrderRepositoryAdapter
                                                      ↓
                                              JpaOrderRepository
```

O Controller chama apenas os UseCases (Ports de Entrada).
Os UseCases usam os Ports de Saída (Repository, Events, etc).

