package Pasos;

import static org.junit.Assert.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Paginas.PaginaFormulario;
import Utilidades.Reporte;
import com.aventstack.extentreports.ExtentTest;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

/**
 * Clase que implementa los pasos de prueba para la validación del formulario
 */
public class Pasos {

    private WebDriver driver;
    private PaginaFormulario paginaFormulario;
    private ExtentTest test;
    private Scenario scenario;

    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        test = Reporte.crearPrueba(scenario.getName());
        paginaFormulario = new PaginaFormulario(driver, test);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ================ PASOS BÁSICOS ================

    @Dado("que el usuario accede al formulario de contacto")
    public void usuarioAccedeAlFormularioDeContacto() {
        paginaFormulario.abrirPagina();
        paginaFormulario.esperarCargaPagina();
    }

    @Cuando("el usuario ingresa {string} en el campo {string}")
    public void usuarioIngresaEnCampo(String entrada, String campo) {
        paginaFormulario.ingresarTextoEnCampo(campo, entrada);

        // Esperar un momento para que se procese la validación
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Entonces("el campo {string} debe mostrar indicador visual de {string}")
    public void campoDebeMostrarIndicadorVisual(String campo, String resultado) {
        boolean resultadoObtenido = false;

        if ("error".equals(resultado)) {
            resultadoObtenido = paginaFormulario.tieneCampoIndicadorError(campo);
            if (resultadoObtenido) {
                Reporte.logExito(test, "El campo '" + campo + "' muestra correctamente indicador de error", driver);
            } else {
                Reporte.logFallo(test, "El campo '" + campo + "' no muestra indicador de error ", driver);
            }
            assertTrue("El campo '" + campo + "' debería mostrar indicador de error", resultadoObtenido);

        } else if ("correcto".equals(resultado)) {
            resultadoObtenido = paginaFormulario.tieneCampoIndicadorCorrecto(campo);
            if (resultadoObtenido) {
                Reporte.logExito(test, "El campo '" + campo + "' muestra correctamente indicador de correcto", driver);
            } else {
                Reporte.logFallo(test, "El campo '" + campo + "' no muestra indicador de correcto ", driver);
            }
            assertTrue("El campo '" + campo + "' debería mostrar indicador de correcto", resultadoObtenido);
        }
    }

    // ================ CASO POSITIVO ================

    @Cuando("lleno el campo nombre con {string}")
    public void llenoElCampoNombreCon(String nombre) {
        paginaFormulario.ingresarTextoEnCampo("nombre", nombre);
    }

    @Y("lleno el campo email con {string}")
    public void llenoElCampoEmailCon(String email) {
        paginaFormulario.ingresarTextoEnCampo("email", email);
    }

    @Y("lleno el campo barrio con {string}")
    public void llenoElCampoBarrioCon(String barrio) {
        paginaFormulario.ingresarTextoEnCampo("barrio", barrio);
    }

    @Y("lleno el campo asunto con {string}")
    public void llenoElCampoAsuntoCon(String asunto) {
        paginaFormulario.ingresarTextoEnCampo("asunto", asunto);
    }

    @Y("lleno el campo mensaje con {string}")
    public void llenoElCampoMensajeCon(String mensaje) {
        paginaFormulario.ingresarTextoAreaMensaje(mensaje);
    }

    @Y("hago clic en el botón Enviar")
    public void hagoClicEnElBotonEnviar() {
        paginaFormulario.clickBotonEnviar();

        // Esperar a que se procese el envío
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Entonces("debería ver el mensaje de envío exitoso")
    public void deberiaVerElMensajeDeEnvioExitoso() {
        boolean mensajeVisible = paginaFormulario.esMensajeExitoVisible();

        if (mensajeVisible) {
            Reporte.logExito(test, "Se muestra correctamente el mensaje de envío exitoso", driver);
        } else {
            Reporte.logFallo(test, "No se muestra el mensaje de envío exitoso", driver);
        }

        assertTrue("Debería mostrarse el mensaje de envío exitoso", mensajeVisible);
    }

    // ================ CASOS NEGATIVOS - BUGS CRÍTICOS ================

    @Cuando("reviso las etiquetas del formulario")
    public void revisoLasEtiquetasDelFormulario() {
        // Solo registrar que se están revisando las etiquetas
        Reporte.logInfo(test, "Revisando las etiquetas del formulario", driver);
    }

    @Entonces("debería encontrar {string} en lugar de {string}")
    public void deberiaEncontrarEnLugarDe(String textoIncorrecto, String textoCorrecto) {
        boolean errorEncontrado = false;

        if ("Varrio".equals(textoIncorrecto) && "Barrio".equals(textoCorrecto)) {
            errorEncontrado = paginaFormulario.verificarErrorOrtograficoBarrio();
            if (errorEncontrado) {
                Reporte.logExito(test, "Bug detectado: Se encontró 'Varrio' en lugar de 'Barrio'", driver);
            } else {
                Reporte.logFallo(test, "No se detectó el error ortográfico en 'Barrio'", driver);
            }
        } else if ("Mensage".equals(textoIncorrecto) && "Mensaje".equals(textoCorrecto)) {
            errorEncontrado = paginaFormulario.verificarErrorOrtograficoMensaje();
            if (errorEncontrado) {
                Reporte.logExito(test, "Bug detectado: Se encontró 'Mensage' en lugar de 'Mensaje'", driver);
            } else {
                Reporte.logFallo(test, "No se detectó el error ortográfico en 'Mensaje'", driver);
            }
        }

        assertTrue("Debería encontrarse el error ortográfico: " + textoIncorrecto + " en lugar de " + textoCorrecto,
                errorEncontrado);
    }



    private String campoInspeccionado;

    @Cuando("inspecciono el campo {string}")
    public void inspeccionoElCampoMejorado(String campo) {
        this.campoInspeccionado = campo;
        Reporte.logInfo(test, "Inspeccionando el campo " + campo, driver);
    }

    @Entonces("no debería tener atributo ID definido")
    public void noDeberiaTenerAtributoIdDefinidoMejorado() {
        boolean tieneId = paginaFormulario.campoTieneId(campoInspeccionado);

        if (!tieneId) {
            Reporte.logExito(test, "Bug detectado: El campo " + campoInspeccionado + " no tiene atributo ID definido", driver);
        } else {
            Reporte.logFallo(test, "El campo " + campoInspeccionado + " sí tiene atributo ID definido", driver);
        }

        assertFalse("El campo " + campoInspeccionado + " no debería tener atributo ID definido", tieneId);
    }
    @Cuando("dejo todos los campos vacíos")
    public void dejoTodosLosCamposVacios() {
        // Los campos ya están vacíos por defecto, solo verificamos
        boolean todosCamposVacios = paginaFormulario.todosCamposVacios();

        if (todosCamposVacios) {
            Reporte.logInfo(test, "Todos los campos están vacíos", driver);
        } else {
            Reporte.logInfo(test, "Algunos campos contienen datos", driver);
        }
    }

    @Entonces("deberían aparecer mensajes de error para cada campo")
    public void deberianAparecerMensajesDeErrorParaCadaCampo() {
        // Esperar a que aparezcan los mensajes de error
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean errorNombre = paginaFormulario.esMensajeErrorCampoVisible("nombre");
        boolean errorEmail = paginaFormulario.esMensajeErrorCampoVisible("email");
        boolean errorBarrio = paginaFormulario.esMensajeErrorCampoVisible("barrio");
        boolean errorAsunto = paginaFormulario.esMensajeErrorCampoVisible("asunto");

        // También verificar el mensaje general de error
        String mensajeGeneral = paginaFormulario.obtenerMensajeErrorFormulario();
        boolean hayMensajeGeneral = !mensajeGeneral.isEmpty();

        if (errorNombre && errorEmail && errorBarrio && errorAsunto && hayMensajeGeneral) {
            Reporte.logExito(test, "Se muestran correctamente todos los mensajes de error", driver);
        } else {
            Reporte.logFallo(test, "No se muestran todos los mensajes de error esperados", driver);
        }

        assertTrue("Debería aparecer mensaje de error para el campo nombre", errorNombre);
        assertTrue("Debería aparecer mensaje de error para el campo email", errorEmail);
        assertTrue("Debería aparecer mensaje de error para el campo barrio", errorBarrio);
        assertTrue("Debería aparecer mensaje de error para el campo asunto", errorAsunto);
        assertTrue("Debería aparecer mensaje de error general", hayMensajeGeneral);
    }



    // ================ NAVEGACIÓN ================

    @Cuando("el usuario hace clic en el logo")
    public void elUsuarioHaceClicEnElLogo() {
        paginaFormulario.clickLogo();
    }

    @Entonces("debe ser redirigido a la página {string}")
    public void debeSerRedirigidoALaPagina(String nombrePagina) {
        // Esperar a que se complete la navegación
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains(nombrePagina));

        boolean estaEnPagina = paginaFormulario.estaEnPagina(nombrePagina);

        if (estaEnPagina) {
            Reporte.logExito(test, "Navegación exitosa a la página: " + nombrePagina, driver);
        } else {
            Reporte.logFallo(test, "No se navegó correctamente a la página: " + nombrePagina +
                    ". URL actual: " + driver.getCurrentUrl(), driver);
        }

        assertTrue("Debería estar en la página: " + nombrePagina, estaEnPagina);
    }

    @Cuando("el usuario hace clic en el enlace {string}")
    public void elUsuarioHaceClicEnElEnlace(String textoEnlace) {
        paginaFormulario.clickEnEnlace(textoEnlace);
    }


// ================ BUGS FUNCIONALES CRÍTICOS ================


    @Cuando("lleno el formulario con datos válidos y lo envío")
    public void llenoElFormularioConDatosValidosYLoEnvio() {
        // Llenar todos los campos con datos válidos según las regex del JavaScript
        paginaFormulario.ingresarTextoEnCampo("nombre", "Usuario Test");
        paginaFormulario.ingresarTextoEnCampo("email", "yeimi.fuquen@gmail.com"); // formato email Aceptado para Validacion
        paginaFormulario.ingresarTextoEnCampo("barrio", "Centro"); //
        paginaFormulario.ingresarTextoEnCampo("asunto", "Consulta General"); //
        paginaFormulario.ingresarTextoAreaMensaje("Mensaje de prueba para contacto");

        // Esperar a que las validaciones en tiempo real se ejecuten
        paginaFormulario.esperarValidacionesCampos();

        // Verificar que todos los campos están validados correctamente antes del submit
        boolean todosLosCamposValidos = paginaFormulario.verificarTodosLosCamposValidos();
        assertTrue("Los campos deberían estar validados correctamente antes del submit", todosLosCamposValidos);

        // Preparar para capturar el alert
        paginaFormulario.prepararCapturarAlert();

        // Enviar el formulario
        paginaFormulario.clickBotonEnviar();
    }

    @Entonces("el formulario debería mostrar mensaje de error incorrectamente")
    public void elFormularioDeberiaMostrarMensajeDeErrorIncorrectamente() {
        // Verificar que se muestra el alert de error cuando no debería
        boolean muestraAlertDeError = paginaFormulario.verificarAlertDeErrorIncorrecto();

        if (muestraAlertDeError) {
            String mensajeAlert = paginaFormulario.obtenerTextoAlert();
            Reporte.logExito(test,
                    "BUG DETECTADO: El formulario muestra alert de error '" + mensajeAlert +
                            "' incluso con datos válidos. Comportamiento esperado: mostrar mensaje de éxito.",
                    driver);
        } else {
            Reporte.logFallo(test,
                    " El bug no fue detectado: El formulario no muestra error incorrecto o no se pudo capturar el alert",
                    driver);
        }

        assertTrue(" BUG: El formulario debería mostrar alert de error incorrectamente con datos válidos",
                muestraAlertDeError);
    }


    @Cuando("verifico la validación del campo email")
    public void verificoLaValidacionDelCampoEmail() {
        // Probar con un email válido que debería fallar con regex incorrecta
        paginaFormulario.ingresarTextoEnCampo("email", "yeimyrociofuquenavila@gmail.com");

        // Esperar procesamiento
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Entonces("debería fallar la validación por regex incorrecta")
    public void deberiaFallarLaValidacionPorRegexIncorrecta() {
        // Verificar si el email válido es marcado como incorrecto
        boolean emailValidoFalla = paginaFormulario.validarEmailConRegexIncorrecta("yeimyrociofuquenavila@gmail.com");

        if (!emailValidoFalla) {
            Reporte.logExito(test, "Bug detectado: Regex de email incorrecta - no incluye @", driver);
        } else {
            Reporte.logFallo(test, "La regex de email funciona correctamente", driver);
        }

        assertFalse("Un email válido debería fallar con la regex incorrecta", emailValidoFalla);
    }

    @Cuando("verifico la validación del campo mensaje")
    public void verificoLaValidacionDelCampoMensaje() {
        Reporte.logInfo(test, "Verificando validación del campo mensaje", driver);
    }

    @Entonces("deberia tener validar validación implementada")
    public void deberiatenervalidarvalidacionimplementada() {
        boolean tieneValidacion = paginaFormulario.campoMensajeTieneValidacion();

        if (!tieneValidacion) {
            Reporte.logExito(test, "Bug detectado: El campo mensaje no tiene validación implementada", driver);
        } else {
            Reporte.logFallo(test, "El campo mensaje sí tiene validación", driver);
        }

        assertFalse("El campo mensaje no debería tener validación implementada", tieneValidacion);
    }

// ================ BUGS DE NAVEGACIÓN Y ESTRUCTURA ================

    @Dado("que navego a la página de índice")
    public void queNavegoALaPaginaDeIndice() {
        paginaFormulario.navegarAPaginaIndice();

        // Esperar a que cargue
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Cuando("reviso los enlaces de navegación")
    public void revisoLosEnlacesDeNavegacion() {
        Reporte.logInfo(test, "Revisando enlaces de navegación", driver);
    }

    @Entonces("debería encontrar rutas inconsistentes")
    public void deberiaEncontrarRutasInconsistentes() {
        boolean rutasInconsistentes = paginaFormulario.verificarRutasInconsistentes();

        if (rutasInconsistentes) {
            Reporte.logExito(test, "Bug detectado: Rutas inconsistentes entre enlaces", driver);
        } else {
            Reporte.logFallo(test, "No se detectaron rutas inconsistentes", driver);
        }

        assertTrue("Deberían existir rutas inconsistentes", rutasInconsistentes);
    }

    @Entonces("debería encontrar enlaces duplicados a Caso 5")
    public void deberiaEncontrarEnlacesDuplicadosACaso5() {
        boolean enlacesDuplicados = paginaFormulario.verificarEnlacesDuplicadosCaso5();

        if (enlacesDuplicados) {
            Reporte.logExito(test, "Bug detectado: Enlaces duplicados apuntando a Caso5.html", driver);
        } else {
            Reporte.logFallo(test, "No se detectaron enlaces duplicados a Caso 5", driver);
        }

        assertTrue("Deberían existir enlaces duplicados a Caso 5", enlacesDuplicados);
    }

    @Cuando("reviso el contenido de las descripciones")
    public void revisoElContenidoDeLasDescripciones() {
        Reporte.logInfo(test, "Revisando contenido de descripciones", driver);
    }

    @Entonces("debería encontrar contenido repetitivo incorrecto")
    public void deberiaEncontrarContenidoRepetitivoIncorrecto() {
        boolean contenidoRepetitivo = paginaFormulario.verificarContenidoRepetitivo();

        if (contenidoRepetitivo) {
            Reporte.logExito(test, "Bug detectado: Contenido repetitivo incorrecto en descripciones", driver);
        } else {
            Reporte.logFallo(test, "No se detectó contenido repetitivo incorrecto", driver);
        }

        assertTrue("Debería existir contenido repetitivo incorrecto", contenidoRepetitivo);
    }

// ================ BUGS DE CÓDIGO Y SINTAXIS ================

    @Cuando("inspecciono las referencias de archivos JavaScript")
    public void inspeccionoLasReferenciasDeArchivosJavaScript() {
        Reporte.logInfo(test, "Inspeccionando referencias de archivos JavaScript", driver);
    }

    @Entonces("debería encontrar espacios extra en las referencias")
    public void deberiaEncontrarEspaciosExtraEnLasReferencias() {
        boolean espaciosExtra = paginaFormulario.verificarEspaciosExtraEnJS();

        if (espaciosExtra) {
            Reporte.logExito(test, "Bug detectado: Espacios extra en referencias JavaScript", driver);
        } else {
            Reporte.logFallo(test, "No se detectaron espacios extra en referencias JS", driver);
        }

        assertTrue("Deberían existir espacios extra en referencias JavaScript", espaciosExtra);
    }
    // ================ PASOS PARA VALIDACIÓN DEL HEADER ================

    @Cuando("inspecciono las clases CSS del header")
    public void inspeccionoLasClasesCSSDelHeader() { //REVISAR
        Reporte.logInfo(test, "Inspeccionando clases CSS del header", driver);
    }

    @Entonces("debería encontrar \"contedenor-cabecera\" en lugar de \"contenedor-cabecera\"")
    public void deberiaEncontrarContedenorCabeceraEnLugarDeContenedorCabecera() {//cabecera
        boolean errorOrtografico = paginaFormulario.verificarErrorOrtograficoContenedorCabecera();

        if (errorOrtografico) {
            Reporte.logExito(test, " Bug detectado: Clase CSS 'contedenor-cabecera' encontrada (error ortográfico)", driver);
        } else {
            Reporte.logFallo(test, "No se detectó el error ortográfico en 'contedenor-cabecera'", driver);
        }

        assertTrue("Debería encontrarse el error ortográfico 'contedenor-cabecera'", errorOrtografico);
    }

    @Cuando("reviso los enlaces de navegación en el header")
    public void revisoLosEnlacesDeNavegacionEnElHeader() {//REVISAR
        Reporte.logInfo(test, "Revisando enlaces de navegación en el header", driver);
    }

    @Y("algunos enlaces deberían usar \"/Frontend/\" y otros no")
    public void algunosEnlacesDeberianUsarFrontendYOtrosNo() {
        boolean rutasInconsistentes = paginaFormulario.verificarRutasInconsistentesHeader();

        if (rutasInconsistentes) {
            Reporte.logExito(test, " Bug detectado: Rutas inconsistentes en navegación del header", driver);
        } else {
            Reporte.logFallo(test, "No se detectaron rutas inconsistentes en el header", driver);
        }

        assertTrue("Deberían existir rutas inconsistentes en el header", rutasInconsistentes);
    }

    @Cuando("inspecciono el enlace \"Caso 2\" en la navegación")
    public void inspeccionoElEnlaceCaso2EnLaNavegacion() {//siete
        Reporte.logInfo(test, "Inspeccionando enlace 'Caso 2' en la navegación", driver);
    }

    @Entonces("debería encontrar un espacio extra en el atributo href")
    @Entonces("el enlace debería contener \"/Frontend/Caso2.html \" con espacio al final")
    public void deberiaEncontrarUnEspacioExtraEnElAtributoHref() {//siete
        boolean espacioExtra = paginaFormulario.verificarEspacioExtraEnlaceCaso2();

        if (espacioExtra) {
            Reporte.logExito(test, "🐛 Bug detectado: Espacio extra al final del href en enlace 'Caso 2'", driver);
        } else {
            Reporte.logFallo(test, "No se detectó espacio extra en enlace 'Caso 2'", driver);
        }

        assertTrue("Debería encontrarse espacio extra en enlace 'Caso 2'", espacioExtra);
    }

    @Cuando("reviso los enlaces de la sección \"iniciosesion\"")
    public void revisoLosEnlacesDeLaSeccionIniciosesion() {//seis
        Reporte.logInfo(test, "Revisando enlaces de la sección iniciosesion", driver);
    }

    @Entonces("ambos enlaces deberían apuntar al mismo archivo \"login.html\"")
    @Entonces("esto debería representar un problema de funcionalidad")
    public void ambosEnlacesDeberianApuntarAlMismoArchivoLoginHtml() {//seis
        boolean enlacesDuplicados = paginaFormulario.verificarEnlacesDuplicadosLogin();

        if (enlacesDuplicados) {
            Reporte.logExito(test, "Bug detectado: Ambos enlaces (Registrarse e Iniciar sesión) apuntan a 'login.html'", driver);
        } else {
            Reporte.logFallo(test, "No se detectaron enlaces duplicados a login.html", driver);
        }

        assertTrue("Ambos enlaces deberían apuntar al mismo archivo login.html", enlacesDuplicados);
    }

    @Entonces("debería navegar a \"Index.html\"")
    @Entonces("podría fallar por inconsistencias de rutas")
    public void deberiaNavegar_A_IndexHtml() {//cinco LOGO
        boolean logoFuncional = paginaFormulario.verificarFuncionalidadLogoHeader();

        if (logoFuncional) {
            Reporte.logExito(test, " Logo del header tiene enlace válido a Index.html", driver);
        } else {
            Reporte.logFallo(test, " Problema con funcionalidad del logo", driver);
        }

        assertTrue("El logo debería tener enlace válido a Index.html", logoFuncional);
    }
    // Verificar múltiples problemas de estructura en el header
    @Cuando("analizo la estructura completa del header")
    public void analizoLaEstructuraCompletaDelHeader() {
        String informacion = paginaFormulario.obtenerInformacionBugsHeader();
        Reporte.logInfo(test, "Análisis completo del header:\n" + informacion, driver);
    }

    @Entonces("debería encontrar los siguientes problemas:")//cuatro
    public void deberiaEncontrarLosSiguientesProblemas(io.cucumber.datatable.DataTable dataTable) {
        // Verificar cada tipo de bug mencionado en la tabla
        boolean errorOrtografico = paginaFormulario.verificarErrorOrtograficoContenedorCabecera();
        boolean espaciosExtra = paginaFormulario.verificarEspacioExtraEnlaceCaso2();

        int bugsDetectados = 0;
        if (errorOrtografico) bugsDetectados++;
        if (espaciosExtra) bugsDetectados++;

        Reporte.logInfo(test, String.format(
                "Bugs detectados en header: %d/2 - Ortográfico: %s, Espacios: %s",
                bugsDetectados, errorOrtografico, espaciosExtra
        ), driver);

        assertTrue("Deberían detectarse múltiples bugs en el header", bugsDetectados >= 1);
    }


    @Cuando("hago clic en \"Caso 1\" desde el header")
    public void hagoClicEnCaso1DesdeElHeader() {
        System.out.println("=== HACIENDO CLIC EN CASO 1 ===");
        paginaFormulario.navegarDesdeHeader("Caso 1");

        // Esperar navegación
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("URL después del clic: " + driver.getCurrentUrl());
    }

    @Entonces("debería navegar correctamente a la página del caso 1")
    public void deberiaNaveguarCorrectamenteALaPaginaDelCaso1() {
        System.out.println("=== VERIFICANDO CASO 1 ===");

        String urlActual = driver.getCurrentUrl();
        String contenidoPagina = driver.getPageSource();

        // VERIFICACIÓN REAL: Debe llegar a la página SIN errores
        boolean urlCorrecta = urlActual.contains("Caso1");
        boolean hayError = paginaFormulario.tieneErrorServidor(contenidoPagina);
        boolean navegacionExitosa = urlCorrecta && !hayError;

        System.out.println("  → URL actual: " + urlActual);
        System.out.println("  → ¿URL contiene 'Caso1'?: " + urlCorrecta);
        System.out.println("  → ¿Hay error de servidor?: " + hayError);
        System.out.println("  → ¿Navegación exitosa?: " + navegacionExitosa);

        if (navegacionExitosa) {
            Reporte.logExito(test, " Navegación exitosa a Caso 1 desde header", driver);
        } else {
            String detalleError = hayError ? "Error de servidor detectado" : "URL incorrecta";
            Reporte.logFallo(test, " Fallo en navegación a Caso 1: " + detalleError + ". URL: " + urlActual, driver);
        }

        assertTrue("Debería navegar correctamente a Caso 1 sin errores", navegacionExitosa);
    }

    @Cuando("hago clic en \"Caso 2\" desde el header")
    public void hagoClicEnCaso2DesdeElHeader() {
        System.out.println("=== HACIENDO CLIC EN CASO 2 ===");
        paginaFormulario.navegarDesdeHeader("Caso 2");

        // Esperar navegación
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("URL después del clic: " + driver.getCurrentUrl());
    }

    @Entonces("debería navegar correctamente a la página del caso 2")
    @Entonces("esto debería funcionar a pesar del espacio extra en la URL")
    public void deberiaNaveguarCorrectamenteALaPaginaDelCaso2() {
        System.out.println("=== VERIFICANDO CASO 2 (CON POSIBLE ESPACIO EXTRA) ===");

        String urlActual = driver.getCurrentUrl();
        String contenidoPagina = driver.getPageSource();

        // VERIFICACIÓN REAL: Debe funcionar a pesar del espacio
        boolean urlCorrecta = urlActual.contains("Caso2");
        boolean hayError = paginaFormulario.tieneErrorServidor(contenidoPagina);
        boolean tieneEspaciosEnUrl = urlActual.contains("%20");
        boolean navegacionExitosa = urlCorrecta && !hayError;

        System.out.println("  → URL actual: " + urlActual);
        System.out.println("  → ¿URL contiene 'Caso2'?: " + urlCorrecta);
        System.out.println("  → ¿URL tiene espacios (%20)?: " + tieneEspaciosEnUrl);
        System.out.println("  → ¿Hay error de servidor?: " + hayError);
        System.out.println("  → ¿Navegación exitosa?: " + navegacionExitosa);

        if (navegacionExitosa) {
            String mensaje = tieneEspaciosEnUrl ?
                    "Navegación exitosa a Caso 2 (manejó correctamente el espacio en URL)" :
                    "Navegación exitosa a Caso 2";
            Reporte.logExito(test, mensaje, driver);
        } else {
            String detalleError;
            if (hayError && tieneEspaciosEnUrl) {
                detalleError = "Error de servidor causado por espacio extra en URL";
            } else if (hayError) {
                detalleError = "Error de servidor";
            } else {
                detalleError = "URL incorrecta";
            }
            Reporte.logFallo(test, "Fallo en navegación a Caso 2: " + detalleError + ". URL: " + urlActual, driver);
        }

        assertTrue("Debería navegar correctamente a Caso 2 a pesar del espacio extra en URL", navegacionExitosa);
    }


    @Cuando("inspecciono la imagen del logo en el header")
    public void inspeccionoLaImagenDelLogoEnElHeader() {
        Reporte.logInfo(test, "Inspeccionando imagen del logo en el header", driver);
    }

    @Entonces("debería tener el atributo alt con valor \"Softesting\"")
    @Entonces("esto debería ser correcto para accesibilidad")
    public void deberiaTenerElAtributoAltConValorSoftesting() {
        boolean altCorrecto = paginaFormulario.verificarAtributoAltLogo();

        if (altCorrecto) {
            Reporte.logExito(test, " Atributo alt del logo es correcto para accesibilidad", driver);
        } else {
            Reporte.logFallo(test, " Problema con atributo alt del logo", driver);
        }

        assertTrue("El logo debería tener atributo alt correcto", altCorrecto);
    }


}