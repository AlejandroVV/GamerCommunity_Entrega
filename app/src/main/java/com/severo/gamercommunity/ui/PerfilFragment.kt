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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.severo.gamercommunity.adapters.MensajeAdapter
import com.severo.gamercommunity.databinding.FragmentMensajeBinding
import com.severo.gamercommunity.databinding.FragmentPerfilBinding
import com.severo.gamercommunity.model.Chat
import com.severo.gamercommunity.model.Mensaje
import com.severo.gamercommunity.model.temp.ModelTempMensaje
import com.severo.gamercommunity.utils.Util
import com.severo.gamercommunity.viewmodel.AppViewModel


class PerfilFragment : Fragment() {
    private var _binding: FragmentPerfilBinding? = null
    private var util = Util()
    private val db = Firebase.firestore

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}