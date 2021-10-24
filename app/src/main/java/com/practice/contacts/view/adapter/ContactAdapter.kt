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

    var names: ArrayList<Contacts> = ArrayList()
    var filteredList: ArrayList<Contacts> = ArrayList()

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
        val contact = filteredList[position].name
        holder.header.text = contact.substring(0, 1)
        holder.item.text = contact

        if ((position > 0 && filteredList[position - 1].name.substring(0, 1) == contact.substring(0, 1)))
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

    override fun getItemCount() = filteredList.size

    fun addNames(names: List<Contacts>) {
        this.names = names as ArrayList<Contacts>
        filteredList = names
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val searchResultList = ArrayList<Contacts>()
                if (charSequence.toString().isEmpty()) {
                    searchResultList.addAll(names)
                } else {
                    val filterPattern = charSequence.toString().lowercase().trim()
                    Log.d("Sohel", "performFiltering Size: ${names.size}")
                    for (item in names) {
                        if (item.name.lowercase().contains(filterPattern) ||
                            (item.numbers.isNotEmpty() && item.numbers[0].contains(charSequence.toString().trim()))
                        ) {
                            Log.d("Sohel", "performFilteringWithNumber: ${item.numbers}")
                            searchResultList.add(item)
                            Log.d("Sohel", "performFiltering Item:$item ")
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
                filteredList = result?.values as ArrayList<Contacts>
                notifyDataSetChanged()
            }
        }
    }
}