# marvel_kotlin

# Arquitectura
Arquitectura clean. Proyecto estructurado en un solo módulo `app` con las capas diferenciadas en packages.
En función del tamaño/complejidad del proyectos las capas pueden separarse en módulos o incluso los módulos por funcionalidad
para proyectos mucho más complejos.

* DI: inyección de dependencias del proyecto. Se utiliza Koin para la gestión de las dependencias. Se separan en diferentes 
`KoinModules` las dependencias por cada capa de la arquitectura. La información configurable se pasa a traves de `properties`
a Koin desde el `MarvelApp` 

* Presentation: capa UI. Implementa un patrón MVVM, separado por funcionalidades, cada carpeta incluye 
su vista, VM y adapter, viewholders, etc.
    * Extension: extension de utilidades para manejar los Livedata, View, etc.
    * Viewmodel: 
        - BaseViewModel padre para el manejo de las coroutinas a las que se llaman desde los demás VM.
    Añade un `SupervisorJob` y `CoroutineExceptionHandler` para controlar el funcionamiento y errores de las coroutinas.
    Se inyecta el `ioDispatcher` para el scope de las coroutinas y habilitar la implementación de tests.
        - Result: sealed class para encapsular los estados de las respuestas a la vista. Desde el VM se crean en función 
        de su estado de `Loading`, `Error`o `Success` para que las vistas pinten los datos en función del estado.
    
* Domain: lógica de negocio de la app. Se controla el flujo en función a errores y no excepciones. 
Simplificamos el manejo de errores al no tener que capturar las excepciones ni tener excepciones no controladas.
Utilizamos el tipo de datos Either para abstraer el error (`Left`) del éxito (`Right`) en la lógica de negocio.
Evitamos así el bifurcar o la utilización de callbacks para controlar los flujos.
    * Error: se definen los `Failure` con los que se maneja en la app.
    * Model: definición de los modelos de datos de negocio, que se utilizan en las demás capas.
    * Use case: casos de uso para ejecutar la lógica de negocio, se pueden anidar entre sí si fuese necesario para no llevar esa
    gestión a otra capa como los VM.
    * Repository: interfaz de repositorio para no tener dependencia de `data` desde `domain`(aunque estando en diferentes packages
    pero dentro del mismo módulo, están visibles)

* Data:
    * Network: capa de red para las llamadas web dentro de `data`.
        - Datasource: se definen los datasources para manejar las llamadas a red con retrofit. Hace uso de 
        un `BaseNetworkDatasource` en el que se recoge todo el flujo de llamada, manejor de errores, parseo de 
        datos y registro de logs de cualquier petición a red. Simplifica mucho la implementación de varios NetworkDatasources
        _ Service: interfaces retrofit para las llamadas red.
        - Interceptor: interceptor para retrofit para control del estado de la conexión.
        - Status: handler para controlar el estado de la conexión
        - Error: definición de `Failure` de la capa de red únicamente.
        - Model: modelo de datos de Api. Separamos de la de Domain para proteger nuestra lógica de la implementación
        concreta de los servicios, así filtramos campos, nullables, valores no esperados, etc.
        - Mapper: mapeadores (extensiones) de modelos de Api a Domain.
        - Logging: utilizades de Logs para guardar las trazas de todos los errores de las peticiones web. Se define tanto interfaz 
        para modificar su implementación como un `DefaultLogger` que pinta por consola todos los errores de las peticiones, y 
        poder registrar las trazas en producción en un crashlytics
    * Repository: implementación de los interfaces de dominio.
    
# Gradle
Para automatizar las builds y cambios de entorno, se añaden una serie de métodos y buenas prácticas de Gradle para automatizar las 
las construcciones y la configuración de cada entorno. En `variants` se recogen las variantes de url, credenciales, etc
necesarias en cada entorno, y se añaden al `BuildConfig` o `res` para que la aplicación las utilice facilmente
sin necesidad de lógica extra.