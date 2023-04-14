package com.severo.gamercommunity.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.severo.gamercommunity.R
import com.severo.gamercommunity.databinding.FragmentArticuloBinding
import com.severo.gamercommunity.databinding.FragmentRedactarBinding
import com.severo.gamercommunity.model.Articulo
import com.severo.gamercommunity.viewmodel.AppViewModel


class RedactarFragment : Fragment() {

    private var _binding: FragmentRedactarBinding? = null
    private val args: RedactarFragmentArgs by navArgs()
    private val viewModel: AppViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRedactarBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Nuevo artículo"
        binding.btGuardarRedactar.setOnClickListener {
            guardarArticulo(args.articulo!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun guardarArticulo(articulo: Articulo) {
//recuperamos los datos
        var id = articulo.id
        val titulo = binding.etTituloRedactar.text.toString()
        val descripcion = binding.etDescripcion.text.toString()
        val contenido = binding.etContenidoRedactar.text.toString()
        val usuario= "Usuario"
        val articulo = Articulo(id, titulo, descripcion, contenido, 0F, usuario)

//guardamos la tarea desde el viewmodel
        viewModel.addArticulo(articulo)
//salimos de editarFragment
        findNavController().popBackStack()
    }

    /*private fun iniciaFabGuardar() {
        binding.fabGuardar.setOnClickListener {
            if (binding.etTecnico.text.toString().isNullOrEmpty() ||
                binding.etDescripcion.text.toString().isNullOrEmpty())
                muestraMensajeError()
            else
                guardaTarea()
        }
    }*/
    private fun muestraMensajeError() {
        Snackbar.make(binding.root, "Es necesario rellenar todos los campos", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}