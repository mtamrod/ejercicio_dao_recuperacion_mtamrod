package org.recualberti.model

class Postre(
    id: Int,
    nombre: String,
    calorias: Int,
    esVegana: Boolean,
    ingredientes: List<String>,
    val esDulce: Boolean
) : Receta(id, nombre, calorias, esVegana, "POSTRE", ingredientes)