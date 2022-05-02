package com.example.ladm_u3_practica1_osunamadrigalbiancagpe

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.fragment.app.Fragment

class AsignacionEquipoComputo (este:Context) {
    private val este = este
    private val base = "computo"
    var codigoBarras = ""
    var tipoEquipo = ""
    var caracteristicas = ""
    var fechaCompra = ""
    private var err = ""

    fun insertar() : Boolean{
        val basedatos = BaseDatos(este, base, null, 1)
        err = ""
        try {
            val tabla = basedatos.writableDatabase
            var datos = ContentValues()

            datos.put("CODIGOBARRAS", codigoBarras)
            datos.put("TIPOEQUIPO", tipoEquipo)
            datos.put("CARACTERISTICAS", caracteristicas)
            datos.put("FECHACOMPRA", fechaCompra)

            val respuesta = tabla.insert("ASIGNACIONEQUIPOCOMPUTO", null, datos)
            if(respuesta == -1L){
                AlertDialog.Builder(este)
                    .setTitle("Error")
                    .setMessage("Respuesta = -1\n" +
                            "Verifique que el código no se repita")
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

    fun mostrarTodos() : ArrayList<AsignacionEquipoComputo> {
        val basedatos = BaseDatos(este, base, null, 1)
        err = ""
        var arreglo = ArrayList<AsignacionEquipoComputo>()

        try{
            val tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ASIGNACIONEQUIPOCOMPUTO"

            var cursor = tabla.rawQuery(SQLSELECT, null)
            if(cursor.moveToFirst()){
                do {
                    val equipo = AsignacionEquipoComputo(este)
                    equipo.codigoBarras = cursor.getString(0)
                    equipo.tipoEquipo = cursor.getString(1)
                    equipo.caracteristicas = cursor.getString(2)
                    equipo.fechaCompra = cursor.getString(3)
                    arreglo.add(equipo)
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

    fun buscarEquiposTipo(tipoBuscar: String) : ArrayList<AsignacionEquipoComputo> {
        val basedatos = BaseDatos(este, base, null, 1)
        err = ""
        var arreglo = ArrayList<AsignacionEquipoComputo>()
        val tb = tipoBuscar

        try {
            val tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ASIGNACIONEQUIPOCOMPUTO WHERE TIPOEQUIPO=?"

            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(tb))
            if(cursor.moveToFirst()){
                do {
                    val equipo = AsignacionEquipoComputo(este)
                    equipo.codigoBarras = cursor.getString(0)
                    equipo.tipoEquipo = cursor.getString(1)
                    equipo.caracteristicas = cursor.getString(2)
                    equipo.fechaCompra = cursor.getString(3)
                    arreglo.add(equipo)
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

    fun buscarEquipo(codigoBuscar: String) : AsignacionEquipoComputo {
        val basedatos = BaseDatos(este, base, null, 1)
        err = ""
        val cb = codigoBuscar
        val equipo = AsignacionEquipoComputo(este)

        try {
            val tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ASIGNACIONEQUIPOCOMPUTO WHERE CODIGOBARRAS=?"

            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(cb))
            if(cursor.moveToFirst()){
                equipo.codigoBarras = cursor.getString(0)
                equipo.tipoEquipo = cursor.getString(1)
                equipo.caracteristicas = cursor.getString(2)
                equipo.fechaCompra = cursor.getString(3)
            }

        } catch (err:SQLiteException) {
            this.err = err.message!!
            AlertDialog.Builder(este)
                .setMessage("Error: ${this.err}")
                .show()
        } finally {
            basedatos.close()
        }
        return equipo
    }

    fun actualizar() : Boolean {
        val basedatos = BaseDatos(este, base, null, 1)
        err = ""
        try {
            val tabla = basedatos.writableDatabase
            var datosActualizados = ContentValues()

            datosActualizados.put("TIPOEQUIPO", tipoEquipo)
            datosActualizados.put("CARACTERISTICAS", caracteristicas)
            datosActualizados.put("FECHACOMPRA", fechaCompra)

            val respuesta = tabla.update("ASIGNACIONEQUIPOCOMPUTO", datosActualizados,
                "CODIGOBARRAS=?", arrayOf(codigoBarras))

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
            val respuesta = tabla.delete("ASIGNACIONEQUIPOCOMPUTO","CODIGOBARRAS=?",arrayOf(codigoBarras))

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