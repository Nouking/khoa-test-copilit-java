# Khoa Spring Boot Skeleton Project

A production-ready Spring Boot skeleton project that serves as a perfect foundation for building Java enterprise applications.

## 🚀 Features

- **Spring Boot 3.2.1** - Latest stable version with Java 17 support
- **Maven** - Dependency management and build tool
- **Spring Security** - Comprehensive security configuration
- **MyBatis** - SQL mapping framework for database operations
- **H2 Database** - In-memory database for development and testing
- **JUnit 5 & Mockito** - Complete testing framework
- **Spring Boot Actuator** - Production-ready monitoring and management
- **Layered Architecture** - Controller, Service, and Data Access layers
- **Security Configuration** - Pre-configured with public endpoints
- **Comprehensive Testing** - Unit tests, integration tests, and MockMvc tests

## 📋 Requirements

- Java 17 or higher
- Maven 3.6 or higher

## 🛠️ Getting Started

### Clone and Run

```bash
git clone <repository-url>
cd khoa-test-copilit-java
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

### Available Endpoints

- **GET /api/user** - Returns a user with random ID (no authentication required)
- **GET /api/actuator/health** - Health check endpoint
- **GET /api/actuator/info** - Application information
- **GET /api/h2-console** - H2 database console (development only)

### Example API Response

```bash
curl http://localhost:8080/api/user
```

Response:
```json
{
  "id": 123456
}
```

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Test Types

- **Unit Tests** - Service layer testing with Mockito
- **Web Layer Tests** - Controller testing with MockMvc
- **Integration Tests** - Full application context testing
- **Security Tests** - Authentication and authorization testing

### Test Coverage

- UserController tests
- UserService tests  
- Security configuration tests
- Application context loading tests

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/example/skeleton/
│   │   ├── KhoaSpringSkeletonApplication.java  # Main application class
│   │   ├── config/
│   │   │   └── SecurityConfig.java              # Security configuration
│   │   ├── controller/
│   │   │   └── UserController.java              # REST controller
│   │   ├── service/
│   │   │   └── UserService.java                 # Business logic
│   │   ├── mapper/
│   │   │   └── UserMapper.java                  # MyBatis mapper
│   │   └── model/
│   │       └── User.java                        # Data model
│   └── resources/
│       ├── application.yml                      # Main configuration
│       └── mapper/                              # MyBatis XML mappings
└── test/
    ├── java/com/example/skeleton/
    │   ├── KhoaSpringSkeletonApplicationTests.java
    │   ├── controller/
    │   │   └── UserControllerTest.java
    │   ├── service/
    │   │   └── UserServiceTest.java
    │   └── integration/
    │       └── UserControllerIntegrationTest.java
    └── resources/
        └── application-test.yml                 # Test configuration
```

## ⚙️ Configuration

### Database Configuration

The project uses H2 in-memory database by default. To switch to a different database:

1. Add the database driver dependency to `pom.xml`
2. Update `application.yml` with your database configuration
3. Configure MyBatis mappings as needed

### Security Configuration

The security is configured to:
- Allow public access to `/api/user` endpoint
- Allow access to H2 console and actuator endpoints
- Require authentication for all other endpoints

To customize security, modify `SecurityConfig.java`.

### Environment Profiles

- **default** - Development profile with H2 database
- **test** - Testing profile with in-memory H2 database

## 📦 Dependencies

### Core Dependencies

- `spring-boot-starter-web` - Web application support
- `spring-boot-starter-security` - Security framework
- `mybatis-spring-boot-starter` - MyBatis integration
- `spring-boot-starter-actuator` - Production monitoring
- `h2` - In-memory database

### Testing Dependencies

- `spring-boot-starter-test` - Testing framework (JUnit 5, Mockito, AssertJ)
- `spring-security-test` - Security testing support
- `mybatis-spring-boot-starter-test` - MyBatis testing support

## 🔧 Build and Package

### Build the Application

```bash
mvn clean compile
```

### Run Tests

```bash
mvn test
```

### Package as JAR

```bash
mvn clean package
```

### Run the Packaged JAR

```bash
java -jar target/khoa-spring-skeleton-1.0.0.jar
```

## 🚀 Production Deployment

Before deploying to production:

1. **Database Configuration**: Replace H2 with a production database
2. **Security**: Configure proper authentication and authorization
3. **Logging**: Adjust logging levels in `application.yml`
4. **Environment Variables**: Use environment-specific configurations
5. **Health Checks**: Utilize actuator endpoints for monitoring

## 🤝 Contributing

This skeleton project is designed to be extended. Common extensions include:

- Adding JWT authentication
- Implementing database migrations with Flyway/Liquibase
- Adding OpenAPI/Swagger documentation
- Implementing caching with Redis
- Adding message queuing with RabbitMQ/Kafka
- Implementing distributed tracing

## 📄 License

This project is intended as a skeleton/template for building Spring Boot applications.

## 🔗 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [MyBatis Documentation](https://mybatis.org/mybatis-3/)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Testing in Spring Boot](https://spring.io/guides/gs/testing-web/)

---

**Happy Coding! 🎉**