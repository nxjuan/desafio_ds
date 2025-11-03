# Agendamento de Transferências — Spring Boot (Java 11) + Vue 3 (TS)

## Visão geral
Sistema para agendar transferências calculando a taxa por faixa de dias e listar os agendamentos.

### Decisões arquiteturais
- **DDD em camadas**: Domain (entidades/VO/políticas), Application (use cases), Infra (web + JPA).
- **Port/Adapter**: `TransferRepository` (port) + adapter JPA.
- **Persistência**: H2 em memória (rápido pro desafio).
- **Front**: Vue 3 + TypeScript + Axios; Vite com **proxy** `/api → :8080` (sem CORS).

### Versões
- **Back**: Java **11** (compila também em 17/21), Spring Boot 2.7.x, Spring Data JPA, H2, Lombok  
- **Front**: Node 18+, Vue 3, Vue Router 4, Vite, TypeScript 5, Axios

---

## Estrutura (resumo)

    repo/
    ├─ api/                         # backend (Spring Boot)
    │  ├─ src/main/java/...         # DDD (domain/application/infra)
    │  └─ src/main/resources/       # application.yaml
    └─ frontend/                    # frontend (Vue 3 + TS)
       └─ src/
          ├─ api/                   # axios client + serviços
          ├─ pages/                 # ScheduleTransfer.vue / TransfersList.vue
          ├─ router/                # rotas
          ├─ utils/                 # helpers (format)
          └─ style.css              # tokens/estilo base

---

## Executar com Docker

> Pré-requisitos: **Docker** e **Docker Compose** instalados.


### Subir os containers
Na **raiz do repositório**:

```bash
docker compose build
docker compose up -d
```

---

## Endpoints (API)

### Agendar transferência
`POST /api/v1/transfers`

    {
      "sourceAccount": "1234567890",
      "destinationAccount": "9999999999",
      "amount": 1000.00,
      "transferDate": "2025-09-01"
    }

**Resposta**

    {
      "id": "a6166995-....",
      "sourceAccount": "1234567890",
      "destinationAccount": "9999999999",
      "amount": 1000.0,
      "fee": 28.0,
      "total": 1028.0,
      "scheduledDate": "2025-09-01",
      "transferDate": "2025-09-01",
      "status": "SCHEDULED"
    }

### Listar transferências
`GET /api/v1/transfers`

    [
      {
        "id": "...",
        "sourceAccount": "1234567890",
        "destinationAccount": "9999999999",
        "amount": 1000.0,
        "fee": 28.0,
        "total": 1028.0,
        "scheduledDate": "2025-09-01",
        "transferDate": "2025-09-01",
        "status": "SCHEDULED"
      }
    ]

---

