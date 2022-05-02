package com.example.ladm_u3_practica1_osunamadrigalbiancagpe.ui.slideshow

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
import com.example.ladm_u3_practica1_osunamadrigalbiancagpe.Asignacion
import com.example.ladm_u3_practica1_osunamadrigalbiancagpe.AsignacionEquipoComputo
import com.example.ladm_u3_practica1_osunamadrigalbiancagpe.MainActivity3
import com.example.ladm_u3_practica1_osunamadrigalbiancagpe.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

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
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mostrarAsignaciones(false)
        binding.spnFiltro.isEnabled = false

        binding.btnCargar.setOnClickListener {
            mostrarAsignaciones(false)
            binding.btnCargar.visibility = View.GONE
            binding.swFiltro.isChecked = false
        }

        binding.swFiltro.setOnCheckedChangeListener({_,isChecked ->
            if(isChecked){
                binding.btnCargar.visibility = View.GONE
                binding.spnFiltro.isEnabled = true
                mostrarAsignaciones(true)
            }else{
                mostrarAsignaciones(false)
            }
        })

        binding.spnFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(binding.swFiltro.isChecked) {
                    mostrarAsignaciones(true)
                }
            }
        }

        return root
    }

    fun mostrarAsignaciones(filtro: Boolean) {
        val f = filtro
        var arregloAsignaciones = Asignacion(requireContext()).mostrarTodos()
        vector.clear()

        if(f){
            arregloAsignaciones.clear()
            arregloAsignaciones = Asignacion(requireContext())
                .buscarAsignacionesArea(binding.spnFiltro.selectedItem.toString())
        }else {
            arregloAsignaciones.clear()
            arregloAsignaciones = Asignacion(requireContext()).mostrarTodos()
            binding.spnFiltro.isEnabled = false
            binding.spnFiltro.setSelection(0)
        }

        if(arregloAsignaciones.size>0) {
            (0..arregloAsignaciones.size - 1).forEach {
                var asign = arregloAsignaciones[it]

                vector.add(
                    "Id: ${asign.idAsignacion}\n" +
                            "Empleado: ${asign.nomEmpleado}\n" +
                            "Area: ${asign.areaTrabajo}\n" +
                            "Fecha asignación: ${asign.fecha}\n" +
                            "Equipo: ${asign.codigoBarras}"
                )
            }
        }

        binding.lista.adapter = ArrayAdapter<String>(requireContext(),
            R.layout.simple_list_item_1, vector)

        binding.lista.setOnItemClickListener { adapterView, view, position, id ->
            AlertDialog.Builder(requireContext())
                .setTitle("ACTUALIZAR O ELIMINAR")
                .setMessage("¿DESEA ACTUALIZAR O ELIMINAR LA ASIGNACIÓN?")
                .setPositiveButton("ACTUALIZAR",{d,i->
                    codigoActual = arregloAsignaciones[position].codigoBarras
                    llamarActualizarA()
                    d.dismiss()
                })
                .setNegativeButton("ELIMINAR",{d,i->
                    val asignEliminar = arregloAsignaciones[position]
                    if(asignEliminar.eliminar()){
                        AlertDialog.Builder(requireContext())
                            .setMessage("Eliminado")
                            .show()
                    }
                    binding.swFiltro.isChecked = false
                    mostrarAsignaciones(false)
                    d.dismiss()
                })
                .setNeutralButton("CANCEL",{d,i->d.cancel()})
                .show()
        }
    }

    fun llamarActualizarA() {
        val vtnActualizarA = Intent(requireContext(), MainActivity3::class.java)
        vtnActualizarA.putExtra("CODIGO", codigoActual)
        startActivity(vtnActualizarA)

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