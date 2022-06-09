package com.example.myvideogamelist.views.actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myvideogamelist.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_MyVideoGameList) //para que se quite el splash screen

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}