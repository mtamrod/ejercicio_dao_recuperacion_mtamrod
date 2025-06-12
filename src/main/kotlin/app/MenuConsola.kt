package org.recualberti.app

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
                "1" -> 
                "2" ->
                "3" ->
                "4" ->
                "5" ->
                "6" ->
            }
        }

        private fun salir() {
            ui.mostrar("Saliendo...", false)
            funcionando = false
        }
    }
}