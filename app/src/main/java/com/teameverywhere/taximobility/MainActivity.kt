package com.teameverywhere.taximobility

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.teameverywhere.taximobility.databinding.ActivityMainBinding
import com.teameverywhere.taximobility.view.activity.HomeActivity
import com.teameverywhere.taximobility.view.activity.SignupActivity
import com.teameverywhere.taximobility.viewmodel.HomeViewModel
import com.teameverywhere.taximobility.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    companion object {
        const val TAG = "MainActivity"
    }

    var name = ""
    var password = ""
    var isName = false
    var isPassword = false
    var isRemember = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindViews()
        observeViewModel()
    }

    private fun bindViews() = with(binding) {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mainViewModel.sendName(p0.toString())
            }
            override fun afterTextChanged(p0: Editable?) = Unit
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mainViewModel.sendPassword(p0.toString())
            }
            override fun afterTextChanged(p0: Editable?) = Unit
        })

        cbRemember.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                MyApplication.prefs.setRemember("isRemember", true)
                name = etName.text.toString()
                password = etPassword.text.toString()
                mainViewModel.remember(name, password)
            }else {
                MyApplication.prefs.setRemember("isRemember", false)
                Toast.makeText(this@MainActivity, "회원정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        isRemember = MyApplication.prefs.getRemember("isRemember", false)
        if(isRemember){
            val loginIntent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

        btnLogin.isEnabled = false
        btnLogin.setOnClickListener {
            name = etName.text.toString()
            password = etPassword.text.toString()
            mainViewModel.login(name, password)
        }

        tvSignUp.setOnClickListener {
            val intent = Intent(this@MainActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        mainViewModel.nameViewModel.observe(this){
            isName = it != ""
            loginBtnSetting()
        }

        mainViewModel.passwordViewModel.observe(this){
            isPassword = it != ""
            loginBtnSetting()
        }

        mainViewModel.loading.observe(this){ isLoading ->
            isLoading?.let {
                if(isLoading){
                    binding.progressbar.visibility = View.VISIBLE
                }else {
                    binding.progressbar.visibility = View.GONE
                }
            }
        }

        mainViewModel.isRemember.observe(this){
            if(it){
                Toast.makeText(this, "자동 로그인이 설정되었습니다.", Toast.LENGTH_SHORT).show()
            }else {
                binding.cbRemember.isChecked = false
                Toast.makeText(this, "회원정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        mainViewModel.loginComplete.observe(this){
            if(it){
                val loginIntent = Intent(this, HomeActivity::class.java)
                startActivity(loginIntent)
                finish()
            }else {
                Toast.makeText(this, "회원정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginBtnSetting(){
        if(isName && isPassword){
            CoroutineScope(Dispatchers.Main).launch {
                binding.btnLogin.setBackgroundResource(R.drawable.login_btn_bg)
                binding.btnLogin.isEnabled = true
            }
        }else {
            CoroutineScope(Dispatchers.Main).launch {
                binding.btnLogin.setBackgroundResource(R.drawable.login_btn_gray_bg)
                binding.btnLogin.isEnabled = false
            }
        }
    }
}