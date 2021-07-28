package com.cito.youoweme.data.model

import com.cito.youoweme.data.YomData

data class Contact(
    override var id: Long? = null,
    var name : String? = null,
    var surname : String? = null,
    var balance: Float? = null
): YomData {

    override fun toString(): String {
        return "$surname $name"
    }
}