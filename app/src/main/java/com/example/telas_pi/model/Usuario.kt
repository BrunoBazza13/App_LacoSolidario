package com.example.telas_pi.model

import com.google.gson.annotations.SerializedName

data class Usuario (
    var nome: String = "",
    var login: String = "",
    var telefone: String = "",
    var senha: String = "",
    @SerializedName("cpf")
    var cpf: String? = null,
    @SerializedName("cnpj")
    var cnpj: String? = null,
    @SerializedName("tipoDeUsuario")
    var tipoDeUsuario: String = "",
    var profileImageUrl: String? = ""
)



//    fun toStringComCPF(): String {
//        return "Cadastro(id= '$id', nome='$nome', login='$login', telefone='$telefone', senha='$senha', cpf=$cpf, tipoDeUsuario='$tipoDeUsuario')"
//    }
//
//    fun toStringComCNPJ(): String {
//        return "Cadastro(nome='$nome', login='$login', telefone='$telefone', senha='$senha', cnpj=$cnpj, tipoDeUsuario='$tipoDeUsuario')"
//    }
