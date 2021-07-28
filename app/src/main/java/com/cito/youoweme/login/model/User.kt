package com.cito.youoweme.login.model

data class User(
    val username: String,
    val passwordHash: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (other is User)
            return username == other.username
        return false
    }
}