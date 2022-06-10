package com.example.myvideogamelist.views.mensajes

class Mensajes {
    /**
     * En esta clase defino los mensajes que se mostraran en los distintos Snackbars a lo largo de la aplicación
     */
    object errores{
        const val LOGIN_FALLIDO = "Algo ha fallado al iniciar sesión, compruebe su conexión a Internet e intentelo de nuevo más tarde"
        const val SIGNUP_FALLIDO = "Algo ha fallado al realizar el registro, compruebe su conexión a Internet e intentelo de nuevo más tarde"
        const val EMAIL_NOVALIDO = "El correo electrónico debe ser válido"
        const val SIGNUP_EMAIL_REPETIDO = "El email que ha introducido ya está en uso"
        const val PASSWORD_MASDE5CHARS = "La contraseña debe de tener 6 o más caracteres"
        const val SIGNUP_PASSWORD_NO_COINCIDEN = "La contraseña introducida no coincide"
        const val CONEXION_INTERNET_FALLIDA = "Ha ocurrido un error desconocido, compruebe su conexión a Internet e intentelo de nuevo más tarde"
        const val LOGIN_SIGNUP_FALTAN_DATOS = "Faltan errores por solucionar o información por rellenar en los campos de textos"
        const val BORRAR_VIDEOJUEGO_QUE_NO_ESTA_EN_LISTA = "No puedes eliminar un videojuego que todavía no está en tu lista"
        const val USERNAME_YA_EN_USO = "Error, ¿nombre de usuario en uso?"
        const val ERROR_GENERICO = "Ha ocurrido un error desconocido"
        const val USERNAME_MASDE2CHARS = "El nombre de usuario debe tener más de 2 carácteres"
        const val ELIGE_UN_ESTADO = "Elija un estado, por favor"
        const val NO_USUARIOS_ENCONTRADOS = "No se han encontrado usuarios"
        const val VIDEOJUEGO_NO_BORRADO = "Ha ocurrido un problema mientras se intentaba eliminar %s de la lista, intentelo de nuevo mas tarde"
        const val VIDEOJUEGO_NO_ANYADIDO = "Ha ocurrido un problema mientras se intentaba añadir %s a la lista, intentelo de nuevo mas tarde"
        const val VIDEOJUEGO_NO_EDITADO = "Ha ocurrido un problema mientras se intentaba editar %s, intentelo de nuevo mas tarde"
    }
    object informacion{
        const val LA_LISTA_ES_PRIVADA = "La lista del usuario es privada"
        const val PEDIR_CORRECION_DE_ERRORES = "Por favor, arregla los problemas informados por pantalla"
        const val MODIFICACION_CREDENCIALES_EXITOSA = "Se han modificado los credenciales exitosamente, vuelva a iniciar sesión, por favor"
        const val MODIFICACION_CREDENCIALES_EXITOSA_NO_RELOG = "Se han modificado los credenciales exitosamente"
        const val CAMBIOS_CORRECTOS_GENERICO = "Los cambios se han efectuado correctamente"
        const val NO_VIDEOJUEGOS_EN_LISTA = "Todavía no hay videojuegos en esta lista"
        const val ERRORES_SIN_CORREGIR = "Hay errores sin corregir en el formulario"
        const val VIDEOJUEGO_BORRADO = "Se ha eliminado %s de la lista"
        const val VIDEOJUEGO_ANYADIDO = "Se ha añadido %s a la lista satisfactoriamente"
        const val VIDEOJUEGO_EDITADO = "Se ha modificado %s satisfactoriamente"
        const val NOMBRE_USUARIO_CAMBIADO_CORRECTAMENTE = "Se ha modificado el nombre de usuario satisfactoriamente"
    }
}