package com.severo.gamercommunity.model.temp

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.severo.gamercommunity.model.Chat

object ModelTempChat {
    private val chats = ArrayList<Chat>()

    private val chatsLiveData = MutableLiveData<ArrayList<Chat>>(chats)

    private lateinit var application: Application

    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
        iniciaPruebaTareas()
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

    fun iniciaPruebaTareas() {
        val tecnicos = listOf(
            "Pepe Gotero",
            "Sacarino Pómez",
            "Mortadelo Fernández",
            "Filemón López",
            "Zipi Climent",
            "Zape Gómez"
        )
        lateinit var chat: Chat
        (1..4).forEach({
            var lista = listOf<String>(
                tecnicos.random(),
                tecnicos.random(),
            )
            chat = Chat(
                tecnicos.random(),
                lista
            )
            chats.add(chat)
        })

        chatsLiveData.value = chats
    }

    fun delChat(chat: Chat) {
        chats.remove(chat)
        chatsLiveData.value = chats
    }

    fun getId() :Long?{
        var id: Long?
        if (chats.isEmpty()){
            id = 0
        } else {
            id = chats[chats.lastIndex].id?.plus(1)
        }
        return id
    }
}