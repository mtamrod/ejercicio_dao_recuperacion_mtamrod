package org.recualberti.data.db

object IdFactory {

    fun generarId(idAnterior: Int): Int {

        var idIncrementado: Int = 0

        idIncrementado += idAnterior + 1
        return idIncrementado
    }
}
