package com.example.telas_pi.crud

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.telas_pi.R
import com.example.telas_pi.databinding.FragmentDoacaoBinding
import com.example.telas_pi.databinding.FragmentListDoacaoBinding
import com.example.telas_pi.model.Doacao
import com.example.telas_pi.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ListDoacaoFragment : Fragment() {
    private var _binding: FragmentListDoacaoBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var eventAdapter: DoacaoAdapter
    private val eventsList = mutableListOf<Doacao>()
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        database = FirebaseDatabase.getInstance().getReference("crudevents")
        _binding = FragmentListDoacaoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        fetchUserDonations(true)
        setupRecyclerView(eventsList)

    }


    private fun setupRecyclerView(eventLister: List<Doacao>) {

        eventAdapter = DoacaoAdapter(eventLister)

        binding.rvEventFuture.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEventFuture.setHasFixedSize(true)
        binding.rvEventFuture.adapter = eventAdapter


    }

    private fun fetchUserDonations(isFuture: Boolean) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        println("resposta=======> " + userId)

        // Supondo que o nó de doações tenha uma estrutura onde cada doação tem um campo userId.
        database = FirebaseDatabase.getInstance().getReference("doacoes")

        database.orderByChild("usuarioId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    eventsList.clear()
                    for (eventSnapshot in snapshot.children) {
                        val doacao = eventSnapshot.getValue(Doacao::class.java) ?: Doacao()
                        doacao.let {
                            if (true) {
                                eventsList.add(it)
                            }
                        }
                    }
                    eventAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Falha ao carregar doações.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}


