package com.severo.gamercommunity.model.temp

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.severo.gamercommunity.model.Seguido
import com.severo.gamercommunity.utils.Util

object ModelTempSeguido {
    private val seguidos = ArrayList<Seguido>()

    private val seguidosLiveData = MutableLiveData<ArrayList<Seguido>>(seguidos)

    val db = ModelTempArticulo.db
    var util = Util()

    private lateinit var application: Application

    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
    }

    fun getAllSeguidos(): LiveData<ArrayList<Seguido>> {
        seguidosLiveData.value= seguidos
        return seguidosLiveData
    }

    fun addSeguido(seguido: Seguido) {
        val pos = seguidos.indexOf(seguido)
        if (pos < 0) {//si no existe
            seguidos.add(seguido)
        } else {
            seguidos[pos] = seguido
        }

        seguidosLiveData.value = seguidos
    }

    fun delSeguido(seguido: Seguido) {
        seguidos.remove(seguido)
        seguidosLiveData.value = seguidos
    }

    fun getId() :Long?{
        var id: Long?
        if (seguidos.isEmpty()){
            id = 0
        } else {
            id = seguidos[seguidos.lastIndex].id?.plus(1)
        }
        return id
    }

    fun addBD(seguido: Seguido){
        var id = seguido.id
        var usuario = seguido.usuario
        var email = seguido.email
        db.collection("usuarios").document(util.getEmail())
            .collection("seguidos").document(id.toString())
            .set(
                hashMapOf(
                    "id" to id,
                    "usuario" to usuario,
                    "email" to email
                )
            )
    }

    fun delBD(seguido: Seguido){
        var id = seguido.id
        db.collection("usuarios").document(util.getEmail())
            .collection("seguidos").document(id.toString())
            .delete()
    }
}