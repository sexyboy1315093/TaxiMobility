package com.teameverywhere.taximobility.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teameverywhere.taximobility.databinding.NoticeItemLayoutBinding
import com.teameverywhere.taximobility.model.Notice

class NoticeAdapter(private val notices: ArrayList<Notice>, private val onClickListener: OnClickListener): RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onClickItem(notice: Notice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NoticeItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notices[position])
    }

    override fun getItemCount(): Int {
        return notices.size
    }

    inner class ViewHolder(private val binding: NoticeItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(notice: Notice){
            binding.tvTitle.text = notice.title
            binding.tvDate.text = notice.date
            binding.clMove.setOnClickListener {
                onClickListener.onClickItem(notice)
            }
        }
    }
}