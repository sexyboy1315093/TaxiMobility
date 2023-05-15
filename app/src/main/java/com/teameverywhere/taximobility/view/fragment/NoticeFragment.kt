package com.teameverywhere.taximobility.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.FragmentNoticeBinding
import com.teameverywhere.taximobility.model.Notice
import com.teameverywhere.taximobility.view.adapter.CarChangeAdapter
import com.teameverywhere.taximobility.view.adapter.NoticeAdapter
import com.teameverywhere.taximobility.viewmodel.NoticeViewModel


class NoticeFragment : Fragment(), NoticeAdapter.OnClickListener {
    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!
    private val noticeViewModel: NoticeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() = with(binding) {
        noticeViewModel.noticeArray.observe(viewLifecycleOwner){
            rvNotice.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvNotice.adapter = NoticeAdapter(it, this@NoticeFragment)
        }

        noticeViewModel.loading.observe(viewLifecycleOwner){ isLoading ->
            isLoading?.let {
                if(isLoading){
                    progressbar.visibility = View.VISIBLE
                }else {
                    progressbar.visibility = View.GONE
                }
            }
        }
    }

    override fun onClickItem(notice: Notice) {
        val manager = requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, NoticeDetailFragment(notice.title, notice.date, notice.description))
            addToBackStack(null)
            commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}