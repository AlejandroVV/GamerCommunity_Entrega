package com.severo.gamercommunity.model

import android.os.Parcelable
import com.severo.gamercommunity.model.temp.ModelTempSeguido
import kotlinx.parcelize.Parcelize

@Parcelize
data class Seguido(
    var id:Long?=null,
    val usuario:String,
    val email:String
):Parcelable {

    constructor(
        usuario: String,
        email: String
    ):this(ModelTempSeguido.getId(), usuario, email){}

    override fun equals(other: Any?): Boolean {
        return (other is Seguido)&&(this.id == other?.id)
    }
}
