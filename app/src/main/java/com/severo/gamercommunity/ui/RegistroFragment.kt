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

    private var email = ""
    private var pwdEmail = ""
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
        ocultar(false)
        binding.btRegistro.setOnClickListener {
            validador()
        }
        binding.btVolver.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        binding.btCambiar.setOnClickListener {
            registrador()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validador(){

        val titulo1 = "Fallo al verificar"
        val mensaje1 = "Se deben rellenar todos los campos"
        val mensaje2 = "- Compruebe su conexión a Internet.\n" +
                        "- Compruebe el correo y la contraseña.\n" +
                        "- Puede que el correo ya esté registrado."
        var nombre = binding.etNombre.text.toString()
        var userName = binding.etUser.text.toString()
        email = binding.etEmail.text.toString()
        pwdEmail = binding.etPasswordEmail.text.toString()
        if (nombre.isNotEmpty() && userName.isNotEmpty() &&
            email.isNotEmpty() && pwdEmail.isNotEmpty()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pwdEmail)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        ocultar(true)
                    }
                }.addOnFailureListener {
                    mostrarAlerta(titulo1, mensaje2)
                }
        } else {
            mostrarAlerta(titulo1, mensaje1)
        }
    }

    private fun registrador(){
        val titulo = "Fallo al registrar"
        val mensaje1 = "Se deben rellenar todos los campos"
        val mensaje2 = "Las contraseñas no coinciden"
        val mensaje3 = "- Compruebe su conexión a Internet.\n" +
                        "- Puede que el correo ya esté registrado."
        val tituloFinal = "Registro realizado con éxito"
        val mensajeFinal = "Ya puede acceder a la aplicación"
        var pwd = binding.etPassword.text.toString()
        var repetir = binding.etRepetir.text.toString()
        if(pwd.isNotEmpty() && pwd == repetir){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pwdEmail)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val user = FirebaseAuth.getInstance().currentUser
                        user!!.updatePassword(pwd).addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                mostrarAlerta(tituloFinal, mensajeFinal)
                                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                            }
                        }
                    }
                }.addOnFailureListener {
                    mostrarAlerta(titulo, mensaje3)
                }
        } else if (pwd != repetir) {
            mostrarAlerta(titulo, mensaje2)
        } else {
            mostrarAlerta(titulo, mensaje1)
        }
    }

    private fun mostrarAlerta(titulo: String, mensaje: String){
        val builder = AlertDialog.Builder(binding.registroLayout.context)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val alerta = builder.create()
        alerta.show()
    }

    private fun ocultar(vista: Boolean){
        if (!vista) {
            binding.tvPassword.visibility = View.GONE
            binding.etPassword.visibility = View.GONE
            binding.tvRepetir.visibility = View.GONE
            binding.etRepetir.visibility = View.GONE
            binding.btCambiar.visibility = View.GONE
        } else {
            binding.tvNombre.visibility = View.GONE
            binding.etNombre.visibility = View.GONE
            binding.tvUser.visibility = View.GONE
            binding.etUser.visibility = View.GONE
            binding.tvPassword.visibility = View.VISIBLE
            binding.etPassword.visibility = View.VISIBLE
            binding.tvRepetir.visibility = View.VISIBLE
            binding.etRepetir.visibility = View.VISIBLE
            binding.btCambiar.visibility = View.VISIBLE
            binding.btRegistro.visibility = View.GONE
            binding.btVolver.visibility = View.GONE
            binding.tvEmail.visibility = View.GONE
            binding.etEmail.visibility = View.GONE
            binding.tvPasswordEmail.visibility = View.GONE
            binding.etPasswordEmail.visibility = View.GONE
        }
    }
}