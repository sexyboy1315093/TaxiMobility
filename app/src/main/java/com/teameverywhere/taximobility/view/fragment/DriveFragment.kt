package com.teameverywhere.taximobility.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView.OnMapReadyListener
import com.skt.tmap.overlay.TMapMarkerItem
import com.teameverywhere.taximobility.databinding.BottomSheetLayoutBinding
import com.teameverywhere.taximobility.databinding.FragmentDriveBinding
import com.teameverywhere.taximobility.model.Feature
import com.teameverywhere.taximobility.model.NaviItem
import com.teameverywhere.taximobility.model.SearchResultEntity
import com.teameverywhere.taximobility.network.NavigationApi
import com.teameverywhere.taximobility.util.Constant
import com.teameverywhere.taximobility.view.adapter.DirectionStepAdapter
import com.teameverywhere.taximobility.viewmodel.NavigationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DriveFragment(private var startPlace: SearchResultEntity, private var endPlace: SearchResultEntity, private var type: Int): Fragment(), OnMapReadyListener {
    private var _binding: FragmentDriveBinding? = null
    private val binding get() = _binding!!
    private val navigationViewModel: NavigationViewModel by activityViewModels()

    companion object {
        const val TAG = "DriveFragment"
    }

    lateinit var adapter: DirectionStepAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    private lateinit var bottomSheetLayoutBinding: BottomSheetLayoutBinding

    lateinit var currentLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: com.google.android.gms.location.LocationRequest

    lateinit var directionStepModels: ArrayList<Feature>
    var hour = 0
    var minute = 0
    var kilometer = 0
    var meter = 0

    lateinit var startPoint: TMapPoint
    lateinit var naviItem: NaviItem

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDriveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        bindViews()
        observeViewModel()
    }

    private fun init() = with(binding){
        bottomSheetLayoutBinding = bottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayoutBinding.root)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        directionStepModels = arrayListOf()
        adapter = DirectionStepAdapter(directionStepModels)
        bottomSheetLayoutBinding.stepRecyclerView.adapter = adapter
        bottomSheetLayoutBinding.stepRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun bindViews() = with(binding){
        tmap.setSKTMapApiKey(Constant.TMAP_KEY)
        tmap.setOnMapReadyListener(this@DriveFragment)
    }

    override fun onMapReady() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            return
        }

        currentLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        currentLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()!!)
    }

    fun onLocationChanged(location: Location) = with(binding) {
        tmap.setCenterPoint(location.latitude, location.longitude)
        tmap.zoomLevel = 17

        val startMarker = TMapMarkerItem()
        startMarker.id = "startMarker"
        Log.d(TAG, "onLocationChanged: type $type")
        if(type == 1){
            startMarker.setTMapPoint(startPlace.locationLatLng.latitude.toDouble(), startPlace.locationLatLng.longitude.toDouble())
        }else {
            startMarker.setTMapPoint(location.latitude, location.longitude)
        }
        tmap.addTMapMarkerItem(startMarker)

        val endMarker = TMapMarkerItem()
        endMarker.id = "endMarker"
        endMarker.setTMapPoint(endPlace.locationLatLng.latitude.toDouble(), endPlace.locationLatLng.longitude.toDouble())
        tmap.addTMapMarkerItem(endMarker)

        val tMapData = TMapData()
        if(type == 1){
            startPoint = TMapPoint(startPlace.locationLatLng.latitude.toDouble(), startPlace.locationLatLng.longitude.toDouble())
        }else {
            startPoint = TMapPoint(location.latitude, location.longitude)
        }
        val endPoint = TMapPoint(endPlace.locationLatLng.latitude.toDouble(), endPlace.locationLatLng.longitude.toDouble())

        tMapData.findPathData(startPoint, endPoint) { tMapPolyLine ->
            tMapPolyLine.lineWidth = 5f
            tMapPolyLine.lineColor = Color.BLUE
            tMapPolyLine.lineAlpha = 255
            tMapPolyLine.outLineWidth = 5f
            tMapPolyLine.outLineColor = Color.RED
            tMapPolyLine.outLineAlpha = 255
            tmap.addTMapPolyLine(tMapPolyLine)
            tmap.zoomLevel = 17
            if(type == 1){
                tmap.setCenterPoint(startPlace.locationLatLng.latitude.toDouble(), startPlace.locationLatLng.longitude.toDouble())
            }else {
                tmap.setCenterPoint(location.latitude, location.longitude)
            }
        }

        tmap.isCompassMode = true
        tmap.setSightVisible(true)
        tmap.setRotateEnable(true)
        tmap.rotation = 0.0f

        if(type == 1){
            naviItem = NaviItem(endPlace.locationLatLng.longitude.toDouble(), endPlace.locationLatLng.latitude.toDouble(), startPlace.locationLatLng.longitude.toDouble(), startPlace.locationLatLng.latitude.toDouble())
        }else {
            naviItem = NaviItem(endPlace.locationLatLng.longitude.toDouble(), endPlace.locationLatLng.latitude.toDouble(), location.longitude, location.latitude)
        }

        CoroutineScope(Dispatchers.Main).launch {
            navigationViewModel.getDirection(naviItem)
        }
    }

    private fun observeViewModel() = with(binding){
        navigationViewModel.loading.observe(viewLifecycleOwner){ isLoading ->
            isLoading?.let {
                if(isLoading){
                    progress.visibility = View.VISIBLE
                }else {
                    progress.visibility = View.GONE
                }
            }
        }

        navigationViewModel.hour.observe(viewLifecycleOwner){
            hour = it
            bottomSheetLayoutBinding.txtSheetTime.text = "${hour}시간 ${minute}분"
        }

        navigationViewModel.minute.observe(viewLifecycleOwner){
            minute = it
            bottomSheetLayoutBinding.txtSheetTime.text = "${hour}시간 ${minute}분"
        }

        navigationViewModel.kilometer.observe(viewLifecycleOwner){
            kilometer = it
            bottomSheetLayoutBinding.txtSheetDistance.text = "${kilometer}km ${meter}m"
        }

        navigationViewModel.meter.observe(viewLifecycleOwner){
            meter = it
            bottomSheetLayoutBinding.txtSheetDistance.text = "${kilometer}km ${meter}m"
        }

        navigationViewModel.directionStepModels.observe(viewLifecycleOwner){
            adapter = DirectionStepAdapter(it)
            bottomSheetLayoutBinding.stepRecyclerView.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}