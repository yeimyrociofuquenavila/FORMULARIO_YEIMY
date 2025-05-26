# Softesting Formulario de Contacto - Proyecto de Pruebas Automatizadas

Este proyecto contiene pruebas automatizadas para el formulario de contacto de Softesting, utilizando Selenium WebDriver con un enfoque BDD (Behavior-Driven Development) implementado con Cucumber. El objetivo es validar el funcionamiento, la UI/UX y detectar posibles bugs en la aplicación web.

## Tecnologías Utilizadas

* Java Development Kit (JDK): Versión 21.0.7 LTS.
* Maven: Versión 3.9.9.
* Cucumber: Versión 7.14.0, para la implementación de pruebas BDD.
* Selenium WebDriver: Versión 4.18.1, para la automatización de navegadores web.
* ExtentReports: Versión 5.1.1, para generar informes de pruebas detallados.
* JUnit: Versión 4.13.2, para la ejecución de pruebas.
* WebDriverManager: Versión 5.6.3 de Bonigarcia, para la gestión automática de controladores de navegadores.
* Logback: Versión 1.4.14, para el sistema de registro de logs.
* IDE: IntelliJ IDEA Community Edition 2025.1.

## Estructura del Proyecto

El proyecto está configurado como una aplicación Maven estándar con las siguientes dependencias principales:
* Cucumber: Framework para la implementación de pruebas BDD.
* Selenium WebDriver: Herramienta para la automatización de navegadores web.
* ExtentReports: Biblioteca para generar informes de pruebas detallados.
* WebDriverManager: Gestor automático de controladores de navegadores.
* Logback: Sistema de registro de logs.
* Commons IO: Biblioteca para operaciones de entrada/salida de archivos.

## Requisitos Previos

Para ejecutar este proyecto, necesitas tener instalado:

1. JDK 21
2. Maven 3.6.0 o superior
3. Un navegador web compatible (Chrome, Firefox, Edge)
4. IDE IntelliJ IDEA Community Edition 2025.1

## Configuración del Entorno

1. Clona este repositorio.
2. Asegúrate de tener configurado Java 21 en tu entorno.
3. Ejecuta `mvn clean install` para descargar todas las dependencias.

## Cómo ejecutar los tests

1. Ejecuta `mvn test` para Chrome.
2. Ejecuta RunCucumberTest.
3. Ejecuta `mvn test -Dbrowser=firefox` (requiere tener Firefox instalado).
4. Ejecuta `mvn test -Dbrowser=edge` (requiere tener Edge instalado).
5. Ejecutar el archivo `ejecutar_pruebas.bat` desde Windows.

## Reportes

Después de la ejecución de las pruebas, los reportes HTML se generan en:

`/reports/cucumber/index.html`

Extent Reports
Los reportes incluyen:
* Capturas de pantalla de cada paso de la prueba.
* Capturas de pantalla de fallos.
* Información detallada de la ejecución.

## Configuración de la Herramienta

El proyecto utiliza Maven para gestionar dependencias y construir la aplicación. La configuración principal se encuentra en el archivo `pom.xml`.

### Versiones Principales

* Java: 21
* Cucumber: 7.14.0
* ExtentReports: 5.1.1
* Selenium WebDriver: 4.18.1

### `PaginaFormulario.java` - Page Object del Formulario de Contacto

* Propósito: Encapsula las interacciones con la página del formulario de contacto de Softesting, proporcionando métodos para interactuar con sus elementos y realizar validaciones.
* Características Clave:
  * Define localizadores (`By`) para los elementos del formulario (nombre, email, barrio, asunto, mensaje, botones, mensajes de error y éxito, logo).
  * Incluye métodos para abrir la página, hacer clic en botones, ingresar texto en campos, llenar el formulario completo, y hacer clic en enlaces.
  * Proporciona métodos para obtener mensajes de error y verificar la visibilidad de mensajes de éxito o error específicos de campos.
  * Contiene lógica para verificar indicadores visuales de error y éxito en los campos del formulario.
  * Métodos para verificar errores ortográficos específicos en etiquetas (e.g., "Varrio" en lugar de "Barrio", "Mensage" en lugar de "Mensaje").
  * Funciones para verificar si un campo tiene atributo ID definido y para obtener información detallada de los IDs de todos los campos.
  * Métodos para verificar la carga de la página, detectar errores de servidor y confirmar que se está en la página correcta.
  * Funciones para verificar el comportamiento del submit del formulario (como el alert de error incorrecto o la falta de e.preventDefault()).
  * Métodos para verificar inconsistencias en rutas de navegación, enlaces duplicados y contenido repetitivo.
  * Simula la validación de email con una regex incorrecta presente en el JavaScript de la aplicación.
  * Incluye métodos para interactuar con el header, como verificar errores ortográficos en clases CSS, inconsistencias en rutas de navegación, espacios extra en enlaces, enlaces duplicados de login, y la funcionalidad del logo.
