package com.severo.gamercommunity.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.severo.gamercommunity.R
import com.severo.gamercommunity.adapters.ArticuloAdapter
import com.severo.gamercommunity.databinding.FragmentListaBinding
import com.severo.gamercommunity.model.Articulo
import com.severo.gamercommunity.model.temp.ModelTempArticulo
import com.severo.gamercommunity.utils.Util
import com.severo.gamercommunity.viewmodel.AppViewModel

class ListaFragment : Fragment() {
    private var _binding: FragmentListaBinding? = null
    private val viewModel: AppViewModel by activityViewModels()
    lateinit var articulosAdapter:ArticuloAdapter
    private val db = Firebase.firestore
    private var util = Util()
    lateinit var username:String
    lateinit var email:String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = util.getUserName()
        email = util.getEmail()
        binding.tvPerfilLista.text = util.getUserName()
        iniciaRecyclerView()
        iniciaArticulos()
        iniciaCRUD()
        viewModel.articulosLiveData.observe(viewLifecycleOwner, Observer<List<Articulo>> { lista ->
            articulosAdapter.setLista(lista)
        })
        db.collection("articulos").addSnapshotListener { documentos, error ->
            if(error != null){
                return@addSnapshotListener
            }
            for (documento in documentos!!.documentChanges){
                documento.document
                var articulo: Articulo = Articulo(
                    documento.document.id.toLongOrNull(),
                    documento.document.get("titulo").toString(),
                    documento.document.get("descripcion").toString(),
                    documento.document.get("contenido").toString(),
                    0F,
                    documento.document.get("usuario").toString(),
                    documento.document.get("email").toString(),
                )
                when(documento.type){
                    DocumentChange.Type.ADDED -> viewModel.addArticulo(articulo)
                    DocumentChange.Type.MODIFIED -> viewModel.addArticulo(articulo)
                    DocumentChange.Type.REMOVED -> viewModel.delArticulo(articulo)
                }
            }
        }
        binding.btChatLista.setOnClickListener {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun iniciaRecyclerView() {
//creamos el adaptador
        articulosAdapter = ArticuloAdapter()
        with(binding.rvArticulos) {
//Creamos el layoutManager
            layoutManager = LinearLayoutManager(activity)
//le asignamos el adaptador
            adapter = articulosAdapter
        }
    }

    private fun iniciaCRUD(){
        binding.btNuevo.setOnClickListener {
            val articulo = Articulo("","","", 0F, username, email)
            val action = ListaFragmentDirections.actionListaFragmentToRedactarFragment(articulo)
            findNavController().navigate(action)
        }
        articulosAdapter.onArticuloClickListener = object :
            ArticuloAdapter.OnArticuloClickListener {
            //**************Editar Tarea*************
            override fun onTareaClick(articulo: Articulo?) {
//creamos acción enviamos argumento la tarea para editarla
                val action = ListaFragmentDirections.actionListaFragmentToArticuloFragment(articulo)
                findNavController().navigate(action)
            }
            //***********Borrar Tarea************
            override fun onTareaBorrarClick(articulo: Articulo?) {
//borramos directamente la tarea
                viewModel.delArticulo(articulo!!)
            }

            override fun onArticuloEditar(articulo: Articulo?) {
                val action = ListaFragmentDirections.actionListaFragmentToRedactarFragment(articulo)
                findNavController().navigate(action)
            }
        }
    }

    fun iniciaArticulos() {
        ModelTempArticulo.db.collection("articulos").get()
            .addOnSuccessListener { documentos ->
                for(documento in documentos){
                    var articulo: Articulo = Articulo(
                        documento.id.toLongOrNull(),
                        documento.get("titulo").toString(),
                        documento.get("descripcion").toString(),
                        documento.get("contenido").toString(),
                        0F,
                        documento.get("usuario").toString(),
                        documento.get("email").toString(),
                    )
                    viewModel.addArticulo(articulo)
                }
            }
    }
}