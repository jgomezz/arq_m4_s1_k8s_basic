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

Desplegar el POD en Kubernetes

```
kubectl apply -f k8s/deployment-v1.yaml
```
Ver los Pods corriendo

```
kubectl get pods
```
Ver los logs del POD

``` 
kubectl logs -f <NOMBRE_DEL_POD>
``` 

### 8.- Exponer el POD con un Service
- Crear el archivo de configuración del Service ( k8s/service-v1.yaml)

``` 
apiVersion: v1
kind: Service
metadata:
  name: lab-service
spec:
  type: NodePort
  selector:
    app: app-k8s                  # Conecta con pods que tengan esta etiqueta
  ports:
    - port: 80                      # Puerto del Service
      targetPort: 8080              # Puerto del contenedor
      nodePort: 30080               # Puerto en localhost
```
Desplegar el Service en Kubernetes
```
kubectl apply -f k8s/service-v1.yaml
```
Ver los Services corriendo
```
kubectl get services
```
Probar en el navegador: http://localhost:30080

## Aumentar la cantidad de replicas

Modificar el Controller

```
package pe.edu.tecsup.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class K8sController {

    private int contador = 0;

    @GetMapping("/")
    public String hello() throws UnknownHostException {

        contador++;
        String hostname = InetAddress.getLocalHost().getHostName();
        
        return String.format("Versión 1 => Pod = %s , Visitas = %s  ", hostname, contador);
    }

}
```
Volver a generar el docker con la nueva versión de la app

```bash
mvn  clean package -DskipTests

docker build -t app-k8s-local:2.0 .

```

Crear un nuevo Deployment modificando el archivo k8s/deployment-v2.yaml

```
spec:
replicas: 3                     # Solo 1 pod por ahora
```

Desplegar el nuevo Deployment

```
kubectl apply -f k8s/deployment-v2.yaml

kubectl get pods

```

Realizar varias peticiones

```
for i in {1..20}; do curl http://localhost:30080; echo ""; done
```
