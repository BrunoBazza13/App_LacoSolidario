package com.example.telas_pi.crud

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.telas_pi.R
import com.example.telas_pi.databinding.FragmentCadastroBinding
import com.example.telas_pi.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.DelicateCoroutinesApi


class CadastroFragment : Fragment() {

    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        val view = binding.root
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        // Oculta a BottomNavigationView
        //  (activity as AppCompatActivity).findViewById<View>(R.id.bottomNavigationView8).visibility =
        View.GONE
        // Exibe a Toolbar
        //   (activity as AppCompatActivity).findViewById<View>(R.id.tobar).visibility = View.VISIBLE

    }

    private fun saveEvent() {
        val editTextCPFouCNPJ = binding.editTextCPFouCNPJ
        val nome = binding.editTextNome.text.toString()
        val login = binding.editTextEmail.text.toString()
        val telefone = binding.editTextPhone.text.toString()
        val senha = binding.editTextSenha.text.toString()
        var cpf: String? = null
        var cnpj: String? = null

        val tipoDeUsuario = when (binding.radioGroupTipo.checkedRadioButtonId) {
            R.id.radioButtonCPF -> {
                if (editTextCPFouCNPJ.text.length == 11) {
                    cpf = editTextCPFouCNPJ.text.toString()
                    "doador"
                } else {
                    throw IllegalArgumentException("CPF inválido")
                }
            }

            R.id.radioButtonCNPJ -> {
                if (editTextCPFouCNPJ.text.length == 14) {
                    val cnpj = editTextCPFouCNPJ.text.toString()
                    "instituicao"
                } else {
                    throw IllegalArgumentException("CNPJ inválido")
                }
            }

            else -> {
                throw IllegalArgumentException("Tipo de usuário inválido")
            }
        }

        val usuario = Usuario(nome, login, telefone, senha, cpf, cnpj, tipoDeUsuario)

        if (validateForm(login, senha)) {
            registerAccount(login, senha, usuario)
        } else {
            Toast.makeText(context, "Dados não conferem", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }

    private fun registerAccount(email: String, password: String, usuario: Usuario) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        saveUserToFirestore(uid, usuario)
                    }
                } else {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(
                        context,
                        "Falha no cadastro: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun saveUserToFirestore(uid: String, usuario: Usuario) {
        val db = FirebaseDatabase.getInstance()
        val usersRef: DatabaseReference = db.getReference("users")
        val userRef: DatabaseReference = usersRef.child(uid)
        val user = hashMapOf(
            "nome" to usuario.nome,
            "login" to usuario.login,
            "telefone" to usuario.telefone,
            "senha" to usuario.senha,
            "tipoDeUsuario" to usuario.tipoDeUsuario
        )

        // Verificar se o usuário é do tipo "doador" (CPF) ou "instituicao" (CNPJ)
        if (usuario.tipoDeUsuario == "doador") {
            usuario.cpf?.let { cpf ->
                user["cpf"] = cpf
            }
        } else if (usuario.tipoDeUsuario == "instituicao") {
            usuario.cnpj?.let { cnpj ->
                user["cnpj"] = cnpj
            }
        }

        userRef.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_cadastroFragment_to_loginFragment)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erro ao salvar informações: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }



    private fun initListeners() {
        binding.buttonCadastrar.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            saveEvent()

        }

    }

}



