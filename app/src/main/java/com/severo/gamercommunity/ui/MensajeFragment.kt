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
import com.severo.gamercommunity.adapters.MensajeAdapter
import com.severo.gamercommunity.databinding.FragmentMensajeBinding
import com.severo.gamercommunity.model.Mensaje
import com.severo.gamercommunity.utils.Util
import com.severo.gamercommunity.viewmodel.AppViewModel


class MensajeFragment : Fragment() {

    private var _binding: FragmentMensajeBinding? = null
    val args: MensajeFragmentArgs by navArgs()
    private val viewModel: AppViewModel by activityViewModels()
    lateinit var mensajesAdapter:MensajeAdapter
    private var util = Util()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMensajeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = args.chat!!.titulo
        iniciaRecyclerView()
        viewModel.mensajesLiveData.observe(viewLifecycleOwner, Observer<List<Mensaje>> { lista ->
            mensajesAdapter.setLista(lista)
        })
        binding.btVolverChats.setOnClickListener {
            val action = MensajeFragmentDirections.actionMensajeFragmentToChatFragment()
            findNavController().navigate(action)
        }
        binding.btEnviar.setOnClickListener {
            if (binding.etMensaje.text.isNotEmpty()){
                var contenido = binding.etMensaje.text.toString()
                var autor = util.getUserName()
                var mensaje = Mensaje(contenido, autor)
                viewModel.addMensaje(mensaje)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun iniciaRecyclerView() {

        mensajesAdapter = MensajeAdapter()
        with(binding.rvMensajes) {
            layoutManager = LinearLayoutManager(activity)
            adapter = mensajesAdapter
        }
    }
}