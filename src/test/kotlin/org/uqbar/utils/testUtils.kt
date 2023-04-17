package org.uqbar.utils

import com.google.gson.Gson

fun Any.toJSON(): String {
    val gson = Gson()
    return gson.toJson(this)
}
