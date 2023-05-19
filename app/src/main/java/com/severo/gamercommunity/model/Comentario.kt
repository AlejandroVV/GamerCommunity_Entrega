package com.severo.gamercommunity.model

import android.os.Parcelable
import com.severo.gamercommunity.model.temp.ModelTempComentario
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comentario(
    var id:Long?=null,
    val contenido:String,
    val usuario:String,
    val email:String
):Parcelable {

    constructor( contenido:String,
                 usuario:String,
                 email: String
                 ):this(ModelTempComentario.getId(), contenido, usuario, email){}

    override fun equals(other: Any?): Boolean {
            return (other is Comentario)&&(this.id == other?.id)
    }
}
