<h1 align="center">RESTFul API Spring boot no serviço de hospedagem Railway</h1>

## :memo: Resumo do projeto
Projeto de uma simples API para deploy da aplicação e do banco PostgreSQL no serviço de cloud da Railway. Para criar esse projeto como prática não precisei de cartão de crédito ou assinar qualquer plano, o serviço fornece 5 doláres para usufruirmos dos recursos como laboratório.

## :classical_building: Diagrama de Classes

```mermaid
classDiagram
class User {
-UUID id
-String login
-String password
-String name
-Account account
-Feature[] features
-Card card
-News[] news
-Integer role
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

```mermaid
classDiagram
    class AuthenticationService {
        +loadUserByUsername(username: String): UserDetails
    }

    class Configurations {
        -unSecuredPaths: String[]
        -exceptionResolver: HandlerExceptionResolver
        +filter(): FilterToken
        +mvc(introspector: HandlerMappingIntrospector): MvcRequestMatcher.Builder
        +getAntPathRequestMatchers(): AntPathRequestMatcher[]
        +filterChain(http: HttpSecurity, mvc: MvcRequestMatcher.Builder): SecurityFilterChain
        +authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager
        +passwordEncoder(): PasswordEncoder
    }

    class FilterToken {
        -exceptionResolver: HandlerExceptionResolver
        -tokenService: TokenService
        -repository: UserRepository
        +FilterToken(exceptionResolver: HandlerExceptionResolver)
        +doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain)
    }

    class TokenService {
        +generateToken(user: User): String
        +getSubject(token: String): String
    }

    AuthenticationService ..> UserRepository: <<Autowired>>
    Configurations ..> HandlerExceptionResolver: <<Autowired>>
    Configurations ..> FilterToken: <<Autowired>>
    Configurations ..> HandlerMappingIntrospector: <<Autowired>>
    Configurations ..> HttpSecurity
    Configurations ..> MvcRequestMatcher Builder
    Configurations ..> AuthenticationConfiguration: <<Autowired>>
    Configurations ..> PasswordEncoder: <<Lazy>>
    FilterToken ..> HandlerExceptionResolver: <<Autowired>>
    FilterToken ..> TokenService: <<Autowired>>
    FilterToken ..> UserRepository: <<Autowired>>
    TokenService ..> User
```

```mermaid
classDiagram
  class OpenApi30Config {
    - moduleName: String
    - apiVersion: String
    - contactName: String
    - contactUrl: String
    - contactEmail: String
    - apiDescription: String
    --
    + customOpenAPI(): OpenAPI
  }
  class OpenAPI {
    --
    + addSecurityItem(SecurityRequirement): OpenAPI
    + components(Components): OpenAPI
    + info(Info): OpenAPI
  }
  class SecurityRequirement {
    + addList(String): SecurityRequirement
  }
  class Components {
    + addSecuritySchemes(String, SecurityScheme): Components
  }
  class SecurityScheme {
    - name: String
    - type: String
    - scheme: String
    - bearerFormat: String
  }
  class Info {
    - title: String
    - description: String
    - version: String
    - contact: Contact
  }
  class Contact {
    - name: String
    - url: String
    - email: String
  }
```


## :books: Funcionalidades originais do projeto com Spring Boot versão 3
- Endpoint para inserir usuário e conta bancária e um endpoint para buscar usuário por id.

## :rocket: Funcionalidades que implementei como forma de estudo e prática de Spring Framework
- Tratamento de erros via ResourceExceptionHandler;
- Validação dos campos;
- Validação da complexidade da senha por meio de anotação personalizada;
- Endpoint para exibir todos os cadastros de usuários usando page/size/sort e limit retornando DTO;
- JWT - Autenticação e Autorização;
- HandlerExceptionResolver para capturar e personalizar as exceções do Spring Security;
- Testes unitários com Junit e Mokito;
- Controle de acesso validando se a conta do usuário está ativa, bloqueada, expirada ou se a senha está expirada;

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
* JUnit 5
* Mokito

## :clipboard: Executando o projeto

Pré-requisitos:
- Conta no serviço online railway.app
- No railway.app adicionar o serviço de DATABASE com PostgreSQL
- No railway.app adicionar o repositório git do projeto
- Criar as variáveis de ambiente para o PostgreSQL
- Criar a variável de ambiente com nome SPRING_PROFILES_ACTIVE com valor prod
- Se tudo estar correto, o deploy será realizado automaticamente a cada commit realizado na branch main
- Após o deploy o railway irá informar a URL de acesso que pode ser personalizado

## :soon: Implementação futura

## :dart: Status do projeto

* Em andamento

## :framed_picture: Screnshoot
![Descrição da imagem](docs/Swagger-2023-10-21.png)