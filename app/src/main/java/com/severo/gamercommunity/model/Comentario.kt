package com.severo.gamercommunity.model

import android.os.Parcelable
import com.severo.gamercommunity.model.temp.ModelTempComentario
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comentario(
    var id:Long?=null,
    val contenido:String,
    val usuario:String,
):Parcelable {

    constructor( contenido:String,
                 usuario:String,
                 ):this(ModelTempComentario.getId(), contenido, usuario){}

    override fun equals(other: Any?): Boolean {
            return (other is Comentario)&&(this.id == other?.id)
    }
}
