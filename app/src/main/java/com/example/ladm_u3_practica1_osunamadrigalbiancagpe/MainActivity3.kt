package com.example.ladm_u3_practica1_osunamadrigalbiancagpe

import android.R
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.ladm_u3_practica1_osunamadrigalbiancagpe.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding

    var cod = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle("Actualizar Asignación")

        cod = this.intent.getStringExtra("CODIGO").toString()
        llenarDatos()
        llenarDisponibles()

        binding.spnTipoA.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                llenarDisponibles()
            }
        }

        binding.btnActualizar.setOnClickListener {
            if(binding.spnCodigo.selectedItem.toString()=="") {
                AlertDialog.Builder(this)
                    .setMessage("No hay equipos libres de ese tipo\n" +
                            "Registre uno nuevo o elimine una asignación")
                    .show()
                return@setOnClickListener
            }
            actualizarAsignacion()
        }

        binding.btnRegresar.setOnClickListener {
            finish()
        }
    }

    fun llenarDatos(){
        var asignacionActualizar = Asignacion(this).buscarAsignacion(cod)
        var equipo = AsignacionEquipoComputo(this).buscarEquipo(cod)

        binding.lblId.setText(asignacionActualizar.idAsignacion)
        binding.txtNombre.setText(asignacionActualizar.nomEmpleado)

        if(asignacionActualizar.areaTrabajo=="Ventas"){
            binding.spnArea.setSelection(0)
        }else if(asignacionActualizar.areaTrabajo=="Finanzas"){
            binding.spnArea.setSelection(1)
        }else if(asignacionActualizar.areaTrabajo=="Producción"){
            binding.spnArea.setSelection(2)
        }else if(asignacionActualizar.areaTrabajo=="Recursos Humanos"){
            binding.spnArea.setSelection(3)
        }

        binding.txtFechaA.setText(asignacionActualizar.fecha)

        binding.lblEquipoActual.setText("Equipo actual:\nTipo ${equipo.tipoEquipo}, Código ${cod}")
    }

    fun actualizarAsignacion(){
        var asignacionActualizar = Asignacion(this).buscarAsignacion(cod)

        asignacionActualizar.nomEmpleado = binding.txtNombre.text.toString()
        asignacionActualizar.areaTrabajo = binding.spnArea.selectedItem.toString()
        asignacionActualizar.fecha = binding.txtFechaA.text.toString()
        asignacionActualizar.codigoBarras = binding.spnCodigo.selectedItem.toString()

        if(asignacionActualizar.actualizar()){
            AlertDialog.Builder(this)
                .setMessage("Actualizado")
                .show()
            finish()
        }
    }

    fun llenarDisponibles(){
        val equiposDisp = ArrayList<String>()
        val equiposTipo = AsignacionEquipoComputo(this)
            .buscarEquiposTipo(binding.spnTipoA.selectedItem.toString())
        if(equiposTipo.size>0) {
            (0..equiposTipo.size - 1).forEach {
                var equipo = equiposTipo[it]
                var asignacion = Asignacion(this).buscarAsignacion(equipo.codigoBarras)
                if (asignacion.codigoBarras == "" || asignacion.codigoBarras==cod) {
                    equiposDisp.add(equipo.codigoBarras)
                }
            }
        }

        if(equiposDisp.size==0){
            equiposDisp.add("")
        }

        val arrayAdap = ArrayAdapter(this, R.layout.simple_spinner_item, equiposDisp)
        arrayAdap.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spnCodigo.setAdapter(arrayAdap)
    }
}