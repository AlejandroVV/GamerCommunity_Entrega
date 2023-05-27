package com.severo.gamercommunity.utils

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Util {

    private var userName = "Perfil"
    private var email = Firebase.auth.currentUser?.email
    private val db = Firebase.firestore

    constructor(){
        db.collection("usuarios").document(email.toString())
            .get().addOnSuccessListener {
                var user = it.get("username").toString()
                userName = user
            }
    }

    fun getUserName(): String{
        return userName
    }

    fun getEmail() :String{
        return email.toString()
    }

   private fun getTotal(valoraciones: Map<String,Float>): Float{
        var total = 0F
        for (valoracion in valoraciones.values){
            total = total.plus(valoracion)
        }
        return total
    }

   private fun getPorcentaje(valoraciones: Map<String,Float>) :Float{
        var totalReal = getTotal(valoraciones)
        var totalPosible = (valoraciones.size*5).toFloat()
        var porcentaje = (totalReal*100F)/totalPosible
        return porcentaje
    }

    fun getValoracionGlobal(valoraciones: Map<String,Float>) :Float{
        var porcentaje = getPorcentaje(valoraciones)
        if(porcentaje >= 10 && porcentaje < 20){
            return 0.5F
        } else if (porcentaje >= 20 && porcentaje < 30){
            return 1F
        } else if (porcentaje >= 30 && porcentaje < 40){
            return 1.5F
        } else if (porcentaje >= 40 && porcentaje < 50){
            return 2F
        } else if (porcentaje >= 50 && porcentaje < 60){
            return 2.5F
        } else if (porcentaje >= 60 && porcentaje < 70){
            return 3F
        } else if (porcentaje >= 70 && porcentaje < 80){
            return 3.5F
        } else if (porcentaje >= 80 && porcentaje < 90){
            return 4F
        } else if (porcentaje >= 90 && porcentaje < 100){
            return 4.5F
        } else if (porcentaje >= 100){
            return 5F
        } else {
            return 0F
        }
    }
}