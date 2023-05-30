package com.severo.gamercommunity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.severo.gamercommunity.R
import com.severo.gamercommunity.databinding.ItemMensajeBinding
import com.severo.gamercommunity.model.Mensaje
import com.severo.gamercommunity.utils.Util

class MensajeAdapter():RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder>() {

    var listaMensajes: List<Mensaje>?=null

    private var util = Util()

    fun setLista(lista:List<Mensaje>){
        listaMensajes=lista
        notifyDataSetChanged()
    }
    inner class MensajeViewHolder(val binding: ItemMensajeBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): MensajeViewHolder {
        val binding = ItemMensajeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MensajeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder,
                                  position: Int) {
        with(holder) {
            with(listaMensajes!![position]) {
                var username = util.getUserName()
                if(this.autor == username){
                    binding.tvAutorMensaje.text = "Tú"
                    binding.cvMensaje.setBackgroundResource(R.color.teal_200)
                } else {
                    binding.tvAutorMensaje.text = username
                }

                binding.tvContenidoMensaje.text = contenido
            }
        }
    }

    override fun getItemCount(): Int = listaMensajes?.size?:0
}