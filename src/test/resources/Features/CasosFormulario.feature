# language: es
@FormularioContacto
Característica: Validar el funcionamiento del formulario de contacto Softesting
  Como usuario del sistema
  Quiero completar un formulario de contacto
  Para poder enviar un mensaje a la empresa

  Antecedentes:
    Dado que el usuario accede al formulario de contacto

  @ValidacionNombre
  Esquema del escenario: Validar diferentes entradas en el campo nombre
    Cuando el usuario ingresa "<entrada>" en el campo "nombre"
    Entonces el campo "nombre" debe mostrar indicador visual de "<resultado>"
    Ejemplos:
      | entrada                    | resultado |
      | AB                        | error     |
      | José                      | correcto  |
      | Yeimy Rocio Fuquen Avila  | correcto  |
      | Juan@                     | error     |
      | Carlos123                 | error     |

  @ValidacionEmail
  Esquema del escenario: Validar diferentes formatos de email
    Cuando el usuario ingresa "<email>" en el campo "email"
    Entonces el campo "email" debe mostrar indicador visual de "<resultado>"
    Ejemplos:
      | email                           | resultado |
      | correo.com                      | error     |
      | test@correo                     | error     |
      | yeimyrociofuquenavila@gmail.com | correcto  |
      | usuario@dominio.co              | correcto  |

  @ValidacionBarrio
  Esquema del escenario: Validar diferentes entradas en el campo barrio
    Cuando el usuario ingresa "<barrio>" en el campo "barrio"
    Entonces el campo "barrio" debe mostrar indicador visual de "<resultado>"
    Ejemplos:
      | barrio   | resultado |
      | AB       | error     |
      | La paz   | correcto  |
      | Soacha#  | error     |
      | 1234     | error     |
      | Centro   | correcto  |

  @ValidacionAsunto
  Esquema del escenario: Validar diferentes entradas en el campo Asunto
    Cuando el usuario ingresa "<asunto>" en el campo "asunto"
    Entonces el campo "asunto" debe mostrar indicador visual de "<resultado>"
    Ejemplos:
      | asunto                          | resultado |
      | Solicitud                       | correcto  |
      | Petici@n                        | error     |
      | Reunión 2025 - Actualización #3 | correcto  |

  @CasoPositivo
  Escenario: Envío exitoso de formulario con datos válidos
    Cuando lleno el campo nombre con "María González"
    Y lleno el campo email con "maria.gonzalez@gmail.com"
    Y lleno el campo barrio con "Centro"
    Y lleno el campo asunto con "Consulta sobre servicios"
    Y lleno el campo mensaje con "Necesito información detallada sobre los servicios que ofrecen. Estoy interesada en conocer precios y disponibilidad."
    Y hago clic en el botón Enviar
    Entonces debería ver el mensaje de envío exitoso

  @CasoNegativo
  Escenario: Validar campos obligatorios vacíos
    Cuando dejo todos los campos vacíos
    Y hago clic en el botón Enviar
    Entonces deberían aparecer mensajes de error para cada campo


  @BugsCriticos
  Escenario: Detectar errores ortográficos en etiquetas
    Cuando reviso las etiquetas del formulario
    Entonces debería encontrar "Varrio" en lugar de "Barrio"
    Y debería encontrar "Mensage" en lugar de "Mensaje"


  @BugsCriticos
  Esquema del escenario: Verificar ausencia de atributo ID en campos del formulario
    Cuando inspecciono el campo "<campo>"
    Entonces no debería tener atributo ID definido

    Ejemplos:
      | campo   |
      | email   |
      | barrio  |
      | asunto  |
      | mensaje |

  @Navegacion
  Escenario: Navegación a página principal mediante logo
    Cuando el usuario hace clic en el logo
    Entonces debe ser redirigido a la página "Index.html"


  @BugsFuncionales
  Escenario: Detectar comportamiento  del submit del formulario
    Cuando lleno el formulario con datos válidos y lo envío
    Entonces el formulario debería mostrar mensaje de error incorrectamente

  @BugsFuncionales
  Escenario: Verificar regex incorrecta en validación de email
    Cuando verifico la validación del campo email
    Entonces debería fallar la validación por regex incorrecta

  @BugsFuncionales
  Escenario: Verificar ausencia de validación en campo mensaje
    Cuando verifico la validación del campo mensaje
    Entonces  deberia tener validar validación implementada

  @BugsNavegacion
  Escenario: Detectar rutas inconsistentes en enlaces
    Dado que navego a la página de índice
    Cuando reviso los enlaces de navegación
    Entonces debería encontrar rutas inconsistentes

  @BugsNavegacion
  Escenario: Detectar enlaces duplicados a Caso 5
    Dado que navego a la página de índice
    Cuando reviso los enlaces de navegación
    Entonces debería encontrar enlaces duplicados a Caso 5

  @BugsContenido
  Escenario: Detectar contenido repetitivo incorrecto
    Dado que navego a la página de índice
    Cuando reviso el contenido de las descripciones
    Entonces debería encontrar contenido repetitivo incorrecto

  @BugsSintaxis
  Escenario: Detectar espacios extra en referencias JavaScript
    Cuando inspecciono las referencias de archivos JavaScript
    Entonces debería encontrar espacios extra en las referencias

  @bug @header
  Escenario: Verificar consistencia en enlaces de registro e inicio de sesión
    Cuando reviso los enlaces de la sección "iniciosesion"
    Entonces ambos enlaces deberían apuntar al mismo archivo "login.html"
    Y esto debería representar un problema de funcionalidad


  @bug @header
  Escenario: Verificar múltiples problemas de estructura en el header
    Cuando analizo la estructura completa del header
    Entonces debería encontrar los siguientes problemas:
      | Tipo de Bug              | Descripción                                    |
      | Ortográfico             | "contedenor-cabecera" en lugar de "contenedor" |
      | Espacios extra          | Espacio al final de "/Frontend/Caso2.html "   |


  @bug @header @navegacion
  Escenario: Verificar que la navegación del header funciona correctamente
    Cuando hago clic en "Caso 1" desde el header
    Entonces debería navegar correctamente a la página del caso 1

  Escenario: Verificar que la navegación del header funciona correctamente
    Cuando hago clic en "Caso 2" desde el header
    Entonces debería navegar correctamente a la página del caso 2
    Y esto debería funcionar a pesar del espacio extra en la URL

  @bug @header @accesibilidad
  Escenario: Verificar que el atributo alt de la imagen del logo está presente
    Cuando inspecciono la imagen del logo en el header
    Entonces debería tener el atributo alt con valor "Softesting"
    Y esto debería ser correcto para accesibilidad

