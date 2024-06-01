package com.example.telas_pi.crud

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.telas_pi.R
import com.example.telas_pi.databinding.FragmentPerfilBinding
import com.example.telas_pi.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class PerfilFragment : Fragment() {

    private lateinit var imageView: ImageView
    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private lateinit var usuario: Usuario
    private lateinit var userId: String
    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = binding.imageView5
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        if (userId.isNotEmpty()) {
            inilistiner(userId)
        } else {
            Toast.makeText(context, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
        }
        excluirConta()
        onBackButtonClicked()
        binding.floatingActionButton3.setOnClickListener {openGallery()}

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val view = binding.root
        return binding.root
    }

    private fun inilistiner(userId: String) {
        database = FirebaseDatabase.getInstance().getReference("users").child(userId)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usuario = snapshot.getValue(Usuario::class.java) ?: Usuario()
                atualizaDados()

                if (!usuario.profileImageUrl.isNullOrEmpty()) {
                    Glide.with(this@PerfilFragment)
                        .load(usuario.profileImageUrl)
                        .circleCrop()
                        .into(imageView)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar dados do usuário", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun atualizaDados() {

        if (usuario.tipoDeUsuario == "instituicao") {
            // Se for uma instituição, muda o texto do TextView CPF para CNPJ
            binding.textViewCPF.text = "CNPJ:"
            binding.textViewCPFGet.text = usuario.cnpj
        }else {
            binding.textViewCPFGet.text = usuario.cpf
        }

        binding.textViewNomeGet.text = usuario.nome
        binding.textViewEmailGet.text = usuario.login
        binding.textViewTelefoneGet.text = usuario.telefone


        binding.textViewNomeGet.setOnClickListener { showEditTextDialog("nome") }
        binding.textViewEmailGet.setOnClickListener { showEditTextDialog("login") }
        binding.textViewTelefoneGet.setOnClickListener { showEditTextDialog("telefone") }
        binding.textViewCPFGet.setOnClickListener { showEditTextDialog("cpf") }
    }

    private fun updateUserInDatabase() {
        database.setValue(usuario).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    requireContext(),
                    "Dados atualizados com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.e("PerfilFragment", "Erro ao atualizar dados", task.exception)
                Toast.makeText(requireContext(), "Erro ao atualizar dados", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showEditTextDialog(field: String) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.fragment_alert_dialog, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)

        builder.setTitle("Atualizar $field")
        builder.setPositiveButton("OK") { dialog, which ->
            val newValue = editText.text.toString()
            when (field) {
                "nome" -> {
                    usuario.nome = newValue
                    binding.textViewNomeGet.text = newValue
                }

                "login" -> {
                    usuario.login = newValue
                    binding.textViewEmailGet.text = newValue
                }

                "telefone" -> {
                    usuario.telefone = newValue
                    binding.textViewTelefoneGet.text = newValue
                }

                "cpf" -> {
                    usuario.cpf = newValue
                    binding.textViewCPFGet.text = newValue
                }
            }
            updateUserInDatabase()
        }
        builder.setNegativeButton("Cancelar", null)
        builder.setView(dialogLayout)
        builder.show()
    }

    private fun excluirConta() {
        binding.button3.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.fragment_alert_dialog_excluir, null)

            builder.setTitle("Deseja realmente excluir sua conta?")
            builder.setPositiveButton("SIM") { _, _ ->
                val currentUser = FirebaseAuth.getInstance().currentUser

                // Exclui os dados do Realtime Database
                database.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Exclui a conta do Firebase Authentication
                        currentUser?.delete()?.addOnCompleteListener { deleteTask ->
                            if (deleteTask.isSuccessful) {
                                Toast.makeText(
                                    requireContext(),
                                    "Conta excluída com sucesso",
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(R.id.action_perfilFragment_to_loginFragment)
                            } else {
                                Log.e(
                                    "PerfilFragment",
                                    "Erro ao excluir conta",
                                    deleteTask.exception
                                )
                                Toast.makeText(
                                    requireContext(),
                                    "Erro ao excluir conta",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Log.e("PerfilFragment", "Erro ao excluir dados do usuário", task.exception)
                        Toast.makeText(
                            requireContext(),
                            "Erro ao excluir dados do usuário",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            builder.setNegativeButton("NÃO", null)
            builder.setView(dialogLayout)
            builder.show()
        }
    }


    private val pickImage =
        this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let { uri ->

                    binding.progressBar.visibility = View.VISIBLE
                   // imageView.visibility = View.GONE

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
                    usuario.profileImageUrl = imageUrl
                    updateUserInDatabase() // Atualiza o usuário no banco de dados
                    binding.progressBar.visibility = View.GONE

                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Falha no upload da imagem: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun onBackButtonClicked() {
        binding.backButton.setOnClickListener { requireActivity().onBackPressed()}

    }

}