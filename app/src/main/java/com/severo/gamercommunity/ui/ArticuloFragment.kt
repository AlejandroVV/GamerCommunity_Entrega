package com.severo.gamercommunity.ui

import android.app.AlertDialog
import android.content.Context
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
import com.google.firebase.firestore.DocumentChange
import com.severo.gamercommunity.adapters.ArticuloAdapter
import com.severo.gamercommunity.adapters.ComentarioAdapter
import com.severo.gamercommunity.databinding.FragmentArticuloBinding
import com.severo.gamercommunity.model.Articulo
import com.severo.gamercommunity.model.Comentario
import com.severo.gamercommunity.model.temp.ModelTempArticulo
import com.severo.gamercommunity.model.temp.ModelTempComentario
import com.severo.gamercommunity.utils.Util
import com.severo.gamercommunity.viewmodel.AppViewModel

class ArticuloFragment : Fragment() {
    private var _binding: FragmentArticuloBinding? = null
    private val args: ArticuloFragmentArgs by navArgs()
    private val viewModel: AppViewModel by activityViewModels()
    lateinit var comentariosAdapter:ComentarioAdapter
    private var util = Util()

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
        binding.tvPerfilArticulo.text = util.getUserName()
        var articulo = args.articulo!!
        iniciaArticulo(articulo)
        iniciaRecyclerView()
        ModelTempComentario.limpiarReciclerView()
        iniciarComentarios(articulo)
        iniciaCRUD(articulo)
        binding.btGuardarArticulo.setOnClickListener {
            guardarArticulo(articulo)
        }
        soloLectura()
        viewModel.comentariosLiveData.observe(viewLifecycleOwner, Observer<List<Comentario>> { lista ->
            comentariosAdapter.setLista(lista)
        })
        ModelTempComentario.db.collection("articulos")
            .document(articulo.id.toString())
            .collection("comentarios")
            .addSnapshotListener{ documentos, error ->
                if(error != null){
                    return@addSnapshotListener
                }
                for (documento in documentos!!.documentChanges){
                    documento.document
                    var comentario = Comentario(
                        documento.document.id.toLongOrNull(),
                        documento.document.get("contenido").toString(),
                        documento.document.get("usuario").toString(),
                        documento.document.get("email").toString(),
                    )
                    when(documento.type){
                        DocumentChange.Type.ADDED -> viewModel.addComentario(comentario)
                        DocumentChange.Type.MODIFIED -> viewModel.addComentario(comentario)
                        DocumentChange.Type.REMOVED -> viewModel.delComentario(comentario)
                    }
                }
            }
        binding.btChatArticulo.setOnClickListener {
            val action = ArticuloFragmentDirections.actionArticuloFragmentToChatFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun iniciaArticulo(articulo: Articulo) {
        val key = util.getEmail()
        val valoraciones = articulo.valoraciones
        binding.etTitulo.setText(articulo.titulo)
        binding.etAutor.setText("Escrito por ${articulo.usuario}")
        binding.etArticulo.setText(articulo.contenido)
        if(valoraciones.containsKey(key)){
            binding.rbValoracion.rating = valoraciones[key]!!
        } else {
            binding.rbValoracion.rating = 0F
        }
//cambiamos el título
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Artículo ${articulo.id}"
    }

    private fun guardarArticulo(articulo: Articulo) {
//recuperamos los datos
        val valoraciones = articulo.valoraciones
        valoraciones.put(util.getEmail(), binding.rbValoracion.rating)
        var id = articulo.id
        val titulo = articulo.titulo
        val descripcion = articulo.descripcion
        val contenido = articulo.contenido
        val valoracion= util.getValoracionGlobal(valoraciones)
        val usuario= articulo.usuario
        val email = articulo.email
        val articulo = Articulo(id, titulo, descripcion, contenido, valoracion, usuario, email,
                                valoraciones)

        ModelTempArticulo.addBD(articulo)
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

    private fun iniciaCRUD(articulo: Articulo){

        binding.btRedactarComentario.setOnClickListener {
            if(binding.etRedactarComentario.text.isNotEmpty()){
                var contenido = binding.etRedactarComentario.text.toString()
                var usuario = util.getUserName()
                var email = util.getEmail()
                var comentario = Comentario(contenido, usuario, email)
                ModelTempComentario.addBD(articulo, comentario)
                viewModel.addComentario(comentario)
                binding.etRedactarComentario.setText("")
            }
        }

        comentariosAdapter.onComentarioClickListener = object :
            ComentarioAdapter.OnComentarioClickListener {

            override fun onComentarioEditarClick(comentario: Comentario?) {
                ModelTempComentario.addBD(articulo, comentario!!)
                viewModel.addComentario(comentario!!)
            }

            override fun onComentarioBorrarClick(comentario: Comentario?){
               borrarComentario(articulo, comentario!!)
            }
        }
    }

    private fun iniciarComentarios(articulo: Articulo){
        ModelTempComentario.db.collection("articulos")
            .document(articulo.id.toString())
            .collection("comentarios").get()
            .addOnSuccessListener { documentos ->
                for (documento in documentos){
                    var comentario = Comentario(
                        documento.id.toLongOrNull(),
                        documento.get("contenido").toString(),
                        documento.get("usuario").toString(),
                        documento.get("email").toString()
                    )
                    viewModel.addComentario(comentario)
                }
            }
    }

    private fun borrarComentario(articulo: Articulo, comentario: Comentario){
        AlertDialog.Builder(activity as Context)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage("¿Desea borrar este comentario?")
            .setPositiveButton(android.R.string.ok){v,_ ->
                ModelTempComentario.delBD(articulo, comentario)
                viewModel.delComentario(comentario)
                v.dismiss()
            }
            .setNegativeButton(android.R.string.cancel){v,_->
                v.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun soloLectura(){
        binding.etTitulo.isFocusable = false
        binding.etArticulo.isFocusable = false
        binding.etAutor.isFocusable = false
    }
}