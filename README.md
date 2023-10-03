<h1 align="center">RESTFul API Spring boot no Railway</h1>

## :memo: Resumo do projeto

## Diagrama de Classes

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

  User "1" *-- "1" Account : account
  User "1" *-- "1..N" Feature : features
  User "1" *-- "1" Card : card
  User "1" *-- "N" News : news
```

## :books: Funcionalidades originais do projeto com Spring Boot versão 3

## :books: Funcionalidades que eu implementei como forma de estudo e prática de Spring Boot

## :wrench: Tecnologias utilizadas

* Spring Boot Web
* Spring Data JPA
* SpringDoc OpenAPI WebMVC UI
* Java 17 

## :rocket: Executando o projeto

Pré-requisitos:

## :soon: Implementação futura

## :dart: Status do projeto

* Em andamento