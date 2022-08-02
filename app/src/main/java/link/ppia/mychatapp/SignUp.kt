package link.ppia.mychatapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //hide the action bar in the signup screen
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        //Here the above will be initialized
        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnSignup = findViewById(R.id.btnSignup)

        //set the listeners for the button
        btnSignup.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            //create a "signup" method to create an account for the user
            signUp(name, email, password)
        }

    }

    private fun signUp(name: String, email: String, password: String) {
        //logic for creating a user
        //https://firebase.google.com/docs/auth/android/password-auth#kotlin+ktx_3

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    //so that the data can be fetched and placed in the recyclerview, they have to be added in the database upon signing in.
                    //add the Name, Email and UID of the user.
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)

                    //code to go to the home activity
                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this@SignUp, "Some error occured", Toast.LENGTH_LONG).show()
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {

//        /**Now , we have to initialize mDbRef through this method "addUserToDatabase". Here we have
//        the variablle and its value. After having the reference of the database in getReference,
//        add the data to it.
//        "child" will add the node to the database and we need to pass the path which is the uid. The path
//        is according to the node in the uid so that we have a unique node for every user.
//        */
//
//        mDbRef = FirebaseDatabase.getInstance().getReference()
//
//        /**"child" will add the node to the database and we need to pass the path which is the uid. The path
//        is according to the node in the uid so that we have a unique node for every user.
//         *
//         * Before passing the uid, a parent node of "user" will be created. Inside the parent node, there will be
//         * multiple users.
//         *
//         * Set the value of the user so create a user and pass the name, email, and uid.
//         * So this now will add user to the database.
//         * (now you have to remove all the users from the Authentication in firebase console.
//         * */
//        mDbRef.child("user").child(uid!!).setValue(User(name, email,uid))

        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid))

    }
}