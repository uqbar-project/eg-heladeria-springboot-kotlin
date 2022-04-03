package org.uqbarprojec.heladeriakotlin.utils

import com.google.gson.Gson
import org.json.JSONObject

fun Map<String, Any>.toJSON() = JSONObject(this).toString()

fun Any.toJson(): String {
    val gson = Gson()
    return gson.toJson(this)
}
