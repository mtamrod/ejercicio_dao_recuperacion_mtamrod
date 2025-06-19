package org.recualberti.model

abstract class Receta ( //Sealed o Abstract?
    val id: Int,
    var nombre: String,
    var calorias: Int,
    var esVegana: Boolean,
    var tipo: String,
    val ingredientes: List<String>
)