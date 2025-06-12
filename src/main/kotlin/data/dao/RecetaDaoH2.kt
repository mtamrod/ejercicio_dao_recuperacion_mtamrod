package org.recualberti.data.dao

import org.recualberti.model.Receta
import javax.sql.DataSource

class RecetaDaoH2 (
    private val ds: DataSource
): IRecetaDaoH2 {
    override fun crearReceta() {
        ds.connection.use { conn ->
            conn.prepareStatement("INSERT INTO recetas (id, nombre, calorias, ingredientes, esVegana) VALUES (?, ?, ?, ?, ?)").use { stmt ->
                stmt.execute()
            }
        }
    }

    override fun leer(): Receta {

        ds.connection.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM recetas WHERE id = ?").use { rs ->
                    val receta =
                        Receta(
                            id = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            calorias = rs.getInt("calorias"),
                            ingredientes = listOf(rs.getString("ingredientes")),
                            esVegana = rs.getBoolean("esVegana")
                        )

                    return receta
                }
            }
        }
    }

    override fun leerTodos(): MutableList<Receta> {
        val recetasList = mutableListOf<Receta>()

        ds.connection.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM recetas").use { rs ->
                    while (rs.next()) {
                        val receta =
                            Receta(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                calorias = rs.getInt("calorias"),
                                ingredientes = listOf(rs.getString("ingredientes")),
                                esVegana = rs.getBoolean("esVegana")
                            )
                        recetasList.add(receta)
                    }
                }
            }
        }

        return recetasList
    }

    override fun actualizar(id: Int, nombre: String, calorias: Int, ingredientes: List<String>, esVegana: Boolean) {
        ds.connection.use { conn ->
            conn.prepareStatement("UPDATE recetas SET nombre = ?, calorias = ?, ingredientes = ?, esVegana = ? WHERE id = ?").use { stmt ->
                stmt.setString(1, nombre)
                stmt.setInt(2, calorias)
                stmt.setString(3, ingredientes.toString())
                stmt.setBoolean(4, esVegana)
                stmt.executeUpdate()
            }
        }
    }

    override fun borrar(id: Int) {
        ds.connection.use { conn ->
            conn.prepareStatement("DELETE FROM recetas WHERE id = ?").use { stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate()
            }
        }
    }
}