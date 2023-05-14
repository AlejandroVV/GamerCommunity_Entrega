package com.severo.gamercommunity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.severo.gamercommunity.databinding.ItemComentarioBinding
import com.severo.gamercommunity.model.Comentario

class ComentarioAdapter():RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder>() {

    var listaComentarios: List<Comentario>?=null
    var onComentarioClickListener:OnComentarioClickListener?=null

    fun setLista(lista:List<Comentario>){
        listaComentarios=lista
//notifica al adaptador que hay cambios y tiene que redibujar el ReciclerView
        notifyDataSetChanged()
    }

    inner class ComentarioViewHolder(val binding: ItemComentarioBinding)
        : RecyclerView.ViewHolder(binding.root){

        init {

            binding.btEditarComentario.setOnClickListener {
                val comentario= listaComentarios!![this.adapterPosition]
                binding.btEditarComentario.visibility = View.GONE
                binding.btBorrar.visibility = View.GONE
                binding.tvComentario.visibility = View.GONE
                binding.etComentario.visibility = View.VISIBLE
                binding.etComentario.setText(comentario.contenido)
                binding.btGuardarComentario.visibility = View.VISIBLE
                binding.btCancelarComentario.visibility = View.VISIBLE
            }

            binding.btBorrar.setOnClickListener {
                val comentario= listaComentarios!![this.adapterPosition]
                onComentarioClickListener?.onComentarioBorrarClick(comentario)
            }

            binding.btCancelarComentario.setOnClickListener(){
                val comentario= listaComentarios!![this.adapterPosition]
                binding.etComentario.visibility = View.GONE
                binding.tvComentario.visibility = View.VISIBLE
                binding.btGuardarComentario.visibility = View.GONE
                binding.btCancelarComentario.visibility = View.GONE
                binding.btEditarComentario.visibility = View.VISIBLE
                binding.btBorrar.visibility = View.VISIBLE
            }

            binding.btGuardarComentario.setOnClickListener(){
                val comentario= listaComentarios!![this.adapterPosition]
                var id = comentario.id
                var contenido = binding.etComentario.text.toString()
                var usuario = comentario.usuario
                var editado = Comentario(id, contenido, usuario)
                onComentarioClickListener?.onComentarioEditarClick(editado)
                binding.btGuardarComentario.visibility = View.GONE
                binding.btCancelarComentario.visibility = View.GONE
                binding.etComentario.visibility = View.GONE
                binding.tvComentario.visibility = View.VISIBLE
                binding.tvComentario.text = contenido
                binding.btEditarComentario.visibility = View.VISIBLE
                binding.btBorrar.visibility = View.VISIBLE
            }
        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): ComentarioViewHolder {
        val binding = ItemComentarioBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ComentarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComentarioViewHolder,
                                  position: Int) {
        with(holder) {
//cogemos la tarea a mostrar y rellenamos los campos del ViewHolder
            with(listaComentarios!![position]) {
                binding.tvUserComentario.text = "$usuario ID: $id"
                binding.etComentario.visibility = View.GONE
                binding.tvComentario.text = contenido
                binding.btGuardarComentario.visibility = View.GONE
                binding.btCancelarComentario.visibility = View.GONE
                if(usuario.contains("usuario")){
                    binding.btEditarComentario.visibility = View.VISIBLE
                    binding.btBorrar.visibility = View.VISIBLE
                } else {
                    binding.btEditarComentario.visibility = View.GONE
                    binding.btBorrar.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int = listaComentarios?.size?:0

    interface OnComentarioClickListener{

        fun onComentarioEditarClick(comentario: Comentario?)

        fun onComentarioBorrarClick(comentario: Comentario?)
    }
}