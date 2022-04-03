package org.uqbarprojec.heladeriakotlin.utils

import org.json.JSONObject

fun Map<String, Any>.toJSON() = JSONObject(this).toString()
