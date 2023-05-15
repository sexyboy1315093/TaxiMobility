package com.teameverywhere.taximobility.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.teameverywhere.taximobility.MyApplication
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.CarItemLayoutBinding
import com.teameverywhere.taximobility.model.Car
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarChangeAdapter(private var carArr: List<Car>, private val onClickListener: OnClickListener): RecyclerView.Adapter<CarChangeAdapter.ViewHolder>() {
    private var changeCar = MyApplication.prefs.getCarChange("changeCar", "")
    
    companion object {
        const val TAG = "CarChangeAdapter"
    }

    interface OnClickListener {
        fun onClick(b: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CarItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(carArr[position])
    }

    override fun getItemCount(): Int {
        return carArr.size
    }

    inner class ViewHolder(private val binding: CarItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(car: Car) = with(binding) {
            tvCarName.text = "${car.name}/${car.number}"

            rdCar.isChecked = changeCar == "${car.name}/${car.number}"

            rdCar.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    MyApplication.prefs.setCarNumber("carNumber", car.number)
                    MyApplication.prefs.setCarCompany("carCompany", car.company)
                    MyApplication.prefs.setCar("carName", car.name)
                    MyApplication.prefs.setCarChange("changeCar", "${car.name}/${car.number}")
                    onClickListener.onClick(isChecked)
                }

//                changeCar = MyApplication.prefs.getCarChange("changeCar", "")
//                CoroutineScope(Dispatchers.Main).launch {
//                    if(changeCar == "${car.name}/${car.number}"){
//                        rdCar.isChecked = true
//                    }else {
//                        rdCar.isChecked = false
//                    }
//                }
            }
        }
    }
}