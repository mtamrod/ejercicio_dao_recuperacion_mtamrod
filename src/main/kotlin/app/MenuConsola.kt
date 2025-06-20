package org.recualberti.app

import org.recualberti.data.db.IdFactory
import org.recualberti.model.*
import org.recualberti.service.IRecetaService
import org.recualberti.ui.IEntradaSalida

class MenuConsola(
    private val servicioReceta: IRecetaService,
    private val ui: IEntradaSalida
) {
    private var funcionando: Boolean = true

    fun iniciar() {

        while (funcionando) {
            ui.limpiarPantalla()
            ui.mostrar(
                """
                    === MENÚ DE RECETAS ===
                    1. Crear
                    2. Buscar
                    3. Buscar Todas
                    4. Actualizar
                    5. Borrar
                    6. Salir
                    === === === === === ===
                """.trimIndent(),
                true
            )

            when (ui.leer("\nOpción: ", true)) {
                "1" -> crearReceta()
                "2" -> mostrarReceta(ui.leer("Introduce el id de la receta", true).toInt())
                "3" -> mostrarTodasReceta()
                "4" -> actualizarReceta()
                "5" -> eliminarReceta()
                "6" -> salir()

                else -> ui.error("Opción no válida.", true)
            }

            //ui.pausa() //TODO
        }
    }



    private fun salir() {
        ui.mostrar("Saliendo...", false)
        funcionando = false
    }

    private fun crearReceta() {
        val nombre = ui.leer("Nombre: ")
        val calorias = ui.leer("Calorias: ").toInt()
        val ingredientes = ui.leerLista("Ingredientes: ")
        val esVegana = ui.leerBool("¿Es Vegana?(Si/No): ")

        val tipo = ui.leer("Tipo de receta (entrante/principal/postre): ", true).lowercase()

        val receta = when (tipo) {
            "entrante" -> {
                val esFrio = ui.leerBool("¿Frío? (Si/No): ", true)
                Entrante(0, nombre, calorias, esVegana, ingredientes, esFrio)
            }
            "principal" -> {
                val momento = ui.leer("¿Momento? (almuerzo, cena...): ", true)
                Principal(0, nombre, calorias, esVegana, ingredientes, momento)
            }
            "postre" -> {
                val esDulce = ui.leerBool("¿Dulce? (Si/No): ", true)
                Postre(0, nombre, calorias, esVegana, ingredientes, esDulce)
            }
            else -> {
                ui.error("El tipo de receta no es válido", true)
                return
            }
        }
        servicioReceta.crearReceta(receta)
        ui.mostrar("Receta insertada correctamente.", true)
    }


    private fun mostrarReceta(id: Int) { //TODO: Mostrar receta deber de ser por id
        /*
        val recetaUnica = servicioReceta.buscarReceta(nombre)
        ui.mostrar(
            "Id: ${recetaUnica.id} - NOmbre: ${recetaUnica.nombre} - Calorias: ${recetaUnica.calorias} - Ingredientes: ${recetaUnica.ingredientes} - Vegana: ${recetaUnica.esVegana}",
            true
        )

         */
    }

    private fun mostrarTodasReceta() {
        val recetas = servicioReceta.obtenerTodas()

        if (recetas.isEmpty()) ui.mostrar("No se encontraron recetas.", true)
        else recetas.forEach {
            ui.mostrar(
                "Id: ${it.id} - NOmbre: ${it.nombre} - Calorias: ${it.calorias} - Ingredientes: ${it.ingredientes} - Vegana: ${it.esVegana}",
                true
            )
        }
    }

    private fun eliminarReceta() {
        val id = ui.leer("Id de la receta a eliminar: ", false).toInt()
        servicioReceta.eliminarReceta(id)
        ui.mostrar("Receta eliminada.", false)
    }

    private fun actualizarReceta() {
        //val id = generadorId //TODO: El id no deberia poderse actualizar
        val nombre = ui.leer("Nombre: ", true)
        val calorias = ui.leer("Calorias: ", true).toInt()
        val ingredientes = ui.leerLista("Ingredientes: ", true)
        val esVegana = ui.leerBool("¿Vegana?: ", true)
    }
}
