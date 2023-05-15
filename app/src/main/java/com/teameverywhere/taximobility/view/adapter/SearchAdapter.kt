package com.teameverywhere.taximobility.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teameverywhere.taximobility.databinding.SearchItemLayoutBinding
import com.teameverywhere.taximobility.model.SearchResultEntity

class SearchAdapter(private val searchResultList: List<SearchResultEntity>, private val onClickListener: OnClickListener, private val type: Int): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    interface OnClickListener {
        fun placeSelect(searchResult: SearchResultEntity, type: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SearchItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchResultList[position])
    }

    override fun getItemCount(): Int {
        return searchResultList.size
    }

    inner class ViewHolder(private val binding: SearchItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(searchResult: SearchResultEntity){
            binding.tvPlace.text = searchResult.name
            binding.tvAddress.text = searchResult.fullAddress
            binding.cl.setOnClickListener {
                onClickListener.placeSelect(searchResult, type)
            }
        }
    }
}