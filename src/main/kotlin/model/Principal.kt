package org.recualberti.model

class Principal(
    id: Int,
    nombre: String,
    calorias: Int,
    esVegana: Boolean,
    ingredientes: List<String>,
    val momento: String
) : Receta(id, nombre, calorias, esVegana, "PRINCIPAL", ingredientes)