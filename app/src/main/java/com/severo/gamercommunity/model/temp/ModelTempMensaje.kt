package com.severo.gamercommunity.model.temp

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.severo.gamercommunity.model.Mensaje

object ModelTempMensaje {
    private val mensajes = ArrayList<Mensaje>()

    private val mensajesLiveData =
        MutableLiveData<ArrayList<Mensaje>>(mensajes)

    private lateinit var application: Application

    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
        iniciaPruebaTareas()
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

    fun iniciaPruebaTareas() {
        val tecnicos = listOf(
            "Pepe Gotero",
            "Sacarino Pómez",
        )
        lateinit var mensaje: Mensaje
        (1..6).forEach({
            mensaje = Mensaje(
                "Mensaje $it realizado por el técnico \nLorem ipsum dolor sit amet, consectetur adipiscing " +
                        "elit. Mauris consequat ligula et vehicula mattis. Etiam tristique ornare lacinia. Vestibulum lacus " +
                        "magna, dignissim et tempor id, convallis sed augue",
                tecnicos.random(),
            )
            mensajes.add(mensaje)
        })

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
}