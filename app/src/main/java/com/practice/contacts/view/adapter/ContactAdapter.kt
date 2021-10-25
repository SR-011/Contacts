package com.practice.contacts.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.contacts.R
import com.practice.contacts.data.Contacts
import com.practice.contacts.databinding.ContactRowBinding

class ContactAdapter() : RecyclerView.Adapter<ContactAdapter.MyViewHolder>(), Filterable {

    var contactList: ArrayList<Contacts> = ArrayList()
    var filteredContactList: ArrayList<Contacts> = ArrayList()


    class MyViewHolder(val binding: ContactRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: Contacts) {
                binding.variable = name
            }
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
        holder.bind(filteredContactList[position])
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