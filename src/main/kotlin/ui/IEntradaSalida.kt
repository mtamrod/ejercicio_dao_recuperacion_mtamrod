package org.recualberti.ui

interface IEntradaSalida {
    fun leer(msj: String, saltoLinea: Boolean): String
    fun mostrar(texto: String, saltoLinea: Boolean)
    fun error(mensaje: String, saltoLinea: Boolean)
    fun limpiarPantalla(lineas: Int = 20)
    fun saltoLinea()
    fun pausa(msj: String = "ENTER para continuar...")
}

