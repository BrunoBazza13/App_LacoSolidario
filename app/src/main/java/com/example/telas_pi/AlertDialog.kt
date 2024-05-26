package com.example.telas_pi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.telas_pi.databinding.FragmentAlertDialogBinding
import com.example.telas_pi.databinding.FragmentCadastroBinding
import com.example.telas_pi.databinding.FragmentPerfilBinding


class AlertDialog : Fragment() {

    private var _binding: FragmentAlertDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlertDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        return binding.root
    }



}