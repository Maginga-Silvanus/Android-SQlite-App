package com.example.afternoonsqliteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var edtName:EditText
    lateinit var edtEmail:EditText
    lateinit var edtIdNumber:EditText
    lateinit var btnSave:Button
    lateinit var btnView:Button
    lateinit var btnDelete:Button
    lateinit var db:SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtName = findViewById(R.id.editTextTextPersonName)
        edtEmail = findViewById(R.id.mEditEmail)
        edtIdNumber = findViewById(R.id.mEditPhone)
        btnSave = findViewById(R.id.mBtnSave)
        btnView = findViewById(R.id.mBtnView)
        btnDelete = findViewById(R.id.mBtnDelete)
        //Create a database called eMobilisDB
        db = openOrCreateDatabase("eMobilis", Context.MODE_PRIVATE, null)
        //Create a table inside the database
        db.execSQL("CREATE TABLE IF NOT EXISTS user(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")
        //Set onClick listeners to the butons
        btnSave.setOnClickListener {
            var name = edtName.text.toString().trim()
            var email = edtEmail.text.toString().trim()
            var idNumber = edtIdNumber.text.toString().trim()
            //Check if the user is submitting empty fields
            if(name.isEmpty() || email.isEmpty() || idNumber.isEmpty()){
                //Use the message function to display the message
                message("EMPTY FIELDS","Please fill all inputs")
            }else{
                // Proceed to save data
                db.execSQL("INSERT INTO users VALUES('"+name+"','"+email+"','"+idNumber+"')")
                clear()
                message("SUCCESS!!","User saved successfully!!!")
            }
        }
        btnView.setOnClickListener {
            var cursor = db.rawQuery("SELECT * FROM users", null)
            // Check if there's any record in the database
            if (cursor.count == 0){
                message("NO RECORDS!!!","Sorry, no users were found!!!")
            }else{
                //Use a String buffer to append all users retrieved using loop
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedIdNumber = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedIdNumber+"\n\n")
                }
                message("USERS",buffer.toString())

            }
        }
        btnDelete.setOnClickListener {
            var idNumber = edtIdNumber.text.toString().trim()
            if (idNumber.isEmpty()){
                message("EMPTY FIELD!","Please fill the ID field")
            }else{
                var cursor = db.rawQuery("SELECT FROM users WHERE kitambulisho='"+idNumber+"'", null)
                if (cursor.count == 0){
                    message("NO RECORDS!","Sorry, no user found!!!")
                }else{
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+idNumber+"'", null)
                    clear()
                    message("DELETE!!","User deleted successfully!!!")
                }
            }
        }
    }
    fun message(title:String, message:String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("cancel",null)
        alertDialog.create().show()
    }
    fun clear(){
        edtName.setText("")
        edtEmail.setText("")
        edtIdNumber.setText("")
    }
}