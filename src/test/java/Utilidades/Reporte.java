package Utilidades;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Base64;

/**
 * Clase Reporte: genera reportes visuales con ExtentReports
 * Adaptada para pruebas de formularios
 */
public class Reporte {

    private static ExtentReports extent;  // Instancia de ExtentReports
    private static final String REPORTE_PATH = "reports/ExtentReports/reporte_formulario.html";  // Ruta del reporte

    static {  // Bloque estático que se ejecuta cuando la clase es cargada por primera vez
        // Inicializa el reporter de tipo Spark (HTML moderno)
        ExtentSparkReporter spark = new ExtentSparkReporter(REPORTE_PATH);

        // Configuración del reporte
        spark.config().setDocumentTitle("Reporte de Pruebas - Formulario");
        spark.config().setReportName("Resultados de Pruebas Automatizadas");
        spark.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");

        extent = new ExtentReports();  // Inicializa la instancia de ExtentReports
        extent.attachReporter(spark);  // Asocia el reporter Spark con la instancia de ExtentReports

        // Agregar información del entorno
        extent.setSystemInfo("Sistema Operativo", System.getProperty("os.name"));
        extent.setSystemInfo("Navegador", "Chrome");
        extent.setSystemInfo("Ambiente", "Pruebas");
    }

    /**
     * Crea un nuevo caso de prueba en el reporte
     * @param nombre Nombre del caso de prueba
     * @return Instancia de ExtentTest para el caso de prueba
     */
    public static ExtentTest crearPrueba(String nombre) {
        return extent.createTest(nombre);
    }

    /**
     * Registra un paso informativo en el reporte
     * @param test Caso de prueba al que se asocia el paso
     * @param mensaje Descripción del paso
     * @param driver WebDriver para captura de pantalla (puede ser null)
     */
    public static void logInfo(ExtentTest test, String mensaje, WebDriver driver) {
        String screenshot = (driver != null) ? capturarPantalla(driver) : null;
        logInfo(test, mensaje, screenshot);
    }

    /**
     * Registra un paso informativo en el reporte
     * @param test Caso de prueba al que se asocia el paso
     * @param mensaje Descripción del paso
     * @param screenshotBase64 Captura de pantalla en formato Base64 (puede ser null)
     */
    public static void logInfo(ExtentTest test, String mensaje, String screenshotBase64) {
        if (screenshotBase64 != null) {
            test.info(mensaje,
                    com.aventstack.extentreports.MediaEntityBuilder
                            .createScreenCaptureFromBase64String(screenshotBase64)
                            .build()
            );
        } else {
            test.info(mensaje);
        }
        extent.flush();  // Escribe los cambios en el archivo de reporte
    }

    /**
     * Registra un paso exitoso en el reporte
     * @param test Caso de prueba al que se asocia el paso
     * @param mensaje Descripción del paso exitoso
     * @param driver WebDriver para captura de pantalla (puede ser null)
     */
    public static void logExito(ExtentTest test, String mensaje, WebDriver driver) {
        String screenshot = (driver != null) ? capturarPantalla(driver) : null;
        logExito(test, mensaje, screenshot);
    }

    /**
     * Registra un paso exitoso en el reporte
     * @param test Caso de prueba al que se asocia el paso
     * @param mensaje Descripción del paso exitoso
     * @param screenshotBase64 Captura de pantalla en formato Base64 (puede ser null)
     */
    public static void logExito(ExtentTest test, String mensaje, String screenshotBase64) {
        if (screenshotBase64 != null) {
            test.pass(mensaje,
                    com.aventstack.extentreports.MediaEntityBuilder
                            .createScreenCaptureFromBase64String(screenshotBase64)
                            .build()
            );
        } else {
            test.pass(mensaje);
        }
        extent.flush();  // Escribe los cambios en el archivo de reporte
    }

    /**
     * Registra un paso fallido en el reporte
     * @param test Caso de prueba al que se asocia el paso
     * @param mensaje Descripción del paso fallido
     * @param driver WebDriver para captura de pantalla (puede ser null)
     */
    public static void logFallo(ExtentTest test, String mensaje, WebDriver driver) {
        String screenshot = (driver != null) ? capturarPantalla(driver) : null;
        logFallo(test, mensaje, screenshot);
    }

