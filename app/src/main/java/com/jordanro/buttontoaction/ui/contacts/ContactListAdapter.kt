package com.jordanro.buttontoaction.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jordanro.buttontoaction.R
import com.jordanro.buttontoaction.data.entities.Contact

class ContactListAdapter(private val listener:OnClickListener,var data: List<Contact>) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_item,parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = data!![position]
        holder.onBind(contact)
        holder.itemView.setOnClickListener(View.OnClickListener {
            listener.onItemClicked(contact)
        })
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var name: TextView
        var phone: TextView

        init {
            name = itemView.findViewById(R.id.name)
            phone = itemView.findViewById(R.id.phone)
        }

        fun onBind(contact: Contact) {
            name.text = contact.name
            phone.text = contact.phoneNumber
        }
    }


    interface OnClickListener{
        fun onItemClicked(contact: Contact)
    }


}