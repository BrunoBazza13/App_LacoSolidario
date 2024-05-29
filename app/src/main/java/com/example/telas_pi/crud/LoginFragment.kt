package com.example.telas_pi.crud

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.telas_pi.R
import com.example.telas_pi.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebase: Firebase


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        firebase = Firebase
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        binding.btnRegistrar.setOnClickListener() {
            findNavController().navigate(R.id.action_loginFragment_to_cadastroFragment)
        }
    }



    private fun login(email: String, password: String) {
        val auth = firebase.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                val database = FirebaseDatabase.getInstance().getReference("users")

                if (userId != null) {
                    database.child(userId).child("tipoDeUsuario").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userType = snapshot.getValue(String::class.java)

                            // Verifica o tipo de usuário e navega para a tela correspondente
                            when (userType) {
                                "instituicao" -> findNavController().navigate(R.id.action_loginFragment_to_blankFragment)
                                "doador" -> findNavController().navigate(R.id.action_loginFragment_to_telaInicialFragment)
                                else -> Toast.makeText(requireContext(), "Tipo de usuário desconhecido", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(requireContext(), "Erro ao obter tipo de usuário", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            } else {
                Toast.makeText(requireContext(), "Usuário ou Senha inválidos", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initListeners() {
        binding.buttonEntrar.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editSenha.text.toString()
            // binding.progressBar.visibility = View.VISIBLE

            login(email, password)
        }
    }

                override fun onDestroyView() {
                    super.onDestroyView()
                    _binding = null
                }

}