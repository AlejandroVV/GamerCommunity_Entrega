package com.severo.gamercommunity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.severo.gamercommunity.databinding.ItemChatBinding
import com.severo.gamercommunity.model.Chat

class ChatAdapter():
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>(){

    var listaChats: List<Chat>?=null
    var onChatClickListener:OnChatClickListener?=null

    fun setLista(lista:List<Chat>){
        listaChats=lista
        notifyDataSetChanged()
    }

    inner class ChatViewHolder(val binding: ItemChatBinding)
        : RecyclerView.ViewHolder(binding.root){
            init {
                binding.btBorrarChat.setOnClickListener(){

                    val chat = listaChats?.get(this.adapterPosition)
                    onChatClickListener?.onChatBorrarClick(chat)
                }

                binding.root.setOnClickListener(){
                    val chat = listaChats?.get(this.adapterPosition)
                    onChatClickListener?.onChatClick(chat)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): ChatViewHolder {
        val binding = ItemChatBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder,
                                  position: Int) {
        with(holder) {

            with(listaChats!![position]) {
                binding.tvNombreChat.text = titulo
            }
        }
    }

    override fun getItemCount(): Int = listaChats?.size?:0

    interface OnChatClickListener{

        fun onChatClick(chat: Chat?)

        fun onChatBorrarClick(chat: Chat?)
    }
}