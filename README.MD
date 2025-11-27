# 🚀 Service Base - Template Spring Boot 3 & Java 21

Este projeto serve como um template de *backend* minimalista e robusto, projetado para iniciar rapidamente novos microsserviços usando as melhores práticas modernas do ecossistema Java e Spring.

## 🌟 Tecnologias Principais

| Categoria | Tecnologia | Versão | Observações |
| :--- | :--- | :--- | :--- |
| **Linguagem** | Java | 21+ | Uso de Records e sintaxe moderna. |
| **Framework** | Spring Boot | 3.2+ | Foco em aplicações *cloud-native*. |
| **Persistência** | Spring Data JPA / Hibernate | | Abstração e gerenciamento de entidades. |
| **Banco de Dados** | PostgreSQL | 16+ | Configuração via Docker para ambiente de desenvolvimento. |
| **Migração DB** | LiquidBase | 4.x | Versionamento automatizado do esquema do banco de dados (DDL/DML). |
| **Utilities** | Lombok | | Redução de *boilerplate* (getters, setters, construtores). |
| **Segurança** | Spring Security | | Base para implementação de segurança e JWT. |

---

## 🛠️ Como Começar

### 1. Pré-Requisitos

Certifique-se de ter os seguintes itens instalados e configurados:

* **Java Development Kit (JDK):** Versão 21 ou superior.
* **Apache Maven:** Para gerenciar dependências e *build*.
* **Docker & Docker Compose:** Para subir o banco de dados local.

### 2. Configuração do Banco de Dados

O ambiente de desenvolvimento utiliza uma instância local do **PostgreSQL** gerenciada pelo Docker Compose.

```bash
# Navegue até a pasta 'docker'
cd docker

# Inicia o container do PostgreSQL em background
docker-compose up -d postgres
```

O banco estará acessível em `localhost:5432` com as credenciais definidas no `docker-compose.yml` e replicadas no `src/main/resources/application.yml`.

### 3. Executando o Serviço

Após o banco de dados estar ativo, você pode iniciar a aplicação Spring Boot usando o Maven:

```bash
# Retorne para o diretório raiz do projeto
cd ..

# Compila o projeto e inicia a aplicação Spring Boot
mvn spring-boot:run
```

O servidor estará disponível em `http://localhost:8080`.

---

## 🗄️ Migrações de Banco de Dados com LiquidBase

O **LiquidBase** gerencia o esquema e garante que o banco de dados esteja sempre na versão correta ao iniciar a aplicação.

* **Arquivo Principal:** `src/main/resources/db/changelog/db.changelog-master.yaml`
* **Changesets:** As alterações iniciais do esquema de autenticação (tabela `tb_user` e *schema* `auth`) estão agrupadas em `db/changelog/initial_schema/`.

**Importante:** Para adicionar novas mudanças, **nunca** altere um arquivo de *changeset* já executado. Crie sempre um **novo arquivo** e inclua-o no `db.changelog-master.yaml`.

## ⚙️ Build

Para gerar o arquivo JAR (para ambientes de homologação ou produção), use o Maven:

```bash
mvn clean install
```

O artefato final (`service-base-*.jar`) será gerado no diretório `target/`.

## 🧪 Testes

### Executando Testes Unitários

(Adicione aqui o comando para rodar os testes unitários, ex: `mvn test`)

```bash
mvn test
```
