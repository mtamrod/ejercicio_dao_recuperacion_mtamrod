package org.recualberti.data.dao

import org.recualberti.model.*
import javax.sql.DataSource

class RecetaDAOH2 (
    private val ds: DataSource
): IRecetaDAOH2 {
    override fun crearReceta(receta: Receta) {
        ds.connection.use { conn ->
            conn.autoCommit = false
            conn.prepareStatement("INSERT INTO recetas (id, nombre, calorias, esVegana, tipo) VALUES (?, ?, ?, ?, ?)")
                .use { stmt ->
                    stmt.setInt(1, receta.id)
                    stmt.setString(2, receta.nombre)
                    stmt.setInt(3, receta.calorias)
                    stmt.setBoolean(4, receta.esVegana)
                    stmt.setString(5, receta.tipo)
                    stmt.execute()
                }

            // Inclusión en la tabla (hija de receta)
            when (receta) {
                is Entrante -> {
                    val stmt = conn.prepareStatement("INSERT INTO ENTRANTES (id, es_frio) VALUES (?, ?)")
                    stmt.setInt(1, receta.id)
                    stmt.setBoolean(2, receta.esFrio)
                    stmt.executeUpdate()
                }

                is Principal -> {
                    val stmt = conn.prepareStatement("INSERT INTO PRINCIPALES (id, momento) VALUES (?, ?)")
                    stmt.setInt(1, receta.id)
                    stmt.setString(2, receta.momento)
                    stmt.executeUpdate()
                }

                is Postre -> {
                    val stmt = conn.prepareStatement("INSERT INTO POSTRES (id, es_dulce) VALUES (?, ?)")
                    stmt.setInt(1, receta.id)
                    stmt.setBoolean(2, receta.esDulce)
                    stmt.executeUpdate()
                }

                else -> {
                    throw IllegalArgumentException("El tipo de receta no existe")
                }
            }

            conn.prepareStatement("INSERT INTO INGREDIENTES (receta_id, descripcion) VALUES (?, ?)").use { stmt ->
                for (ingrediente in receta.ingredientes) {
                    stmt.setInt(1, receta.id)
                    stmt.setString(2, ingrediente)
                    stmt.execute()
                }

                conn.commit()
                conn.autoCommit = true
            }
        }
    }

    override fun buscarReceta(nombre: String): Receta {
        TODO("Not yet implemented")
    }

    override fun obtenerTodas(): MutableList<Receta> {
        TODO("Not yet implemented")
    }

    override fun actualizarReceta(receta: Receta) {
        ds.connection.use { conn ->
            conn.prepareStatement(
                "UPDATE recetas SET nombre = ?, calorias = ?, es_vegana = ?, tipo = ? WHERE id = ?").use { stmt ->
                stmt.setString(1, receta.nombre)
                stmt.setInt(2, receta.calorias)
                stmt.setBoolean(3, receta.esVegana)
                stmt.setString(4, receta.tipo)
                stmt.setInt(5, receta.id)
                stmt.executeUpdate()
            }
        }
    }

    override fun borrarReceta(id: Int) {
        ds.connection.use { conn ->
            conn.prepareStatement("DELETE FROM recetas WHERE id = ?").use { stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate()
            }
        }
    }
}
