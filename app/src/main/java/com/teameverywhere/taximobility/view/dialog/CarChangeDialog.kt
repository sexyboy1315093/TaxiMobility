package com.teameverywhere.taximobility.view.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teameverywhere.taximobility.databinding.FragmentCarChangeDialogBinding
import com.teameverywhere.taximobility.view.adapter.CarChangeAdapter
import com.teameverywhere.taximobility.viewmodel.CarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarChangeDialog : DialogFragment(), CarChangeAdapter.OnClickListener {
    private var _binding: FragmentCarChangeDialogBinding? = null
    private val binding get() = _binding!!
    private val carViewModel: CarViewModel by activityViewModels()

    companion object {
        const val TAG = "CarChangeDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCarChangeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        observeViewModel()
    }

    private fun bindViews() = with(binding){
        btnChange.setOnClickListener {
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun observeViewModel() = with(binding) {
        CoroutineScope(Dispatchers.IO).launch {
            val carList = carViewModel.getCar()
            if(carList.isEmpty()){
                txtNoCar.visibility = View.VISIBLE
            }else {
                txtNoCar.visibility = View.GONE
                rvCar.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                rvCar.adapter = CarChangeAdapter(carList, this@CarChangeDialog)
            }
        }

        carViewModel.loading.observe(viewLifecycleOwner){ isLoading ->
            isLoading?.let {
                if(isLoading){
                    progressbar.visibility = View.VISIBLE
                }else {
                    progressbar.visibility = View.GONE
                }
            }
        }
    }

    override fun onClick(b: Boolean) {
        if(b){
            CoroutineScope(Dispatchers.IO).launch {
                val carList = carViewModel.getCar()
                withContext(Dispatchers.Main){
                    binding.rvCar.adapter = CarChangeAdapter(carList, this@CarChangeDialog)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}