package org.recualberti.data.db

import java.sql.Connection
import javax.sql.DataSource

object DbStarter {

    fun start(ds: DataSource) {
        var conn: Connection? = null
        try {
            conn = ds.connection
            conn.autoCommit = false

            val stmt = conn.createStatement()

            // Creacción de tablas
            stmt.execute("""
            CREATE TABLE RECETAS (
                id         INT AUTO_INCREMENT PRIMARY KEY,
                nombre     VARCHAR(120)  NOT NULL,
                calorias   INT           NOT NULL CHECK (calorias > 0),
                es_vegana  BOOLEAN       NOT NULL
            );
            """.trimIndent())

            stmt.execute("""
            CREATE TABLE INGREDIENTES (
                id          INT AUTO_INCREMENT PRIMARY KEY,
                receta_id   INT          NOT NULL,
                descripcion VARCHAR(200) NOT NULL,
                CONSTRAINT FK_ING_RECETA
                    FOREIGN KEY (receta_id)
                    REFERENCES RECETAS(id)
                    ON DELETE CASCADE
            );
            """.trimIndent())

            //Creacción de índice con las consultas comunes
            stmt.execute("""
            CREATE INDEX IDX_RECETA_NOMBRE ON RECETAS (LOWER(nombre));
            CREATE INDEX IDX_ING_RECETA_ID ON INGREDIENTES (receta_id);
            """.trimIndent())

            conn.commit()
        } catch (e: Exception) {
            try {
                conn?.rollback()
            } catch (rollbackEx: Exception) {
                throw RuntimeException("Error durante rollback: ${rollbackEx.message}")
            }
            throw RuntimeException("Error en el arranque de la base de datos: ${e.message}", e)
        } finally {
            conn?.close()
        }
    }
}