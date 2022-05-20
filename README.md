# FIRESIDE
## Общее описание проекта
Веб-сайт **«FIRESIDE»** предназначен для поиска праздников в определенные дни, а также для отображения рецептов
дня с последующей возможностью добавить их в личные рецепты пользователя.<br>
Среди функций **«FIRESIDE»** можно выделить:
1. Отображение рецептов дня
2. Добавление рецептов дня в личные рецепты пользователя
3. Удаление рецептов пользователя
4. Поиск рецептов пользователя по категориям
5. Отображение праздников стран в определенные дни

---

## Описание зависимостей
В проекте используется сборщик «Maven», все зависимости можно увидеть ниже:<br>
```groovy
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.6.7</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.example</groupId>
	<artifactId>fireside</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>fireside</name>
	<description>Demo project for Spring Boot</description>
	<properties>
		<java.version>11</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>

```
<img src="https://easyjava.ru/wp-content/uploads/2017/10/spring-boot-logo.jpg">
<img src="https://developer.asustor.com/uploadIcons/0020_999_1617260177_postgresql-icon_256.png">
<br><br>
К главным зависимостям, использующимся в проекте можно отнести Spring Boot для Java, упрощающий разработку веб-приложений. Также сюда можно отнести Lombok для расшире
ния функциональности языка Java. Thymeleaf - шаблонизатор, позволяющий связывать HTML5 и XML с веб-приложением уровня Spring MVC. В проекте также используется система управления базами данных PostgreSQL.

---

## Как запустить проект
Для запуска проекта понадобится JAVA с с версией 11 и выше, а также сборщик проектов Maven. В файл pom.xml необходимо перенести зависимости приведенные в листинге выше. Нужные зависимости загрузятся автоматически, после чего вы сможете запустить проект через любую среду разработки. Для этого необходимо найти файл ниже и выполнить инструкцию -> Run:
```java
@SpringBootApplication
public class FiresideApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiresideApplication.class, args);
	}

}
```
Помимо этого запустить проект можно непосредственно с помощью Maven:
```bash
./mvnw spring-boot:run
```
Также если у вас нет Maven, Spring, Postgres, но при этом есть Docker существует возможность запустить проект через него, для этого необходимо склонировать репозиторий к себе, перейти в каталог src/main/docker после чего ввести команду:
<img src="https://www.docker.com/wp-content/uploads/2022/03/vertical-logo-monochromatic.png" width="500px" height="400px">
```docker
docker-compose up
```
