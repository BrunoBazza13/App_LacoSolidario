package com.example.telas_pi.crud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.telas_pi.R
import com.example.telas_pi.databinding.FragmentRecoverBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth


class Recover : Fragment() {

    private var _binding: FragmentRecoverBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebase: Firebase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebase = Firebase
        _binding = FragmentRecoverBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initListeners() {
        val auth = FirebaseAuth.getInstance()

        binding.buttonRecuperarSenha.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.editEmail.text.toString().trim()

            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Email de redefinição enviado.", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_recover_to_loginFragment)
                        } else {
                            Toast.makeText(context, "Falha ao enviar email de redefinição.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Por favor, insira seu email.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}