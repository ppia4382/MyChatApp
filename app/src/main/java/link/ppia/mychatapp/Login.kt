package link.ppia.mychatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //hide the action bar in the login screen
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignup = findViewById(R.id.btnSignup)

        btnSignup.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {

            //These are variables to get the email and password inputted
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            //method where the email and password will be passed
            login(email, password)
        }
    }
    //Here will be the details of the login method created above
    private fun login(email: String, password: String) {
        //logic for logging in a user (check the documentation of login for firebase and kotlin)

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for logging in user
                    //code to go to the home activity
                    val intent = Intent(this@Login, MainActivity::class.java)
                    finish()
                    startActivity(intent)


                } else {
                    Toast.makeText(this@Login, "User does not exist", Toast.LENGTH_LONG).show()
                }
            }

    }
}




