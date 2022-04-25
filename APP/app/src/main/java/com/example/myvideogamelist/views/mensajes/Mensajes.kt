package com.example.myvideogamelist.views.mensajes

class Mensajes {
    object errores{
        const val LOGIN_FALLIDO = "Algo ha fallado al iniciar sesión, compruebe su conexión a Internet e intentelo de nuevo más tarde"
        const val SIGNUP_FALLIDO = "Algo ha fallado al realizar el registro, compruebe su conexión a Internet e intentelo de nuevo más tarde"
        const val EMAIL_VACIO = "El correo electrónico no puede estar vacío"
        const val EMAIL_NOVALIDO = "El correo electrónico debe ser válido"
        const val PASSWORD_VACIO = "La contraseña no puede estar vacía"
        const val PASSWORD_MASDE6CHARS = "La contraseña debe de tener más de 6 caracteres"
        const val SIGNUP_PASSWORD_NO_COINCIDEN = "La contraseña introducida no coincide"
        const val SIGNUP_USERNAME_VACIO = "El nombre de usuario no puede estar vacío"
    }
}