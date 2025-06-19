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
        val lista = mutableListOf<String>()
        var salir = true
        while (salir) {
            if (saltoLinea) {
                println(msj)
            } else {
                print(msj)
            }

            val input = readln().trim()

            if (input.isBlank()) {
                // El usuario introduce un msj vacio == salir del bucle
                salir = false
            } else {
                lista.add(input)
            }
        }
        return lista
    }


    override fun leerBool(msj:String, saltoLinea: Boolean): Boolean {
        val input: String = readln().trim()
        var vegana: Boolean = false
        var salir = true
        while (salir) {
            if (input == "Si") {
                vegana = true
                salir = false
            } else if (input == "No") {
                vegana = false
                salir = false
            } else {
                error("Debes indicar con 'Si' o 'No'", true)
                salir = true
            }
        }
        return vegana
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