package org.recualberti.app

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
                    2. Buscar (Id)
                    3. Listar Todas
                    4. Actualizar 
                    5. Borrar
                    6. Salir
                    === === === === === ===
                """.trimIndent(),
                true
            )

            when (ui.leer("Opción: ", false)) {
                "1" -> {
                    ui.limpiarPantalla()
                    crearReceta()
                    ui.pausa()
                }
                "2" -> {
                    ui.limpiarPantalla()
                    buscarReceta()
                    ui.pausa()
                }
                "3" -> {
                    ui.limpiarPantalla()
                    listarRecetas()
                    ui.pausa()
                }
                "4" -> {
                    ui.limpiarPantalla()
                    actualizarReceta()
                    ui.pausa()
                }
                "5" -> {
                    ui.limpiarPantalla()
                    eliminarReceta()
                    ui.pausa()
                }
                "6" -> {
                    ui.limpiarPantalla()
                    salir()
                }

                else -> ui.error("Opción no válida.", true)
            }
        }
    }

    private fun salir() {
        ui.mostrar("Saliendo...", true)
        funcionando = false
    } //Errores ✅

    private fun crearReceta() {
        try {
            val nombre = ui.leer("Nombre: ")
            val caloriasInput = ui.leer("Calorías: ")
            val calorias = caloriasInput.toIntOrNull()
            if (calorias == null) {
                ui.error("Las calorías deben ser un número entero.", true)
                return
            }

            val ingredientes = ui.leerLista("Ingredientes: ")
            val esVegana = ui.leerBool("¿Es Vegana? (Si/No): ")

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
            ui.pausa("Pulsa ENTER para continuar...")
            ui.limpiarPantalla()
        } catch (e: Exception) {
            ui.error("Error al crear la receta: $e", true)
        }
    } //Errores ✅

    private fun buscarReceta() {
        val input = ui.leer("Introduce el id de la receta a buscar: ", true)
        val id = input.toIntOrNull()

        if(id == null) {
            ui.error("El id debe de ser un número", true)
            return
        }

        if (!comprobarId(id)) {
            return
        }

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
                is Entrante -> if (receta.esFrio) "Frío: Si" else "Frío: No"
                is Principal -> "Momento: ${receta.momento}"
                is Postre -> if (receta.esDulce) "Dulce: Si" else "Dulce: No"
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
    } //Errores ✅

    private fun listarRecetas() {
        val recetas = servicioReceta.obtenerTodas()

        if (recetas.isEmpty()) {
            ui.mostrar("No existen recetas en la base de datos.", true)
            return
        }

        // Encabezado con columnas
        val encabezado = String.format(
            "%-4s | %-20s | %-9s | %-8s | %-40s | %-15s",
            "ID", "Nombre", "Calorías", "Vegana", "Ingredientes", "Extra"
        )
        ui.mostrar(encabezado, true)
        ui.mostrar("-".repeat(encabezado.length), true)

        // Mostrar cada receta
        for (receta in recetas) {
            val ingredientesStr = receta.ingredientes.joinToString(", ")
            val extra = when (receta) {
                is Entrante -> if (receta.esFrio)"Frío: Si" else "Frío: No"
                is Principal -> "Momento: ${receta.momento}"
                is Postre -> if (receta.esDulce)"Dulce: Si" else "Dulce: No"
                else -> "N/A"
            }

            val fila = String.format(
                "%-4d | %-20s | %-9d | %-8s | %-40s | %-15s",
                receta.id,
                receta.nombre,
                receta.calorias,
                if (receta.esVegana) "Sí" else "No",
                ingredientesStr.take(40), // Truncar para que no desborde
                extra
            )
            ui.mostrar(fila, true)
        }
    } //Errores ✅

    private fun eliminarReceta() {
        try {
            val id = ui.leer("Id de la receta a eliminar: ", false).toInt()
            if (!comprobarId(id)) {
                return
            }
            servicioReceta.eliminarReceta(id)
            ui.mostrar("Receta eliminada.")
        } catch (e: NumberFormatException) {
            ui.error("Id no válido, deber ser un número.", true)
            return
        } catch (e: Exception) {
            ui.error("Error inesperado al eliminar la receta: $e", true)
            return
        }
    } //Errores ✅

    private fun actualizarReceta() {
        val recetaExistente: Receta
        val id: Int
        try {
            id = ui.leer("Id de la receta a actualizar: ", false).toInt()
            if (!comprobarId(id)) {
                return
            }

            recetaExistente = servicioReceta.buscarReceta(id)!!
        } catch (e: NumberFormatException) {
            ui.error("Id no válido, deber ser un número.", true)
            return
        } catch (e: Exception) {
            ui.error("Error inesperado al actualizar la receta: $e", true)
            return
        }


        ui.mostrar("Introduce los nuevos datos (ENTER para mantener los anteriores): ", true)

        // Nombre
        val nombreInput = ui.leer("Nombre [${recetaExistente.nombre}]: ")
        val nombre = nombreInput.ifBlank { recetaExistente.nombre }

        // Calorías
        val caloriasInput = ui.leer("Calorías [${recetaExistente.calorias}]: ")
        val calorias: Int = if (caloriasInput.isBlank()) {
            recetaExistente.calorias
        } else {
            try {
                caloriasInput.toInt()
            } catch (e: NumberFormatException) {
                ui.error("Las calorías deben tener formato numérico entero.", true)
                return
            }
        }

        // Ingredientes
        val ingredientesInput = ui.leerLista("Ingredientes (coma separados) [${recetaExistente.ingredientes.joinToString(", ")}]: ")
        val ingredientes = ingredientesInput.ifEmpty { recetaExistente.ingredientes }

        // Vegana
        val veganaInput = ui.leer("¿Es vegana? (Si/No) [${if (recetaExistente.esVegana) "Sí" else "No"}]: ")
        val esVegana = if (veganaInput.isBlank()) {
            recetaExistente.esVegana
        } else {
            veganaInput.lowercase() == "sí"
        }

        val recetaActualizada: Receta = when (recetaExistente) {
            is Entrante -> {
                val esFrio = ui.leer("¿Frío? (Si/No) [${if (recetaExistente.esFrio) "sí" else "no"}]: ").ifBlank { if (recetaExistente.esFrio) "sí" else "no" }.lowercase() == "sí"
                Entrante(id, nombre, calorias, esVegana, ingredientes, esFrio)
            }

            is Principal -> {
                val momento = ui.leer("Momento [${recetaExistente.momento}]: ").ifBlank { recetaExistente.momento }
                Principal(id, nombre, calorias, esVegana, ingredientes, momento)
            }

            is Postre -> {
                val esDulce = ui.leer("¿Dulce? (Si/No) [${if (recetaExistente.esDulce) "sí" else "no"}]: ").ifBlank { if (recetaExistente.esDulce) "sí" else "no" }.lowercase() == "sí"
                Postre(id, nombre, calorias, esVegana, ingredientes, esDulce)
            }

            else -> {
                ui.error("Tipo de receta no existente", true)
                return
            }
        }
        servicioReceta.actualizarReceta(recetaActualizada)
        ui.mostrar("Receta actualizada.", true)
    } //Errores ✅

    private fun comprobarId(id: Int): Boolean {
        val recetaExistente = servicioReceta.buscarReceta(id)

        if (recetaExistente == null) {
            ui.error("No existe ninguna receta con el ID: $id", true)
            return false
        }
        return true
    } //Errores ✅
}
