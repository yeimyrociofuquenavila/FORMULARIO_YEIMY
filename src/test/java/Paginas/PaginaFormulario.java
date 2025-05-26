package Paginas;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utilidades.Reporte;
import com.aventstack.extentreports.ExtentTest;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Clase que representa la página del formulario de contacto
 * Contiene los elementos de la página y métodos para interactuar con ellos
 */
public class PaginaFormulario {

    @Getter
    private WebDriver driver;
    private ExtentTest test;

    // Localizadores para los elementos del formulario
    private final By campoNombre = By.id("nombre");
    private final By campoEmail = By.name("email");
    private final By campoBarrio = By.name("barrio");
    private final By campoAsunto = By.name("asunto");
    private final By campoMensaje = By.name("Mensaje");
    private final By botonEnviar = By.cssSelector(".formulario__btn");
    private final By mensajeErrorFormulario = By.cssSelector(".formulario__mensaje-error");
    private final By mensajeExito = By.id("formulario__mensaje-exito");
    private final By logoLink = By.xpath("//a[contains(@href, 'Index.html')]");

    // URL del formulario de contacto
    private final String URL_FORMULARIO = "http://64.227.54.255/Softesting/Frontend/Caso1.html";

    /**
     * Constructor de la página
     */
    public PaginaFormulario(WebDriver driver, ExtentTest test) {
        this.driver = driver;
        this.test = test;
    }

    /**
     * Abre la página del formulario de contacto
     */
    public void abrirPagina() {
        driver.get(URL_FORMULARIO);
        Reporte.logInfo(test, "Abriendo la página del formulario de contacto", driver);
    }

