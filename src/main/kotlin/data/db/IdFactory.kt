package org.recualberti.data.db

import java.sql.Connection

object IdFactory {
    fun generarId(conn: Connection): Int {

        var idNuevo = 1
        conn.prepareStatement("SELECT MAX(id) FROM recetas").use { stmt ->
            val rs = stmt.executeQuery()
            if (rs.next()) {
                val ultimoId = rs.getInt(1)
                if (ultimoId != 0) {
                    idNuevo = ultimoId + 1
                }
            }
        }
        return idNuevo
    }
}

