package com.severo.gamercommunity.utils

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Util {

    private var userName = "Perfil"
    private var email = Firebase.auth.currentUser?.email
    private val db = Firebase.firestore

    private fun setUserName(user: String){
        userName = user
    }

    fun getUserName(): String{
        db.collection("usuarios").document(email.toString())
            .get().addOnSuccessListener {
                var user = it.get("username").toString()
                setUserName(user)
            }
        return userName
    }

    fun getEmail() :String{
        return email.toString()
    }
}