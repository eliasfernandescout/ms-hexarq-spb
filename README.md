# MS Hexagonal Architecture - Spring Boot

## 📐 Arquitetura Hexagonal (Ports & Adapters)

Este projeto implementa uma arquitetura hexagonal completa para um sistema de pedidos de comida (Food Order).

## 🏗️ Estrutura do Projeto

```
ms-hexarq-spb/
├── domain/                          # 🟡 DOMÍNIO (Núcleo)
│   ├── model/                       # Entidades de domínio
│   │   ├── FoodOrder.java          # Entidade rica com regras de negócio
│   │   ├── OrderItem.java
│   │   └── OrderStatus.java
│   └── event/                       # Eventos de domínio
│       ├── OrderEvent.java
│       └── OrderEventType.java
│
├── application/                     # 🔴 APLICAÇÃO (Casos de Uso)
│   ├── port/
│   │   ├── input/                  # Portas de Entrada (o que a aplicação faz)
│   │   │   ├── CreateOrderUseCase.java
│   │   │   ├── TrackOrderUseCase.java
│   │   │   └── UpdateOrderStatusUseCase.java
│   │   └── output/                 # Portas de Saída (o que a aplicação precisa)
│   │       ├── OrderRepositoryPort.java
│   │       ├── OrderEventPublisherPort.java
│   │       └── NotificationServicePort.java
│   └── service/                    # Implementação dos casos de uso
│       ├── CreateOrderUseCaseImpl.java
│       ├── TrackOrderUseCaseImpl.java
│       └── UpdateOrderStatusUseCaseImpl.java
│
└── adapter/                        # 🔵 INFRAESTRUTURA (Adapters)
    ├── input/                      # Adapters de Entrada
    │   ├── rest/                   # Controladores REST (HTTP)
    │   │   ├── OrderController.java
    │   │   ├── dto/
    │   │   │   ├── CreateOrderRequest.java
    │   │   │   ├── OrderResponse.java
    │   │   │   ├── OrderItemRequest.java
    │   │   │   └── OrderItemResponse.java
    │   │   └── mapper/
    │   │       └── OrderRestMapper.java
    │   └── messaging/
    │       └── kafka/              # Consumidor Kafka
    │           └── OrderEventKafkaListener.java
    │
    └── output/                     # Adapters de Saída
        ├── persistence/            # Persistência SQL (JPA)
        │   ├── entity/
        │   │   ├── OrderEntity.java
        │   │   └── OrderItemEntity.java
        │   ├── repository/
        │   │   └── JpaOrderRepository.java
        │   ├── mapper/
        │   │   └── OrderPersistenceMapper.java
        │   └── OrderRepositoryAdapter.java
        ├── messaging/
        │   └── kafka/              # Produtor Kafka
        │       └── KafkaOrderEventPublisher.java
        └── service/                # Serviços externos
            └── NotificationServiceAdapter.java
```

## 🎯 Conceitos da Arquitetura Hexagonal

### Camadas

1. **DOMÍNIO (Centro)** 🟡
   - Contém as regras de negócio puras
   - Independente de frameworks e tecnologias
   - Entidades ricas com comportamento

2. **APLICAÇÃO (Casos de Uso)** 🔴
   - Orquestra o fluxo de dados
   - Define portas (interfaces)
   - Implementa casos de uso

3. **INFRAESTRUTURA (Adapters)** 🔵
   - Implementa as portas
   - Conecta com tecnologias externas (DB, HTTP, Kafka)
   - Adaptadores podem ser trocados sem afetar o domínio

### Portas (Ports)

- **Input Ports**: Interfaces que definem O QUE a aplicação faz (casos de uso)
- **Output Ports**: Interfaces que definem O QUE a aplicação precisa (repositórios, serviços)

### Adapters

- **Input Adapters**: Controladores REST, consumidores Kafka (HTTP, Message Broker)
- **Output Adapters**: Repositórios JPA, produtores Kafka, serviços externos (SQL, SMTP)

## 🔄 Fluxo de Dados

```
HTTP Request → REST Controller → UseCase → Domain Logic → Repository Port → JPA Adapter → Database
                    ↓                ↓
              REST Mapper      Event Publisher → Kafka → External Systems
```

## 📋 Endpoints REST

### Criar Pedido
```http
POST /api/orders
Content-Type: application/json

{
  "customerId": "123",
  "customerName": "João Silva",
  "deliveryAddress": "Rua ABC, 123",
  "items": [
    {
      "productId": "p1",
      "productName": "Pizza Margherita",
      "quantity": 2,
      "price": 35.00
    }
  ]
}
```

### Buscar Pedido
```http
GET /api/orders/{orderId}
```

### Atualizar Status
```http
PATCH /api/orders/{orderId}/confirm
PATCH /api/orders/{orderId}/prepare
PATCH /api/orders/{orderId}/ready
PATCH /api/orders/{orderId}/deliver
PATCH /api/orders/{orderId}/cancel
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17
- Maven
- Kafka (opcional, para mensageria)

### Executar a aplicação
```bash
mvnw spring-boot:run
```

### Acessar H2 Console
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:orderdb
- Username: sa
- Password: (deixar vazio)

### Kafka (Opcional)
Se quiser testar com Kafka:
```bash
# Iniciar Zookeeper
zookeeper-server-start config/zookeeper.properties

# Iniciar Kafka
kafka-server-start config/server.properties
```

## 🧪 Testes

### Exemplo de requisição com cURL
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "123",
    "customerName": "João Silva",
    "deliveryAddress": "Rua ABC, 123",
    "items": [
      {
        "productId": "p1",
        "productName": "Pizza",
        "quantity": 2,
        "price": 35.00
      }
    ]
  }'
```

## 🎓 Benefícios da Arquitetura Hexagonal

✅ **Testabilidade**: Fácil mockar portas  
✅ **Manutenibilidade**: Separação clara de responsabilidades  
✅ **Flexibilidade**: Trocar adapters sem afetar domínio  
✅ **Independência**: Domínio não depende de frameworks  
✅ **Escalabilidade**: Adicionar novos adapters facilmente  

## 📚 Tecnologias Utilizadas

- Spring Boot 3.5.4
- Spring Data JPA
- Spring Kafka
- H2 Database
- Jackson
- Maven

