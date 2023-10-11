package Android.Previsao_do_Tempo

import Android.Previsao_do_Tempo.databinding.ActivityCadastroBinding
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "CadastroActivity"

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val emailCadastro = binding.editTextEmailCadastro
        val senhaCadastro = binding.editTextPassWordCadastro
        val buttonCadastrar = binding.botaoCadastrar

        buttonCadastrar.setOnClickListener {
            val emailValido = validaEmail(emailCadastro.text.toString(), this)
            if (!emailValido) {
                binding.textInputLayoutEmailCadastro.error = getString(R.string.email_invalido)
            }

            val senhaValida = validaSenha(senhaCadastro.text.toString())
            if (!senhaValida) {
                binding.textInputLayoutPasswordCadastro.error = getString(R.string.senha_invalida)
            }

            if(emailValido && senhaValida) cadastrar(emailCadastro.text.toString(), senhaCadastro.text.toString())
        }

    }

    private fun cadastrar(email: String, password: String) {
        if (email.isEmpty()) {
            Toast.makeText(this, getString(R.string.o_campo_email_nao_pode_estar_vazio), Toast.LENGTH_SHORT).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val usuarioCadastrado = auth.currentUser
                        Log.d(TAG, "createUserWithEmail:success :  ${usuarioCadastrado?.email} ")
                        Toast.makeText(this, "Usuário cadastrado é ${usuarioCadastrado?.email}", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}