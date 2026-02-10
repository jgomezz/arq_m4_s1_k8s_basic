# Introducción a Kubernate

### 0.- Activar Kubernetes en Docker Desktop

<img src="images/k8s_enable_local.png" />

- Verificar que el cluster de Kubernetes esté corriendo

```bash
kubectl cluster-info
```

### 1.- Tener una aplicación básica

<img src="images/k8s_basic_app.png" />

### 2.- Compilar la app generando el archivo .jar

```bash
mvn  clean package -DskipTests
```

### 3.- Crear el archivo Dockerfile

```dockerfile
# Dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```