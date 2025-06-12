package org.recualberti.app

import org.recualberti.model.Receta
import org.recualberti.service.IRecetaService
import org.recualberti.ui.IEntradaSalida

class MenuConsola(
    private val servicioReceta: IRecetaService,
    private val ui: IEntradaSalida
) {
    private var funcionando: Boolean = true

    public fun iniciar() {
        fun salir() {
            ui.mostrar("Saliendo...", false)
            funcionando = false
        }

        fun crearReceta() {
            val id = 1 //TODO: Id generado automaticamente
            val nombre = ui.leer("Nombre: ", true)
            val calorias = ui.leer("Calorias: ", true).toInt()
            val ingredientes = ui.leerLista("Ingredientes: ", true)
            val esVegana = ui.leerBool("¿Es Vegana?: ", true)
                servicioReceta.crearReceta(Receta(id, nombre, calorias, ingredientes, esVegana))
                ui.mostrar("Usuario insertado.", true)
        }

        fun mostrarReceta() {
                val recetaUnica = servicioReceta.mostrarReceta()
                if (recetaUnica.isEmpty()) ui.mostrar("No se encontró la receta.", true)
                else recetaUnica.forEach { ui.mostrar("Id: ${it.id} - NOmbre: ${it.nombre} - Calorias: ${it.calorias} - Ingredientes: ${it.ingredientes} - Vegana: ${it.esVegana}", true) }
        }

        fun mostrarTodasReceta() {
            val recetas = servicioReceta.mostrarTodasReceta()

            if (recetas.isEmpty()) ui.mostrar("No se encontraron recetas.", true)
            else recetas.forEach { ui.mostrar("Id: ${it.id} - NOmbre: ${it.nombre} - Calorias: ${it.calorias} - Ingredientes: ${it.ingredientes} - Vegana: ${it.esVegana}", true) }
        }

        fun eliminarReceta() {
            val id = ui.leer("Id de la receta a eliminar: ", false).toInt()
                servicioReceta.eliminarReceta(id)
                ui.mostrar("Receta eliminada.", false)
        }

        fun actualizarReceta() {
            TODO("No")
        }

        while (funcionando) {
            ui.limpiarPantalla()
            ui.mostrar(
                """
                    === MENÚ DE RECETAS===
                    1. Crear
                    2. Buscar
                    3. Buscar Todas
                    4. Actualizar
                    5. Borrar
                    6. Salir
                """.trimIndent(),
            false)

            when (ui.leer("Opción: ", true)) {
                "1" -> crearReceta()
                "2" -> mostrarReceta()
                "3" -> mostrarTodasReceta()
                "4" -> actualizarReceta()
                "5" -> eliminarReceta()
                "6" -> salir()

                else -> ui.error("Opción no válida.", true)
            }

            //ui.pausa() //TODO
        }
    }
}