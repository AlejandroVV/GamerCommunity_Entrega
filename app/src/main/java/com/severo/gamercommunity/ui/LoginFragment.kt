package com.severo.gamercommunity.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.severo.gamercommunity.R
import com.severo.gamercommunity.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var auth: FirebaseAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(auth.currentUser != null){
            auth.signOut()
        }
        println("Usuario: ${auth.currentUser}")
        binding.btRegistroLogin.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.btLogin.setOnClickListener {
            var email = binding.etEmailLogin.text.toString()
            var pwd = binding.etPasswordLogin.text.toString()
            if (email.isNotEmpty() && pwd.isNotEmpty()){
                auth.signInWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            findNavController().navigate(R.id.action_LoginFragment_to_listaFragment)
                        } else {
                           mostrarAlerta()
                        }
                    }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun mostrarAlerta(){
        val builder = AlertDialog.Builder(binding.loginLayout.context)
        builder.setTitle("Acceso denegado")
        builder.setMessage("Compruebe el correo y la contraseña\n" +
                            "Asegúrese de estar conectado a Internet")
        builder.setPositiveButton("Aceptar", null)
        val alerta = builder.create()
        alerta.show()
    }
}