package com.example.myvideogamelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        var a: TextView
        a = findViewById(R.id.txt1234)
        a.text = if (auth.currentUser?.uid.equals("NqCpXpe1epMmqR7S1ilkMQkFhIy1")) "true" else "false"
    }
}