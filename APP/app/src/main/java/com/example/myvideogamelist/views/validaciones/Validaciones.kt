package com.example.myvideogamelist.views.validaciones

import com.example.myvideogamelist.views.mensajes.Mensajes
import com.google.android.material.textfield.TextInputLayout

object Validaciones{

    fun validacionPassword(password: TextInputLayout): Boolean {
        var eTPasswordCorrecta = false

        if(password.editText!!.text!!.length >= 6){
            eTPasswordCorrecta = true
            password.error = null
        }else{
            password.error = Mensajes.errores.PASSWORD_MASDE5CHARS
        }
        return eTPasswordCorrecta
    }

    fun validacionEmail(email: TextInputLayout):Boolean {
        var eTCorreoCorrecto = false

        if(email.editText!!.text!!.length >= 3 && email.editText!!.text!!.contains("@")){
            eTCorreoCorrecto = true
            email.error = null
        }else{
            email.error = Mensajes.errores.EMAIL_NOVALIDO
        }
        return eTCorreoCorrecto
    }

    fun validacionRepetirPassword(repeatPassword: TextInputLayout, contrasenya: String):Boolean{
        var eTRepetirPassword = false

        if(repeatPassword.editText!!.text!!.toString() == contrasenya){
            eTRepetirPassword = true
            repeatPassword.error = null
        }else{
            repeatPassword.error = Mensajes.errores.SIGNUP_PASSWORD_NO_COINCIDEN
        }
        return eTRepetirPassword
    }

    fun validacionUsername(username: TextInputLayout):Boolean{
        var eTUsername = false

        if(username.editText!!.text.toString().length > 2){
            eTUsername = true
            username.error = null
        }else{
            username.error = Mensajes.errores.USERNAME_MASDE2CHARS
        }
        return eTUsername
    }

}