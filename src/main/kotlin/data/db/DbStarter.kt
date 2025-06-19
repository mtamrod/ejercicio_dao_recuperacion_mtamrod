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
            CREATE TABLE IF NOT EXISTS RECETAS (
                id            INT PRIMARY KEY,
                nombre        VARCHAR(120)  NOT NULL,
                calorias      INT           NOT NULL CHECK (calorias > 0),
                es_vegana     BOOLEAN       NOT NULL,
                tipo          VARCHAR(12)   NOT NULL
            );
            """.trimIndent())

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS ENTRANTES (
                id       INT PRIMARY KEY,
                es_frio  BOOLEAN NOT NULL,
                CONSTRAINT FK_ENTRANTE_RECETA FOREIGN KEY (id) REFERENCES RECETAS(id) ON DELETE CASCADE
            );
            """.trimIndent())

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS PRINCIPALES (
                id        INT PRIMARY KEY,
                momento   VARCHAR(6) NOT NULL,
                CONSTRAINT FK_PRINCIPAL_RECETA FOREIGN KEY (id) REFERENCES RECETAS(id) ON DELETE CASCADE
            );
            """.trimIndent())

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS POSTRES (
                id        INT PRIMARY KEY,
                es_dulce  BOOLEAN NOT NULL,
                CONSTRAINT FK_POSTRE_RECETA FOREIGN KEY (id) REFERENCES RECETAS(id) ON DELETE CASCADE
            );
            """.trimIndent())

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS INGREDIENTES (
                id         INT AUTO_INCREMENT PRIMARY KEY,
                receta_id  INT          NOT NULL,
                descripcion VARCHAR(200) NOT NULL,
                CONSTRAINT FK_ING_RECETA FOREIGN KEY (receta_id) REFERENCES RECETAS(id) ON DELETE CASCADE
            );
            """.trimIndent())

            //Los índices han sido modificados debido a que la versión de H2 del sistema no era compatible con el código proporcionado por el profesor
            stmt.execute("""
            CREATE INDEX IF NOT EXISTS IDX_RECETA_NOMBRE ON RECETAS (nombre);
            CREATE INDEX IF NOT EXISTS IDX_RECETA_TIPO ON RECETAS (tipo);
            CREATE INDEX IF NOT EXISTS IDX_ING_RECETA_ID ON INGREDIENTES (receta_id);  
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