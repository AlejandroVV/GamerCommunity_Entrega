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
import com.severo.gamercommunity.adapters.ChatAdapter
import com.severo.gamercommunity.databinding.FragmentChatBinding
import com.severo.gamercommunity.model.Chat
import com.severo.gamercommunity.utils.Util
import com.severo.gamercommunity.viewmodel.AppViewModel


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val viewModel: AppViewModel by activityViewModels()
    lateinit var chatsAdapter: ChatAdapter
    private var util = Util()

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

        iniciaRecyclerView()
        iniciarCRUD()
        viewModel.chatsLiveData.observe(viewLifecycleOwner, Observer<List<Chat>> { lista ->
            chatsAdapter.setLista(lista)
        })
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
                var titulo = "Chat con $miembro2"
                var miembros = listOf<String>(miembro1,miembro2)
                var chat = Chat(titulo, miembros)
                viewModel.addChat(chat)
                println(chat.id)
            }
        }

        chatsAdapter.onChatClickListener = object :
            ChatAdapter.OnChatClickListener {

            override fun onChatClick(chat: Chat?) {

                val action = ChatFragmentDirections.actionChatFragmentToMensajeFragment(chat)
                findNavController().navigate(action)
            }

            override fun onChatBorrarClick(chat: Chat?) {
                viewModel.delChat(chat!!)
            }
        }
    }
}