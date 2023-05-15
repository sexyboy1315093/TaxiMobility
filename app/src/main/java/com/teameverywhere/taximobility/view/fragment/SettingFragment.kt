package com.teameverywhere.taximobility.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.teameverywhere.taximobility.BuildConfig
import com.teameverywhere.taximobility.MyApplication
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.FragmentSettingBinding
import com.teameverywhere.taximobility.view.activity.HomeActivity
import com.teameverywhere.taximobility.view.dialog.CarAddDialog
import com.teameverywhere.taximobility.view.dialog.CarChangeDialog
import com.teameverywhere.taximobility.viewmodel.HomeViewModel

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()

    companion object {
        const val TAG = "SettingFragment"
    }

    var isRemember = MyApplication.prefs.getRemember("isRemember", false)
    var isVibration = MyApplication.prefs.getIsVibration("isVibration", false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

        (requireActivity() as HomeActivity).binding.toolbar.visibility = View.VISIBLE
        (requireActivity() as HomeActivity).binding.bottomNav.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
    }

    private fun bindViews() = with(binding){
        swAutoStart.isChecked = isRemember
        swAutoStart.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                MyApplication.prefs.setRemember("isRemember", isChecked)
            }else {
                MyApplication.prefs.setRemember("isRemember", isChecked)
            }
        }

        cl2.setOnClickListener {
            var manager = requireActivity().supportFragmentManager
            CarAddDialog().show(manager, "CarAddDialog")
        }

        cl3.setOnClickListener {
            var manager = requireActivity().supportFragmentManager
            CarChangeDialog().show(manager, "CarChangeDialog")
        }

        swVibration.isChecked = isVibration
        swVibration.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                MyApplication.prefs.setIsVibration("isVibration", true)
                homeViewModel.isVibrationSetting(true)
            }else {
                MyApplication.prefs.setIsVibration("isVibration", false)
                homeViewModel.isVibrationSetting(false)
            }
        }

        cl5.setOnClickListener {
            val manager = requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, NoticeFragment())
                addToBackStack(null)
                commit()
            }
        }

        cl6.setOnClickListener {
            val phoneNumber = "tel:02-563-8034"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber))
            startActivity(intent)
        }

        tvVersion.text = "v.${BuildConfig.VERSION_CODE.toString()}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}