package com.severo.gamercommunity.repository

import android.app.Application
import android.content.Context
import com.severo.gamercommunity.model.Articulo
import com.severo.gamercommunity.model.Comentario
import com.severo.gamercommunity.model.temp.ModelTempArticulo
import com.severo.gamercommunity.model.temp.ModelTempComentario

object Repository {
    //instancia al modelo
    private lateinit var modelArticulos:ModelTempArticulo
    private lateinit var modelComentarios:ModelTempComentario
    //el context suele ser necesario para recuperar datos
    private lateinit var application: Application
    //inicio del objeto singleton
    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
    //iniciamos el modelo
        ModelTempArticulo(application)
        modelArticulos=ModelTempArticulo
        ModelTempComentario(application)
        modelComentarios=ModelTempComentario
    }

    fun addArticulo(articulo: Articulo)= modelArticulos.addArticulo(articulo)
    fun delArticulo(articulo: Articulo)= modelArticulos.delArticulo(articulo)
    fun getAllArticulos()= modelArticulos.getAllArticulos()
    fun addComentario(comentario: Comentario)= modelComentarios.addComentario(comentario)
    fun delComentario(comentario: Comentario)= modelComentarios.delComentario(comentario)
    fun getAllComentarios()= modelComentarios.getAllComentarios()
}