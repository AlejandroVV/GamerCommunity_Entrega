package com.severo.gamercommunity.model.temp

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.severo.gamercommunity.model.Chat
import com.severo.gamercommunity.model.Mensaje

object ModelTempMensaje {
    private val mensajes = ArrayList<Mensaje>()

    private val mensajesLiveData =
        MutableLiveData<ArrayList<Mensaje>>(mensajes)

    private lateinit var application: Application
    val db = ModelTempArticulo.db

    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
    }

    fun getAllMensajes(): LiveData<ArrayList<Mensaje>> {
        mensajesLiveData.value= mensajes
        return mensajesLiveData
    }

    fun addMensaje(mensaje: Mensaje) {
        val pos = mensajes.indexOf(mensaje)
        if (pos < 0) {
            mensajes.add(mensaje)
        } else {

            mensajes[pos] = mensaje
        }

        mensajesLiveData.value = mensajes
    }

    fun limpiarReciclerView() {
        mensajes.removeAll(mensajes.toSet())
        mensajesLiveData.value = mensajes
    }

    fun delMensaje(mensaje: Mensaje) {
        mensajes.remove(mensaje)
        mensajesLiveData.value = mensajes
    }

    fun getId() :Long?{
        var id: Long?
        if (mensajes.isEmpty()){
            id = 0
        } else {
            id = mensajes[mensajes.lastIndex].id?.plus(1)
        }
        return id
    }

    fun addBD(chat: Chat, mensaje: Mensaje){
        var id = mensaje.id.toString()
        var contenido = mensaje.contenido
        var autor = mensaje.autor

        db.collection("chats").document(chat.idBD)
            .collection("mensajes").document(id)
            .set(
                hashMapOf(
                    "id" to id,
                    "contenido" to contenido,
                    "autor" to autor,
                )
            )
    }
}