package com.severo.gamercommunity.model

import android.os.Parcelable
import com.severo.gamercommunity.model.temp.ModelTempArticulo
import com.severo.gamercommunity.model.temp.ModelTempChat
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val titulo:String,
    val miembros:List<String>,
    val idBD: String
):Parcelable {

    override fun equals(other: Any?): Boolean {
        return (other is Chat)&&(this.idBD == other?.idBD)
    }
}
