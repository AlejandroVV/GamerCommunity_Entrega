package com.severo.gamercommunity.model.temp

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.severo.gamercommunity.model.Chat

object ModelTempChat {
    private val chats = ArrayList<Chat>()

    private val chatsLiveData = MutableLiveData<ArrayList<Chat>>(chats)

    private lateinit var application: Application
    private val db = Firebase.firestore

    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
    }

    fun getAllChats(): LiveData<ArrayList<Chat>> {
        chatsLiveData.value= chats
        return chatsLiveData
    }

    fun addChat(chat: Chat) {
        val pos = chats.indexOf(chat)
        if (pos < 0) {
            chats.add(chat)
        } else {
            chats[pos] = chat
        }
        chatsLiveData.value = chats
    }

    fun addBD(chat: Chat){
        var titulo = chat.titulo
        var miembros = chat.miembros
        var idBD = chat.idBD
        db.collection("chats").document(idBD)
            .set(
                hashMapOf(
                    "titulo" to titulo,
                    "miembros" to miembros,
                    "idBD" to idBD
                )
            )
    }

    fun delChat(chat: Chat) {
        chats.remove(chat)
        chatsLiveData.value = chats
    }

    fun delBD(chat: Chat){
        var idBD = chat.idBD
        db.collection("chats").document(idBD)
            .delete()
    }
}