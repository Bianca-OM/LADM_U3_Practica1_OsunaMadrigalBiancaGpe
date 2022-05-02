package com.example.ladm_u3_practica1_osunamadrigalbiancagpe

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Switch
import androidx.fragment.app.FragmentManager
import com.example.ladm_u3_practica1_osunamadrigalbiancagpe.databinding.ActivityMain2Binding
import com.example.ladm_u3_practica1_osunamadrigalbiancagpe.ui.home.HomeFragment

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding

    var cod = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle("Actualizar Equipo")

        cod = this.intent.getStringExtra("CODIGO").toString()
        llenarDatos()

        binding.spnTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position==0){
                    tipoComputadora()
                }else if(position==1){
                    tipoTableta()
                }else if(position==2){
                    tipoImpresora()
                }else if(position==3){
                    tipoProyector()
                }
            }
        }

        binding.btnActualizar.setOnClickListener {
            actualizarEquipo()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    fun llenarDatos(){
        var equipoActualizar = AsignacionEquipoComputo(this).buscarEquipo(cod)

        binding.lblCodigo.setText(equipoActualizar.codigoBarras)

        if(equipoActualizar.tipoEquipo=="Computadora") {
            binding.spnTipo.setSelection(0)
            val aC = equipoActualizar.caracteristicas.split(",")
            binding.txtProcesador.setText(aC[0])
            binding.txtRam.setText(aC[1])
            binding.txtDisco.setText(aC[2])
        }else if (equipoActualizar.tipoEquipo=="Tableta") {
            binding.spnTipo.setSelection(1)
            if(equipoActualizar.caracteristicas=="Apple"){
                binding.spnMarca.setSelection(0)
            }else if(equipoActualizar.caracteristicas=="Samsung"){
                binding.spnMarca.setSelection(1)
            }else if(equipoActualizar.caracteristicas=="Xiaomi"){
                binding.spnMarca.setSelection(2)
            }else if(equipoActualizar.caracteristicas=="Huawei"){
                binding.spnMarca.setSelection(3)
            }
        }else if (equipoActualizar.tipoEquipo=="Impresora") {
            binding.spnTipo.setSelection(2)
            if(equipoActualizar.caracteristicas=="Toner"){
                binding.spnImprime.setSelection(0)
            }else if(equipoActualizar.caracteristicas=="Tinta"){
                binding.spnImprime.setSelection(1)
            }
        }else if (equipoActualizar.tipoEquipo=="Proyector") {
            binding.spnTipo.setSelection(3)
            if(equipoActualizar.caracteristicas=="HDMI"){
                binding.spnEntrada.setSelection(0)
            }else if(equipoActualizar.caracteristicas=="DVI"){
                binding.spnEntrada.setSelection(1)
            }else if(equipoActualizar.caracteristicas=="VGA"){
                binding.spnEntrada.setSelection(2)
            }
        }

        binding.txtFechaC.setText(equipoActualizar.fechaCompra)
    }

    fun actualizarEquipo(){
        var equipoActualizar = AsignacionEquipoComputo(this).buscarEquipo(cod)

        equipoActualizar.tipoEquipo = binding.spnTipo.selectedItem.toString()
        if(binding.spnTipo.selectedItemPosition==0){
            equipoActualizar.caracteristicas = "${binding.txtProcesador.text}," +
                    "${binding.txtRam.text},${binding.txtDisco.text}"
        }else if(binding.spnTipo.selectedItemPosition==1){
            equipoActualizar.caracteristicas = "${binding.spnMarca.selectedItem}"
        }else if(binding.spnTipo.selectedItemPosition==2){
            equipoActualizar.caracteristicas = "${binding.spnImprime.selectedItem}"
        }else if(binding.spnTipo.selectedItemPosition==3){
            equipoActualizar.caracteristicas = "${binding.spnEntrada.selectedItem}"
        }
        equipoActualizar.fechaCompra = binding.txtFechaC.text.toString()

        if(equipoActualizar.actualizar()){
            AlertDialog.Builder(this)
                .setMessage("Actualizado")
                .show()
            finish()
        }
    }

    fun tipoComputadora(){
        binding.txtProcesador.visibility = View.VISIBLE
        binding.txtRam.visibility = View.VISIBLE
        binding.txtDisco.visibility = View.VISIBLE

        binding.lblMarca.visibility = View.GONE
        binding.spnMarca.visibility = View.GONE
        binding.lblImprime.visibility = View.GONE
        binding.spnImprime.visibility = View.GONE
        binding.lblEntrada.visibility = View.GONE
        binding.spnEntrada.visibility = View.GONE
    }

    fun tipoTableta(){
        binding.lblMarca.visibility = View.VISIBLE
        binding.spnMarca.visibility = View.VISIBLE

        binding.txtProcesador.visibility = View.GONE
        binding.txtRam.visibility = View.GONE
        binding.txtDisco.visibility = View.GONE
        binding.lblImprime.visibility = View.GONE
        binding.spnImprime.visibility = View.GONE
        binding.lblEntrada.visibility = View.GONE
        binding.spnEntrada.visibility = View.GONE
    }

    fun tipoImpresora(){
        binding.lblImprime.visibility = View.VISIBLE
        binding.spnImprime.visibility = View.VISIBLE

        binding.txtProcesador.visibility = View.GONE
        binding.txtRam.visibility = View.GONE
        binding.txtDisco.visibility = View.GONE
        binding.lblMarca.visibility = View.GONE
        binding.spnMarca.visibility = View.GONE
        binding.lblEntrada.visibility = View.GONE
        binding.spnEntrada.visibility = View.GONE
    }

    fun tipoProyector(){
        binding.lblEntrada.visibility = View.VISIBLE
        binding.spnEntrada.visibility = View.VISIBLE

        binding.txtProcesador.visibility = View.GONE
        binding.txtRam.visibility = View.GONE
        binding.txtDisco.visibility = View.GONE
        binding.lblMarca.visibility = View.GONE
        binding.spnMarca.visibility = View.GONE
        binding.lblImprime.visibility = View.GONE
        binding.spnImprime.visibility = View.GONE
    }
}