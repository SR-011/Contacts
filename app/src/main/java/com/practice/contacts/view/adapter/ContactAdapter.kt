package com.practice.contacts.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.contacts.R
import com.practice.contacts.data.Contacts
import com.practice.contacts.databinding.ContactRowBinding
import kotlin.collections.ArrayList

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.MyViewHolder>(), Filterable {

    var contactList: ArrayList<Contacts> = ArrayList()
    var filteredContactList: ArrayList<Contacts> = ArrayList()

    //var OnItemClick: ((Contacts) -> Unit)? = null

    class MyViewHolder(binding: ContactRowBinding) : RecyclerView.ViewHolder(binding.root) {

        val header: TextView = binding.sectionHeader
        val item: TextView = binding.contactName
        /*fun bind(names: Contacts) {
                binding.variable = names
            }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.contact_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contact = filteredContactList[position].name
        holder.header.text = contact.substring(0, 1)
        holder.item.text = contact

        if ((position > 0 && filteredContactList[position - 1].name.substring(0, 1) == contact.substring(0, 1)))
        {
            holder.header.visibility = View.GONE
        }
        else {
            holder.header.visibility = View.VISIBLE
        }

        /*if(contact.substring(0, 1) in "A" .. "Z"){
            if (position > 0 && filteredList[position - 1].name.substring(0, 1) == contact.substring(0, 1)) {
                holder.header.visibility = View.GONE
            } else {
                holder.header.visibility = View.VISIBLE
            }
        } else{
            holder.header.text = "#"
            holder.header.visibility = View.VISIBLE
        }*/
        // holder.bind(names[position])
    }

    override fun getItemCount() = filteredContactList.size

    fun addNames(names: List<Contacts>) {
        this.contactList = names as ArrayList<Contacts>
        filteredContactList = names
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val searchResultList = ArrayList<Contacts>()
                if (charSequence.toString().isEmpty()) {
                    searchResultList.addAll(contactList)
                } else {
                    val filterPattern = charSequence.toString().lowercase().trim()
                    Log.d("Sohel", "performFiltering Size: ${contactList.size}")
                    for (item in contactList) {
                        val contactsSearchResult = item.name.lowercase().contains(filterPattern)
                        val numberSearchResult = item.numbers.filter { number -> number.contains(filterPattern) }.toMutableList()
                        if (numberSearchResult.size == 0 && contactsSearchResult) {
                            searchResultList.add(Contacts(id = item.id, name = item.name, numbers = item.numbers, emails = item.emails))
                        } else if (!contactsSearchResult && numberSearchResult.size > 0) {
                            Log.d("Sohel", "numberSearchResult: ${numberSearchResult.size}")
                            Log.d("Sohel", "numberSearchResult: $numberSearchResult")
                            searchResultList.add(Contacts(id = item.id, name = item.name, numbers = numberSearchResult, emails = item.emails))
                        }
                        else{
                            continue
                        }
                    }
                }
                Log.d("Sohel", "performFiltering:${searchResultList.size} ")
                Log.d("Sohel", "performFiltering:$searchResultList ")
                val filterResult = FilterResults()
                filterResult.values = searchResultList
                Log.d("Sohel", "performFiltering:${filterResult}")
                return filterResult
            }

            override fun publishResults(charSequence: CharSequence?, result: FilterResults?) {
                filteredContactList = result?.values as ArrayList<Contacts>
                notifyDataSetChanged()
            }
        }
    }
}