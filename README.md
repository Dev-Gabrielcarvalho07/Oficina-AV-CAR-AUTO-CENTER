# AV CAR AUTO CENTER

## Pre-requisitos

- Java 17+
- PostgreSQL
- Node.js + npm
- Maven 3.8+ opcional, caso queira usar `mvn exec:java`

## Rodar o projeto completo

No PowerShell, execute:

```powershell
powershell -ExecutionPolicy Bypass -File .\start-app.ps1
```

Isso inicializa o banco `avcar`, sobe o back-end em `http://localhost:7000`
e sobe o front-end em `http://127.0.0.1:4200`.

Antes de executar, configure as credenciais do banco:

Copie `src/main/resources/database.properties.example` para
`src/main/resources/database.properties` e preencha `db.url`, `db.user` e
`db.password` com as credenciais do seu PostgreSQL local.

## Configuracao manual do banco

Copie o arquivo de exemplo e preencha com suas credenciais:

```bash
cp src/main/resources/database.properties.example src/main/resources/database.properties
```

No PowerShell:

```powershell
Copy-Item src/main/resources/database.properties.example src/main/resources/database.properties
```

## Compilar

```bash
mvn compile
```

## Executar

```bash
mvn exec:java
```

Se aparecer erro dizendo que `mvn` nao e reconhecido, instale o Maven ou abra
o projeto como Maven pela IDE. As bibliotecas `Javalin`, `Gson` e `PostgreSQL
JDBC` sao baixadas pelo Maven a partir do `pom.xml`.
