package com.severo.gamercommunity.model.temp

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.severo.gamercommunity.model.Articulo
import com.severo.gamercommunity.model.Comentario

object ModelTempComentario {

    val db = ModelTempArticulo.db

    private val comentarios = ArrayList<Comentario>()

    private val comentariosLiveData =
        MutableLiveData<ArrayList<Comentario>>(comentarios)

    private lateinit var application: Application

    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
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

    fun limpiarReciclerView() {
        comentarios.removeAll(comentarios.toSet())
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

    fun addBD(articulo: Articulo, comentario: Comentario){
        var id = comentario.id
        var contenido = comentario.contenido
        var usuario = comentario.usuario
        db.collection("articulos").document(articulo.id.toString())
            .collection("comentarios").document(id.toString())
            .set(
                hashMapOf(
                    "id" to id,
                    "contenido" to contenido,
                    "usuario" to usuario
                )
            )
    }

    fun delBD(articulo: Articulo, comentario: Comentario){
        db.collection("articulos").document(articulo.id.toString())
            .collection("comentarios").document(comentario.id.toString())
            .delete()
    }
}