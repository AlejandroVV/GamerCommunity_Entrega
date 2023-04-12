package com.severo.gamercommunity.model.temp

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.severo.gamercommunity.model.Articulo

object ModelTempArticulo {
    //lista de articulos
    private val articulos = ArrayList<Articulo>()
    //LiveData para observar en la vista los cambios en la lista
    private val articulosLiveData =
        MutableLiveData<ArrayList<Articulo>>(articulos)
    //el context que suele ser necesario en acceso a datos
    private lateinit var application: Application

    //Permite iniciar el objeto Singleton
    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
        iniciaPrueba()
    }

    /**
     * devuelve un LiveData en vez de MutableLiveData
    para evitar su modificación en las capas superiores
     */
    fun getAllArticulos(): LiveData<ArrayList<Articulo>> {
        articulosLiveData.value= articulos
        return articulosLiveData
    }
    /**
     * añade un articulo, si existe se sustituye
     * y si no se añade. Posteriormente actualiza el LiveData
     * que permitirá avisar a quien esté observando
     */
    fun addArticulo(articulo: Articulo) {
        val pos = articulos.indexOf(articulo)
        if (pos < 0) {//si no existe
            articulos.add(articulo)
            println("ID: ${articulo.id}")
        } else {
            articulos[pos] = articulo
        }
    //actualiza el LiveData
        articulosLiveData.value = articulos
    }
    fun iniciaPrueba() {
        val tecnicos = listOf(
            "Pepe Gotero",
            "Sacarino Pómez",
            "Mortadelo Fernández",
        )
        lateinit var articulo: Articulo
        (1..10).forEach({
            articulo = Articulo(
                "Titulo $it",
                "Descripción",
                "Contenido",
                3.5F,
                tecnicos.random()
            )
            articulos.add(articulo)
            var pos = articulos.indexOf(articulo)
            println("ID: $pos")
        })
//actualizamos el LiveData
        articulosLiveData.value = articulos
    }
    /**
     * Borra un articulo y actualiza el LiveData
     * para avisar a los observadores
     */
    fun delArticulo(articulo: Articulo) {
        articulos.remove(articulo)
        articulosLiveData.value = articulos
    }

    public fun getId() :Long?{
        var id: Long?
        if (articulos.isEmpty()){
            id = 0
        } else {
            id = articulos[articulos.lastIndex].id?.plus(1)
        }
        return id
    }
}