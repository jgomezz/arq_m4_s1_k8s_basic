package pe.edu.tecsup.app.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class K8sController {

    // Leer variables

    @Value("${APP_NAME:default}")
    private String appName;

    @Value("${APP_ENV:default}")
    private String appEnv;

    @Value("${APP_VERSION:default}")
    private String appVersion;

    private boolean estadoSalud = true;

    private int contador = 0;



    @GetMapping("/")
    public String hello() throws UnknownHostException {

        contador++;
        String hostname = InetAddress.getLocalHost().getHostName();

        return String.format("Versión 1 => Pod = %s , Visitas = %d , Nombre = %s, Env=%s, Ver =%s "
                , hostname, contador, appName, appEnv, appVersion);
    }

    /**
     *  Indica si la aplicación está viva o no
     * @return
     */
    @GetMapping("/health/liveness")
    public String liveness() {
        return "OK";
    }


    /**
     *  Indica si la aplicación está lista para recibir tráfico
     * @return
     */
    @GetMapping("/health/readiness")
    public String readiness() {
        if(estadoSalud) {
            return "OK";
        }
        throw  new RuntimeException("No estoy listo");
    }

    /**
     * Cambia el estado de salud del micro
     * @return
     */
    @GetMapping("/health/switch")
    public String switchHealth() {
        estadoSalud = !estadoSalud;
        return "Estado de salud cambiado a: " + (estadoSalud ? "OK" : "NOT OK");
    }

}