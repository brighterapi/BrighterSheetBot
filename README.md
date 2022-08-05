# BrighterSheetBot
API which stores Data to Google Sheet API

# Hi, I'm Milind Mehta! 👋


## 🚀 About Me
I'm a Java-Spring boot Microservice developer. who works with banking applications.



## Features

- Stores Data to Google Sheet and no client authentication Needed

## API Reference

#### Get all items

```http
  POST /user/add
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `string` | name |
| `city` | `string` | city |
| `id` | `string` | id |

#### Get item

```http
  POST /user/add/${sheetId}
  Sheet ID which does have persmission to user service account
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `key1` | `String/Integer/Boolean` |  key name can be anything  and value is from given type|

you can pass the any number of key. currently I have configured it at 3 value

## Color Reference

| Color             | Hex                                                                |
| ----------------- | ------------------------------------------------------------------ |
| Google library | ![Google Github](https://github.com/googleapis/google-auth-library-java#application-default-credentials) https://github.com/googleapis/google-auth-library-java#application-default-credentials|


## Tech Stack
**Server:** Java, Maven, Spring boot, Google APIs

## Run Locally

Clone the project

```bash
  git clone https://github.com/brighterapi/BrigherCodeBot7
```

Go to the project directory

```bash
  mvn clean install
```

Start the server

```bash
  mvn spring-boot:run
```

  Milind Mehta
## 🔗 Links
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/milindmehtamca/)
[![twitter](https://img.shields.io/badge/twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/milindmehtamca)