    /**
     * Registra un paso fallido en el reporte
     * @param test Caso de prueba al que se asocia el paso
     * @param mensaje Descripción del paso fallido
     * @param screenshotBase64 Captura de pantalla en formato Base64 (puede ser null)
     */
    public static void logFallo(ExtentTest test, String mensaje, String screenshotBase64) {
        if (screenshotBase64 != null) {
            test.fail(mensaje,
                    com.aventstack.extentreports.MediaEntityBuilder
                            .createScreenCaptureFromBase64String(screenshotBase64)
                            .build()
            );
        } else {
            test.fail(mensaje);
        }
        extent.flush();  // Escribe los cambios en el archivo de reporte
    }

    /**
     * Registra una advertencia en el reporte
     * @param test Caso de prueba al que se asocia el paso
     * @param mensaje Descripción de la advertencia
     * @param driver WebDriver para captura de pantalla (puede ser null)
     */
    public static void logAdvertencia(ExtentTest test, String mensaje, WebDriver driver) {
        String screenshot = (driver != null) ? capturarPantalla(driver) : null;
        logAdvertencia(test, mensaje, screenshot);
    }

    /**
     * Registra una advertencia en el reporte
     * @param test Caso de prueba al que se asocia el paso
     * @param mensaje Descripción de la advertencia
     * @param screenshotBase64 Captura de pantalla en formato Base64 (puede ser null)
     */
    public static void logAdvertencia(ExtentTest test, String mensaje, String screenshotBase64) {
        if (screenshotBase64 != null) {
            test.warning(mensaje,
                    com.aventstack.extentreports.MediaEntityBuilder
                            .createScreenCaptureFromBase64String(screenshotBase64)
                            .build()
            );
        } else {
            test.warning(mensaje);
        }
        extent.flush();  // Escribe los cambios en el archivo de reporte
    }

    /**
     * Captura una screenshot del estado actual del navegador
     * @param driver WebDriver actual
     * @return String en formato Base64 con la captura de pantalla
     */
    public static String capturarPantalla(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Espera a que la página se cargue completamente
     * @param driver WebDriver actual
     * @param segundos Tiempo máximo de espera en segundos
     */
    public static void esperarCargaPagina(WebDriver driver, int segundos) {
        new WebDriverWait(driver, Duration.ofSeconds(segundos))
                .until(webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Espera a que un elemento sea visible
     * @param driver WebDriver actual
     * @param elemento WebElement a esperar
     * @param segundos Tiempo máximo de espera en segundos
     * @return true si el elemento es visible, false si no
     */
    public static boolean esperarElementoVisible(WebDriver driver, WebElement elemento, int segundos) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(segundos))
                    .until(ExpectedConditions.visibilityOf(elemento));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Espera a que un elemento sea clickeable
     * @param driver WebDriver actual
     * @param elemento WebElement a esperar
     * @param segundos Tiempo máximo de espera en segundos
     * @return true si el elemento es clickeable, false si no
     */
    public static boolean esperarElementoClickeable(WebDriver driver, WebElement elemento, int segundos) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(segundos))
                    .until(ExpectedConditions.elementToBeClickable(elemento));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Resalta un elemento en la página para mejorar la visibilidad en capturas de pantalla
     * @param driver WebDriver actual
     * @param elemento Elemento a resaltar
     */
    public static void resaltarElemento(WebDriver driver, WebElement elemento) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String estiloOriginal = (String) js.executeScript(
                    "var estilo = arguments[0].getAttribute('style'); return estilo;",
                    elemento);

            // Añadir borde rojo al elemento
            js.executeScript(
                    "arguments[0].setAttribute('style', arguments[1] + '; border: 2px solid red; background-color: yellow; color: black;')",
                    elemento,
                    estiloOriginal != null ? estiloOriginal : "");

            // Pequeña pausa para visualizar el resaltado
            Thread.sleep(300);

            // Restaurar estilo original
            js.executeScript(
                    "arguments[0].setAttribute('style', arguments[1])",
                    elemento,
                    estiloOriginal != null ? estiloOriginal : "");
        } catch (Exception e) {
            // Ignorar excepciones
        }
    }
}