package com.example.myvideogamelist.views.sharedData

import androidx.lifecycle.MutableLiveData

object SharedData {
    //Creo este object para no estar pasando el objeto Firebase auth entre todos los fragments
    var idUsuario : String = ""
    var nombreUsuario : MutableLiveData<String> = MutableLiveData("") //lo hago como liveData porque el nombre de usuario se rellenara gracias a una llamada a la API
}