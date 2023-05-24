package com.severo.gamercommunity.model

import android.os.Parcelable
import com.severo.gamercommunity.model.temp.ModelTempArticulo
import com.severo.gamercommunity.model.temp.ModelTempChat
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    var id:Long?=null,
    val titulo:String,
    val miembros:List<String>,
):Parcelable {
    constructor(titulo: String,
                miembros: List<String>,
    ):this(ModelTempChat.getId(), titulo, miembros){}

    override fun equals(other: Any?): Boolean {
        return (other is Chat)&&(this.id == other?.id)
    }
}
