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
import com.severo.gamercommunity.model.temp.ModelTempArticulo
import com.severo.gamercommunity.utils.Util
import com.severo.gamercommunity.viewmodel.AppViewModel


class RedactarFragment : Fragment() {

    private var _binding: FragmentRedactarBinding? = null
    private val args: RedactarFragmentArgs by navArgs()
    private val viewModel: AppViewModel by activityViewModels()
    private var util = Util()

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
        iniciaArticulo(args.articulo!!)
        binding.btGuardarRedactar.setOnClickListener {
            guardarArticulo(args.articulo!!)
        }
        binding.btVolverRedactar.setOnClickListener {
            val action = RedactarFragmentDirections.actionRedactarFragmentToListaFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun guardarArticulo(articulo: Articulo) {
        var id = articulo.id
        val titulo = binding.etTituloRedactar.text.toString()
        val descripcion = binding.etDescripcion.text.toString()
        val contenido = binding.etContenidoRedactar.text.toString()
        if(titulo.isNotEmpty() && descripcion.isNotEmpty() && contenido.isNotEmpty()){
            val usuario= util.getUserName()
            val valoracion = articulo.valoracion
            val email = articulo.email
            val valoraciones = articulo.valoraciones
            val articulo = Articulo(id, titulo, descripcion, contenido, valoracion, usuario, email,
                valoraciones)
            ModelTempArticulo.addBD(articulo)
            viewModel.addArticulo(articulo)
            findNavController().popBackStack()
        } else {
            muestraMensajeError()
        }
    }
    private fun muestraMensajeError() {
        Snackbar.make(binding.root, "Es necesario rellenar todos los campos", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    private fun iniciaArticulo(articulo: Articulo) {
        binding.etTituloRedactar.setText(articulo.titulo)
        binding.etDescripcion.setText(articulo.descripcion)
        binding.etContenidoRedactar.setText(articulo.contenido)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Artículo ${articulo.id}"
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false)
    }
}