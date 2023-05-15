package com.teameverywhere.taximobility.view.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.FragmentNavigationBinding
import com.teameverywhere.taximobility.model.LocationLatLngEntity
import com.teameverywhere.taximobility.model.Poi
import com.teameverywhere.taximobility.model.Pois
import com.teameverywhere.taximobility.model.SearchResultEntity
import com.teameverywhere.taximobility.view.activity.HomeActivity
import com.teameverywhere.taximobility.view.adapter.CarChangeAdapter
import com.teameverywhere.taximobility.view.adapter.SearchAdapter
import com.teameverywhere.taximobility.viewmodel.NavigationViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class NavigationFragment : Fragment(), SearchAdapter.OnClickListener {
    private var _binding: FragmentNavigationBinding? = null
    private val binding get() = _binding!!
    private val navigationViewModel: NavigationViewModel by activityViewModels()

    companion object {
        const val TAG = "NavigationFragment"
        const val PERMISSION_REQUEST_CODE = 1
    }

    var type = 0
    private lateinit var startPlace: SearchResultEntity
    private lateinit var endPlace: SearchResultEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

        (requireActivity() as HomeActivity).binding.toolbar.visibility = View.GONE
        (requireActivity() as HomeActivity).binding.bottomNav.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNavigationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        observeViewModel()
    }

    private fun bindViews() = with(binding){
        etStartPlace.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                type = 1
                searchKeyword(etStartPlace.text.toString().trim())
                val manager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(etStartPlace.windowToken, 0)
                view.clearFocus()
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }

        etEndPlace.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                type = 2
                searchKeyword(etEndPlace.text.toString().trim())
                val manager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(etEndPlace.windowToken, 0)
                view.clearFocus()
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }
    }

    private fun searchKeyword(place: String){
        CoroutineScope(Dispatchers.IO).launch {
            navigationViewModel.getPlace(place)
        }
    }

    private fun observeViewModel() = with(binding){
        navigationViewModel.searchResult.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                txtEmpty.visibility = View.VISIBLE
            }else {
                rvPlace.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                rvPlace.adapter = SearchAdapter(it, this@NavigationFragment, type)
            }
        }
    }

    override fun placeSelect(searchResult: SearchResultEntity, type: Int) {
        CoroutineScope(Dispatchers.Main).launch{
            if(type == 1){
                startPlace = searchResult
                binding.etStartPlace.setText(searchResult.fullAddress)
                Log.d(TAG, "placeSelect: $startPlace")
            }else {
                endPlace = searchResult
                binding.etEndPlace.setText(searchResult.fullAddress)
                if(binding.etStartPlace.text!!.isNotEmpty()){
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.container, DriveFragment(startPlace, endPlace, 1))
                        addToBackStack(null)
                        commit()
                    }
                }else {
                    Toast.makeText(requireContext(), "출발지를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            binding.rvPlace.adapter = SearchAdapter(emptyList(), this@NavigationFragment, type)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}