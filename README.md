# AndreasJoyas # 

Este repositorio contiene la configuración y el setup del backend para la aplicación AndreaJoyas, una tienda de joyas desarrollada con Android y Firebase. A continuación, se presentan las instrucciones para configurar y ejecutar el backend, junto con una descripción de los servicios implementados.

## Requisitos Previos ##

Antes de continuar, hay que asegurarse de tener instalado y configurado lo siguiente:

Node.js (si se utilizan Firebase Cloud Functions, se recomienda la versión 14.x o superior)
Firebase CLI
Java Development Kit (JDK) (versión 11 o superior, para compatibilidad con Android Studio)
Android Studio (para la aplicación frontend)
Una cuenta de Firebase y un proyecto (para esto hay que crear uno en Firebase Console)
Cuenta de Google con acceso a Firebase y Google Cloud

## Instrucciones de Configuración ##
### 1. Inicializar el Proyecto Firebase ###

Accedimos a la Firebase Console y creamos un nuevo proyecto llamado "AndreaJoyas".
Habilitamos Autenticación y configuramos el inicio de sesión con correo y contraseña.
Habilitamos Firestore y configuramos la base de datos con la siguiente estructura de colección:
usuarios (documentos con campos: nombre, apellido, telefono, direccion, email, rol)


### 2. Configurar Firebase en el Proyecto ###

Descargamos el archivo google-services.json desde la Firebase Console y lo colocamos en el directorio app/ de nuestro proyecto Android.
Iniciamos sesión en Firebase con firebase login usando nuestra cuenta de Google.

### 3. Desplegar el Backend ###

Desplegamos las reglas y configuraciones de seguridad de Firestore:firebase deploy --only firestore:rules

Verificamos el despliegue en la Firebase Console en las secciones "Database" y "Functions".

### 4. Ejecutar la Aplicación Android ###

Abrimos el proyecto Android en Android Studio.
Sincronizamos el proyecto con los archivos Gradle.
Usamos el emulador integrado de Android Studio (se puede conectar también un dispositivo android).
Ejecutamos la app usando el botón "Run" en Android Studio.
La aplicación se conectará automáticamente al backend Firebase usando la configuración de google-services.json.

## Servicios Implementados ##
### 1. Gestión de Autenticación de Usuarios ###

Descripción: Maneja el registro y el inicio de sesión de usuarios mediante correo y contraseña a través de Firebase Authentication.
Funciones/EndPoints:
login(email, password): Autentica a un usuario y devuelve un token de sesión.
register(email, password): Crea una nueva cuenta de usuario y almacena datos básicos en Firestore.


Uso: Accedido a través de LoginScreen y RegisterScreen en la aplicación Android.

### 2. Gestión de Perfil de Usuario ###

Descripción: Administra los datos del perfil del usuario (nombre, apellido, teléfono, dirección) almacenados en Firestore.
Funciones/EndPoints:
saveProfile(data): Guarda o actualiza la información del perfil del usuario en la colección usuarios.
getProfile(userId): Recupera los datos del perfil del usuario actual.


Uso: Integrado con CompleteProfileScreen y ProfileScreen para la entrada y edición de datos.

### 3. Almacenamiento de Datos ###

Descripción: Utiliza Firestore para almacenar y recuperar datos de usuario de forma persistente.
Colección: usuarios con documentos que contienen campos específicos del usuario.
Uso: Soporta los flujos de completación y edición de perfil en la aplicación.

### 4. (Opcional) Funciones Personalizadas de Cloud ###

Descripción: Si se implementan, permiten lógica del lado del servidor personalizada (por ejemplo, notificaciones, validación de datos).
Ejemplo: Una función para enviar correos de bienvenida o validar números de teléfono.
Uso: Desplegado y desencadenado mediante HTTP o eventos de base de datos según sea necesario.

## Solución de Problemas ##

Problemas de Autenticación: se debe de asegurar de que los proveedores de correo/contraseña estén habilitados en la Firebase Console.
Errores de Firestore: Verificar las reglas de seguridad es necesario y asegurarse de que google-services.json esté correctamente colocado.
Fallos de Despliegue: Confirmar que la Firebase CLI esté autenticada y tenga seleccionado el proyecto correcto (firebase use <project-id>).
