package com.teameverywhere.taximobility.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.FragmentNoticeDetailBinding

class NoticeDetailFragment(val title: String, val date: String, val description: String) : Fragment() {
    private var _binding: FragmentNoticeDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoticeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
    }

    private fun bindViews() = with(binding){
        tvNoticeTitle.text = title
        tvDate.text = date
        tvNoticeDetail.text = description
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}