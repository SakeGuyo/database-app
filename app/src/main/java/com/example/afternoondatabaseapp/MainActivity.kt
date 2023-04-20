package com.example.afternoondatabaseapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var editName: EditText
    lateinit var editEmail: EditText
    lateinit var editIDNumber:EditText
    lateinit var btnSave: Button
    lateinit var btnView: Button
    lateinit var progressDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editName = findViewById(R.id.mEditName)
        editEmail = findViewById(R.id.mEdtemail)
        editIDNumber = findViewById(R.id.mEdtIDNumber)
        btnSave = findViewById(R.id.mbtnSave)
        btnView = findViewById(R.id.mbtnView)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Saving")
        progressDialog.setMessage("Please wait...")
        btnSave.setOnClickListener {
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
                // Create a reference to firebas database
                var ref = FirebaseDatabase.getInstance().
                        getReference().child("Users/"+id)
                // start saving
                progressDialog.show()
                ref.setValue(user).addOnCompleteListener {
                    progressDialog.dismiss()
                    if(it.isSuccessful){
                        Toast.makeText(this, "User saved succesfully",  Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "User saving failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        btnView.setOnClickListener {
            var intent = Intent(this,UsersActivity::class.java)
            startActivity(intent)
        }
    }
}