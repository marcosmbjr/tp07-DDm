package com.example.firebaseauth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauth.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            atualizarTela()
        } else {
            Toast.makeText(this, "Login cancelado ou falhou", Toast.LENGTH_SHORT).show()
            iniciarLogin()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSair.setOnClickListener { sair() }

        if (FirebaseAuth.getInstance().currentUser == null) {
            iniciarLogin()
        } else {
            atualizarTela()
        }
    }

    private fun iniciarLogin() {
        binding.conteudo.visibility = View.INVISIBLE

        // O EmailBuilder já traz o fluxo de cadastro (criar conta) na tela do FirebaseUI
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .build()

        signInLauncher.launch(intent)
    }

    private fun atualizarTela() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        binding.conteudo.visibility = View.VISIBLE
        binding.tvBemVindo.text = "Bem-vindo(a)!"
        binding.tvEmail.text = user.email
    }

    private fun sair() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            iniciarLogin() // volta para a tela de login
        }
    }
}
