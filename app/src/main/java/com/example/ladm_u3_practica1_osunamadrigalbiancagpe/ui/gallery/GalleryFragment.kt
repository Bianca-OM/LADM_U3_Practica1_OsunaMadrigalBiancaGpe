package com.example.ladm_u3_practica1_osunamadrigalbiancagpe.ui.gallery

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_practica1_osunamadrigalbiancagpe.Asignacion
import com.example.ladm_u3_practica1_osunamadrigalbiancagpe.AsignacionEquipoComputo
import com.example.ladm_u3_practica1_osunamadrigalbiancagpe.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnRegistrar.setOnClickListener {
            if(binding.spnRegistro.selectedItemPosition==0){
                if(binding.txtCodigo.text.toString()==""){
                    AlertDialog.Builder(requireContext())
                        .setMessage("Código de barras no puede ser nulo")
                        .show()
                    return@setOnClickListener
                }
                registrarEquipo()
            }else{
                if(binding.spnCodigo.selectedItem.toString()=="") {
                    AlertDialog.Builder(requireContext())
                        .setMessage("No hay equipos libres de ese tipo\n" +
                                "Registre uno nuevo o elimine una asignación")
                        .show()
                    return@setOnClickListener
                }
                registrarAsignacion()
            }
        }

        binding.spnRegistro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position==0){
                    vistaEquipo()
                }else if(position==1){
                    vistaAsignacion()
                }
            }
        }

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

        binding.spnTipoA.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                llenarDisponibles()
            }
        }

        return root
    }

    fun registrarEquipo(){
        val equipo = AsignacionEquipoComputo(requireContext())

        equipo.codigoBarras = binding.txtCodigo.text.toString()
        equipo.tipoEquipo = binding.spnTipo.selectedItem.toString()
        if(binding.spnTipo.selectedItemPosition==0){
            equipo.caracteristicas = "${binding.txtProcesador.text}," +
                    "${binding.txtRam.text},${binding.txtDisco.text}"
        }else if(binding.spnTipo.selectedItemPosition==1){
            equipo.caracteristicas = "${binding.spnMarca.selectedItem}"
        }else if(binding.spnTipo.selectedItemPosition==2){
            equipo.caracteristicas = "${binding.spnImprime.selectedItem}"
        }else if(binding.spnTipo.selectedItemPosition==3){
            equipo.caracteristicas = "${binding.spnEntrada.selectedItem}"
        }
        equipo.fechaCompra = binding.txtFechaC.text.toString()

        if(equipo.insertar()){
            AlertDialog.Builder(requireContext())
                .setMessage("Registrado")
                .show()
            vistaEquipo()
        }
    }

    fun registrarAsignacion(){
        var asignacion = Asignacion(requireContext())

        asignacion.nomEmpleado = binding.txtNombre.text.toString()
        asignacion.areaTrabajo = binding.spnArea.selectedItem.toString()
        asignacion.fecha = binding.txtFechaA.text.toString()
        asignacion.codigoBarras = binding.spnCodigo.selectedItem.toString()

        if(asignacion.insertar()){
            AlertDialog.Builder(requireContext())
                .setMessage("Registrado")
                .show()
            vistaAsignacion()
        }
    }

    fun llenarDisponibles(){
        val equiposDisp = ArrayList<String>()
        val equiposTipo = AsignacionEquipoComputo(requireContext())
            .buscarEquiposTipo(binding.spnTipoA.selectedItem.toString())
        if(equiposTipo.size>0) {
            (0..equiposTipo.size - 1).forEach {
                var equipo = equiposTipo[it]
                var asignacion = Asignacion(requireContext()).buscarAsignacion(equipo.codigoBarras)
                if (asignacion.codigoBarras == "") {
                    equiposDisp.add(equipo.codigoBarras)
                }
            }
        }

        if(equiposDisp.size==0){
            equiposDisp.add("")
        }

        val arrayAdap = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, equiposDisp)
        arrayAdap.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spnCodigo.setAdapter(arrayAdap)
    }

    fun vistaEquipo(){
        binding.txtCodigo.visibility = View.VISIBLE
        binding.txtCodigo.setText("")
        binding.lblTipo.visibility = View.VISIBLE
        binding.spnTipo.visibility = View.VISIBLE
        binding.spnTipo.setSelection(0)
        binding.txtProcesador.visibility = View.VISIBLE
        binding.txtProcesador.setText("")
        binding.txtRam.visibility = View.VISIBLE
        binding.txtRam.setText("")
        binding.txtDisco.visibility = View.VISIBLE
        binding.txtDisco.setText("")
        binding.lblMarca.visibility = View.GONE
        binding.spnMarca.visibility = View.GONE
        binding.lblImprime.visibility = View.GONE
        binding.spnImprime.visibility = View.GONE
        binding.lblEntrada.visibility = View.GONE
        binding.spnEntrada.visibility = View.GONE
        binding.txtFechaC.visibility = View.VISIBLE
        binding.txtFechaC.setText("")

        binding.txtNombre.visibility = View.GONE
        binding.lblArea.visibility = View.GONE
        binding.spnArea.visibility = View.GONE
        binding.txtFechaA.visibility = View.GONE
        binding.lblTipoA.visibility = View.GONE
        binding.spnTipoA.visibility = View.GONE
        binding.lblDisponible.visibility = View.GONE
        binding.spnCodigo.visibility = View.GONE
    }

    fun vistaAsignacion(){
        binding.txtNombre.visibility = View.VISIBLE
        binding.txtNombre.setText("")
        binding.lblArea.visibility = View.VISIBLE
        binding.spnArea.visibility = View.VISIBLE
        binding.spnArea.setSelection(0)
        binding.txtFechaA.visibility = View.VISIBLE
        binding.txtFechaA.setText("")
        binding.lblTipoA.visibility = View.VISIBLE
        binding.spnTipoA.visibility = View.VISIBLE
        binding.spnTipoA.setSelection(0)
        binding.lblDisponible.visibility = View.VISIBLE
        binding.spnCodigo.visibility = View.VISIBLE
        binding.spnCodigo.setSelection(0)
        llenarDisponibles()

        binding.txtCodigo.visibility = View.GONE
        binding.lblTipo.visibility = View.GONE
        binding.spnTipo.visibility = View.GONE
        binding.txtProcesador.visibility = View.GONE
        binding.txtRam.visibility = View.GONE
        binding.txtDisco.visibility = View.GONE
        binding.lblMarca.visibility = View.GONE
        binding.spnMarca.visibility = View.GONE
        binding.lblImprime.visibility = View.GONE
        binding.spnImprime.visibility = View.GONE
        binding.lblEntrada.visibility = View.GONE
        binding.spnEntrada.visibility = View.GONE
        binding.txtFechaC.visibility = View.GONE
    }

    fun tipoComputadora(){
        binding.txtProcesador.visibility = View.VISIBLE
        binding.txtProcesador.setText("")
        binding.txtRam.visibility = View.VISIBLE
        binding.txtRam.setText("")
        binding.txtDisco.visibility = View.VISIBLE
        binding.txtDisco.setText("")

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
        binding.spnMarca.setSelection(0)

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
        binding.spnImprime.setSelection(0)

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
        binding.spnEntrada.setSelection(0)

        binding.txtProcesador.visibility = View.GONE
        binding.txtRam.visibility = View.GONE
        binding.txtDisco.visibility = View.GONE
        binding.lblMarca.visibility = View.GONE
        binding.spnMarca.visibility = View.GONE
        binding.lblImprime.visibility = View.GONE
        binding.spnImprime.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}