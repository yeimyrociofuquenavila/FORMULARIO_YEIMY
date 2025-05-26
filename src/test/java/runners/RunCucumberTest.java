package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;



@RunWith(Cucumber.class)  // Le indica a JUnit que ejecute esta clase usando Cucumber como el ejecutor de las pruebas.
@CucumberOptions(  // Inicia la configuración de las opciones de Cucumber.
        features = "src/test/resources/features",  // Especifica la ubicación de los archivos ".feature" que contienen los escenarios de prueba.
        glue = {"Pasos", "utilidades"},  // Indica los paquetes donde se encuentran las implementaciones de los pasos (steps) y utilidades necesarias para las pruebas.
        plugin = {"pretty","html:reports/cucumber/index.html"}  // Activa el plugin "pretty" para generar una salida más legible en la consola durante la ejecución de las pruebas.
)
public class RunCucumberTest {
    // Esta clase solo sirve como punto de entrada para ejecutar las pruebas
}