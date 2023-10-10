@file:Suppress("PackageName")

package Android.Previsao_do_Tempo

import Android.Previsao_do_Tempo.databinding.ActivityMainBinding
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val email = binding.editTextEmail
        val password = binding.editTextPassWord
        val button = binding.botaoEntrar





        button.setOnClickListener {
            val emailValido = validaEmail(email.text.toString())
            if (!emailValido) Toast.makeText(this, "Email INVÁLIDO", Toast.LENGTH_SHORT).show()

            val senhaValida = validaSenha(password.text.toString())
            if (!senhaValida) Toast.makeText(this, "Senha INVÁLIDA", Toast.LENGTH_SHORT).show()

            if (autenticaEmailEPassword(email.text.toString(), password.text.toString())) {
                Log.i(TAG, "botão:  O botão foi clicado e o usuário está validado")
            }


            //Log.i(TAG, "onCreate:   O texto após o click no botão é ${email.text}")
            //            Toast.makeText(this, "O texto após o click no botão é ${login.text}", Toast.LENGTH_SHORT).show()
        }
    }

    //    public override fun onStart() {
    //        super.onStart()
    //        // Check if user is signed in (non-null) and update UI accordingly.
    //        val currentUser = auth.currentUser
    //        if (currentUser != null) {
    //            reload()
    //        }
    //    }

    fun autenticaEmailEPassword(email: String, password: String): Boolean {
        var retorno = false
        if (email.isEmpty() || password.isEmpty()) {
            retorno = false
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userLoged = auth.currentUser
                        Log.i(TAG, "LOGIN SUCESSO:  Usuário logado é ${userLoged?.email}")
                        Toast.makeText(this, "Usuário logado é ${userLoged?.email}", Toast.LENGTH_SHORT).show()
                        retorno = true
                    } else {
                        Log.w(TAG, "LOGIN FALHOU!! ", task.exception)
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        retorno = false
                    }
                }
        }
        return retorno
    }

    fun validaEmail(email: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, " Email não pode estar em branco", Toast.LENGTH_SHORT).show()
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validaSenha(password: String): Boolean {
        val minLength = 6 // Mínimo de 6 caracteres
        val hasDigit = password.any { it.isDigit() }
        val hasUppercase = password.any { it.isUpperCase() }

        return password.length >= minLength && hasDigit && hasUppercase
    }
}