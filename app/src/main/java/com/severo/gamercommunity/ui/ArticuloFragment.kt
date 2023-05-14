package com.severo.gamercommunity.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.severo.gamercommunity.adapters.ArticuloAdapter
import com.severo.gamercommunity.adapters.ComentarioAdapter
import com.severo.gamercommunity.databinding.FragmentArticuloBinding
import com.severo.gamercommunity.model.Articulo
import com.severo.gamercommunity.model.Comentario
import com.severo.gamercommunity.viewmodel.AppViewModel

class ArticuloFragment : Fragment() {
    private var _binding: FragmentArticuloBinding? = null
    private val args: ArticuloFragmentArgs by navArgs()
    private val viewModel: AppViewModel by activityViewModels()
    lateinit var comentariosAdapter:ComentarioAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentArticuloBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniciaArticulo(args.articulo!!)
        iniciaRecyclerView()
        iniciaCRUD()
        binding.btGuardarArticulo.setOnClickListener {
            guardarArticulo(args.articulo!!)
        }
        soloLectura()
        viewModel.comentariosLiveData.observe(viewLifecycleOwner, Observer<List<Comentario>> { lista ->
            comentariosAdapter.setLista(lista)
        })
        binding.btChatArticulo.setOnClickListener {
          println(comentariosAdapter.itemCount)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun iniciaArticulo(articulo: Articulo) {
        binding.etTitulo.setText(articulo.titulo)
        binding.etAutor.setText("Escrito por ${articulo.usuario}")
        binding.etArticulo.setText(articulo.contenido)
        binding.rbValoracion.rating = articulo.valoracion
//cambiamos el título
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Artículo ${articulo.id}"
    }

    private fun guardarArticulo(articulo: Articulo) {
//recuperamos los datos
        var id = articulo.id
        val titulo = articulo.titulo
        val descripcion = articulo.descripcion
        val contenido = articulo.contenido
        val valoracion=binding.rbValoracion.rating
        val usuario= articulo.usuario
        val articulo = Articulo(id, titulo, descripcion, contenido, valoracion, usuario)

        viewModel.addArticulo(articulo)
        findNavController().popBackStack()
    }

    private fun iniciaRecyclerView() {
//creamos el adaptador
        comentariosAdapter = ComentarioAdapter()
        with(binding.rvComentarios) {
//Creamos el layoutManager
            layoutManager = LinearLayoutManager(activity)
//le asignamos el adaptador
            adapter = comentariosAdapter
        }
    }

    private fun iniciaCRUD(){

        binding.btRedactarComentario.setOnClickListener {
            if(binding.etRedactarComentario.text.isNotEmpty()){
                var contenido = binding.etRedactarComentario.text.toString()
                var usuario = "usuario"
                var comentario = Comentario(contenido, usuario)
                viewModel.addComentario(comentario)
                binding.etRedactarComentario.setText("")
            }
        }

        comentariosAdapter.onComentarioClickListener = object :
            ComentarioAdapter.OnComentarioClickListener {

            override fun onComentarioEditarClick(comentario: Comentario?) {
                viewModel.addComentario(comentario!!)
            }

            override fun onComentarioBorrarClick(comentario: Comentario?){
                viewModel.delComentario(comentario!!)
            }
        }
    }

    private fun soloLectura(){
        binding.etTitulo.isFocusable = false
        binding.etArticulo.isFocusable = false
        binding.etAutor.isFocusable = false
    }
}