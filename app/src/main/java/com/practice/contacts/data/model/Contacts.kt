package com.practice.contacts.data

data class Contacts(
    var id: String,
    var name: String,
    var numbers: MutableList<String>,
    var emails: MutableList<String>
) {
    constructor() : this(
        id = "",
        name = "",
        numbers = mutableListOf(),
        emails = mutableListOf()
    )
}