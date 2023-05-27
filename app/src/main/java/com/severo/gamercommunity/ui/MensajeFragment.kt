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
import com.severo.gamercommunity.databinding.FragmentMensajeBinding
import com.severo.gamercommunity.viewmodel.AppViewModel


class MensajeFragment : Fragment() {

    private var _binding: FragmentMensajeBinding? = null
    val args: MensajeFragmentArgs by navArgs()
    private val viewModel: AppViewModel by activityViewModels()

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

        binding.btVolverChats.setOnClickListener {
            val action = MensajeFragmentDirections.actionMensajeFragmentToChatFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}