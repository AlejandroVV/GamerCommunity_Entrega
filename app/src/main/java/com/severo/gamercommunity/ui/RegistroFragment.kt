package com.severo.gamercommunity.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.severo.gamercommunity.R
import com.severo.gamercommunity.databinding.FragmentRegistroBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegistroFragment : Fragment() {

    private var _binding: FragmentRegistroBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btVolver.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        binding.btRegistro.setOnClickListener {
            registrador()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun registrador() {
        var email = binding.etEmail.text.toString()
        var pwd = binding.etPassword.text.toString()
        var pwd2 = binding.etRepetir.text.toString()
        if (email.isNotEmpty() && pwd.isNotEmpty() && pwd == pwd2){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pwd)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                    } else {
                        mostrarAlerta()
                    }
                }
        } else {
            mostrarAlerta()
        }
    }

    private fun mostrarAlerta(){
        val builder = AlertDialog.Builder(binding.registroLayout.context)
        builder.setTitle("Fallo")
        builder.setMessage("Registro fallido")
        builder.setPositiveButton("Aceptar", null)
        val alerta = builder.create()
        alerta.show()
    }
}