* Dependencias: Selenium WebDriver, Lombok (@Getter), Utilidades.Reporte.

### `Pasos.java` - Definiciones de Pasos de Cucumber

* Propósito: Implementa los pasos de prueba definidos en los archivos `.feature` para el formulario de contacto, utilizando Selenium WebDriver y el Page Object `PaginaFormulario`.
* Características Clave:
  * Define los métodos correspondientes a los pasos de Cucumber (@Dado, @Cuando, @Y, @Entonces).
  * Realiza la inicialización (@Before) y el cierre (@After) del WebDriver para cada escenario, gestionando ChromeDriver y maximizando la ventana.
  * Utiliza la clase PaginaFormulario para realizar las interacciones con la UI.
  * Incluye aserciones (assertTrue, assertFalse) para validar los resultados de las pruebas.
  * Integra ExtentReports a través de la clase Reporte para generar logs y capturas de pantalla de los pasos y fallos.
  * Implementa la lógica para verificar diversos bugs funcionales y de UI/UX, incluyendo errores ortográficos, ausencia de IDs en campos, validaciones incorrectas, y problemas de navegación.
  * Maneja específicamente los bugs relacionados con el header, como la clase CSS incorrecta, espacios extra en URLs, y enlaces duplicados.
* Dependencias: Selenium WebDriver, JUnit, Cucumber Java, Paginas.PaginaFormulario, Utilidades.Reporte, ExtentReports, WebDriverManager.

### `Reporte.java` - Utilidades para Informes de Pruebas

* Propósito: Genera informes de pruebas detallados y visuales utilizando ExtentReports.
* Características Clave:
  * Gestión de la inicialización de ExtentReports y ExtentSparkReporter.
  * Métodos para crear pruebas (crearPrueba) y registrar el estado de los pasos (logInfo, logExito, logFallo, logAdvertencia).
  * Funcionalidad para adjuntar capturas de pantalla al informe en cada paso relevante o en caso de fallo.
  * Asegura que los cambios en el informe se vuelquen (flush) al final de la ejecución.
* Uso: Se utiliza en la clase Pasos.java para documentar la ejecución de las pruebas.
* Dependencias: ExtentReports, Selenium WebDriver.

### Flujo de Ejecución

1. Los archivos `.feature` definen los escenarios de prueba en formato Gherkin.
2. Cucumber lee los archivos `.feature` y `Pasos.java` se encarga de interpretar y ejecutar cada paso.
3. La clase `PaginaFormulario.java` abstrae las interacciones con la interfaz de usuario del formulario de contacto.
4. Durante la ejecución, `Reporte.java` genera un informe detallado con logs y capturas de pantalla de cada paso y cualquier fallo detectado.

## Casos de Prueba (Archivos Feature)

Los casos de prueba están definidos en formato Gherkin y se encuentran en el directorio `src/test/resources/features`. Cubren los siguientes aspectos:

* **Validación de Campos Individuales**: Pruebas de validación para los campos Nombre, Email, Barrio y Asunto con datos válidos e inválidos.
* **Envío de Formulario**:
  * **Caso Positivo**: Envío exitoso del formulario con datos válidos.
  * **Caso Negativo**: Envío del formulario con campos vacíos y verificación de mensajes de error.
* **Detección de Bugs Críticos**:
  * Errores ortográficos en etiquetas ("Varrio", "Mensage").
  * Ausencia de atributos ID en campos del formulario.
* **Navegación**: Verificación de la navegación a la página principal a través del logo.
* **Bugs Funcionales Específicos**:
  * Comportamiento incorrecto del `submit` del formulario (muestra error incluso con datos válidos).
  * Regex incorrecta en la validación de email.
  * Ausencia de validación implementada en el campo de mensaje.
* **Bugs de Navegación y Estructura**:
  * Rutas inconsistentes en enlaces.
  * Enlaces duplicados (ej. a "Caso 5").
  * Contenido repetitivo incorrecto en descripciones.
* **Bugs de Código y Sintaxis**:
  * Espacios extra en referencias a archivos JavaScript.
* **Validaciones del Header**:
  * Errores ortográficos en clases CSS (`contedenor-cabecera`).
  * Inconsistencias en rutas de navegación en el header.
  * Espacio extra en el atributo `href` del enlace "Caso 2".
  * Enlaces duplicados a `login.html` en la sección de inicio de sesión.
  * Funcionalidad y atributo `alt` del logo del header.

Puedes encontrar el detalle completo de los escenarios y sus pasos en los archivos `.feature` dentro del proyecto.






