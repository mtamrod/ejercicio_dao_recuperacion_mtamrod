package org.recualberti.model

class Entrante(
    id: Int,
    nombre: String,
    calorias: Int,
    ingredientes: List<String>,
    esVegana: Boolean,
    esFrio: Boolean

) : Receta(id, nombre, calorias, ingredientes, esVegana) {


}