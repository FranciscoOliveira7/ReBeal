package ipca.project.rebeal

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import ipca.project.rebeal.databinding.RegisterProfileBinding
import ipca.project.rebeal.ui.isPasswordValid
import ipca.project.rebeal.ui.isValidEmail


class RegisterActivity : AppCompatActivity() {



        private lateinit var auth: FirebaseAuth
        lateinit var binding: RegisterProfileBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            auth = Firebase.auth

            binding = RegisterProfileBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.RegistarButton.setOnClickListener {

                val username = binding.utilizadorID.text.toString()
                val email = binding.editTextEmailAddress.text.toString()
                val password = binding.editTextPasswordRegisto.text.toString()
                val password2 = binding.editTextPasswordRegistoConfirm.text.toString()

                if (password != password2){
                    Toast.makeText(
                        baseContext,
                        "Passwords do not match.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    return@setOnClickListener
                }

                if (!password.isPasswordValid()){
                    Toast.makeText(
                        baseContext,
                        "Password must have at least 6 chars.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    return@setOnClickListener
                }

                if (!email.isValidEmail()){
                    Toast.makeText(
                        baseContext,
                        "Email is not valid.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    return@setOnClickListener
                }

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            intent.putExtra("username", username)
                            startActivity(intent)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.${task.exception}",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }
    }