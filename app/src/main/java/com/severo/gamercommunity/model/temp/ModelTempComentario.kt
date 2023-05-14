package com.severo.gamercommunity.model.temp

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.severo.gamercommunity.model.Comentario

object ModelTempComentario {

    private val comentarios = ArrayList<Comentario>()

    private val comentariosLiveData =
        MutableLiveData<ArrayList<Comentario>>(comentarios)

    private lateinit var application: Application

    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
        iniciaPruebaTareas()
    }

    fun getAllComentarios(): LiveData<ArrayList<Comentario>> {
        comentariosLiveData.value= comentarios
        return comentariosLiveData
    }

    fun addComentario(comentario: Comentario) {
        val pos = comentarios.indexOf(comentario)
        if (pos < 0) {//si no existe
            comentarios.add(comentario)
        } else {
//si existe se sustituye
            comentarios[pos] = comentario
        }
//actualiza el LiveData
        comentariosLiveData.value = comentarios
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
        lateinit var comentario: Comentario
        (1..10).forEach({
            comentario = Comentario(
                "tarea $it realizada por el técnico \nLorem ipsum dolor sit amet, consectetur adipiscing " +
                        "elit. Mauris consequat ligula et vehicula mattis. Etiam tristique ornare lacinia. Vestibulum lacus " +
                        "magna, dignissim et tempor id, convallis sed augue",
                tecnicos.random(),
            )
            comentarios.add(comentario)
        })
//actualizamos el LiveData
        comentariosLiveData.value = comentarios
    }

    fun delComentario(comentario: Comentario) {
        comentarios.remove(comentario)
        comentariosLiveData.value = comentarios
    }

    public fun getId() :Long?{
        var id: Long?
        if (comentarios.isEmpty()){
            id = 0
        } else {
            id = comentarios[comentarios.lastIndex].id?.plus(1)
        }
        return id
    }
}