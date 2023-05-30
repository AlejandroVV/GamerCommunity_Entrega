package com.severo.gamercommunity.ui

import android.annotation.SuppressLint
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
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.severo.gamercommunity.adapters.ChatAdapter
import com.severo.gamercommunity.databinding.FragmentChatBinding
import com.severo.gamercommunity.model.Articulo
import com.severo.gamercommunity.model.Chat
import com.severo.gamercommunity.model.temp.ModelTempArticulo
import com.severo.gamercommunity.model.temp.ModelTempChat
import com.severo.gamercommunity.utils.Util
import com.severo.gamercommunity.viewmodel.AppViewModel


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val viewModel: AppViewModel by activityViewModels()
    lateinit var chatsAdapter: ChatAdapter
    private var util = Util()
    private val db = Firebase.firestore

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPerfilChat.text = util.getUserName()
        iniciaRecyclerView()
        iniciarChats()
        iniciarCRUD()
        viewModel.chatsLiveData.observe(viewLifecycleOwner, Observer<List<Chat>> { lista ->
            chatsAdapter.setLista(lista)
        })
        db.collection("chats").whereArrayContains("miembros", util.getEmail())
            .addSnapshotListener{ documentos, error ->
            if(error != null){
                return@addSnapshotListener
            }
            for (documento in documentos!!.documentChanges){
                var chat = Chat(
                    documento.document.get("titulo").toString(),
                    documento.document.get("miembros") as List<String>,
                    documento.document.get("idBD").toString()
                )
                when(documento.type){
                    DocumentChange.Type.ADDED -> viewModel.addChat(chat)
                    DocumentChange.Type.MODIFIED -> viewModel.addChat(chat)
                    DocumentChange.Type.REMOVED -> viewModel.delChat(chat)
                }
            }
        }
        binding.btVolverLista.setOnClickListener {
            val action = ChatFragmentDirections.actionChatFragmentToListaFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun iniciaRecyclerView() {
        chatsAdapter = ChatAdapter()
        with(binding.rvChats) {

            layoutManager = LinearLayoutManager(activity)
            adapter = chatsAdapter
        }
    }

    private fun iniciarCRUD(){

        binding.btBusqueda.setOnClickListener {
            if(binding.etBusqueda.text.isNotEmpty()){
                var miembro1 = util.getEmail()
                var miembro2 = binding.etBusqueda.text.toString()
                db.collection("usuarios").document(miembro2)
                    .get().addOnSuccessListener {
                        if(it.exists()){
                            var receptor = it.get("username").toString()
                            var titulo = "Chat ${util.getUserName()}-$receptor"
                            var miembros = listOf<String>(miembro1,miembro2)
                            var idBD = "$miembro1-$miembro2"
                            var chat = Chat(titulo, miembros, idBD)
                            ModelTempChat.addBD(chat)
                            viewModel.addChat(chat)
                            val action = ChatFragmentDirections.actionChatFragmentToMensajeFragment(chat)
                            findNavController().navigate(action)
                        }
                    }
            }
        }

        chatsAdapter.onChatClickListener = object :
            ChatAdapter.OnChatClickListener {

            override fun onChatClick(chat: Chat?) {

                val action = ChatFragmentDirections.actionChatFragmentToMensajeFragment(chat)
                findNavController().navigate(action)
            }

            override fun onChatBorrarClick(chat: Chat?) {
               borrarChat(chat!!)
            }
        }
    }

    private fun iniciarChats(){
        db.collection("chats").whereArrayContains("miembros", util.getEmail())
            .get().addOnSuccessListener {
                documentos ->
                for (documento in documentos){
                    var chat = Chat(
                        documento.get("titulo").toString(),
                        documento.get("miembros") as List<String>,
                        documento.get("idBD").toString()
                    )
                        viewModel.addChat(chat)
                }
            }
    }

    private fun borrarChat(chat: Chat){
        AlertDialog.Builder(activity as Context)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage("¿Desea borrar el chat \"${chat.titulo}\"?")
            .setPositiveButton(android.R.string.ok){v,_ ->
                ModelTempChat.delBD(chat)
                viewModel.delChat(chat)
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