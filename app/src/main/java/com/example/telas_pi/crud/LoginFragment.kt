package com.example.telas_pi.crud

import android.os.Bundle
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
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                findNavController().navigate(R.id.action_loginFragment_to_telaInicialFragment)
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