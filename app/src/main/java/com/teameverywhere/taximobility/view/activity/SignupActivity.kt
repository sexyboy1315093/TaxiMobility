package com.teameverywhere.taximobility.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.databinding.ActivitySignupBinding
import com.teameverywhere.taximobility.view.fragment.SignupFragment

//이름, 핸드폰, 택시운전자격증번호, 차번호, 차종
class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeFragment(SignupFragment())
    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}