package com.severo.gamercommunity.model.temp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.severo.gamercommunity.model.Articulo

object ModelTempArticulo {
    //BBDD
    val db = Firebase.firestore
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
        } else {
            articulos[pos] = articulo
        }
    //actualiza el LiveData
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

    fun addBD(articulo: Articulo){
        var id = articulo.id
        var titulo = articulo.titulo
        var descripcion = articulo.descripcion
        var contenido = articulo.contenido
        var valoracion = articulo.valoracion
        var usuario = articulo.usuario
        var email = articulo.email
        var valoraciones = hashMapOf<String,Float>(usuario to valoracion)
        val pos = articulos.indexOf(articulo)
        if (pos > 0) {//si existe
            db.collection("articulos").document(id.toString())
                .get().addOnSuccessListener {
                    valoraciones = it.get("valoraciones") as HashMap<String, Float>
                }
        }
        db.collection("articulos").document(id.toString())
            .set(
                hashMapOf(
                    "id" to id,
                    "titulo" to titulo,
                    "descripcion" to descripcion,
                    "contenido" to contenido,
                    "valoracion" to valoracion,
                    "usuario" to usuario,
                    "email" to email,
                    "valoraciones" to valoraciones
                )
            )
    }

    fun delBD(articulo: Articulo){
        var id = articulo.id.toString()
        db.collection("articulos").document(id)
            .delete()
    }
}