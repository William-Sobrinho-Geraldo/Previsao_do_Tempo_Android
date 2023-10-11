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
        //EDIT TEXTS
        val emailCadastro = binding.editTextEmailCadastro
        val editTextPasswordCadastro = binding.editTextPassWordCadastro
        val editTextConfirmarPasswordCadastro = binding.editTextConfirmarPassWordCadastro
        var senhasIguais = false


        //TEXT INPUT LAYOUTS
        val inputLayoutEmailCadastro = binding.textInputLayoutEmailCadastro
        val inputLayoutPasswordCadastro = binding.textInputLayoutPasswordCadastro
        val inputLayoutConfirmaPasswordCadastro = binding.textInputLayoutConfirmarPasswordCadastro

        val buttonCadastrar = binding.botaoCadastrar

        buttonCadastrar.setOnClickListener {
            val emailValido = validaEmail(emailCadastro.text.toString(), this)
            if (!emailValido) {
                inputLayoutEmailCadastro.error = getString(R.string.email_invalido)
            } else inputLayoutEmailCadastro.error = ""

            val senhaValida = validaSenha(editTextPasswordCadastro.text.toString())
            if (!senhaValida) {
                inputLayoutPasswordCadastro.error = getString(R.string.senha_invalida)
                inputLayoutConfirmaPasswordCadastro.error = getString(R.string.senha_invalida)
            } else {
                senhasIguais = editTextPasswordCadastro.text.toString() == editTextConfirmarPasswordCadastro.text.toString()
                if (!senhasIguais) {
                    inputLayoutPasswordCadastro.error = getString(R.string.senhas_diferentes)
                    inputLayoutConfirmaPasswordCadastro.error = getString(R.string.senhas_diferentes)
                } else {
                    inputLayoutPasswordCadastro.error = ""
                    inputLayoutConfirmaPasswordCadastro.error = ""
                }
            }

            //SE TUDO ESTIVER CORRETO  ->  PODE CADASTRAR O USUÁRIO
            if (emailValido && senhaValida && senhasIguais) {
                cadastrar ( emailCadastro.text.toString(), editTextPasswordCadastro.text.toString())
            }
        }
    }

    private fun cadastrar(email: String, password: String) {
        if (email.isEmpty()) {
            Toast.makeText(this, getString(R.string.o_campo_email_nao_pode_estar_vazio), Toast.LENGTH_SHORT)
                .show()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val usuarioCadastrado = auth.currentUser
                        Log.d(TAG, "createUserWithEmail:success :  ${usuarioCadastrado?.email} ")
                        Toast.makeText(
                            this,
                            "Usuário cadastrado é ${usuarioCadastrado?.email}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}