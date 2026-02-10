# Introducción a Kubernate

### 1.- Activar Kubernetes en Docker Desktop

<img src="images/k8s_enable_local.png" />

- Verificar que el cluster de Kubernetes esté corriendo

```bash
kubectl cluster-info
```

### 2.- Tener una aplicación básica

<img src="images/k8s_basic_app.png" />

### 3.- Compilar la app generando el archivo .jar

```bash
mvn  clean package -DskipTests
```

### 4.- Crear el archivo Dockerfile

```dockerfile
# Dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 5.- Construir la imagen Docker

```bash
docker build -t app-k8s-local:1.0 .
```
