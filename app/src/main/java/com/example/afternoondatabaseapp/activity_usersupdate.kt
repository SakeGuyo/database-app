package com.example.afternoondatabaseapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class activity_usersupdate : AppCompatActivity() {
    lateinit var editName: EditText
    lateinit var editEmail: EditText
    lateinit var editIDNumber: EditText
    lateinit var btnUpdate: Button
    lateinit var progressDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usersupdate)
        editName = findViewById(R.id.mEditName)
        editEmail = findViewById(R.id.mEdtemail)
        editIDNumber = findViewById(R.id.mEdtIDNumber)
        btnUpdate = findViewById(R.id.mbtnUpdateUser)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Updating")
        progressDialog.setMessage("Please wait...")

        //Receive data from the intent
        var receivedName = intent.getStringExtra("name")
        var receivedEmail = intent.getStringExtra("email")
        var receivedIdNumber = intent.getStringExtra("idNumber")
        var receivedId = intent.getStringExtra("id")

        // Display the received data on the EditTexts
        editName.setText(receivedName)
        editEmail.setText(receivedEmail)
        editIDNumber.setText(receivedIdNumber)

        //Set an onClick listener to button update
        btnUpdate.setOnClickListener {
            // Receive data from user
            var name = editName.text.toString().trim()
            var email = editEmail.text.toString().trim()
            var idNumber = editIDNumber.text.toString().trim()
            var id = System.currentTimeMillis().toString()
            // Check if the user is submitting empty fields
            if (name.isEmpty()){
                editName.setError("Please fill this field")
                editName.requestFocus()
            }else if (email.isEmpty()){
                editEmail.setError("Please fill this field")
                editEmail.requestFocus()
            }else if (idNumber.isEmpty()) {
                editIDNumber.setError("Please fill this field")
                editIDNumber.requestFocus()
            }else{
                // proceed to save data
                var user = User(name, email, idNumber, id)
                // Create a reference to firebase database
                var ref = FirebaseDatabase.getInstance().
                getReference().child("Users/"+id)
                // start saving
                progressDialog.show()
                ref.setValue(user).addOnCompleteListener {
                    progressDialog.dismiss()
                    if(it.isSuccessful){
                        Toast.makeText(this, "User updated succesfully",
                            Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@activity_usersupdate,UsersActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this, "User updating failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}