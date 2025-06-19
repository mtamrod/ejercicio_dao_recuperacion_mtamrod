package org.recualberti.model

class Entrante(
    id: Int,
    nombre: String,
    calorias: Int,
    esVegana: Boolean,
    ingredientes: List<String>,
    val esFrio: Boolean
) : Receta(id, nombre, calorias, esVegana, "ENTRANTE", ingredientes)