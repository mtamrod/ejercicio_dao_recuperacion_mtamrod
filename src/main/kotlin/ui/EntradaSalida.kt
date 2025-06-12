package org.recualberti.ui

class EntradaSalida: IEntradaSalida {
    override fun mostrar(texto: String, saltoLinea: Boolean) {
        print("$texto${if (saltoLinea) "\n" else ""}")
    }

    override fun leer(msj: String, saltoLinea: Boolean): String {
        if (msj.isNotBlank()) mostrar(msj, saltoLinea)
        return readln()
    }

    override fun error(mensaje: String, saltoLinea: Boolean) {
        mostrar("# ERROR! => $mensaje", saltoLinea)
    }

    override fun saltoLinea() {
        mostrar("", true)
    }

    override fun limpiarPantalla(lineas: Int) {
        repeat(20) { mostrar("", true) }
    }

    override fun pausa(msj: String) {
        leer(msj, true)
    }
}