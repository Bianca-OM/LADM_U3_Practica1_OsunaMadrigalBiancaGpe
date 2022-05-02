package com.example.ladm_u3_practica1_osunamadrigalbiancagpe

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException

class Asignacion (este:Context) {
    private val este = este
    private val base = "computo"
    var idAsignacion = ""
    var nomEmpleado = ""
    var areaTrabajo = ""
    var fecha = ""
    var codigoBarras = ""
    private var err = ""

    fun insertar() : Boolean{
        val basedatos = BaseDatos(este, base, null, 1)
        err = ""
        try {
            val tabla = basedatos.writableDatabase
            var datos = ContentValues()

            datos.put("NOM_EMPLEADO", nomEmpleado)
            datos.put("AREA_TRABAJO", areaTrabajo)
            datos.put("FECHA", fecha)
            datos.put("CODIGOBARRAS", codigoBarras)

            val respuesta = tabla.insert("ASIGNACION", null, datos)
            if(respuesta == -1L){
                AlertDialog.Builder(este)
                    .setTitle("Error")
                    .setMessage("Respuesta = -1")
                    .show()
                return false
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
            AlertDialog.Builder(este)
                .setMessage("Error: ${this.err}")
                .show()
            return false
        } finally {
            basedatos.close()
        }
        return true
    }

    fun mostrarTodos() : ArrayList<Asignacion> {
        val basedatos = BaseDatos(este, base, null, 1)
        err = ""
        var arreglo = ArrayList<Asignacion>()

        try{
            val tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ASIGNACION"

            var cursor = tabla.rawQuery(SQLSELECT, null)
            if(cursor.moveToFirst()){
                do {
                    val asign = Asignacion(este)
                    asign.idAsignacion = cursor.getString(0)
                    asign.nomEmpleado = cursor.getString(1)
                    asign.areaTrabajo = cursor.getString(2)
                    asign.fecha = cursor.getString(3)
                    asign.codigoBarras = cursor.getString(4)
                    arreglo.add(asign)
                }while (cursor.moveToNext())
            }
        } catch (err:SQLiteException) {
            this.err = err.message!!
            AlertDialog.Builder(este)
                .setMessage("Error: ${this.err}")
                .show()
        } finally {
            basedatos.close()
        }
        return arreglo
    }

    fun buscarAsignacionesArea(areaBuscar: String) : ArrayList<Asignacion> {
        val basedatos = BaseDatos(este, base, null, 1)
        err = ""
        var arreglo = ArrayList<Asignacion>()
        val ab = areaBuscar

        try {
            val tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ASIGNACION WHERE AREA_TRABAJO=?"

            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(ab))
            if(cursor.moveToFirst()){
                do {
                    val asign = Asignacion(este)
                    asign.idAsignacion = cursor.getString(0)
                    asign.nomEmpleado = cursor.getString(1)
                    asign.areaTrabajo = cursor.getString(2)
                    asign.fecha = cursor.getString(3)
                    asign.codigoBarras = cursor.getString(4)
                    arreglo.add(asign)
                }while (cursor.moveToNext())
            }

        } catch (err:SQLiteException) {
            this.err = err.message!!
            AlertDialog.Builder(este)
                .setMessage("Error: ${this.err}")
                .show()
        } finally {
            basedatos.close()
        }
        return arreglo
    }

    fun buscarAsignacion(codigoBuscar: String) : Asignacion {
        val basedatos = BaseDatos(este, base, null, 1)
        err = ""
        val cb = codigoBuscar
        val asign = Asignacion(este)

        try {
            val tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ASIGNACION WHERE CODIGOBARRAS=?"

            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(cb))
            if(cursor.moveToFirst()){
                asign.idAsignacion = cursor.getString(0)
                asign.nomEmpleado = cursor.getString(1)
                asign.areaTrabajo = cursor.getString(2)
                asign.fecha = cursor.getString(3)
                asign.codigoBarras = cursor.getString(4)
            }

        } catch (err:SQLiteException) {
            this.err = err.message!!
            AlertDialog.Builder(este)
                .setMessage("Error: ${this.err}")
                .show()
        } finally {
            basedatos.close()
        }
        return asign
    }

    fun actualizar() : Boolean {
        val basedatos = BaseDatos(este, base, null, 1)
        err = ""
        try {
            val tabla = basedatos.writableDatabase
            var datosActualizados = ContentValues()

            datosActualizados.put("NOM_EMPLEADO", nomEmpleado)
            datosActualizados.put("AREA_TRABAJO", areaTrabajo)
            datosActualizados.put("FECHA", fecha)
            datosActualizados.put("CODIGOBARRAS", codigoBarras)

            val respuesta = tabla.update("ASIGNACION", datosActualizados,
                "IDASIGNACION=?", arrayOf(idAsignacion))

            if(respuesta == 0){
                AlertDialog.Builder(este)
                    .setTitle("Error")
                    .setMessage("Respuesta = 0")
                    .show()
                return false
            }

        } catch (err:SQLiteException) {
            this.err = err.message!!
            AlertDialog.Builder(este)
                .setMessage("Error: ${this.err}")
                .show()
            return false
        } finally {
            basedatos.close()
        }
        return true
    }

    fun eliminar(): Boolean{
        val basedatos = BaseDatos(este, base, null, 1)
        try {
            val tabla = basedatos.writableDatabase
            val respuesta = tabla.delete("ASIGNACION","IDASIGNACION=?",arrayOf(idAsignacion))

            if(respuesta == 0){
                AlertDialog.Builder(este)
                    .setTitle("Error")
                    .setMessage("Respuesta = 0")
                    .show()
                return false
            }

        } catch (err:SQLiteException) {
            this.err = err.message!!
            AlertDialog.Builder(este)
                .setMessage("Error: ${this.err}")
                .show()
            return false
        } finally {
            basedatos.close()
        }
        return true
    }
}