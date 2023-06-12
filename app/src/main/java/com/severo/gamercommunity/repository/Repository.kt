package com.severo.gamercommunity.repository

import android.app.Application
import android.content.Context
import com.severo.gamercommunity.model.*
import com.severo.gamercommunity.model.temp.*

object Repository {
    //instancia al modelo
    private lateinit var modelArticulos:ModelTempArticulo
    private lateinit var modelComentarios:ModelTempComentario
    private lateinit var modelChats:ModelTempChat
    private lateinit var modelMensajes:ModelTempMensaje
    private lateinit var modelSeguidos:ModelTempSeguido
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
        ModelTempChat(application)
        modelChats=ModelTempChat
        ModelTempMensaje(application)
        modelMensajes=ModelTempMensaje
        ModelTempSeguido(application)
        modelSeguidos=ModelTempSeguido
    }

    fun addArticulo(articulo: Articulo)= modelArticulos.addArticulo(articulo)
    fun delArticulo(articulo: Articulo)= modelArticulos.delArticulo(articulo)
    fun getAllArticulos()= modelArticulos.getAllArticulos()
    fun addComentario(comentario: Comentario)= modelComentarios.addComentario(comentario)
    fun delComentario(comentario: Comentario)= modelComentarios.delComentario(comentario)
    fun getAllComentarios()= modelComentarios.getAllComentarios()
    fun addChat(chat: Chat)= modelChats.addChat(chat)
    fun delChat(chat: Chat)= modelChats.delChat(chat)
    fun getAllChats()= modelChats.getAllChats()
    fun addMensaje(mensaje: Mensaje)= modelMensajes.addMensaje(mensaje)
    fun delMensaje(mensaje: Mensaje)= modelMensajes.delMensaje(mensaje)
    fun getAllMensajes()= modelMensajes.getAllMensajes()
    fun addSeguido(seguido: Seguido)= modelSeguidos.addSeguido(seguido)
    fun delSeguido(seguido: Seguido)= modelSeguidos.delSeguido(seguido)
    fun getAllSeguidos()= modelSeguidos.getAllSeguidos()
}