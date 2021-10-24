package com.practice.contacts.data

data class Contacts(
    val id: String,
    val name: String
) {
    var numbers = ArrayList<String>()
    var emails = ArrayList<String>()

}