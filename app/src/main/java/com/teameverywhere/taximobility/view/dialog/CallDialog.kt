package com.teameverywhere.taximobility.view.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.FragmentCallDialogBinding


class CallDialog(private val onClickListener: OnClickListener) : DialogFragment() {
    private var _binding: FragmentCallDialogBinding? = null
    private val binding get() = _binding!!

    interface OnClickListener {
        fun startRace(isStart: Boolean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCallDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
    }

    private fun bindViews() = with(binding){
        btnOk.setOnClickListener {
            onClickListener.startRace(true)
            dismiss()
        }

        btnCancel.setOnClickListener { dismiss() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}