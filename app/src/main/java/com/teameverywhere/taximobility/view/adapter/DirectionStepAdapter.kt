package com.teameverywhere.taximobility.view.adapter

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.StepItemLayoutBinding
import com.teameverywhere.taximobility.model.DirectionStep
import com.teameverywhere.taximobility.model.Feature

class DirectionStepAdapter(private var directionStepModels: List<Feature>): RecyclerView.Adapter<DirectionStepAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(StepItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(directionStepModels[position])
    }

    override fun getItemCount(): Int {
        return directionStepModels.size
    }

    fun setDirectionStepModels(directionStepModels: List<Feature>){
        this.directionStepModels = directionStepModels
        notifyDataSetChanged()
    }

    inner class ViewHolder(private var binding: StepItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(stepModel: Feature){
            when(stepModel.properties.turnType){
                11, 0, 200 -> {
                    binding.ivDirection.setImageResource(R.drawable.free_icon_straight)
                }
                12 -> {
                    binding.ivDirection.setImageResource(R.drawable.left)
                }
                13 -> {
                    binding.ivDirection.setImageResource(R.drawable.right)
                }
                14 -> {
                    binding.ivDirection.setImageResource(R.drawable.uturn)
                }
            }
            binding.tvDistance.text = "${stepModel.properties.description.toString()}"
        }
    }
}