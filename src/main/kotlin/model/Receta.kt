package org.recualberti.model

open class Receta (
    val id: Int,
    var nombre: String,
    var calorias: Int,
    var ingredientes: List<String>,
    var esVegana: Boolean

) {


}