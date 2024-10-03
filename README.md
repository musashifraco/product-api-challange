# Product API Challange

Descrição breve do projeto.

## Pré-requisitos

Antes de iniciar o projeto, certifique-se de que você possui os seguintes requisitos instalados:

- **Java**: 17 ou superior
- **Docker**: Certifique-se de que o Docker está instalado em sua máquina.
- **Docker Compose**: O plugin Docker Compose deve estar instalado.

## Inicialização do Projeto

Para executar o projeto, siga os passos abaixo:

1. Navegue até a pasta do Docker Compose:

   ```bash
   cd docker

2. Execute o comando para iniciar os containers:

   ```bash
   docker compose up -d

3. (Opcional) Para visualizar os logs do container, você pode usar:

   ```bash
   docker compose logs -f

4. Para parar os containers, utilize:

   ```bash
   docker compose down

## Endpoints da API

### Obter Produtos

- **Endpoint:** `GET /api/products`
- **Descrição:** Retorna uma lista de produtos cadastrados.

#### Resposta

A resposta será um array de objetos, onde cada objeto representa um produto com as seguintes propriedades:

- **id**: `integer` - Identificador único do produto.
- **description**: `string` - Descrição do produto.
- **barcode**: `string` - Código de barras do produto.
- **unitPrice**: `number` - Preço unitário do produto.
- **unitOfMeasure**: `string` - Unidade de medida do produto.
- **registrationDate**: `string` - Data de registro do produto (formato ISO 8601).

### Obter Produto por ID

- **Endpoint:** `GET /api/products/{id}`
- **Descrição:** Retorna os detalhes de um produto específico, identificado pelo seu ID.

#### Resposta

A resposta será um objeto representando um produto, com as seguintes propriedades:

- **id**: `integer` - Identificador único do produto.
- **description**: `string` - Descrição do produto.
- **barcode**: `string` - Código de barras do produto.
- **unitPrice**: `number` - Preço unitário do produto.
- **unitOfMeasure**: `string` - Unidade de medida do produto.
- **registrationDate**: `string` - Data de registro do produto (formato ISO 8601).
### Deletar Produto

- **Endpoint:** `DELETE /api/products/{id}`
- **Descrição:** Remove um produto específico, identificado pelo seu ID.

#### Resposta

Este endpoint não retorna um corpo na resposta. O resultado da operação é indicado pelo código de status HTTP:

- **204 No Content**: Indica que o produto foi deletado com sucesso.

### Atualizar Produto

- **Endpoint:** `PUT /api/products/{id}`
- **Descrição:** Atualiza os detalhes de um produto específico, identificado pelo seu ID.

#### Corpo da Requisição

O corpo da requisição deve estar no seguinte formato:

   ```json
  {
    "description": "string",
    "barcode": "string",
    "unitPrice": "number",
    "unitOfMeasure": "string"
   }
```

### Verificar Status do Cache

- **Endpoint:** `GET /api/products/cache-status`
- **Descrição:** Retorna o status do cache da aplicação.

#### Resposta

A resposta será uma string informando se o cache está ativo ou não.

#### Exemplo de Resposta

Um exemplo de resposta poderia ser:

- **"Cache Ativo"**: Indica que o cache está em uso.
- **"Cache Inativo"**: Indica que o cache não está em uso.
