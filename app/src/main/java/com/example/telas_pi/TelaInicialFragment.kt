package com.example.telas_pi

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.telas_pi.databinding.FragmentTelaInicialBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController


class TelaInicialFragment : Fragment() {

    private var _binding: FragmentTelaInicialBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTelaInicialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = arguments?.getLong("userId") ?: -1


        initListeners2(userId)

    }

    private fun initListeners2(UserId: Long){
        binding.buttonMeuPerfil.setOnClickListener {
           val action = TelaInicialFragmentDirections.actionTelaInicialFragmentToPerfilFragment()
            findNavController().navigate(action)
        }

        binding.buttonDoar.setOnClickListener {
            val action = TelaInicialFragmentDirections.actionTelaInicialFragmentToDoacaoFragment()
            findNavController().navigate(action)
        }

        binding.button2.setOnClickListener {
            val action = TelaInicialFragmentDirections.actionTelaInicialFragmentToListDoacaoFragment()
            findNavController().navigate(action)
        }

    }

}