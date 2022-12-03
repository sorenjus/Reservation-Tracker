package com.example.reservationtracker

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val emailText = findViewById<EditText>(R.id.loginEmail)
        val passText = findViewById<EditText>(R.id.loginPassword)
        val loginBtn = findViewById<Button>(R.id.loginConfirm)
        val signupBtn = findViewById<Button>(R.id.loginSignup)

        auth = Firebase.auth
        val db = Firebase.firestore

        val docRef = db.collection("Restaurant")

        val restaurantEmails = ArrayList<String>()
        docRef.get().addOnSuccessListener {
            for (item in it.documents) {
                val emails = item.data!!["email"] as String
                restaurantEmails.add(emails)
                Log.d(TAG, emails)
            }
        }

        loginBtn.setOnClickListener {
            val email = emailText.text.toString()
            val password = passText.text.toString()

            if (email == "" || password == "") {
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        /*
                        Load table view
                         */

                        var isRestaurant = false
                        Log.d(TAG, email)
                        Log.d(TAG, restaurantEmails.toString())
                        for (item in restaurantEmails) {
                            Log.d(TAG, item)
                            if (item == email) {
                                isRestaurant = true
                                Log.d(TAG, "is restaurant")
                            }
                        }

                        if (isRestaurant) {
                            val i = Intent(this, DisplayReservations::class.java)
                            startActivity(i)
                        }
                        else{
                            Log.d(TAG, "user reservations")
                            val i = Intent(this, userReservations::class.java)
                            startActivity(i)
                        }
                    }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


        signupBtn.setOnClickListener {
            val i = Intent(this, Signup::class.java)
            startActivity(i)
        }
    }

    public override fun onResume() {
        super.onResume()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            /*
            Load table view
             */
        }
    }
}