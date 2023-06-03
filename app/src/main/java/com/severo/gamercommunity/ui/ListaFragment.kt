package com.severo.gamercommunity.ui

import android.app.AlertDialog
import android.content.Context
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
                    documento.document.get("valoracion").toString().toFloat(),
                    documento.document.get("usuario").toString(),
                    documento.document.get("email").toString(),
                    documento.document.get("valoraciones") as HashMap<String, Float>,
                )
                when(documento.type){
                    DocumentChange.Type.ADDED -> viewModel.addArticulo(articulo)
                    DocumentChange.Type.MODIFIED -> viewModel.addArticulo(articulo)
                    DocumentChange.Type.REMOVED -> viewModel.delArticulo(articulo)
                }
            }
        }
        binding.btChatLista.setOnClickListener {
            val action = ListaFragmentDirections.actionListaFragmentToChatFragment()
            findNavController().navigate(action)
        }
        binding.tvPerfilLista.setOnClickListener {
            val action = ListaFragmentDirections.actionListaFragmentToPerfilFragment()
            findNavController().navigate(action)
        }
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
            var valoraciones = hashMapOf<String,Float>(util.getEmail() to 0F)
            val articulo = Articulo("","","", 0F, username, email,
                                    valoraciones)
            val action = ListaFragmentDirections.actionListaFragmentToRedactarFragment(articulo)
            findNavController().navigate(action)
        }
        articulosAdapter.onArticuloClickListener = object :
            ArticuloAdapter.OnArticuloClickListener {

            override fun onTareaClick(articulo: Articulo?) {

                val action = ListaFragmentDirections.actionListaFragmentToArticuloFragment(articulo)
                findNavController().navigate(action)
            }

            override fun onTareaBorrarClick(articulo: Articulo?) {
                borrarArticulo(articulo!!)
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
                    var articulo = Articulo(
                        documento.id.toLongOrNull(),
                        documento.get("titulo").toString(),
                        documento.get("descripcion").toString(),
                        documento.get("contenido").toString(),
                        documento.get("valoracion").toString().toFloat(),
                        documento.get("usuario").toString(),
                        documento.get("email").toString(),
                        documento.get("valoraciones") as HashMap<String, Float>
                    )
                    viewModel.addArticulo(articulo)
                }
            }
    }

    private fun borrarArticulo(articulo: Articulo){
        AlertDialog.Builder(activity as Context)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage("¿Desea borrar el artículo \"${articulo.titulo}\"?")
            .setPositiveButton(android.R.string.ok){v,_ ->
                ModelTempArticulo.delBD(articulo)
                viewModel.delArticulo(articulo)
                v.dismiss()
            }
            .setNegativeButton(android.R.string.cancel){v,_->
                v.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}