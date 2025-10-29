# README.md

## Referer

- https://github.com/gracefulife/fastcampus-test-payment-system

## Specification

- SpringBoot 3.3.4
- Gradle
- Java 17

## Initialized Dependencies

- Spring Configure Processor
- Lombok
- Spring Web
- Spring Data JPA
- Thymeleaf
- Spring Data Jdbc
- MySQL Driver

```text
implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
implementation 'org.springframework.boot:spring-boot-starter-web'
compileOnly 'org.projectlombok:lombok'
runtimeOnly 'com.mysql:mysql-connector-j'
annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
annotationProcessor 'org.projectlombok:lombok'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
```

## SQL

- wallet
    ```sql
    CREATE TABLE `payment`.`wallet` (
      `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
      `user_id` INT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    ```