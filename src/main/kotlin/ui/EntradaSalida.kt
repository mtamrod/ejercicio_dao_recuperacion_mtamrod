package org.recualberti.ui

class EntradaSalida: IEntradaSalida {
    override fun mostrar(texto: String, saltoLinea: Boolean) {
        print("$texto${if (saltoLinea) "\n" else ""}")
    }

    override fun leer(msj: String, saltoLinea: Boolean): String {
        if (msj.isNotBlank()) mostrar(msj, saltoLinea)
        return readln()
    }

    override fun leerLista(msj: String, saltoLinea: Boolean): List<String> {
        val lista = mutableListOf("")
        while (msj.isNotBlank()) {
            lista.add(readln())
        }

        return lista
    }

    override fun leerBool(msj:String, saltoLinea: Boolean): Boolean {
        if (msj == "Si") {
            return true
        } else if (msj == "No") {
            return false
        } else {
            error("Debes indicar con 'Si' o 'No'", true)
        }
    }

    override fun error(mensaje: String, saltoLinea: Boolean) {
        mostrar("#ERROR -> $mensaje", saltoLinea)
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