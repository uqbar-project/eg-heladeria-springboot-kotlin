package org.uqbar.utils

import com.google.gson.Gson
import org.uqbar.heladeriakotlin.model.Duenio
import org.uqbar.heladeriakotlin.model.Heladeria
import org.uqbar.heladeriakotlin.model.TipoHeladeria

fun Any.toJSON(): String {
    val gson = Gson()
    return gson.toJson(this)
}

fun getHeladeriaBase(): Heladeria {
    return Heladeria("Tucán", TipoHeladeria.ECONOMICA, Duenio("Carlos Martinelli", 1L), 1L).apply {
        this.gustos["Dulce de leche"] = 5
    }
}