package com.example.telas_pi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.telas_pi.databinding.FragmentBlankBinding
import com.example.telas_pi.databinding.FragmentTelaInicialBinding


class BlankFragment : Fragment() {

    private var _binding: FragmentBlankBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners2()

    }

    private fun initListeners2(){
        binding.buttonMeuPerfil.setOnClickListener {
            val action = BlankFragmentDirections.actionBlankFragmentToPerfilFragment()
            findNavController().navigate(action)
        }



        binding.button2.setOnClickListener {
            val action = BlankFragmentDirections.actionBlankFragmentToListDoacaoFragment()
            findNavController().navigate(action)
        }

    }

}