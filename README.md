<h1 align="center">RESTFul API Spring boot no Railway</h1>

## :memo: Resumo do projeto
Projeto de uma simples API para deploy da aplicação e do banco PostgreSQL no serviço de cloud da Railway.

## :classical_building: Diagrama de Classes

```mermaid
classDiagram
class User {
-Long id
-String name
-Account account
-Feature[] features
-Card card
-News[] news
}

class Account {
-Long id
-String number
-String agency
-Number balance
-Number limit
 }

class Feature {
-Long id
-String icon
-String description
}

class Card {
-Long id
-String number
-Number limit
}

class News {
-Long id
-String icon
-String description
}

User "1" *-- "1" Account: account
User "1" *-- "1..N" Feature: features
User "1" *-- "1" Card: card
User "1" *-- "N" News: news
```

```mermaid
classDiagram
    class OpenAPIDefinition {
        servers
    }

    class Server {
        url
        description
    }

    class SpringBootApplication

    class Application {
        main(String[] args)
    }

    Application --> SpringBootApplication
    Application --> OpenAPIDefinition
    OpenAPIDefinition --> Server

    class UserRepository~User,UUID~ {
        <<interface>>
        +existsByAccountNumber(String accountNumber) boolean
    }

    class UserService {
        <<interface>>
        +findById(UUID id) Optional~User~
        +create(User userToCreate) Optional~User~
        +findAll() List<User>
    }

    class UserServiceImpl {
        <<Service>>
        - UserRepository repository
        +findById(UUID id) Optional~User~
        +create(User userToCreate) Optional~User~
        +findAll() List<User>
    }

    class UserController {
        <<RestController>>
        +findById(UUID id) Optional~User~
        +create(User userToCreate) Optional~User~
        +findAll() List<User>
    }

    class ResourceExceptionHandler {
        <<ControllerAdvice>>
        +handleException(Exception ex)
    }

    UserController ..> UserService: uses
    UserServiceImpl ..> UserRepository: uses
    UserServiceImpl --|> UserService: uses
```

## :books: Funcionalidades originais do projeto com Spring Boot versão 3
- Endpoint para inserir usuário e conta bancária e um endpoint para buscar usuário por id.

## :rocket: Funcionalidades que implementei como forma de estudo e prática de Spring Boot
- Tratamento de erros via ResourceExceptionHandler;
- Validação dos campos;
- Endpoint para exibir todos os cadastros de usuários (ainda será implementado o page, sort e limit);

## :wrench: Tecnologias utilizadas

* Spring Boot 3
* Spring Data JPA
* SpringDoc OpenAPI WebMVC UI
* Lombok
* Gradle 8
* Spring Boot Start Validation
* PostgreSQL
* Java 17
* Railway
* Spring Security 6
* JWT auth0 4.4

## :clipboard: Executando o projeto

Pré-requisitos:

## :soon: Implementação futura

## :dart: Status do projeto

* Em andamento

## :framed_picture: Screnshoot
![Descrição da imagem](docs/Swagger-2023-10-05%2009-39-52.png)