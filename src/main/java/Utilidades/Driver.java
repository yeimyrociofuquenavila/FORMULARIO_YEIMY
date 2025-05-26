package Utilidades;
// Clase Driver centraliza la gestión del WebDriver para evitar duplicación
import org.openqa.selenium.WebDriver; // Importa la clase WebDriver, que permite controlar el navegador
import org.openqa.selenium.chrome.ChromeDriver;// Importa el controlador específico para el navegador Chrome
import org.openqa.selenium.chrome.ChromeOptions;// Importa opciones para personalizar cómo se abre Chrome (por ejemplo)
import org.openqa.selenium.edge.EdgeDriver;// Importa el controlador específico para el navegador Edge
import org.openqa.selenium.firefox.FirefoxDriver;// Importa el controlador específico para el navegador Firefox
import org.openqa.selenium.firefox.FirefoxOptions;// Importa opciones para personalizar cómo se abre Firefox
import org.openqa.selenium.support.ui.WebDriverWait;// Importa WebDriverWait, que permite esperar a que ciertas condiciones se cumplan (como que aparezca un botón)
import io.github.bonigarcia.wdm.WebDriverManager;// Importa WebDriverManager, que se encarga de descargar automáticamente el driver del navegador que vamos a usar
import java.time.Duration;// Importa Duration, que se usa para definir cuánto tiempo esperamos

public class Driver {// Declaramos una clase llamada Driver, donde gestionaremos los navegadores y sus esperas
    // Creamos una variable llamada "driver" que guarda una copia del navegador para cada hilo (ejecución en paralelo)
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    // Creamos una variable llamada "wait" que guarda una copia del temporizador de espera para cada hilo
    private static ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();

    // Tiempo de espera aumentado a 20 segundos
    private static final int WAIT_TIMEOUT = 20;

    /**
     * Obtiene o crea una instancia de WebDriver para el hilo actual
     */
    public static WebDriver getDriver() {// Método público y estático que devuelve el navegador WebDriver actual
        if (driver.get() == null) {  // Verifica si aún no se ha creado un navegador para este hilo
            initDriver();        // Si no hay navegador, lo inicializa (lo crea y configura)
        }
        return driver.get();    // Devuelve el navegador correspondiente a este hilo
    }

    /**
     * Obtiene o crea una instancia de WebDriverWait para el hilo actual
     */
    // Método público y estático que devuelve el objeto WebDriverWait (espera) para este hilo
    public static WebDriverWait getWait() {
        if (wait.get() == null && driver.get() != null) {    // Si todavía no se ha creado una espera Y el navegador ya está disponible
            wait.set(new WebDriverWait(driver.get(), Duration.ofSeconds(WAIT_TIMEOUT)));   // Crea una nueva espera (WebDriverWait) con el navegador actual y un tiempo de espera máximo
        }
        return wait.get();    // Devuelve la espera correspondiente a este hilo

    }

    /**
     * Inicializa el WebDriver según el navegador configurado
     */
    // Método privado que se encarga de crear y configurar el navegador WebDriver
    private static void initDriver() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();    // Obtiene el nombre del navegador que queremos usar, por defecto es "chrome"
        WebDriver webDriver;    // Variable que guardará el navegador que vamos a crear

        // Según el navegador que se pidió, crea el WebDriver correspondiente
        switch (browser) {
            case "firefox":            // Si es Firefox, configura y crea un navegador Firefox
                webDriver = setupFirefoxDriver();
                break;
            case "edge":                // Si es Edge, configura y crea un navegador Edge

                webDriver = setupEdgeDriver();
                break;
            case "chrome":                    // Si es Chrome o cualquier otro valor, configura y crea un navegador Chrome

            default:
                webDriver = setupChromeDriver();
                break;
        }

