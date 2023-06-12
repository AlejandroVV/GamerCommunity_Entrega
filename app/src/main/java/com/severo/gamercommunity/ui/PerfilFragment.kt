package com.severo.gamercommunity.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.severo.gamercommunity.adapters.MensajeAdapter
import com.severo.gamercommunity.adapters.SeguidoAdapter
import com.severo.gamercommunity.databinding.FragmentMensajeBinding
import com.severo.gamercommunity.databinding.FragmentPerfilBinding
import com.severo.gamercommunity.model.Chat
import com.severo.gamercommunity.model.Mensaje
import com.severo.gamercommunity.model.Seguido
import com.severo.gamercommunity.model.temp.ModelTempMensaje
import com.severo.gamercommunity.model.temp.ModelTempSeguido
import com.severo.gamercommunity.utils.Util
import com.severo.gamercommunity.viewmodel.AppViewModel


class PerfilFragment : Fragment() {
    private var _binding: FragmentPerfilBinding? = null
    private var util = Util()
    private val db = Firebase.firestore
    private val viewModel: AppViewModel by activityViewModels()
    lateinit var seguidosAdapter: SeguidoAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db.collection("usuarios").document(util.getEmail())
            .get().addOnSuccessListener {
                binding.tvUserPerfil.text = it.get("username").toString()
                binding.tvEmailValor.text = it.id
                binding.tvNombreValor.text = it.get("nombre").toString()
            }
        binding.btVolverPerfil.setOnClickListener {
            val action = PerfilFragmentDirections.actionPerfilFragmentToListaFragment()
            findNavController().navigate(action)
        }
        iniciaRecyclerView()
        iniciarSeguidos()
        iniciarCRUD()
        viewModel.seguidosLiveData.observe(viewLifecycleOwner, Observer<List<Seguido>> { lista ->
            seguidosAdapter.setLista(lista)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun iniciaRecyclerView() {
        seguidosAdapter = SeguidoAdapter()
        with(binding.rvSigo) {
            layoutManager = LinearLayoutManager(activity)
            adapter = seguidosAdapter
        }
    }

    private fun iniciarCRUD(){
        binding.btAddSigo.setOnClickListener {
            if(binding.etAddSigo.text.isNotEmpty()){
                var email = binding.etAddSigo.text.toString()
                db.collection("usuarios").document(email)
                    .get().addOnSuccessListener {
                        if(it.exists()){
                            var usuario = it.get("username").toString()
                            var seguido = Seguido(usuario, email)
                            ModelTempSeguido.addBD(seguido)
                            viewModel.addSeguido(seguido)
                        }
                    }
                binding.etAddSigo.setText("")
            }
        }

        seguidosAdapter.onSeguidoClickListener = object :
            SeguidoAdapter.OnSeguidoClickListener {

            override fun onSeguidoClick(seguido: Seguido?) {}

            override fun onSeguidoBorrarClick(seguido: Seguido?) {
                borrarSeguido(seguido!!)
            }
        }
    }

    private fun iniciarSeguidos(){
        ModelTempSeguido.db.collection("usuarios").document(ModelTempSeguido.util.getEmail())
            .collection("seguidos").get()
            .addOnSuccessListener { documentos ->
                for (documento in documentos){
                    var seguido = Seguido(
                        documento.id.toLongOrNull(),
                        documento.get("usuario").toString(),
                        documento.get("email").toString(),
                    )
                    viewModel.addSeguido(seguido)
                }
            }
    }

    private fun borrarSeguido(seguido: Seguido){
        AlertDialog.Builder(activity as Context)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage("¿Desea dejar de seguir a ${seguido.usuario}?")
            .setPositiveButton(android.R.string.ok){v,_->
                ModelTempSeguido.delBD(seguido)
                viewModel.delSeguido(seguido)
                v.dismiss()
            }
            .setNegativeButton(android.R.string.cancel){v,_-> v.dismiss()}
            .setCancelable(false)
            .create()
            .show()
    }
}