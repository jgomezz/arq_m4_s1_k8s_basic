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
### 6.- Probar la imagen localmente

```bash
docker run -p 8080:8080 app-k8s-local:1.0
```
- Probar en el navegador: http://localhost:8080


## KUBERNETES

### 7.- Desplegar un POD en Kubernetes
- Crear el archivo de configuración del POD ( k8s/deployment-v1.yaml)

```yaml
apiVersion: apps/v1
kind: Deployment  # Tipo de recurso: Deployment 
metadata:
  name: app-k8s
spec:
  replicas: 1                     # Solo 1 pod por ahora
  selector:
    matchLabels:
      app: app-k8s
  template:
    metadata:
      labels:
        app: app-k8s
    spec:
      containers:
        - name: app-k8s
          image: app-k8s-local:1.0
          imagePullPolicy: Never     # Usar imagen local
          ports:
            - containerPort: 8080
```
