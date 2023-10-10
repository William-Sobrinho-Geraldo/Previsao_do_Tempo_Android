package Android.Previsao_do_Tempo

import Android.Previsao_do_Tempo.databinding.ActivityCadastroBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val emailCadastro = binding.editTextEmailCadastro
        val senhaCadastro = binding.editTextPassWordCadastro

    }
}