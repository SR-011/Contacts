package com.practice.contacts.view.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.contacts.R
import com.practice.contacts.data.Contacts
import com.practice.contacts.databinding.FragmentContactBinding
import com.practice.contacts.view.adapter.ContactAdapter
import com.practice.contacts.viewmodel.ContactsViewModel
import android.view.MenuInflater
import android.view.View.OnFocusChangeListener


class ContactFragment : Fragment() {

    private lateinit var binding: FragmentContactBinding
    private lateinit var contactAdapter: ContactAdapter
    private val contactViewModel: ContactsViewModel by viewModels()
    var contactList: ArrayList<Contacts> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false)
        checkPermission()
        setupObserver()
        setupUI()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.perform_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                contactAdapter.filter.filter(text)
                return false
            }

        })
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupObserver() {

        contactViewModel.contactsLiveData.observe(viewLifecycleOwner, {
            contactList = it
            Log.d("Sohel", "setupObserver: $contactList")
            contactAdapter.addNames(contactList)
        })
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS), 0
            )
        } else contactViewModel.fetchContacts()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d(
            "Sohel",
            "onRequestPermissionsResult: Request Code: $requestCode, grant Result: ${grantResults[0]}"
        )
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            contactViewModel.fetchContacts()
        }
    }

    private fun setupUI() {
        contactAdapter = ContactAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.contactRecycler.layoutManager = layoutManager
        binding.contactRecycler.adapter = contactAdapter
    }
}