package com.cito.youoweme.data.model

data class Contact(
    var id: Long? = null,
    var name : String? = null,
    var surname : String? = null,
    var balance: Float? = null,
    var usernameRef: String? = null
) {

    override fun toString(): String {
        return "$name $surname"
    }
}