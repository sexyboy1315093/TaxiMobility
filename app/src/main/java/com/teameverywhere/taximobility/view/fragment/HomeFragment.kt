package com.teameverywhere.taximobility.view.fragment

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.teameverywhere.taximobility.MyApplication
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.FragmentHomeBinding
import com.teameverywhere.taximobility.model.LocationLatLngEntity
import com.teameverywhere.taximobility.model.Notice
import com.teameverywhere.taximobility.model.SearchResultEntity
import com.teameverywhere.taximobility.model.Weather
import com.teameverywhere.taximobility.util.Notification
import com.teameverywhere.taximobility.view.activity.HomeActivity
import com.teameverywhere.taximobility.view.adapter.ViewPagerAdapter
import com.teameverywhere.taximobility.view.dialog.CallDialog
import com.teameverywhere.taximobility.view.dialog.CarAddDialog
import com.teameverywhere.taximobility.viewmodel.CarViewModel
import com.teameverywhere.taximobility.viewmodel.HomeViewModel
import com.teameverywhere.taximobility.viewmodel.NoticeViewModel
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.timerTask

class HomeFragment(private val weather: Weather) : Fragment(), ViewPagerAdapter.OnClickListener, CallDialog.OnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val noticeViewModel: NoticeViewModel by viewModels()
    private val carViewModel: CarViewModel by activityViewModels()

    companion object{
        const val TAG = "HomeFragment"
    }

    var carNumber = ""
    var carCompany = ""
    var carName = ""

    var callWaiting = true
    var currentPage = 0

    var lastLatitude = 0.0f
    var lastLongitude = 0.0f
    lateinit var currentLocationProviderClient: FusedLocationProviderClient
    lateinit var lastLocation: Location
    lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation!!)
        }
    }

    var handler = Handler()
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        bindViews()
        observeViewModel()
    }

    private fun init() = with(binding){
        locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun bindViews() = with(binding){
        val imageUrl = "https://openweathermap.org/img/wn/${weather.list.get(0).weather.get(0).icon}.png"
        Glide.with(root)
            .load(imageUrl)
            .into(ivWeather)

        val temp = weather.list[0].temp.day
        tvTemp.text = "${temp.toInt()}\u2103"

        carNumber = MyApplication.prefs.getCarNumber("carNumber", "없음")
        carCompany = MyApplication.prefs.getCarCompany("carCompany", "없음")
        carName = MyApplication.prefs.getCar("carName","없음")
        if(carNumber == "없음" || carCompany == "없음" || carName == "없음"){
            tvCarNumber.visibility = View.GONE
            tvCarCompany.visibility = View.GONE
            tvCarName.visibility = View.GONE
            ivCar.visibility = View.GONE
            cd2.isEnabled = false
        }else {
            tvCarNumber.text = carNumber
            tvCarCompany.text = "${carCompany}/"
            tvCarName.text = carName
            txtCarAdd.visibility = View.GONE
            txtSubCarAdd.visibility = View.GONE
        }

        cd.setOnClickListener {
            var manager = requireActivity().supportFragmentManager
            CarAddDialog().show(manager, "CarAddDialog")
        }

        cd2.setOnClickListener {
            if(callWaiting){
                ltAnimation.visibility = View.VISIBLE
                tvCall.text = "콜 대기 중"
                tvCall.setTextColor(resources.getColor(R.color.sub_color))
                callWaiting = false

                handler.postDelayed({
                    val manager = requireActivity().supportFragmentManager
                    CallDialog(this@HomeFragment).show(manager, "CallDialog")

                    isVibration = MyApplication.prefs.getIsVibration("isVibration", false)
                    if(isVibration){
                        Notification.notificationSetting(requireContext(), resources.getString(R.string.app_name), "고객님의 호출이 있습니다.",
                            HomeActivity.CHANNEL_ID2
                        )
                    }else {
                        Notification.notificationSetting(requireContext(), resources.getString(R.string.app_name), "고객님의 호출이 있습니다.",
                            HomeActivity.CHANNEL_ID
                        )
                    }
                }, 20000)
            }else {
                ltAnimation.visibility = View.GONE
                tvCall.text = "콜 멈춤"
                tvCall.setTextColor(resources.getColor(R.color.main_color))
                callWaiting = true
            }
        }
    }

    private fun observeViewModel() = with(binding){
        homeViewModel.isStart.observe(viewLifecycleOwner){ isStart ->
            if(isStart){
                clRest.visibility = View.GONE
                cl.visibility = View.VISIBLE
                cl2.visibility = View.VISIBLE
                cd.visibility = View.VISIBLE
                cd2.visibility = View.VISIBLE
            }else {
                clRest.visibility = View.VISIBLE
                cl.visibility = View.GONE
                cl2.visibility = View.GONE
                cd.visibility = View.GONE
                cd2.visibility = View.GONE
            }
        }

        noticeViewModel.noticeArray.observe(viewLifecycleOwner){
            pager.adapter = ViewPagerAdapter(it, this@HomeFragment)

            Timer().scheduleAtFixedRate(timerTask {
                CoroutineScope(Dispatchers.Main).launch {
                    if(currentPage >= it.size){
                        currentPage = 0
                        pager.currentItem = currentPage
                    }else {
                        pager.currentItem = currentPage
                        currentPage++
                    }
                }
            },0, 5000)
        }

        carViewModel.addCar.observe(viewLifecycleOwner){
           CoroutineScope(Dispatchers.IO).launch {
               MyApplication.prefs.setCarNumber("carNumber", it.number)
               MyApplication.prefs.setCarCompany("carCompany", it.company)
               MyApplication.prefs.setCar("carName", it.name)

               withContext(Dispatchers.Main){
                   tvCarNumber.visibility = View.VISIBLE
                   tvCarCompany.visibility = View.VISIBLE
                   tvCarName.visibility = View.VISIBLE
                   ivCar.visibility = View.VISIBLE

                   tvCarNumber.text = it.number
                   tvCarCompany.text = "${it.company}/"
                   tvCarName.text = it.name
                   txtCarAdd.visibility = View.GONE
                   txtSubCarAdd.visibility = View.GONE

                   cd2.isEnabled = true
               }
           }
        }
    }

    private fun onLocationChanged(location: Location){
        lastLocation = location
        lastLatitude = lastLocation.latitude.toFloat()
        lastLongitude = lastLocation.longitude.toFloat()
    }

    private suspend fun startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return
        }else {
            currentLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            currentLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }
    }

    override fun onClickItem(notice: Notice) {
        val manager = requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, NoticeDetailFragment(notice.title, notice.date, notice.description))
            addToBackStack(null)
            commit()
        }
    }

    override fun startRace(isStart: Boolean) {
        if(isStart){
            CoroutineScope(Dispatchers.IO).async {
                startLocationUpdates()
            }

            val startPlace  = SearchResultEntity(fullAddress="", name="", locationLatLng= LocationLatLngEntity(latitude = lastLatitude, longitude=lastLongitude))
            val endPlace = SearchResultEntity(fullAddress="대구 동구 신암동  294 0", name="동대구역", locationLatLng= LocationLatLngEntity(latitude=35.87993f, longitude=128.62814f))

            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, DriveFragment(startPlace, endPlace, 2))
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}