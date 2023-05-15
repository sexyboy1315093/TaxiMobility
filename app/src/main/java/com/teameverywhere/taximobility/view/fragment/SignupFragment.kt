package com.teameverywhere.taximobility.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.teameverywhere.taximobility.MainActivity
import com.teameverywhere.taximobility.MyApplication
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.FragmentSignupBinding
import com.teameverywhere.taximobility.view.activity.HomeActivity
import com.teameverywhere.taximobility.view.dialog.CarAddDialog
import com.teameverywhere.taximobility.viewmodel.MainViewModel
import com.teameverywhere.taximobility.viewmodel.SignupViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val signupViewModel: SignupViewModel by viewModels()

    companion object {
        const val TAG = "SignupFragment"
    }

    var isUserName = false
    var isUserPhone = false
    var isUserLicense = false
    var isUserPassword = false

    var name = ""
    var phone = ""
    var license = ""
    var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        observeViewModel()
    }

    private fun bindViews() = with(binding){
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                name = s.toString()
                signupViewModel.sendUserName(s.toString())
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                phone = s.toString()
                signupViewModel.sendUserPhone(s.toString())
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        etLicense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                license = s.toString()
                signupViewModel.sendUserLicense(s.toString())
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                password = s.toString()
                signupViewModel.sendUserPassword(s.toString())
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        btnSignup.setOnClickListener {
            signupViewModel.signUp(name, phone, license, password)
        }

        txtLogin.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        signupViewModel.userName.observe(viewLifecycleOwner){
            isUserName = it != ""
            signupBtnSetting()
        }

        signupViewModel.userPhone.observe(viewLifecycleOwner){
            isUserPhone = it != ""
            signupBtnSetting()
        }

        signupViewModel.userLicense.observe(viewLifecycleOwner){
            isUserLicense = it != ""
            signupBtnSetting()
        }

        signupViewModel.userPassword.observe(viewLifecycleOwner){
            isUserPassword = it != ""
            signupBtnSetting()
        }

        signupViewModel.loading.observe(viewLifecycleOwner){ isLoading ->
            isLoading?.let {
                if(isLoading){
                    binding.progressbar.visibility = View.VISIBLE
                }else {
                    binding.progressbar.visibility = View.GONE
                }
            }
        }

        signupViewModel.loginComplete.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(requireContext(), "회원가입이 정상 처리 되었습니다.", Toast.LENGTH_SHORT).show()
                val loginIntent = Intent(requireContext(), MainActivity::class.java)
                startActivity(loginIntent)
            }else {
                Toast.makeText(requireContext(), "입력하신 정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signupBtnSetting() = with(binding){
        if(isUserName && isUserPhone && isUserLicense && isUserPassword){
            CoroutineScope(Dispatchers.Main).launch {
                btnSignup.setBackgroundResource(R.drawable.login_btn_bg)
                btnSignup.isEnabled = true
            }
        }else {
            CoroutineScope(Dispatchers.Main).launch {
                btnSignup.setBackgroundResource(R.drawable.login_btn_gray_bg)
                btnSignup.isEnabled = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}