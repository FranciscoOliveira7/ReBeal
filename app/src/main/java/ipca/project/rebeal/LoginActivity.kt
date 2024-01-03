package ipca.project.rebeal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import ipca.project.rebeal.databinding.LoginProfileBinding


const val TAG = "rebeal"
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var binding: LoginProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.LoginButton.setOnClickListener {

            val email = binding.editEmailAddress.text.toString()
            val password = binding.editPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {


                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed. ${task.exception}",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

        }

        binding.buttonRegistar.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}