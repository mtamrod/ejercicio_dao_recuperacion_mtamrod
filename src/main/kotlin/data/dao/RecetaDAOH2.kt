package org.recualberti.data.dao

import org.recualberti.data.db.IdFactory
import org.recualberti.model.*
import javax.sql.DataSource

class RecetaDAOH2 (
    private val ds: DataSource
): IRecetaDAOH2 {

    override fun crearReceta(receta: Receta) {
        ds.connection.use { conn ->
            val id = IdFactory.generarId(conn)  // Generamos el ID aquí

            conn.autoCommit = false
            conn.prepareStatement("INSERT INTO recetas (id, nombre, calorias, es_Vegana, tipo) VALUES (?, ?, ?, ?, ?)")
                .use { stmt ->
                    stmt.setInt(1, id)
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
                    stmt.setInt(1, id)
                    stmt.setBoolean(2, receta.esFrio)
                    stmt.executeUpdate()
                }

                is Principal -> {
                    val stmt = conn.prepareStatement("INSERT INTO PRINCIPALES (id, momento) VALUES (?, ?)")
                    stmt.setInt(1, id)
                    stmt.setString(2, receta.momento)
                    stmt.executeUpdate()
                }

                is Postre -> {
                    val stmt = conn.prepareStatement("INSERT INTO POSTRES (id, es_dulce) VALUES (?, ?)")
                    stmt.setInt(1, id)
                    stmt.setBoolean(2, receta.esDulce)
                    stmt.executeUpdate()
                }

                else -> {
                    throw IllegalArgumentException("El tipo de receta no existe")
                }
            }

            conn.prepareStatement("INSERT INTO INGREDIENTES (receta_id, descripcion) VALUES (?, ?)").use { stmt ->
                for (ingrediente in receta.ingredientes) {
                    stmt.setInt(1, id)
                    stmt.setString(2, ingrediente)
                    stmt.execute()
                }

                conn.commit()
                conn.autoCommit = true
            }
        }
    }

    override fun buscarReceta(id: Int): Receta? {
        ds.connection.use { conn ->
            conn.prepareStatement("SELECT * FROM recetas WHERE id = ?").use { stmt ->
                stmt.setInt(1, id)

                val consulta = stmt.executeQuery()
                if (consulta.next()) {
                    val nombre = consulta.getString("nombre")
                    val calorias = consulta.getInt("calorias")
                    val esVegana = consulta.getBoolean("es_vegana")
                    val tipo = consulta.getString("tipo").uppercase()

                    //Consulta de la tabla ingredientes (Para poder mostrarlos)
                    val ingredientesReceta = mutableListOf<String>() //Ingredientes a mostrar
                    conn.prepareStatement("SELECT descripcion FROM INGREDIENTES WHERE receta_id = ?").use { ingredienteStmt ->
                        ingredienteStmt.setInt(1, id)

                        val consultaIngredientes = ingredienteStmt.executeQuery()
                        while (consultaIngredientes.next()) {
                            ingredientesReceta.add(consultaIngredientes.getString("descripcion"))
                        }
                    }

                    // Crear la instancia concreta según el tipo
                    return when (tipo) {
                        "ENTRANTE" -> {
                            conn.prepareStatement("SELECT es_frio FROM ENTRANTES WHERE id = ?").use { tipoStmt ->
                                tipoStmt.setInt(1, id)

                                val resultadoSub = tipoStmt.executeQuery()
                                if (resultadoSub.next()) {
                                    Entrante(id, nombre, calorias, esVegana, ingredientesReceta,resultadoSub.getBoolean("es_frio"))
                                } else {
                                    println("No se encontraron ENTRANTES para el id $id")
                                    null
                                }
                            }
                        }

                        "PRINCIPAL" -> {
                            conn.prepareStatement("SELECT momento FROM PRINCIPALES WHERE id = ?").use { tipoStmt ->
                                tipoStmt.setInt(1, id)

                                val resultadoSub = tipoStmt.executeQuery()
                                if (resultadoSub.next()) {
                                    Principal(id, nombre, calorias, esVegana, ingredientesReceta, resultadoSub.getString("momento"))
                                } else {
                                    println("No se encontraron PRINCIPALES para el id $id")
                                    null
                                }
                            }
                        }

                        "POSTRE" -> {
                            conn.prepareStatement("SELECT es_dulce FROM POSTRES WHERE id = ?").use { tipoStmt ->
                                tipoStmt.setInt(1, id)

                                val resultadoSub = tipoStmt.executeQuery()
                                if (resultadoSub.next()) {
                                    Postre(id, nombre, calorias, esVegana, ingredientesReceta, resultadoSub.getBoolean("es_dulce"))
                                } else {
                                    println("No se encontraron POSTRES para el id $id")
                                    null
                                }
                            }
                        }

                        else -> null
                    }
                }
            }
        }
        return null
    }

    override fun obtenerTodas(): List<Receta> {
        val recetas = mutableListOf<Receta>()

        ds.connection.use { conn ->
            conn.prepareStatement("SELECT * FROM recetas").use { stmt ->
                val consulta = stmt.executeQuery()

                while (consulta.next()) {
                    val id = consulta.getInt("id")
                    val nombre = consulta.getString("nombre")
                    val calorias = consulta.getInt("calorias")
                    val esVegana = consulta.getBoolean("es_vegana")
                    val tipo = consulta.getString("tipo").uppercase() // para evitar errores por mayúsculas

                    //Consulta de la tabla ingredientes (Para poder mostrarlos)
                    val ingredientesReceta = mutableListOf<String>() //Ingredientes a mostrar
                    conn.prepareStatement("SELECT descripcion FROM INGREDIENTES WHERE receta_id = ?").use { ingredienteStmt ->
                        ingredienteStmt.setInt(1, id)
                        val consultaIngredientes = ingredienteStmt.executeQuery()
                        while (consultaIngredientes.next()) {
                            ingredientesReceta.add(consultaIngredientes.getString("descripcion"))
                        }
                    }

                    // Crear la instancia concreta según tipo
                    val receta: Receta? = when (tipo) {
                        "ENTRANTE" -> {
                            conn.prepareStatement("SELECT es_frio FROM ENTRANTES WHERE id = ?").use { tipoStmt ->
                                tipoStmt.setInt(1, id)
                                val resultadoSub = tipoStmt.executeQuery()
                                if (resultadoSub.next()) {
                                    Entrante(id, nombre, calorias, esVegana, ingredientesReceta, resultadoSub.getBoolean("es_frio"))
                                } else {
                                    null
                                }
                            }
                        }
                        "PRINCIPAL" -> {
                            conn.prepareStatement("SELECT momento FROM PRINCIPALES WHERE id = ?").use { tipoStmt ->
                                tipoStmt.setInt(1, id)
                                val resultadoSub = tipoStmt.executeQuery()

                                if (resultadoSub.next()) {
                                    Principal(id, nombre, calorias, esVegana, ingredientesReceta, resultadoSub.getString("momento"))
                                } else {
                                    null
                                }
                            }
                        }
                        "POSTRE" -> {
                            conn.prepareStatement("SELECT es_dulce FROM POSTRES WHERE id = ?").use { tipoStmt ->
                                tipoStmt.setInt(1, id)
                                val resultadoSub = tipoStmt.executeQuery()
                                if (resultadoSub.next()) {
                                    Postre(id, nombre, calorias, esVegana, ingredientesReceta, resultadoSub.getBoolean("es_dulce"))
                                } else {
                                    null
                                }
                            }
                        }
                        else -> null
                    }

                    if (receta != null) {
                        recetas.add(receta)
                    }
                }
            }
        }
        return recetas
    }

    override fun actualizarReceta(receta: Receta) {
        ds.connection.use { conn ->
            conn.prepareStatement("UPDATE recetas SET nombre = ?, calorias = ?, es_vegana = ?, tipo = ? WHERE id = ?").use { stmt ->
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
