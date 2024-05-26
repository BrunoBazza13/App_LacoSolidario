package com.example.telas_pi.crud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.telas_pi.databinding.ItemDoacaoBinding
import com.example.telas_pi.model.Doacao

class DoacaoAdapter ( private val doacaoList: List<Doacao>): RecyclerView.Adapter<DoacaoAdapter.DoacaoViewHolder>(){


    class DoacaoViewHolder(val binding: ItemDoacaoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoacaoViewHolder {
        val binding = ItemDoacaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoacaoViewHolder(binding)
    }

    override fun getItemCount(): Int = doacaoList.size

    override fun onBindViewHolder(holder: DoacaoViewHolder, position: Int) {
        val doacao = doacaoList[position]


          holder.binding.textTipoDoacao.text = doacao.tipo
         holder.binding.textEndereco.text = doacao.localizacao
         holder.binding.textDescricao.text = doacao.descricao

        Glide.with(holder.itemView.context)
            .load(doacao.imagem)
            .centerCrop()
            .into(holder.binding.eventImageView)
    }

}