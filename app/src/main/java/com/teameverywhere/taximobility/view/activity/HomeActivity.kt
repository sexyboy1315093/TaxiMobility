package com.teameverywhere.taximobility.view.activity

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.teameverywhere.taximobility.MyApplication
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.ActivityHomeBinding
import com.teameverywhere.taximobility.model.Weather
import com.teameverywhere.taximobility.service.MyService
import com.teameverywhere.taximobility.util.Notification
import com.teameverywhere.taximobility.view.fragment.*
import com.teameverywhere.taximobility.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    companion object {
        const val TAG = "HomeActivity"
        const val CHANNEL_ID = "vibration"
        const val CHANNEL_ID2 = "noVibration"
        const val CHANNEL_NAME = "channelName"
        const val NOTIF_ID = 0
    }

    lateinit var weathers: Weather
    var isVibration = MyApplication.prefs.getIsVibration("isVibration", false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //드로어 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24) //드로어 홈 버튼 아이콘
        supportActionBar?.setTitle("")
        
        bindViews()
        observeViewModel()
        createNotificationChannel()
        bottomNavMove()
    }
    
    private fun bindViews() = with(binding){
        mainNavigationView.setNavigationItemSelectedListener(this@HomeActivity)
        swStart.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                homeViewModel.sendIsStart(isChecked)
                CoroutineScope(Dispatchers.IO).launch {
                    val intent = Intent(this@HomeActivity, MyService::class.java)
                    applicationContext.startForegroundService(intent)
                    checkPermission()
                }
            }else {
                homeViewModel.sendIsStart(isChecked)
                Toast.makeText(this@HomeActivity, "영업을 종료합니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@HomeActivity, MyService::class.java)
                applicationContext.stopService(intent)
            }
        }

        mainNavigationView.getHeaderView(0).findViewById<TextView>(R.id.tvDriver).text = MyApplication.prefs.getName("name", "없다")
    }

    private fun observeViewModel(){
        homeViewModel.weather.observe(this){ weather ->
            weathers = weather
            changeFragment(HomeFragment(weathers))
        }

        homeViewModel.isVibration.observe(this){ b ->
            if(b){
                Notification.noSoundChannelSetting(this)
            }else {
                Notification.channelSetting(this)
            }
        }
    }

    private fun createNotificationChannel(){
        if(isVibration){
            Notification.noSoundChannelSetting(this)
        }else {
            Notification.channelSetting(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                binding.mainDrawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> {
                binding.bottomNav.selectedItemId = R.id.bottom_nav_home
            }
            R.id.nav_drive -> {
                binding.bottomNav.selectedItemId = R.id.bottom_nav_navi
            }
            R.id.nav_setting -> {
                binding.bottomNav.selectedItemId = R.id.bottom_nav_setting
            }
            R.id.nav_document -> {
                binding.bottomNav.selectedItemId = R.id.bottom_nav_document
            }
        }
        binding.mainDrawerLayout.closeDrawers()
        return false
    }

    private fun bottomNavMove(){
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.bottom_nav_home -> {
                    changeFragment(HomeFragment(weathers))
                }
                R.id.bottom_nav_navi -> {
                    changeFragment(NavigationFragment())
                }
                R.id.bottom_nav_setting -> {
                    changeFragment(SettingFragment())
                }
                R.id.bottom_nav_document -> {
                    changeFragment(DocumentFragment())
                }
            }
            true
        }
    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun checkPermission(){
        var permission = mutableMapOf<String, String>()
        permission["fine"] = Manifest.permission.ACCESS_FINE_LOCATION
        permission["coarse"] = Manifest.permission.ACCESS_COARSE_LOCATION

        var denied = permission.count{
            ContextCompat.checkSelfPermission(this, it.value) == PackageManager.PERMISSION_DENIED
        }

        if(denied > 0){
            requestPermissions(permission.values.toTypedArray(),
                NavigationFragment.PERMISSION_REQUEST_CODE
            )
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == NavigationFragment.PERMISSION_REQUEST_CODE){
            grantResults.forEach {
                if(it == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "서비스의 필요한 권한입니다.\n권한에 동의해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        if(binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.mainDrawerLayout.closeDrawers()
        }else {
            super.onBackPressed()
        }
    }
}