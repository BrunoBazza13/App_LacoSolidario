package com.example.telas_pi.crud

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.telas_pi.R
import com.example.telas_pi.databinding.FragmentDoacaoBinding
import com.example.telas_pi.model.Doacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class DoacaoFragment : Fragment() {

    private var _binding: FragmentDoacaoBinding? = null
    private val binding get() = _binding!!
    private lateinit var doacao: Doacao
    private lateinit var database: DatabaseReference
    private lateinit var resumoView: View
  //  private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance().reference.child("doacoes")
        saveEvent2()
        binding.backButton.setOnClickListener { onBackButtonClicked() }
        binding.buttonAddFt.setOnClickListener {openGallery()}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoacaoBinding.inflate(inflater, container, false)
        return binding.root



    }

    private fun saveEvent2() {
        binding.buttonDoar.setOnClickListener {
            val descricao = binding.editDescricao.text.toString()
            val endereco = binding.editEndereco.text.toString()
            val tipoDoacao = when (binding.radioGroupTipo.checkedRadioButtonId) {
                R.id.radioButtonAlimento -> "Alimento"
                R.id.radioButtonBrinquedos -> "Brinquedo"
                R.id.radioButtonRoupas -> "Roupa"
                else -> ""
            }
            val url = binding.textUrl.text.toString()

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            userId?.let { uid ->
                if (descricao.isNotEmpty() && endereco.isNotEmpty() && tipoDoacao.isNotEmpty()) {
                    val inflater = LayoutInflater.from(requireContext())
                    val resumoDoacaoView = inflater.inflate(R.layout.fragment_resumo_doacao, null)

                    resumoDoacaoView.findViewById<TextView>(R.id.textViewTipoDocaoGet).text = tipoDoacao
                    resumoDoacaoView.findViewById<TextView>(R.id.textViewEnderecoGet).text = endereco
                    resumoDoacaoView.findViewById<TextView>(R.id.textViewDescricaoGet).text = descricao

                    if (url.isNotEmpty()) {
                        Glide.with(this)
                            .load(url)
                            .into(resumoDoacaoView.findViewById(R.id.eventImageView))
                    }
                    val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
                    builder.setView(resumoDoacaoView)
                        .setPositiveButton("Confirmar") { dialog, _ ->
                            confirmaDoacao(uid, tipoDoacao, descricao, endereco, url)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancelar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                } else {
                    Toast.makeText(
                        context,
                        "Por favor, preencha todos os campos.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun confirmaDoacao(uid: String, tipoDoacao: String, descricao: String, endereco: String, url: String ){
        val eventoId = database.push().key
        val evento = Doacao(uid, tipoDoacao, descricao, endereco, url)
        if (eventoId != null) {
            database.child(eventoId).setValue(evento)
                .addOnCompleteListener {
                    Toast.makeText(context, "Doação salva com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Falha ao salvar Doação.", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private val pickImage =
        this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let { uri ->

                    uploadImageToFirebase(uri)
                }
            }
        }

    private fun openGallery()  {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImage.launch(intent)
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val fileName = UUID.randomUUID().toString()
        val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

        refStorage.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    binding.textUrl.text = imageUrl


                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Falha no upload da imagem: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun onBackButtonClicked() {
        // Adicione aqui o código para lidar com o clique na imagem de voltar
        requireActivity().onBackPressed()
    }

}


