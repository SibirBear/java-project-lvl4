### Hexlet tests and linter status:
[![Actions Status](https://github.com/SibirBear/java-project-lvl4/workflows/hexlet-check/badge.svg)](https://github.com/SibirBear/java-project-lvl4/actions)

### Project CI status:
[![Maintainability](https://api.codeclimate.com/v1/badges/6aea2e0f32be451d3c05/maintainability)](https://codeclimate.com/github/SibirBear/java-project-lvl4/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/6aea2e0f32be451d3c05/test_coverage)](https://codeclimate.com/github/SibirBear/java-project-lvl4/test_coverage)

#
### Description
A website based on the Javelin framework. Here the basic principles of building modern websites on the MVC architecture are worked out: working with routing, request handlers and a template engine, interacting with the database via ORM.

### Links to the deployed application:
* [Railway](https://java-project-lvl4-production.up.railway.app/)
* [Heroku](https://seopageanalyzer.herokuapp.com/) _(It may not work after 28th November 2022)_

### Implemented: 
Checking that the page is being added for the first time.
Checking that the entered link is a site (with a protocol, domain)
Each added site displays the date of the last check and the response code.
For each added site, you can run a check that the site is available and see its titles and description.

### Technologies and approach to development:
* Framework: ***Javelin***
* ORM: ***Ebean***
* Web: ***Thymeleaf, Bootstrap***
* Parser: ***Jsoup***
* Tests: ***JUnit 5, Unirest***
* Tests report: ***Jacoco***
* Linter: ***Checkstyle***
* DB: ***H2 (development), PostgreSQL (production)***

Deployment on Railway & Heroku.

### Requirements:
* Java 17
* Gradle 7.xx
* Make

#
### Launching the app:
1. Building project
```Makefile
make build
```
2. Run app
```Makefile
make start
```
_The app will be launch on http://localhost:3000/_

3. Run tests
```Makefile
make test
```

Some screenshots:
![image](https://user-images.githubusercontent.com/62481984/196159386-44d95231-e4fb-42d5-ad4a-2809c39ca06e.png)
#
![image](https://user-images.githubusercontent.com/62481984/196159612-a9ae058d-3dbb-4f9d-ac8c-0dd79d163048.png)
#
![image](https://user-images.githubusercontent.com/62481984/196159692-c4e0c328-6e43-402b-8fce-6675b7ffaff0.png)
#
![image](https://user-images.githubusercontent.com/62481984/196159790-3d2aec29-9826-4d1b-a30b-78b67acea035.png)
#
![image](https://user-images.githubusercontent.com/62481984/196159883-acff6306-cb3e-4595-b9aa-f5e0b5ac4f23.png)

