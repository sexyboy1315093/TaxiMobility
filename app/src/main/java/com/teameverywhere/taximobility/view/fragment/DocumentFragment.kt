package com.teameverywhere.taximobility.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.FragmentDocumentBinding
import com.teameverywhere.taximobility.view.activity.HomeActivity

class DocumentFragment : Fragment() {
    private var _binding: FragmentDocumentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

        (requireActivity() as HomeActivity).binding.toolbar.visibility = View.VISIBLE
        (requireActivity() as HomeActivity).binding.bottomNav.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}