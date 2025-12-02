Documentación del Proyecto TechClassUC
📋 Descripción General
TechClassUC es una aplicación de escritorio desarrollada en Java que implementa un sistema de gestión de cola de clientes con soporte para prioridades y funcionalidades de deshacer acciones. La aplicación utiliza una interfaz gráfica moderna con Swing y sigue el patrón MVC (Modelo-Vista-Controlador).
<hr></hr>
🏗️ Arquitectura del Proyecto
Estructura de Carpetas
TechClassUC-main/
├── src/main/java/
│   ├── app/
│   │   └── Main.java              # Punto de entrada de la aplicación
│   ├── controlador/
│   │   └── Controlador.java       # Lógica de negocio y eventos
│   ├── modelo/
│   │   ├── Cliente.java           # Entidad de cliente
│   │   ├── GestionCliente.java    # Gestión de cola de clientes
│   │   ├── HistorialAtencion.java # Registro de clientes atendidos
│   │   ├── RegistroAcciones.java  # Sistema de undo/redo
│   │   └── EstadisticasSistema.java # Cálculo de estadísticas
│   └── vista/
│       └── Vista.java             # Interfaz gráfica (GUI)
├── resources/
│   └── iconos/                    # Recursos gráficos
└── pom.xml                        # Configuración de Maven
<hr></hr>
📦 Componentes Principales
1. Main.java (Punto de Entrada)
// Inicializa la aplicación en el Event Dispatch Thread
SwingUtilities.invokeLater(() -> {
    Vista vista = new Vista();
    Controlador controlador = new Controlador(vista);
    vista.setVisible(true);
});
Responsabilidades:
Punto de entrada de la aplicación
Garantiza la ejecución en el hilo de eventos de Swing
<hr></hr>
2. Modelo (Capa de Datos)
Cliente.java
Representa un cliente en el sistema.
Atributos:
id: Identificador único del cliente
nombre: Nombre del cliente
tipoSolicitud: Tipo de servicio (Soporte, Mantenimiento, Reclamo)
prioridad: Nivel de atención (Normal, Urgente)
tiempoLlegada: Timestamp de registro en el sistema
tiempoAtencion: Timestamp de inicio de atención
Métodos principales:
Getters y setters para todos los atributos
toString(): Representación en texto
<hr></hr>
GestionCliente.java
Gestiona la cola de espera de clientes utilizando una ArrayDeque.
Métodos clave:
Método
Descripción
agregarCliente(Cliente)
Añade cliente al final de la cola
agregarClienteAlFrente(Cliente)
Inserta cliente al inicio (para deshacer)
atenderCliente()
Extrae cliente del frente (FIFO)
atenderClientePorPrioridad(String)
Busca cliente con prioridad específica
eliminarCliente(int id)
Remueve cliente por ID
buscarClienteEnEspera(int id)
Localiza cliente en espera
totalClientesEnEspera()
Retorna tamaño de la cola
clienteActual()
Obtiene cliente en frente sin remover
<hr></hr>
HistorialAtencion.java
Mantiene registro de clientes atendidos en una LinkedList.
Métodos principales:
Método
Descripción
agregarClienteAtendido(Cliente)
Registra cliente atendido
removerUltimoCliente(Cliente)
Elimina cliente del historial
buscarPorId(int)
Busca cliente en historial por ID
buscarPorTipoSolicitud(String)
Filtra clientes por tipo
calcularPromedioTiempoAtencion()
Calcula promedio en segundos
getHistorial()
Retorna lista completa
Cálculo de Promedio:
Promedio = Suma(tiempoAtencion - tiempoLlegada) / cantidad de clientes
<hr></hr>
RegistroAcciones.java
Sistema de auditoría con capacidad de deshacer (Undo).
Estructura interna - Clase Accion:
- tipoAccion: String        // "agregar", "eliminar", "atender"
- cliente: Cliente          // Referencia al cliente involucrado
- fechaHora: LocalDateTime  // Timestamp de la acción
Métodos principales:
Método
Descripción
registrarAccion(tipo, cliente)
Registra nueva acción
popUltimaAccionUndoable()
Extrae última acción deshacer
hayAccionesUndoable()
Verifica si hay undo disponible
getHistorialCompleto()
Retorna todas las acciones
Lógica Undo:
Solo acciones base se pueden deshacer: agregar, eliminar, atender
Las acciones de deshacer (deshacer_*) se registran pero no son undoables
Se usa una Stack para mantener orden LIFO
<hr></hr>
EstadisticasSistema.java
Contenedor de datos para estadísticas (actualmente sin uso activo).
<hr></hr>
3. Vista (Capa de Presentación)
Vista.java
Interfaz gráfica con Swing - componentes principales:
Botones de Acción:
btnAgregar (Azul): Agrega cliente a la cola
btnAtender (Naranja): Atiende cliente del frente
btnDeshacer (Gris): Revierte última acción
btnEliminar (Rojo): Elimina cliente de la cola
btnBuscar (Azul claro): Busca en historial
Tablas:
tblEspera: Clientes en espera
tblAtendidos: Clientes ya atendidos
tblAcciones: Registro de todas las acciones
tblBusqueda: Resultados de búsqueda
Etiquetas de Estadísticas:
lblClienteActual: Cliente siendo atendido
lblTotalEspera: Cantidad en cola
lblTotalAtendidos: Total histórico atendido
lblPromedioTiempoAtencion: Tiempo promedio en segundos
Características visuales:
Botones redondos con iconos
Paleta de colores: Azul (#1E88E5) y Naranja (#FFAA00)
Tooltips con retardo mínimo (1ms)
Interfaz con pestañas (tabs)
<hr></hr>
4. Controlador (Lógica de Negocio)
Controlador.java
Orquesta la comunicación entre modelo y vista.
Atributos principales:
- vista: Vista                      // Referencia a interfaz
- gestionCliente: GestionCliente    // Gestión de cola
- historialAtencion: HistorialAtencion  // Historial
- registroAcciones: RegistroAcciones    // Auditoría
- normalClientsAttendedCounter: int     // Contador para prioridades
Métodos Principales:
agregarCliente()
Solicita ID, nombre, tipo y prioridad mediante diálogos
Valida ID único en espera
Crea nuevo cliente y lo añade a la cola
Registra la acción
Flujo de validación:
ID válido? → Único en espera? → Nombre no vacío? → Tipo/Prioridad → Agregar
atenderCliente()
Implementa lógica de prioridades: Cada 2 clientes normales → 1 urgente
Extrae cliente de la cola
Registra timestamp de atención
Agrega a historial
Algoritmo de prioridades:
if (normalClientsAttendedCounter >= 2) {
    Buscar cliente "Urgente"
    Si existe → Resetear contador
}
Si no existe urgente → Atender cliente FIFO (Normal)
eliminarCliente()
Solo elimina clientes en espera
Valida que no esté ya atendido
Registra eliminación
deshacerUltimaAccion()
Extrae última acción de la pila
Revierte según tipo:
agregar → Elimina de cola
eliminar → Restaura a cola
atender → Retorna a inicio de cola
Ajusta contador de prioridades
buscarHistorial()
Acepta ID numérico o tipo de solicitud
Retorna resultados en tabla de búsqueda
actualizarEstadoBotones()
Habilita/deshabilita botones según estado:
Botón
Habilitado si...
Agregar
Siempre
Atender
Hay clientes en espera
Eliminar
Hay clientes en espera
Deshacer
Hay acciones undoable
Buscar
Hay clientes en historial
Métodos de Actualización
actualizarVistaCompleta()           // Actualiza todo
├── actualizarEstadisticas()        // Labels con números
├── actualizarClientesEnEspera()    // Tabla de cola
├── actualizarClientesAtendidos()   // Tabla de historial
├── actualizarRegistroAcciones()    // Tabla de auditoría
└── actualizarEstadoBotones()       // Estado de botones
<hr></hr>
🔄 Flujos Principales
Flujo de Atención de Cliente
┌─────────────────────┐
│  atenderCliente()   │
└──────────┬──────────┘
           │
      ¿2+ normales?
         ╱ ╲
       SÍ   NO
       │     │
  ¿Hay       Atender FIFO
 urgente?    (Normal)
   │ │
  SÍ NO
   │  │
   │  └→ Atender
   │     Normal
   │
   └→ Atender
      Urgente
      Reset contador
Flujo de Deshacer
┌────────────────────────┐
│ deshacerUltimaAccion() │
└───────────┬────────────┘
            │
      ¿Tipo de acción?
      ╱    │    ╲
   agregar eliminar atender
    │       │        │
  Quitar  Restaurar Retornar
  de cola a cola   a inicio
<hr></hr>
📊 Estructuras de Datos Utilizadas
Clase
Estructura
Ventaja
Uso
GestionCliente
ArrayDeque<Cliente>
O(1) insert/delete frente
Cola FIFO
HistorialAtencion
LinkedList<Cliente>
O(1) insert/delete
Historial ordenado
RegistroAcciones
Stack<Accion>
O(1) push/pop LIFO
Undo/Redo
RegistroAcciones
LinkedList<Accion>
Auditoría completa
Historial total
<hr></hr>
🎨 Patrones de Diseño
1. MVC (Modelo-Vista-Controlador)
Modelo: Clases en paquete modelo
Vista: Vista.java
Controlador: Controlador.java
2. Observer Pattern (Implícito)
Listeners de botones en Controlador
Actualización automática de vistas
3. Command Pattern (Undo/Redo)
RegistroAcciones.Accion encapsula comando
Stack para historial reversible
<hr></hr>
🔐 Validaciones Implementadas
ID cliente:
Debe ser numérico
Único en espera (no duplicados)
Nombre cliente:
No puede estar vacío
Eliminación:
No se pueden eliminar clientes ya atendidos
Búsqueda:
Campo no vacío requerido
<hr></hr>
📈 Cálculos de Estadísticas
Promedio Tiempo Atención
Fórmula: Σ(tiempoAtencion - tiempoLlegada) / cantidad
Unidad: Segundos
Precisión: 2 decimales
Casos especiales: Si no hay clientes → 0.0
Contador de Prioridades
Propósito: Garantizar equidad en atención
Lógica:
  - Incrementa con cada cliente "Normal" atendido
  - Se resetea cuando se atiende cliente "Urgente"
  - Máximo de 2 normales antes de buscar urgente
<hr></hr>
🛠️ Tecnologías Utilizadas
Componente
Tecnología
Lenguaje
Java 11+
GUI
Swing (JFrame, JPanel, JTable)
Build
Maven
Estructuras
Collections Framework
Fecha/Hora
java.time.LocalDateTime
<hr></hr>
📝 Configuración de Maven
<!-- pom.xml -->
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>techclassuc</groupId>
    <artifactId>TechClassUC</artifactId>
    <version>1.0</version>
    <packaging>jar</packaging>
    
    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
    </properties>
</project>
<hr></hr>
🚀 Cómo Ejecutar
Compilar
mvn clean compile
Ejecutar
mvn exec:java -Dexec.mainClass="app.Main"
Empaquetar
mvn package
<hr></hr>
🐛 Consideraciones de Debugging
Casos críticos a probar:
Agregar cliente con ID duplicado
Atender cuando cola está vacía
Deshacer múltiples veces consecutivas
Búsqueda con tipos parciales
Validación de timestamps con LocalDateTime.now()
<hr></hr>
📌 Notas Importantes
Thread Safety: Actual implementación NO es thread-safe. Para multithreading se requeriría Collections.synchronizedList()
Persistencia: No hay guardado a disco. Datos se pierden al cerrar
Escalabilidad: Estructura actual soporta ~10,000 clientes sin problemas de performance
Interfaz: Diseño responsivo con ToolTips instantáneos (1ms delay)
<hr></hr>
Versión de Documentación: 1.0
Fecha: 2025
Autor: Equipo TechClassUC
Estado: Completo