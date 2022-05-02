package com.example.ladm_u3_practica1_osunamadrigalbiancagpe.ui.home

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_practica1_osunamadrigalbiancagpe.*
import com.example.ladm_u3_practica1_osunamadrigalbiancagpe.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var vector = ArrayList<String>()
    var codigoActual = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mostrarEquipos(false)
        binding.spnFiltro.isEnabled = false

        binding.btnCargar.setOnClickListener {
            mostrarEquipos(false)
            binding.btnCargar.visibility = View.GONE
            binding.swFiltro.isChecked = false
        }

        binding.swFiltro.setOnCheckedChangeListener({_,isChecked ->
            if(isChecked){
                binding.btnCargar.visibility = View.GONE
                binding.spnFiltro.isEnabled = true
                mostrarEquipos(true)
            }else{
                mostrarEquipos(false)
            }
        })

        binding.spnFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(binding.swFiltro.isChecked) {
                    mostrarEquipos(true)
                }
            }
        }

        return root
    }

    fun mostrarEquipos(filtro: Boolean) {
        val f = filtro
        var arregloEquipos = AsignacionEquipoComputo(requireContext()).mostrarTodos()
        vector.clear()

        if(f){
            arregloEquipos.clear()
            arregloEquipos = AsignacionEquipoComputo(requireContext())
                .buscarEquiposTipo(binding.spnFiltro.selectedItem.toString())
        }else {
            arregloEquipos.clear()
            arregloEquipos = AsignacionEquipoComputo(requireContext()).mostrarTodos()
            binding.spnFiltro.isEnabled = false
            binding.spnFiltro.setSelection(0)
        }

        if(arregloEquipos.size>0) {
            (0..arregloEquipos.size - 1).forEach {
                var equipo = arregloEquipos[it]
                var asignacion = Asignacion(requireContext()).buscarAsignacion(equipo.codigoBarras)
                var estado = "No asignado"
                if(asignacion.codigoBarras==equipo.codigoBarras){
                    estado = "Asignado a: ${asignacion.nomEmpleado}\n"
                }
                vector.add(
                    "Código: ${equipo.codigoBarras}\n" +
                            "Tipo: ${equipo.tipoEquipo}\n" +
                            "Caracteristicas: ${equipo.caracteristicas}\n" +
                            "Fecha compra: ${equipo.fechaCompra}\n" + estado
                )
            }
        }

        binding.lista.adapter = ArrayAdapter<String>(requireContext(),
            R.layout.simple_list_item_1, vector)

        binding.lista.setOnItemClickListener { adapterView, view, position, id ->
            AlertDialog.Builder(requireContext())
                .setTitle("ACTUALIZAR O ELIMINAR")
                .setMessage("¿DESEA ACTUALIZAR O ELIMINAR EL EQUIPO?")
                .setPositiveButton("ACTUALIZAR",{d,i->
                    codigoActual = arregloEquipos[position].codigoBarras
                    llamarActualizarE()
                    d.dismiss()
                })
                .setNegativeButton("ELIMINAR",{d,i->
                    val equipoEliminar = arregloEquipos[position]
                    val asignacionEliminar = Asignacion(requireContext())
                        .buscarAsignacion(equipoEliminar.codigoBarras)
                    if(asignacionEliminar.codigoBarras==equipoEliminar.codigoBarras){
                        asignacionEliminar.eliminar()
                    }
                    if(equipoEliminar.eliminar()){
                        AlertDialog.Builder(requireContext())
                            .setMessage("Eliminado")
                            .show()
                    }
                    binding.swFiltro.isChecked = false
                    mostrarEquipos(false)
                    d.dismiss()
                })
                .setNeutralButton("CANCELAR",{d,i->d.cancel()})
                .show()
        }
    }

    fun llamarActualizarE() {
        val vtnActualizarE = Intent(requireContext(), MainActivity2::class.java)
        vtnActualizarE.putExtra("CODIGO", codigoActual)
        startActivity(vtnActualizarE)

        binding.swFiltro.isChecked = false
        vector.clear()
        binding.lista.adapter = ArrayAdapter<String>(requireContext(),
            R.layout.simple_list_item_1, vector)
        binding.btnCargar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}