        webDriver.manage().window().maximize();      // Maximiza la ventana del navegador para que ocupe toda la pantalla
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));      // Configura un tiempo de espera implícito de 5 segundos para encontrar elementos en la página
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));    // Configura un tiempo máximo de 30 segundos para que una página termine de cargar
        webDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));          // Configura un tiempo máximo de 30 segundos para la ejecución de scripts JavaScript

        driver.set(webDriver);    // Guarda el navegador creado en la variable "driver" específica para este hilo
        wait.set(new WebDriverWait(webDriver, Duration.ofSeconds(WAIT_TIMEOUT)));    // Crea y guarda una nueva espera (WebDriverWait) para este navegador con el tiempo máximo definido
    }

    /**
     * Configura ChromeDriver con opciones optimizadas
     */
    // Método privado que configura y crea un navegador Chrome listo para usar en pruebas
    private static WebDriver setupChromeDriver() {
        // Descarga y configura automáticamente el driver de Chrome para que funcione con Selenium
        WebDriverManager.chromedriver().setup();
        // Creamos un objeto para definir opciones o configuraciones personalizadas de Chrome
        ChromeOptions options = new ChromeOptions();

        // Opciones para evitar que Chrome detecte que está siendo controlado por un bot o prueba automática
        options.addArguments("--disable-blink-features=AutomationControlled");// Desactiva la característica que indica automatización
        options.addArguments("--disable-infobars"); // Elimina la barra de información que aparece en modo automático
        options.addArguments("--disable-notifications");// Evita que aparezcan notificaciones molestas
        options.addArguments("--start-maximized");// Abre la ventana maximizada desde el inicio

        // Cambia el User-Agent (identificador del navegador) a uno más común y realista para evitar bloqueos
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.7103.49 Safari/537.36");

        // Opciones comentadas que puedes activar si tienes problemas con el sandbox o recursos en sistemas Linux
        // Evitar problemas con el sandbox (opcional, usar con precaución)
        // options.addArguments("--no-sandbox");
        // options.addArguments("--disable-dev-shm-usage");

        // Desactiva todas las extensiones instaladas para evitar interferencias
        options.addArguments("--disable-extensions");

        // Usar perfil limpio
        // options.addArguments("--incognito");

        // Evitar mostrar el mensaje "Chrome está siendo controlado por software automatizado"
        options.setExperimentalOption("excludeSwitches", java.util.Arrays.asList("enable-automation"));
        // Finalmente, crea y devuelve el navegador Chrome con todas estas configuraciones
        return new ChromeDriver(options);
    }

    /**
     * Configura FirefoxDriver con opciones optimizadas
     */
    private static WebDriver setupFirefoxDriver() {
        // Configurar FirefoxDriver usando WebDriverManager
        WebDriverManager.firefoxdriver().setup();
        // Crea un objeto con opciones personalizadas para el navegador Firefox
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-private");    // Abre el navegador en modo privado

        // User-Agent más realista
        options.addPreference("general.useragent.override",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:120.0) Gecko/20100101 Firefox/120.0");

        return new FirefoxDriver(options);    // Crea y devuelve un nuevo navegador Firefox con estas opciones configuradas
    }

    /**
     * Configura EdgeDriver
     */
    private static WebDriver setupEdgeDriver() {
        // Configurar EdgeDriver usando WebDriverManager
        WebDriverManager.edgedriver().setup();

        return new EdgeDriver();
    }

    /**
     * Cierra el WebDriver actual
     */
    // Método público y estático que cierra y limpia el navegador y la espera para este hilo
    public static void quitDriver() {
        // Si el navegador (driver) existe para este hilo
        if (driver.get() != null) {
            // Cierra completamente el navegador y termina el proceso
            driver.get().quit();
            // Elimina la referencia al navegador de este hilo para liberar memoria
            driver.remove();
        }
        // Si la espera (wait) existe para este hilo
        if (wait.get() != null) {
            // Elimina la referencia a la espera para liberar recursos
            wait.remove();
        }
    }
}
