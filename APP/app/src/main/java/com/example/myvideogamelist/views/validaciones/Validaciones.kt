package com.example.myvideogamelist.views.validaciones

import com.example.myvideogamelist.views.mensajes.Mensajes
import com.google.android.material.textfield.TextInputLayout

object Validaciones{

    /**
     * Cabecera: fun validacionPassword(password: TextInputLayout): Boolean
     * Descripcion: Este metodo se encargara de hacer una validacion basica al campo de texto introducido por parametros
     * Precondiciones: El campo de texto introducido por parametros debe existir
     * Postcondiciones: Se devolvera true si el campo texto tiene 6 o mas caracteres
     * Entrada:
     *      password: TextInputLayout -> El campo de texto a validar
     * Salida:
     *      eTPasswordCorrecta: Boolean -> True si es valido, false si no
     */
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

    /**
     * Cabecera: fun validacionEmail(email: TextInputLayout):Boolean
     * Descripcion: Este metodo se encargara de hacer una validacion basica al campo de texto introducido por parametros
     * Precondiciones: El campo de texto introducido por parametros debe existir
     * Postcondiciones: Se devolvera true si el campo texto tiene 3 o mas caracteres y contiene un "@"
     * Entrada:
     *      email: TextInputLayout -> El campo de texto a validar
     * Salida:
     *      eTCorreoCorrecto: Boolean -> True si es valido, false si no
     */
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

    /**
     * Cabecera: fun validacionRepetirPassword(repeatPassword: TextInputLayout, contrasenya: String):Boolean
     * Descripcion: Este metodo se encargara de hacer una validacion basica al campo de texto introducido por parametros
     * Precondiciones: El campo de texto introducido por parametros debe existir
     * Postcondiciones: Se devolvera true si la contraseña coincide con el campo de texto de repetir contraseña
     * Entrada:
     *      repeatPassword: TextInputLayout -> El campo de texto a validar
     *      contrasenya: String -> la contraseña verdadera
     * Salida:
     *      eTRepetirPassword: Boolean -> True si es valido, false si no
     */
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

    /**
     * Cabecera: fun validacionUsername(username: TextInputLayout):Boolean
     * Descripcion: Este metodo se encargara de hacer una validacion basica al campo de texto introducido por parametros
     * Precondiciones: El campo de texto introducido por parametros debe existir
     * Postcondiciones: Se devolvera true si el nombre de usuario tiene 3 o mas caracteres
     * Entrada:
     *      username: TextInputLayout -> El campo de texto a validar
     * Salida:
     *      eTUsername: Boolean -> True si es valido, false si no
     */
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