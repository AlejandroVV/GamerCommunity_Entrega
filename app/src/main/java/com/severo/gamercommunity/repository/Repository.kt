package com.severo.gamercommunity.repository

import android.app.Application
import android.content.Context
import com.severo.gamercommunity.model.Articulo
import com.severo.gamercommunity.model.temp.ModelTempArticulo

object Repository {
    //instancia al modelo
    private lateinit var modelArticulos:ModelTempArticulo
    //el context suele ser necesario para recuperar datos
    private lateinit var application: Application
    //inicio del objeto singleton
    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
    //iniciamos el modelo
        ModelTempArticulo(application)
        modelArticulos=ModelTempArticulo
    }

    fun addArticulo(articulo: Articulo)= modelArticulos.addArticulo(articulo)
    fun delArticulo(articulo: Articulo)= modelArticulos.delArticulo(articulo)
    fun getAllArticulos()= modelArticulos.getAllArticulos()
}