    /**
     * Hace clic en el botón enviar del formulario
     */
    public void clickBotonEnviar() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement boton = wait.until(ExpectedConditions.elementToBeClickable(botonEnviar));
        boton.click();
        Reporte.logInfo(test, "Haciendo clic en el botón Enviar", driver);
    }

    /**
     * Hace clic en el logo para navegar a index
     */
    public void clickLogo() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement logo = wait.until(ExpectedConditions.elementToBeClickable(logoLink));
        logo.click();
        Reporte.logInfo(test, "Haciendo clic en el logo", driver);
    }

    /**
     * Ingresa texto en un campo del formulario
     */
    public void ingresarTextoEnCampo(String campo, String texto) {
        By localizador = obtenerLocalizadorCampo(campo);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(localizador));
        element.clear();
        element.sendKeys(texto);

        // Esperar un momento para que se procese la validación
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Reporte.logInfo(test, "Ingresando texto '" + texto + "' en el campo '" + campo + "'", driver);
    }

    /**
     * Llena todos los campos del formulario
     */
    public void llenarFormularioCompleto(String nombre, String email, String barrio, String asunto, String mensaje) {
        ingresarTextoEnCampo("nombre", nombre);
        ingresarTextoEnCampo("email", email);
        ingresarTextoEnCampo("barrio", barrio);
        ingresarTextoEnCampo("asunto", asunto);
        ingresarTextoAreaMensaje(mensaje);
    }

    /**
     * Ingresa texto en el área de mensaje
     */
    public void ingresarTextoAreaMensaje(String texto) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(campoMensaje));
        element.clear();
        element.sendKeys(texto);
        Reporte.logInfo(test, "Ingresando texto en el área de mensaje", driver);
    }

    /**
     * Hace clic en un enlace por su texto
     */
    public void clickEnEnlace(String textoEnlace) {
        By localizador = By.linkText(textoEnlace);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(localizador));
        element.click();
        Reporte.logInfo(test, "Haciendo clic en el enlace '" + textoEnlace + "'", driver);
    }

    /**
     * Obtiene el mensaje de error general del formulario
     */
    public String obtenerMensajeErrorFormulario() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(mensajeErrorFormulario));
            String mensaje = element.getText();
            Reporte.logInfo(test, "Mensaje de error del formulario: " + mensaje, driver);
            return mensaje;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Verifica si el mensaje de éxito es visible
     */
    public boolean esMensajeExitoVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(mensajeExito));
            boolean esVisible = element.isDisplayed();
            Reporte.logInfo(test, "Verificando si el mensaje de éxito es visible: " + esVisible, driver);
            return esVisible;
        } catch (Exception e) {
            Reporte.logInfo(test, "El mensaje de éxito no es visible", driver);
            return false;
        }
    }

    /**
     * Verifica si el mensaje de error específico para un campo es visible
     */
    public boolean esMensajeErrorCampoVisible(String campo) {
        By localizador = By.cssSelector("#grupo__" + campo + " .formulario__input-error");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
            boolean esVisible = element.isDisplayed();
            Reporte.logInfo(test, "Verificando si el mensaje de error para el campo '" + campo + "' es visible: " + esVisible, driver);
            return esVisible;
        } catch (Exception e) {
            Reporte.logInfo(test, "El mensaje de error para el campo '" + campo + "' no es visible", driver);
            return false;
        }
    }

    /**
     * Verifica si un campo tiene indicador visual de error
     */
    public boolean tieneCampoIndicadorError(String campo) {
        By localizador = By.cssSelector("#grupo__" + campo);
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
            boolean tieneError = element.getAttribute("class").contains("formulario__grupo-incorrecto");
            Reporte.logInfo(test, "Verificando si el campo '" + campo + "' tiene indicador de error: " + tieneError, driver);
            return tieneError;
        } catch (Exception e) {
            Reporte.logInfo(test, "No se pudo verificar si el campo '" + campo + "' tiene indicador de error", driver);
            return false;
        }
    }

    /**
     * Verifica si un campo tiene indicador visual de correcto
     */
    public boolean tieneCampoIndicadorCorrecto(String campo) {
        By localizador = By.cssSelector("#grupo__" + campo);
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
            boolean tieneCorrecto = element.getAttribute("class").contains("formulario__grupo-correcto");
            Reporte.logInfo(test, "Verificando si el campo '" + campo + "' tiene indicador de correcto: " + tieneCorrecto, driver);
            return tieneCorrecto;
        } catch (Exception e) {
            Reporte.logInfo(test, "No se pudo verificar si el campo '" + campo + "' tiene indicador de correcto", driver);
            return false;
        }
    }

    /**
     * Verifica si todos los campos están vacíos
     */
    public boolean todosCamposVacios() {
        try {
            WebElement nombre = driver.findElement(campoNombre);
            WebElement email = driver.findElement(campoEmail);
            WebElement barrio = driver.findElement(campoBarrio);
            WebElement asunto = driver.findElement(campoAsunto);
            WebElement mensaje = driver.findElement(campoMensaje);

            return nombre.getAttribute("value").isEmpty() &&
                    email.getAttribute("value").isEmpty() &&
                    barrio.getAttribute("value").isEmpty() &&
                    asunto.getAttribute("value").isEmpty() &&
                    mensaje.getAttribute("value").isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica errores ortográficos en las etiquetas
     */
    public boolean verificarErrorOrtograficoBarrio() {
        try {
            By etiquetaBarrio = By.xpath("//label[contains(text(), 'Varrio')]");
            WebElement elemento = driver.findElement(etiquetaBarrio);
            return elemento != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verificarErrorOrtograficoMensaje() {
        try {
            By etiquetaMensaje = By.xpath("//label[contains(text(), 'Mensage')]");
            WebElement elemento = driver.findElement(etiquetaMensaje);
            return elemento != null;
        } catch (Exception e) {
            return false;
        }
    }
//CAMBIO AQUI
    /**
     * Verifica si un campo específico tiene atributo ID definido
     */
    public boolean campoTieneId(String campo) {
        try {
            By localizador = obtenerLocalizadorCampo(campo);
            WebElement campoElement = driver.findElement(localizador);
            String id = campoElement.getAttribute("id");

            boolean tieneId = id != null && !id.isEmpty();

            Reporte.logInfo(test, String.format(
                    "Campo '%s' - ID: '%s' - Tiene ID: %s",
                    campo, id != null ? id : "null", tieneId
            ), driver);

            return tieneId;

        } catch (Exception e) {
            Reporte.logInfo(test, "Error al verificar ID del campo " + campo + ": " + e.getMessage(), driver);
            return false;
        }
    }

    /**
     * Verifica específicamente si el campo email tiene ID (método existente )
     */
    public boolean campoEmailTieneId() {
        return campoTieneId("email");
    }

    /**
     * Verifica si el campo barrio tiene ID definido
     */
    public boolean campoBarrioTieneId() {
        return campoTieneId("barrio");
    }

    /**
     * Verifica si el campo asunto tiene ID definido
     */
    public boolean campoAsuntoTieneId() {
        return campoTieneId("asunto");
    }

    /**
     * Verifica si el campo mensaje tiene ID definido
     */
    public boolean campoMensajeTieneId() {
        return campoTieneId("mensaje");
    }

    /**
     * Obtiene información detallada de los IDs de todos los campos
     */
    public String obtenerInformacionIDs() {
        StringBuilder info = new StringBuilder();
        String[] campos = {"nombre", "email", "barrio", "asunto", "mensaje"};

        for (String campo : campos) {
            try {
                By localizador = obtenerLocalizadorCampo(campo);
                WebElement elemento = driver.findElement(localizador);
                String id = elemento.getAttribute("id");
                String name = elemento.getAttribute("name");

                info.append(String.format(
                        "Campo: %s | Name: %s | ID: %s | Tiene ID: %s%n",
                        campo, name, id != null ? id : "null", id != null && !id.isEmpty()
                ));

            } catch (Exception e) {
                info.append(String.format("Campo: %s | Error: %s%n", campo, e.getMessage()));
            }
        }

        return info.toString();
    }
    //Hasta aqui

    /**
     * Obtiene el localizador de un campo según su nombre
     */
    private By obtenerLocalizadorCampo(String campo) {
        switch (campo.toLowerCase()) {
            case "nombre":
                return campoNombre;
            case "email":
                return campoEmail;
            case "barrio":
                return campoBarrio;
            case "asunto":
                return campoAsunto;
            case "mensaje":
                return campoMensaje;
            default:
                throw new IllegalArgumentException("Campo no reconocido: " + campo);
        }
    }

    /**
     * Espera a que la página se cargue completamente
     */
    public void esperarCargaPagina() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("form")));
    }

    /**
     * Detecta si la página actual tiene error de servidor
     */
    public boolean tieneErrorServidor(String contenidoPagina) {
        return contenidoPagina.contains("Not Found") ||
                contenidoPagina.contains("404") ||
                contenidoPagina.contains("Apache/") ||
                contenidoPagina.contains("The requested URL was not found") ||
                driver.getTitle().toLowerCase().contains("error") ||
                driver.getTitle().toLowerCase().contains("not found");
    }

    /**
     * Método  mantén este también
     */
    public boolean estaEnPagina(String nombrePagina) {
        String urlActual = driver.getCurrentUrl();
        String contenidoPagina = driver.getPageSource();

        boolean urlCorrecta = urlActual.contains(nombrePagina);
        boolean sinError = !tieneErrorServidor(contenidoPagina);

        return urlCorrecta && sinError;
    }

    /**
     * Método para debugging - puedes usarlo si necesitas más detalles
     */
    public void imprimirEstadoPagina(String contexto) {
        System.out.println("=== ESTADO DE PÁGINA: " + contexto + " ===");
        System.out.println("URL: " + driver.getCurrentUrl());
        System.out.println("Título: " + driver.getTitle());
        System.out.println("¿Contiene error?: " + tieneErrorServidor(driver.getPageSource()));
        System.out.println("========================================");
    }

    /**
     * Verifica si el formulario siempre muestra alerta de error al enviar
     */




    /**
     * Espera a que las validaciones en tiempo real se ejecuten
     */
    public void esperarValidacionesCampos() {
        try {
            // Esperar un momento para que se ejecuten las validaciones keyup/blur
            Thread.sleep(1500);
            Reporte.logInfo(test, "Esperando a que se ejecuten las validaciones de campos", driver);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Verifica que todos los campos están validados correctamente
     */
    public boolean verificarTodosLosCamposValidos() {
        try {
            boolean nombreValido = tieneCampoIndicadorCorrecto("nombre");
            boolean emailValido = tieneCampoIndicadorCorrecto("email");
            boolean barrioValido = tieneCampoIndicadorCorrecto("barrio");
            boolean asuntoValido = tieneCampoIndicadorCorrecto("asunto");

            boolean todosValidos = nombreValido && emailValido && barrioValido && asuntoValido;

            Reporte.logInfo(test, String.format(
                    "Validación de campos - Nombre: %s, Email: %s, Barrio: %s, Asunto: %s, Todos válidos: %s",
                    nombreValido, emailValido, barrioValido, asuntoValido, todosValidos
            ), driver);

            return todosValidos;
        } catch (Exception e) {
            Reporte.logInfo(test, "Error al verificar validación de campos: " + e.getMessage(), driver);
            return false;
        }
    }

    /**
     * Prepara para capturar el alert que aparecerá
     */
    public void prepararCapturarAlert() {
        Reporte.logInfo(test, "Preparando para capturar alert del formulario", driver);
        // En Selenium, no necesitamos preparación especial, solo capturaremos el alert cuando aparezca
    }

    /**
     * Verifica si aparece el alert de error incorrecto
     * Según el JavaScript, SIEMPRE muestra "UPPPPS ALGO HA FALLADO :(" sin importar si los datos son válidos
     */
    public boolean verificarAlertDeErrorIncorrecto() {
        try {
            // Esperar a que aparezca el alert
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            if (alert != null) {
                String textoAlert = alert.getText();
                Reporte.logInfo(test, "Alert detectado con texto: " + textoAlert, driver);

                // El bug es que siempre muestra este mensaje, incluso con datos válidos
                boolean esBugDetectado = textoAlert.contains("UPPPPS ALGO HA FALLADO");

                // Aceptar el alert después de verificarlo
                alert.accept();

                return esBugDetectado;
            }

            return false;

        } catch (Exception e) {
            Reporte.logInfo(test, "No se detectó alert o error al capturarlo: " + e.getMessage(), driver);
            return false;
        }
    }

    /**
     * Obtiene el texto del alert si está presente
     */
    public String obtenerTextoAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            if (alert != null) {
                String texto = alert.getText();
                alert.accept(); // Aceptar el alert
                return texto;
            }

            return "";

        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Método  para llenar formulario con datos específicamente válidos según las regex
     */
    public void llenarFormularioConDatosValidos() {
        // Usar datos que específicamente cumplan con las regex del JavaScript
        ingresarTextoEnCampo("nombre", "Juan Perez");     // 2-12 caracteres, solo letras y espacios
        ingresarTextoEnCampo("email", "juan.perez@test.com"); // Formato email (aunque la regex tiene bug)
        ingresarTextoEnCampo("barrio", "Centro");         // 1-40 caracteres
        ingresarTextoEnCampo("asunto", "Consulta Test");  // 1-40 caracteres
        ingresarTextoAreaMensaje("Este es un mensaje de prueba para el formulario de contacto");

        Reporte.logInfo(test, "Formulario llenado con datos válidos específicos", driver);
    }

    /**
     * Verifica el comportamiento del bug del submit event listener
     * El JavaScript tiene: formulario.addEventListener('submit', (e) => { alert("UPPPPS ALGO HA FALLADO :("); });
     */
    public boolean verificarBugSubmitSiempreFalla() {
        try {
            // Llenar con datos válidos
            llenarFormularioConDatosValidos();

            // Esperar validaciones
            esperarValidacionesCampos();

            // Verificar que campos están validados correctamente
            boolean camposValidos = verificarTodosLosCamposValidos();

            if (!camposValidos) {
                Reporte.logInfo(test, "Los campos no están validados correctamente, no se puede probar el bug del submit", driver);
                return false;
            }

            // Hacer click en enviar (esto debería triggear el event listener con el bug)
            clickBotonEnviar();

            // Verificar si aparece el alert incorrecto
            boolean alertIncorrectoDetectado = verificarAlertDeErrorIncorrecto();

            if (alertIncorrectoDetectado) {
                Reporte.logInfo(test, "BUG DETECTADO: Submit siempre muestra error incluso con datos válidos", driver);
            } else {
                Reporte.logInfo(test, "Bug del submit no detectado o formulario funciona correctamente", driver);
            }

            return alertIncorrectoDetectado;

        } catch (Exception e) {
            Reporte.logInfo(test, "Error al verificar bug del submit: " + e.getMessage(), driver);
            return false;
        }
    }

    /**
     * Verifica si el formulario previene el comportamiento por defecto del submit
     * (En el JavaScript falta e.preventDefault(), por eso siempre falla)
     */
    public boolean verificarSubmitSinPreventDefault() {
        try {
            // El bug está en que el event listener no tiene e.preventDefault()
            // Por lo tanto, después del alert, el formulario intenta enviarse de forma tradicional
            // y como no hay validación del lado del servidor, falla

            llenarFormularioConDatosValidos();
            esperarValidacionesCampos();

            String urlAntes = driver.getCurrentUrl();

            clickBotonEnviar();

            // Manejar el alert primero
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                alert.accept();
            } catch (Exception alertException) {
                // Si no hay alert, continuar
            }

            // Esperar un momento para ver si la página cambia o recarga
            Thread.sleep(2000);

            String urlDespues = driver.getCurrentUrl();

            // Si la URL cambió o se recargó, indica que el submit se ejecutó (bug del preventDefault)
            boolean urlCambio = !urlAntes.equals(urlDespues);

            Reporte.logInfo(test, String.format(
                    "Verificación preventDefault - URL antes: %s, URL después: %s, Cambió: %s",
                    urlAntes, urlDespues, urlCambio
            ), driver);

            return urlCambio;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica inconsistencias en rutas de navegación
     */
    public boolean verificarRutasInconsistentes() {
        try {
            // Verificar diferentes patrones de rutas en los enlaces
            By enlaceConFrontend = By.xpath("//a[contains(@href, '/Frontend/')]");
            By enlaceSinFrontend = By.xpath("//a[contains(@href, 'Caso') and not(contains(@href, '/Frontend/'))]");

            boolean hayEnlacesConFrontend = !driver.findElements(enlaceConFrontend).isEmpty();
            boolean hayEnlacesSinFrontend = !driver.findElements(enlaceSinFrontend).isEmpty();

            // Si hay ambos tipos, hay inconsistencia
            return hayEnlacesConFrontend && hayEnlacesSinFrontend;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si existen enlaces duplicados a "Caso 5"
     */
    public boolean verificarEnlacesDuplicadosCaso5() {
        try {
            // Buscar enlaces que contengan "Caso5.html"
            List<WebElement> enlacesCaso5 = driver.findElements(By.xpath("//a[contains(@href, 'Caso5.html')]"));

            // Si hay más de un enlace apuntando a Caso5.html, hay duplicación
            return enlacesCaso5.size() > 1;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el campo mensaje tiene validación implementada
     */
    public boolean campoMensajeTieneValidacion() {
        try {
            // Limpiar el campo mensaje
            WebElement campoMensajeElement = driver.findElement(campoMensaje);
            campoMensajeElement.clear();

            // Intentar enviar formulario con mensaje vacío pero otros campos llenos
            ingresarTextoEnCampo("nombre", "Test Usuario");
            ingresarTextoEnCampo("email", "test@test.com");
            ingresarTextoEnCampo("barrio", "Test Barrio");
            ingresarTextoEnCampo("asunto", "Test Asunto");


            // Esperar y verificar si aparece error específico para el campo mensaje
            Thread.sleep(1000);
            return esMensajeErrorCampoVisible("mensaje");

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica espacios extra en referencias de archivos JavaScript
     */
    public boolean verificarEspaciosExtraEnJS() {
        try {
            // Verificar en el código fuente si hay espacios extra en referencias JS
            String paginaSource = driver.getPageSource();
            return paginaSource.contains("validarformulario.js \"") ||
                    paginaSource.contains("validarformulario.js \t");

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica contenido repetitivo incorrecto en descripciones
     */
    public boolean verificarContenidoRepetitivo() {
        try {
            // Buscar si hay texto repetitivo como "El caso 1 se debe realizar"
            // en contextos donde debería decir caso 2, 3, 4, etc.
            String paginaSource = driver.getPageSource();

            // Contar ocurrencias de "caso 1" en minúsculas
            int ocurrenciasCaso1 = (paginaSource.toLowerCase().split("caso 1")).length - 1;

            // Si hay más de 2 ocurrencias, probablemente hay contenido repetitivo incorrecto
            return ocurrenciasCaso1 > 2;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Simula la validación de email con regex incorrecta
     */
    public boolean validarEmailConRegexIncorrecta(String email) {
        // Simular la regex incorrecta del JavaScript: sin @
        String regexIncorrecta = "^[a-zA-Z0-9_.+-]+[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

        // Un email válido como "yeimyrociofuquenavila@gmail.com" debería fallar con la regex incorrecta
        return email.matches(regexIncorrecta);
    }

    /**
     * Navega a página de índice para verificar bugs de navegación
     */
    public void navegarAPaginaIndice() {
        String urlIndice = "http://64.227.54.255/Softesting/Frontend/Index.html";
        driver.get(urlIndice);
        Reporte.logInfo(test, "Navegando a la página de índice", driver);
    }

    /**
     * Verifica error ortográfico en clase CSS "contedenor-cabecera"
     */
    public boolean verificarErrorOrtograficoContenedorCabecera() {//cuatro//cabecera
        try {
            // Buscar elemento con la clase CSS incorrecta
            By elementoConClaseIncorrecta = By.className("contedenor-cabecera");
            WebElement elemento = driver.findElement(elementoConClaseIncorrecta);

            boolean errorEncontrado = elemento != null;

            if (errorEncontrado) {
                Reporte.logInfo(test, "Bug detectado: Clase CSS 'contedenor-cabecera' encontrada (debería ser 'contenedor-cabecera')", driver);
            }

            return errorEncontrado;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica inconsistencias en rutas de navegación del header
     */
    public boolean verificarRutasInconsistentesHeader() {//cuatro
        try {
            // Verificar enlaces con /Frontend/
            List<WebElement> enlacesConFrontend = driver.findElements(
                    By.xpath("//nav[@class='navegacion']//a[contains(@href, '/Frontend/')]")
            );

            // Verificar enlaces sin /Frontend/ pero que apuntan a casos
            List<WebElement> enlacesSinFrontend = driver.findElements(
                    By.xpath("//nav[@class='navegacion']//a[contains(@href, 'Caso') and not(contains(@href, '/Frontend/'))]")
            );

            boolean hayInconsistencias = !enlacesConFrontend.isEmpty() && !enlacesSinFrontend.isEmpty();

            if (hayInconsistencias) {
                Reporte.logInfo(test, String.format(
                        "Bug detectado: Inconsistencias en rutas - %d enlaces con '/Frontend/', %d sin '/Frontend/'",
                        enlacesConFrontend.size(), enlacesSinFrontend.size()
                ), driver);
            }

            return hayInconsistencias;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica espacio extra en enlace "Caso 2"
     */
    public boolean verificarEspacioExtraEnlaceCaso2() {//uno// cuatro//siete
        try {
            // El href debería ser "/Frontend/Caso2.html " (con espacio al final)
            By enlaceCaso2ConEspacio = By.xpath("//a[@href='/Frontend/Caso2.html ']");
            WebElement elemento = driver.findElement(enlaceCaso2ConEspacio);

            boolean espacioExtraEncontrado = elemento != null;

            if (espacioExtraEncontrado) {
                String href = elemento.getAttribute("href");
                Reporte.logInfo(test, "Bug detectado: Espacio extra en enlace Caso 2. Href: '" + href + "'", driver);
            }

            return espacioExtraEncontrado;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica que ambos enlaces de inicio de sesión apuntan al mismo archivo
     */
    public boolean verificarEnlacesDuplicadosLogin() {//cuatro
        try {
            // Buscar todos los enlaces que apuntan a login.html en la sección iniciosesion
            List<WebElement> enlacesLogin = driver.findElements(
                    By.xpath("//div[@class='iniciosesion']//a[@href='login.html']")
            );

            boolean enlacesDuplicados = enlacesLogin.size() > 1;

            if (enlacesDuplicados) {
                Reporte.logInfo(test, String.format(
                        "Bug detectado: %d enlaces apuntan al mismo archivo 'login.html' (Registrarse e Iniciar sesión)",
                        enlacesLogin.size()
                ), driver);
            }

            return enlacesDuplicados;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica funcionalidad del logo como enlace
     */
    public boolean verificarFuncionalidadLogoHeader() {//cinco LOGO
        try {
            By logoEnlace = By.xpath("//header//a[contains(@href, 'Index.html')]");
            WebElement logo = driver.findElement(logoEnlace);

            String href = logo.getAttribute("href");
            boolean tieneEnlaceValido = href != null && href.contains("Index.html");

            if (tieneEnlaceValido) {
                Reporte.logInfo(test, "Logo del header tiene enlace válido: " + href, driver);
            } else {
                Reporte.logInfo(test, "Problema con enlace del logo: " + href, driver);
            }

            return tieneEnlaceValido;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Navega usando el enlace del header
     */
    public void navegarDesdeHeader(String enlace) {//tres
        try {
            By localizadorEnlace = By.xpath("//nav[@class='navegacion']//a[contains(text(), '" + enlace + "')]");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement elemento = wait.until(ExpectedConditions.elementToBeClickable(localizadorEnlace));

            elemento.click();
            Reporte.logInfo(test, "Navegando desde header usando enlace: " + enlace, driver);

        } catch (Exception e) {
            Reporte.logInfo(test, "Error al navegar desde header con enlace: " + enlace + " - " + e.getMessage(), driver);
        }
    }

    /**
     * Verifica atributo alt de la imagen del logo
     */
    public boolean verificarAtributoAltLogo() {//dos
        try {
            By logoImg = By.xpath("//header//img[@alt='Softesting']");
            WebElement logo = driver.findElement(logoImg);

            String altText = logo.getAttribute("alt");
            boolean altCorrecto = "Softesting".equals(altText);

            if (altCorrecto) {
                Reporte.logInfo(test, "Atributo alt del logo es correcto: " + altText, driver);
            } else {
                Reporte.logInfo(test, "Problema con atributo alt del logo: " + altText, driver);
            }

            return altCorrecto;

        } catch (Exception e) {
            return false;
        }
    }

    /*
    /**
     * Obtiene información completa de problemas de sintaxis en el header
 */
    public String obtenerInformacionBugsHeader() {//uno//cuatro
        StringBuilder info = new StringBuilder();

        info.append("=== ANÁLISIS DE BUGS EN HEADER ===\n");

        // Error ortográfico en clase CSS
        boolean errorOrtografico = verificarErrorOrtograficoContenedorCabecera();
        info.append(" Clase CSS incorrecta 'contedenor-cabecera': ").append(errorOrtografico ? "DETECTADO" : "No detectado").append("\n");


        // Espacio extra
        boolean espacioExtra = verificarEspacioExtraEnlaceCaso2();
        info.append(" Espacio extra en Caso 2: ").append(espacioExtra ? "DETECTADO" : "No detectado").append("\n");

        return info.toString();
    }

}
