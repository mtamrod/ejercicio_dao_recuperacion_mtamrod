package org.recualberti.ui

interface IEntradaSalida {
    fun leer(msj: String, saltoLinea: Boolean = false): String

    fun leerLista(msj: String, saltoLinea: Boolean = false): List<String>

    fun leerBool(msj: String, saltoLinea: Boolean = false): Boolean

    fun mostrar(texto: String, saltoLinea: Boolean = false)

    fun error(mensaje: String, saltoLinea: Boolean = false)

    fun limpiarPantalla(lineas: Int = 20)

    fun saltoLinea()

    fun pausa(msj: String = "ENTER para continuar...")
}

