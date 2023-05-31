package com.severo.gamercommunity.model

import android.os.Parcelable
import com.severo.gamercommunity.model.temp.ModelTempMensaje
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mensaje(
    var id:Long?=null,
    val contenido:String,
    val autor:String,
    val email:String
):Parcelable {

    constructor(contenido:String,
                autor:String,
                email: String
    ):this(ModelTempMensaje.getId(), contenido, autor, email){}

    override fun equals(other: Any?): Boolean {
        return (other is Mensaje)&&(this.id == other?.id)
    }
}
