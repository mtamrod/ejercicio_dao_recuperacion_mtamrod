package org.recualberti.model

class Principal(
    id: Int,
    nombre: String,
    calorias: Int,
    ingredientes: List<String>,
    esVegana: Boolean,
    momento: String

) : Receta(id, nombre, calorias, ingredientes, esVegana) {

}