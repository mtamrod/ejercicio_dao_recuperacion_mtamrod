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

            when (ui.leer("Opción: ", false)) {
                "1" -> crearReceta()
                "2" -> buscarReceta(ui.leer("Introduce el id de la receta: ").toInt())
                "3" -> listarRecetas()
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

    private fun buscarReceta(id: Int) {
        val receta = servicioReceta.buscarReceta(id)

        if (receta != null) {
            // Encabezado
            val encabezado = String.format(
                "%-4s | %-15s | %-9s | %-11s | %-30s | %-15s",
                "ID", "Nombre", "Calorías", "Vegana", "Ingredientes", "Extra"
            )
            ui.mostrar(encabezado, true)
            ui.mostrar("-".repeat(encabezado.length), true)

            // Ingredientes como string
            val ingredientesStr = receta.ingredientes.joinToString(", ")

            // Campo adicional según el tipo de receta
            val extra = when (receta) {
                is Entrante -> "Frío: ${receta.esFrio}"
                is Principal -> "Momento: ${receta.momento}"
                is Postre -> {
                    if (receta.esDulce) {
                        "Dulce: Si"
                    } else {
                        "Dulce: No"
                    }
                }
                else -> "N/A"
            }

            // Fila de datos
            val fila = String.format(
                "%-4d | %-15s | %-9d | %-11s | %-30s | %-15s",
                receta.id,
                receta.nombre,
                receta.calorias,
                if (receta.esVegana) "Sí" else "No",
                ingredientesStr.take(30), // Truncamos si es muy largo
                extra
            )

            ui.mostrar(fila, true)
        } else {
            ui.error("No se encontró ninguna receta con el ID: $id", true)
        }
    }

    private fun listarRecetas() {
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
