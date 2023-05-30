package com.severo.gamercommunity.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.severo.gamercommunity.model.Articulo
import com.severo.gamercommunity.model.Chat
import com.severo.gamercommunity.model.Comentario
import com.severo.gamercommunity.model.Mensaje
import com.severo.gamercommunity.repository.Repository

class AppViewModel(application: Application) : AndroidViewModel(application) {
    //repositorio
    private val repositorio: Repository
    //liveData de lista de tareas
    val articulosLiveData : LiveData<ArrayList<Articulo>>
    val comentariosLiveData : LiveData<ArrayList<Comentario>>
    val chatsLiveData: LiveData<ArrayList<Chat>>
    val mensajesLiveData : LiveData<ArrayList<Mensaje>>
    //inicio ViewModel
    init {
//inicia repositorio
        Repository(getApplication<Application>().applicationContext)
        repositorio=Repository
        articulosLiveData =repositorio.getAllArticulos()
        comentariosLiveData =repositorio.getAllComentarios()
        chatsLiveData = repositorio.getAllChats()
        mensajesLiveData = repositorio.getAllMensajes()
    }
    fun addArticulo(articulo: Articulo) = repositorio.addArticulo(articulo)
    fun delArticulo(articulo: Articulo) = repositorio.delArticulo(articulo)
    fun addComentario(comentario: Comentario) = repositorio.addComentario(comentario)
    fun delComentario(comentario: Comentario) = repositorio.delComentario(comentario)
    fun addChat(chat: Chat) = repositorio.addChat(chat)
    fun delChat(chat: Chat) = repositorio.delChat(chat)
    fun addMensaje(mensaje: Mensaje) = repositorio.addMensaje(mensaje)
    fun delMensaje(mensaje: Mensaje) = repositorio.delMensaje(mensaje)
}