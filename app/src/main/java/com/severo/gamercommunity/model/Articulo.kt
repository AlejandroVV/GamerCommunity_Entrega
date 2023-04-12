package com.severo.gamercommunity.model

import android.os.Parcelable
import com.severo.gamercommunity.model.temp.ModelTempArticulo
import kotlinx.parcelize.Parcelize

@Parcelize
data class Articulo(
    var id:Long?=null,//id único
    val titulo:String,
    val descripcion:String,
    val contenido:String,
    val valoracion:Float,
    val usuario:String,
): Parcelable {

    //segundo constructor que genera id nuevo
    constructor( titulo: String,
                 descripcion: String,
                 contenido: String,
                 valoracion: Float,
                 usuario: String
                 ):this(ModelTempArticulo.getId(), titulo, descripcion, contenido, valoracion, usuario){}

    override fun equals(other: Any?): Boolean {
        return (other is Articulo)&&(this.id == other?.id)
    }
}
