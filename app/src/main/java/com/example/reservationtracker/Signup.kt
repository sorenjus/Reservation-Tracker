package com.example.reservationtracker

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Signup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val emailText = findViewById<EditText>(R.id.signupEmail)
        val passText = findViewById<EditText>(R.id.signupPassword)
        val passConfText = findViewById<EditText>(R.id.signupPasswordConfirm)
        val userRadio = findViewById<RadioButton>(R.id.signupUser)
        val restaurantRadio = findViewById<RadioButton>(R.id.signupRestaurant)
        val confBtn = findViewById<Button>(R.id.signupConfirm)
        val backBtn = findViewById<Button>(R.id.signupBack)

        auth = Firebase.auth

        confBtn.setOnClickListener {
            val email = emailText.text.toString()
            val password = passText.text.toString()
            val passConf = passConfText.text.toString()
            val isRestaurant = restaurantRadio.isChecked

            if (email == "" || password == "" || passConf == "") {
                return@setOnClickListener
            }

            else if (password == passConf) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            if (isRestaurant) {
                                //Add data point with restaurant and email
                                // Create a new user
                                val user = hashMapOf(
                                    "email" to email,
                                )

                                // Add a new document with a generated ID
                                    db.collection("Restaurant").document(email)
                                        .set(user)
                            } else {
                                val user = hashMapOf(
                                    "email" to email,
                                )

                                // Add a new document with a generated ID
                                auth.currentUser?.let { it1 ->
                                    db.collection("Customer").document(email)
                                        .set(user)
                                }
                            }
                            this.finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        backBtn.setOnClickListener {
            this.finish()
        }
    }
}