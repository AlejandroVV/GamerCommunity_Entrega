package com.severo.gamercommunity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.severo.gamercommunity.databinding.ItemSeguidoBinding
import com.severo.gamercommunity.model.Seguido

class SeguidoAdapter:RecyclerView.Adapter<SeguidoAdapter.SeguidoViewHolder>() {

    var listaSeguidos: List<Seguido>?=null
    var onSeguidoClickListener:OnSeguidoClickListener?=null

    fun setLista(lista:List<Seguido>){
        listaSeguidos=lista
        notifyDataSetChanged()
    }

    inner class SeguidoViewHolder(val binding: ItemSeguidoBinding)
        : RecyclerView.ViewHolder(binding.root){

        init {
            binding.btBorrarSeguido.setOnClickListener(){
                val seguido= listaSeguidos!![this.adapterPosition]
                onSeguidoClickListener?.onSeguidoBorrarClick(seguido)
            }

            binding.root.setOnClickListener(){
                val seguido= listaSeguidos!![this.adapterPosition]
                onSeguidoClickListener?.onSeguidoClick(seguido)
            }
        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): SeguidoViewHolder {
        val binding = ItemSeguidoBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SeguidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeguidoViewHolder, position: Int) {
        with(holder) {

            with(listaSeguidos!![position]) {
                binding.tvSeguido.text = usuario
            }
        }
    }

    override fun getItemCount(): Int = listaSeguidos?.size?:0

    interface OnSeguidoClickListener{

        fun onSeguidoClick(seguido: Seguido?)

        fun onSeguidoBorrarClick(seguido: Seguido?)
    }
}