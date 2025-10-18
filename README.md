# Microserviço de Atos Cambiais

Microserviço desenvolvido com **Arquitetura Hexagonal** e **Domain Driven Design** para gerenciamento de atos cambiais: **Endosso** e **Aval**.

## 🏗️ Arquitetura

### Estrutura do Projeto

```
src/main/java/com/kraftbrains/mshexarqspb/
├── domain/                          # Camada de Domínio (Core)
│   ├── model/                       # Entidades e Value Objects
│   │   ├── Endosso.java
│   │   ├── Aval.java
│   │   ├── TipoEndosso.java
│   │   ├── StatusEndosso.java
│   │   ├── TipoAval.java
│   │   └── StatusAval.java
│   └── port/                        # Portas (Interfaces)
│       ├── in/                      # Portas de entrada (Use Cases)
│       │   ├── EndossoUseCasePort.java
│       │   └── AvalUseCasePort.java
│       └── out/                     # Portas de saída (Repositórios)
│           ├── EndossoRepositoryPort.java
│           └── AvalRepositoryPort.java
├── application/                     # Camada de Aplicação
│   └── service/                     # Serviços que implementam Use Cases
│       ├── EndossoService.java
│       └── AvalService.java
└── infrastructure/                  # Camada de Infraestrutura
    ├── persistence/                 # Adaptadores de persistência
    │   ├── entity/                  # Entidades JPA
    │   │   ├── EndossoEntity.java
    │   │   └── AvalEntity.java
    │   ├── repository/              # Repositórios JPA
    │   │   ├── EndossoJpaRepository.java
    │   │   └── AvalJpaRepository.java
    │   ├── mapper/                  # Mapeadores Domain <-> Entity
    │   │   ├── EndossoMapper.java
    │   │   └── AvalMapper.java
    │   └── adapter/                 # Implementação das portas
    │       ├── EndossoRepositoryAdapter.java
    │       └── AvalRepositoryAdapter.java
    └── web/                         # Adaptadores Web (REST)
        ├── controller/              # Controllers REST
        │   ├── EndossoController.java
        │   └── AvalController.java
        ├── dto/                     # DTOs de Request/Response
        │   ├── EndossoRequestDTO.java
        │   ├── EndossoResponseDTO.java
        │   ├── AvalRequestDTO.java
        │   └── AvalResponseDTO.java
        ├── mapper/                  # Mapeadores DTO <-> Domain
        │   ├── EndossoDTOMapper.java
        │   └── AvalDTOMapper.java
        └── exception/               # Tratamento de exceções
            └── GlobalExceptionHandler.java
```

## 🔑 Conceitos Implementados

### Arquitetura Hexagonal (Ports and Adapters)
- **Domínio isolado**: Lógica de negócio independente de frameworks
- **Portas**: Interfaces que definem contratos
- **Adaptadores**: Implementações concretas das portas

### Domain Driven Design (DDD)
- **Entidades de Domínio**: Endosso e Aval com comportamentos ricos
- **Value Objects**: Enums para tipos e status
- **Validações de Domínio**: Regras de negócio nas entidades
- **Linguagem Ubíqua**: Termos do domínio cambial

## 📋 Funcionalidades

### Endosso (Transferência de Título)
- Criar endosso
- Buscar por ID ou número do título
- Aprovar endosso
- Cancelar endosso
- Listar todos os endossos

**Tipos de Endosso:**
- `EM_BRANCO`: Sem beneficiário especificado
- `EM_PRETO`: Com beneficiário especificado
- `MANDATO`: Para cobrança (procuração)
- `CAUCAO`: Em garantia

### Aval (Garantia de Pagamento)
- Criar aval
- Buscar por ID ou número do título
- Aprovar aval
- Executar aval
- Cancelar aval
- Listar todos os avais

**Tipos de Aval:**
- `TOTAL`: Pelo valor total do título
- `PARCIAL`: Por valor parcial do título

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **Lombok**
- **H2 Database** (em memória)
- **Maven**

## ▶️ Como Executar

```bash
# Compilar o projeto
mvnw clean install

# Executar a aplicação
mvnw spring-boot:run
```

Acesse:
- **API**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:atoscambiaisdb`
  - Username: `sa`
  - Password: (vazio)

## 📡 Endpoints da API

### Endosso

```http
POST   /api/v1/endossos              # Criar endosso
GET    /api/v1/endossos               # Listar todos
GET    /api/v1/endossos/{id}          # Buscar por ID
GET    /api/v1/endossos/titulo/{numero} # Buscar por número do título
PUT    /api/v1/endossos/{id}/aprovar  # Aprovar endosso
PUT    /api/v1/endossos/{id}/cancelar # Cancelar endosso
DELETE /api/v1/endossos/{id}          # Deletar endosso
```

**Exemplo de Request - Criar Endosso:**
```json
{
  "numeroTitulo": "TIT-2024-001",
  "endossante": "João Silva",
  "endossatario": "Maria Santos",
  "tipoEndosso": "EM_PRETO",
  "observacoes": "Transferência de direitos"
}
```

### Aval

```http
POST   /api/v1/avais                  # Criar aval
GET    /api/v1/avais                  # Listar todos
GET    /api/v1/avais/{id}             # Buscar por ID
GET    /api/v1/avais/titulo/{numero}  # Buscar por número do título
PUT    /api/v1/avais/{id}/aprovar     # Aprovar aval
PUT    /api/v1/avais/{id}/executar    # Executar aval
PUT    /api/v1/avais/{id}/cancelar    # Cancelar aval
DELETE /api/v1/avais/{id}             # Deletar aval
```

**Exemplo de Request - Criar Aval:**
```json
{
  "numeroTitulo": "TIT-2024-001",
  "avalista": "Pedro Costa",
  "avalizado": "João Silva",
  "tipoAval": "TOTAL",
  "valorAval": 10000.00,
  "observacoes": "Garantia total do pagamento"
}
```

## 🧪 Testes

Execute os testes com:
```bash
mvnw test
```

## 📝 Notas

- O microserviço usa H2 em memória, os dados são perdidos ao reiniciar
- Para produção, configure um banco de dados persistente (PostgreSQL, MySQL, etc.)
- As validações de domínio garantem a integridade dos dados
- Tratamento global de exceções implementado

