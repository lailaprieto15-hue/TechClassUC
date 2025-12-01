# MANUAL DE USUARIO - TechClassUC: Soporte Inteligente

Bienvenido al sistema **TechClassUC**, una aplicación de escritorio diseñada para gestionar el flujo de clientes de soporte técnico, aplicando reglas de prioridad y brindando una funcionalidad completa de reversión de acciones.

## 1. Inicio y Estructura

La aplicación se inicia directamente mostrando el módulo principal de **Gestión y Monitoreo**.

### Encabezado del Sistema: Métricas Clave

El encabezado superior muestra el estado del sistema en tiempo real:
* **Cliente Actual:** El cliente que está en la parte frontal de la cola.
* **Total Clientes en Espera:** Cantidad de clientes en la cola (FIFO).
* **Total Clientes Atendidos:** Cantidad de clientes en el historial.
* **Promedio Atención:** Tiempo promedio (en segundos) que los clientes pasaron en el sistema antes de ser atendidos.

## 2. Flujo de Trabajo y Acciones Principales

Todas las operaciones se realizan desde el formulario de **Datos del Cliente** y la barra de **Botones de Acción** superior.

| Acción | Botón | Flujo de Uso |
| :--- | :--- | :--- |
| **Registrar Cliente** | **➕ AGREGAR** | Llene los campos **ID**, **Nombre**, **Tipo Solicitud** y **Prioridad**. El cliente se añade al final de la Cola de Espera. (El ID debe ser único en la cola). |
| **Atender Cliente** | **➡️ ATENDER** | Procesa al cliente en la parte frontal de la cola, basándose en la **Regla de Prioridad (2:1)**. El cliente pasa al Historial de Atendidos y se registra su tiempo de atención. |
| **Eliminar Cliente** | **🗑️ ELIMINAR** | Ingrese el **ID** del cliente en el campo **ID** y presione el botón. El cliente es removido de la Cola de Espera. **RESTRICCIÓN:** No se puede eliminar un cliente que ya haya sido Atendido. |
| **Revertir Acción** | **↩️ DESHACER** | Revierte la última acción reversible (Agregar, Eliminar o Atender). |

### Regla de Prioridad de Atención (2:1) 

El sistema prioriza automáticamente a los clientes Urgentes:
* Por cada **dos clientes Normales** atendidos consecutivamente, el sistema intentará atender al siguiente cliente con prioridad **Urgente** (si hay alguno en la cola).

## 3. Pestaña "Registro y Búsqueda"

Esta pestaña permite auditar y consultar los datos:
* **Registro de Acciones:** Muestra un log completo e inmutable de **todas** las operaciones realizadas, incluyendo los eventos de **DESHACER**, manteniendo la trazabilidad histórica.
* **Búsqueda en Historial:** Permite buscar clientes por **ID** o por **Tipo de Solicitud** entre los clientes ya atendidos.
