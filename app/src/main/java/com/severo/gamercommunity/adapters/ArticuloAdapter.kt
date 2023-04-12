package com.severo.gamercommunity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.severo.gamercommunity.databinding.ItemArticuloBinding
import com.severo.gamercommunity.model.Articulo

class ArticuloAdapter(): RecyclerView.Adapter<ArticuloAdapter.ArticuloViewHolder>() {
    var listaArticulos: List<Articulo>?=null
    var onArticuloClickListener:OnArticuloClickListener?=null

    fun setLista(lista:List<Articulo>){
        listaArticulos=lista
//notifica al adaptador que hay cambios y tiene que redibujar el ReciclerView
        notifyDataSetChanged()
    }
    inner class ArticuloViewHolder(val binding: ItemArticuloBinding)
        : RecyclerView.ViewHolder(binding.root){

        init {
//inicio del click de icono borrar
            binding.ivBorrar.setOnClickListener(){
                val tarea=listaArticulos?.get(this.adapterPosition)
                onArticuloClickListener?.onTareaBorrarClick(tarea)
            }
            binding.root.setOnClickListener(){
                val tarea=listaArticulos?.get(this.adapterPosition)
                onArticuloClickListener?.onTareaClick(tarea)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        //utilizamos binding, en otro caso hay que indicar el item.xml.
        //Para más detalles puedes verlo en la documentación
        val binding = ItemArticuloBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticuloViewHolder(binding)
    }

    override fun getItemCount(): Int = listaArticulos?.size?:0

    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        //Nos pasan la posición del item a mostrar en el viewHolder
        with(holder) {
//cogemos la tarea a mostrar y rellenamos los campos del ViewHolder
            with(listaArticulos!!.get(position)) {
                if (this.usuario != "Pepe Gotero"){
                    binding.ivBorrar.visibility = View.GONE
                    binding.btEditar.visibility = View.GONE
                } else {
                    binding.ivBorrar.visibility = View.VISIBLE
                    binding.btEditar.visibility = View.VISIBLE
                }

                binding.tvTitulo.text = titulo
                binding.tvDescripcion.text = descripcion
                binding.rbValoracionLista.rating = valoracion
            }
        }
    }

    interface OnArticuloClickListener{
        //editar tarea que contiene el ViewHolder
        fun onTareaClick(articulo: Articulo?)
        //borrar tarea que contiene el ViewHolder
        fun onTareaBorrarClick(articulo: Articulo?)
    }
}