# 🌟 Sistema de Gerenciamento de Benefícios Corporativos

> Aplicação Fullstack para controle e gestão de benefícios de funcionários, com arquitetura modular, backend robusto em Spring Boot, frontend moderno em Angular e integração com banco de dados relacional.

---

## 📌 Visão Geral

Este projeto consiste em um sistema completo para **gerenciamento de benefícios corporativos**, incluindo funcionalidades de:

* Cadastro, atualização e remoção de benefícios.
* Transferência de valores entre benefícios.
* Validação de regras de negócio e prevenção de conflitos.
* Interface web moderna e responsiva.

O sistema foi desenvolvido como parte de um **desafio técnico fullstack**, focando em **arquitetura limpa, qualidade de código e confiabilidade transacional**.

---

## 🏗 Arquitetura do Sistema

O projeto segue uma arquitetura em camadas, garantindo separação de responsabilidades e escalabilidade:

1. **Frontend (Angular 17)**

    * SPA com **Standalone Components** e **Angular Material**.
    * Comunicação com backend via **HttpClient** e **RxJS**.
    * Validações de formulário e feedback ao usuário via **SnackBar**.

2. **Backend (Spring Boot + Spring Data JPA)**

    * Exposição de APIs REST para CRUD e transferências.
    * Validações de regras de negócio.
    * Controle de concorrência com **Optimistic Locking** e transações seguras.

3. **EJB Module (Jakarta EE)**

    * Sessões Stateless para operações críticas.
    * Persistência com JPA e controle de transações distribuídas (JTA).
    * Garantia de integridade em operações de transferência simultâneas.

4. **Banco de Dados**

    * **H2** para testes unitários e integração.
    * Suporte a PostgreSQL para produção.
    * Estrutura normalizada de benefícios, usuários e transações.

---

## ⚙️ Funcionalidades Principais

* **CRUD completo de benefícios**: criação, listagem, atualização e exclusão (soft delete).
* **Transferência de valores entre benefícios**: com validação de saldo e regras de negócio.
* **Interface web responsiva**: fácil de usar, com feedback visual imediato.
* **Gerenciamento de temas**: suporte a modo claro e escuro.
* **Testes automatizados**: unitários e de integração, garantindo estabilidade.

---

## 🛠 Tecnologias e Ferramentas

| Camada             | Tecnologias                                                               |
| ------------------ | ------------------------------------------------------------------------- |
| **Frontend**       | Angular 17.3, Angular Material, RxJS 7.8, TypeScript 5.4                  |
| **Backend**        | Java 17, Spring Boot, Spring Data JPA, Hibernate, Maven, JUnit 5, Mockito |
| **EJB Module**     | Jakarta EE 10, EJB Stateless, JPA, JTA, Unit Tests                        |
| **Database**       | H2 (teste)                                      |
| **DevOps / Build** | Maven, npm, Angular CLI                                                   |

---

## 🔄 Fluxo de uma Transferência

1. O usuário envia o formulário de transferência pelo frontend.
2. O **Controller** do backend valida a requisição (DTO).
3. O **Service** aplica regras de negócio, verificando saldo e status dos benefícios.
4. O **Repository / EJB** aplica **Optimistic Lock** para evitar conflitos simultâneos.
5. O banco persiste a transação de forma segura e atômica.
6. O backend retorna a resposta de sucesso ou erro para o frontend.

---

## ✅ Diferenciais Implementados

* **Controle de concorrência**: prevenção de inconsistências durante transferências simultâneas.
* **Testes automatizados**: cobertura de regras críticas do negócio.
* **Frontend moderno**: SPA responsiva, com validação de formulários e feedback visual.
* **Código limpo e modular**: seguindo princípios SOLID e boas práticas.

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos

* Java 17
* Maven 3.8+
* Node.js 20+
* Angular CLI 17+

### Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
ng serve
```

O frontend estará disponível em `http://localhost:4200` e o backend em `http://localhost:8080`.

---

## 🧪 Testes

* Backend: **JUnit 5 + Mockito**

---

## 💡 Observações

* O sistema utiliza **proxy no Angular** para `/api`, facilitando o desenvolvimento local sem configurar CORS.
* Transações críticas no backend são **atomicamente controladas**, garantindo consistência mesmo em cenários concorrentes.
* Tema claro/escuro persistido via **localStorage** no frontend.

