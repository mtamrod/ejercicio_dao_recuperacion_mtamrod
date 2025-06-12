package org.recualberti.model

class Postre (
    id: Int,
    nombre: String,
    calorias: Int,
    ingredientes: List<String>,
    esVegana: Boolean,
    var esDulce: Boolean

) : Receta(id, nombre, calorias, ingredientes, esVegana) {


}