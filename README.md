# WorkBoard API

Sistema de gerenciamento de tarefas desenvolvido para controle e acompanhamento de atividades entre gestores e funcionários.

## Funcionalidades

* Cadastro de usuários
* Login com autenticação utilizando BCrypt
* Controle de acesso por perfil

  * Gestor
  * Funcionário
* Cadastro de tarefas
* Atualização de tarefas
* Exclusão de tarefas
* Alteração de status
* Histórico de alterações de status
* Consulta de tarefas atrasadas
* Consulta de tarefas por usuário
* Consulta de tarefas por status

## Tecnologias Utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* Lombok
* BCrypt

## Estrutura de Perfis

### Gestor

* Criar tarefas
* Editar tarefas
* Excluir tarefas
* Atribuir tarefas aos funcionários
* Visualizar todas as tarefas

### Funcionário

* Visualizar apenas suas tarefas
* Alterar status das tarefas atribuídas

## Endpoints Principais

### Usuários

GET /usuarios

POST /usuarios

POST /usuarios/login

DELETE /usuarios/{id}

### Tarefas

GET /tarefas

GET /tarefas/{id}

POST /tarefas

PUT /tarefas/{id}

DELETE /tarefas/{id}

PATCH /tarefas/{id}/status

GET /tarefas/usuario/{id}

GET /tarefas/status/{status}

GET /tarefas/atrasadas

GET /tarefas/{id}/historico

## Banco de Dados

MySQL

Configurar o banco de dados no arquivo application.properties.

## Como Executar

1. Clonar o repositório
2. Configurar o banco MySQL
3. Atualizar as credenciais no application.properties
4. Executar a aplicação Spring Boot

## Autor

João Vitor Souza dos Santos
