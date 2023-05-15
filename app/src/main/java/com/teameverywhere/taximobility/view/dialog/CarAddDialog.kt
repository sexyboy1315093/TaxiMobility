package com.teameverywhere.taximobility.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.teameverywhere.taximobility.MyApplication
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.FragmentCarAddDialogBinding
import com.teameverywhere.taximobility.model.Car
import com.teameverywhere.taximobility.viewmodel.CarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CarAddDialog: DialogFragment() {
    private var _binding: FragmentCarAddDialogBinding? = null
    private val binding get() = _binding!!
    private val carViewModel: CarViewModel by activityViewModels()

    companion object {
        const val TAG = "CarAddDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCarAddDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        dropdownMenu()
    }

    private fun bindViews() = with(binding){
        btnAdd.setOnClickListener {
            val company = autoCompleteCompany.text.toString()
            val name = autoCompleteName.text.toString()
            val size = autoCompleteSize.text.toString()
            val number = etCarNumber.text.toString()

            if(company == "" || name == "" || size == "" || number == ""){
                Toast.makeText(requireContext(), "다시 한번 확인해주세요", Toast.LENGTH_SHORT).show()
            }else {
                val car = Car(UUID.randomUUID(), company, name, size, number)
                CoroutineScope(Dispatchers.IO).launch {
                    carViewModel.insertCar(car)
                }
                MyApplication.prefs.setCarNumber("carNumber", number)
                MyApplication.prefs.setCarCompany("carCompany", company)
                MyApplication.prefs.setCar("carName", name)
                MyApplication.prefs.setCarChange("changeCar", "${name}/${number}")
                Toast.makeText(requireContext(), "차량을 등록했습니다", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun dropdownMenu() = with(binding){
        val company = resources.getStringArray(R.array.company)
        val companyAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, company)
        autoCompleteCompany.setAdapter(companyAdapter)

        autoCompleteCompany.setOnItemClickListener { parent, view, position, id ->
            var model = resources.getStringArray(R.array.HModel)
            when(position){
                0 -> {
                    model = resources.getStringArray(R.array.HModel)
                }
                1 -> {
                    model = resources.getStringArray(R.array.KModel)
                }
                2 -> {
                    model = resources.getStringArray(R.array.SModel)
                }
            }
            val modelAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, model)
            autoCompleteName.setAdapter(modelAdapter)
        }

        val size = resources.getStringArray(R.array.size)
        val sizeAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, size)
        autoCompleteSize.setAdapter(sizeAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}