package org.uqbarprojec.heladeriakotlin.utils

import com.google.gson.Gson

fun Any.toJSON(): String {
    val gson = Gson()
    return gson.toJson(this)
}
