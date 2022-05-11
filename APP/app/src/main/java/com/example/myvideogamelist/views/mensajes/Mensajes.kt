package com.example.myvideogamelist.views.mensajes

class Mensajes {
    object errores{
        const val LOGIN_FALLIDO = "Algo ha fallado al iniciar sesión, compruebe su conexión a Internet e intentelo de nuevo más tarde"
        const val SIGNUP_FALLIDO = "Algo ha fallado al realizar el registro, compruebe su conexión a Internet e intentelo de nuevo más tarde"
        const val EMAIL_VACIO = "El correo electrónico no puede estar vacío"
        const val EMAIL_NOVALIDO = "El correo electrónico debe ser válido"
        const val SIGNUP_EMAIL_REPETIDO = "El email que ha introducido ya está en uso"
        const val PASSWORD_VACIO = "La contraseña no puede estar vacía"
        const val PASSWORD_MASDE6CHARS = "La contraseña debe de tener más de 6 caracteres"
        const val SIGNUP_PASSWORD_NO_COINCIDEN = "La contraseña introducida no coincide"
        const val SIGNUP_USERNAME_VACIO = "El nombre de usuario no puede estar vacío"
        const val CONEXION_INTERNET_FALLIDA = "Ha ocurrido un error desconocido, compruebe su conexión a Internet e intentelo de nuevo más tarde"
        const val LOGIN_SIGNUP_FALTAN_DATOS = "Faltan errores por solucionar o información por rellenar en los campos de textos"
        const val BORRAR_VIDEOJUEGO_QUE_NO_ESTA_EN_LISTA = "No puedes eliminar un videojuego que todavía no está en tu lista"
    }
    object informacion{
        const val LA_LISTA_ES_PRIVADA = "La lista del usuario es privada"
    }
}