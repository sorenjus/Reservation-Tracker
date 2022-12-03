package com.example.reservationtracker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import java.time.format.DateTimeFormatter

class CreateReservation : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    data class Reservation(
        val email: String? = null,
        val name: String? = null,
        val timeVal: String? = null,
        val sizeVal: String? = null,
        val resEmail: String? = null
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_reservation)
        val db = Firebase.firestore
        auth = Firebase.auth

        val emailText = findViewById<EditText>(R.id.createEmail)
        val nameText = findViewById<EditText>(R.id.createName)
        val timeEstText = findViewById<EditText>(R.id.createTime)
        val sizeText = findViewById<EditText>(R.id.createTableSize)
        val createBtn = findViewById<Button>(R.id.createCreate)
        val backBtn = findViewById<Button>(R.id.createBack)

        val formatter = DateTimeFormatter.ofPattern("HH:mm")


        createBtn.setOnClickListener {
            val email = emailText.text.toString()
            val name = nameText.text.toString()
            var timeVal = timeEstText.text.toString()
            val sizeVal = sizeText.text.toString()
            val resEmail = auth.currentUser?.email

            timeVal = timeVal.format(formatter)

            if (email != "" && name != "" && timeVal != "" && sizeVal != "" && resEmail != "") {
                // Create a new reservation entry
                val reservation = Reservation(email, name, timeVal, sizeVal, resEmail)

                // Add a new document with a generated ID
                auth.currentUser?.let { it1 ->
                    it1.email?.let { it2 ->
                        db.collection("Restaurant").document(it2)
                            .collection("Reservations").document(email).set(reservation)
                    }
                }

                db.collection("Customer").document(email)
                    .collection("Reservations").add(reservation)
                this.finish()
                }
        }

        backBtn.setOnClickListener {
            this.finish()
        }


    }
}