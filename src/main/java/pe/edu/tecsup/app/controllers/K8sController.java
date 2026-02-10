package pe.edu.tecsup.app.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class K8sController {

    private boolean estadoSalud = true;

    private int contador = 0;

    // Leer variables

    @Value("${APP_NAME:default}")
    private String appName;

    @Value("${APP_ENV:default}")
    private String appEnv;

    @Value("${APP_VERSION:default}")
    private String appVersion;

    // Value Secret

    @Value("${DB_USER:default}")
    private String dbUser;

    @Value("${DB_PASSWORD:default}")
    private String dbPwd;

    @GetMapping("/")
    public String hello() throws UnknownHostException {

        contador++;
        String hostname = InetAddress.getLocalHost().getHostName();

        return String.format("Versión 1 => Pod = %s , Visitas = %d , Nombre = %s, Env=%s, Ver =%s, UserDB=%s, PassDB=%s"
                , hostname, contador, appName, appEnv, appVersion, dbUser, dbPwd);
    }

    /**
     *  Liveness: Indica si la aplicación está viva o no. Si el endpoint devuelve un estado de éxito (por ejemplo, HTTP 200), Kubernetes considerará que el contenedor está vivo. Si devuelve un error (por ejemplo, HTTP 500), Kubernetes considerará que el contenedor no está vivo y lo reiniciará.
     * @return
     */
    @GetMapping("/health/liveness")
    public String liveness() {
        return "OK";
    }

    /**
     *  Readiness: Indica si la aplicación está lista para recibir tráfico. Si el endpoint devuelve un estado de éxito, Kubernetes considerará que el contenedor está listo y comenzará a enviar tráfico a él. Si devuelve un error, Kubernetes considerará que el contenedor no está listo y no enviará tráfico a él hasta que esté listo.
     * @return
     */
     @GetMapping("/health/readiness")
     public String readiness() {
         if(estadoSalud) {
             return "OK";
         }
         throw  new RuntimeException("No estoy listo");
     }

     @GetMapping("/health/switch")
     public String switchHealth() {
         estadoSalud = !estadoSalud;
         return "Estado de salud cambiado a: " + (estadoSalud ? "OK" : "NOT OK");
     }


}
