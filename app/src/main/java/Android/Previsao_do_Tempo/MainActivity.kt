@file:Suppress("PackageName")

package Android.Previsao_do_Tempo

import Android.Previsao_do_Tempo.databinding.ActivityMainBinding
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
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
        val buttonEntrar = binding.botaoEntrar
        val linkCadastro = binding.linkDeCadastro

        //NAVEGANDO PARA TELA DE CADASTRO
        linkCadastro.setOnClickListener {startActivity(Intent(this, CadastroActivity::class.java))}

        // ESCONDENDO SENHA AO CLICAR NO TOOGLE
        binding.textInputLayoutSenha.setEndIconOnClickListener {
            val passwordVisible = (password.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
            val inputType = if (passwordVisible) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            password.inputType = inputType
            password.setSelection(password.text!!.length)
        }

        //VALIDANDO EMAIL E SENHA E AUTENTICANDO APÓS CLICAR EM ENTRAR
        buttonEntrar.setOnClickListener {
            val emailValido = validaEmail(email.text.toString(), this)
            if (!emailValido) {
                binding.textInputLayoutEmail.error = getString(R.string.email_invalido)
            }

            val senhaValida = validaSenha(password.text.toString())
            if (!senhaValida) {
                binding.textInputLayoutSenha.error = getString(R.string.senha_invalida)
            }

            if (emailValido && senhaValida) {
                autenticaEmailEPassword(email.text.toString(), password.text.toString()) { resultado ->
                    if (resultado) {
                        // Autenticação bem sucedida - Navegar para outra tela
                        // startActivity(Intent(this,CadastroActivity::class.java))
                        Log.i(TAG, "onCreate:    PODE NAVEGAR PRA OUTRA TELA")
                    } else {
                        Log.i(TAG, "onCreate: autenticação inválida")
                    }
                }
            }
        }
    }


    private fun autenticaEmailEPassword(email: String, password: String, callback: (Boolean) -> Unit) {

        if (email.isEmpty() || password.isEmpty()) {
            callback(false)
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    val retorno = if (task.isSuccessful) {
                        val userLoged = auth.currentUser
                        Log.i(TAG, "LOGIN SUCESSO:  Usuário logado é ${userLoged?.email}")
                        Toast.makeText(this, "Usuário logado é ${userLoged?.email}", Toast.LENGTH_SHORT)
                            .show()
                        true
                    } else {
                        Log.w(TAG, "LOGIN FALHOU!! ", task.exception)
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        false
                    }
                    callback(retorno)
                }
        }
    }
}

fun validaEmail(email: String, context: Context): Boolean {
    if (email.isEmpty()) Toast.makeText(context, " Email não pode estar em branco", Toast.LENGTH_SHORT).show()
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validaSenha(password: String): Boolean {
    val minLength = 6                            // Mínimo de 6 caracteres
    val hasDigit = password.any { it.isDigit() }
    val hasUppercase = password.any { it.isUpperCase() }
    return password.length >= minLength && hasDigit && hasUppercase
}

