package com.example.teamproject.setting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teamproject.R
import com.example.teamproject.databinding.ActivitySettingBinding
import com.example.teamproject.userinfo.UserInfoDBHelper
import com.google.android.material.tabs.TabLayoutMediator

/*사용자 설정*/
// 설정에는 두가지 메뉴가 존재.
// 캘린더 커스터마이징, 사용자 정보 변경.

// 구현은 Fragment 사용.
// Adapter 이름은 SettingFragmentAdapter
// Fragment 이름은 CustomizingFragment, UserInfoFragment (default 는 customizing)

// id는 Activity 이름, 역할을 추가한다.
// tablayoutSetting, viewPagerSetting

class SettingActivity : AppCompatActivity() {

    lateinit var userInfoDBHelper: UserInfoDBHelper

    lateinit var settingbinding: ActivitySettingBinding
    val settingFragText = arrayListOf("User Info","Customizing")
    val settingFragIcon = arrayListOf(
        R.drawable.ic_baseline_people_24,
        R.drawable.ic_baseline_brush_24
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingbinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(settingbinding.root)
        init()
    }

    private fun init() {
        userInfoDBHelper = UserInfoDBHelper(this)
        settingbinding.viewPagerSetting.adapter = SettingFragmentAdapter(this)
        TabLayoutMediator(
            settingbinding.tablayoutSetting,
            settingbinding.viewPagerSetting
        ) { tab, position ->
            tab.text = settingFragText[position]
            tab.setIcon(settingFragIcon[position])
        }.attach()
    }

    override fun onBackPressed() {
        if(userInfoDBHelper.findUserInfo("age")!="default" &&
            userInfoDBHelper.findUserInfo("age")!="" &&
            userInfoDBHelper.findUserInfo("gender")!="default" &&
            userInfoDBHelper.findUserInfo("height")!="default" &&
            userInfoDBHelper.findUserInfo("height")!="" &&
            userInfoDBHelper.findUserInfo("weight")!="default" &&
            userInfoDBHelper.findUserInfo("weight")!="" &&
            userInfoDBHelper.findUserInfo("goal")!="default") {
            super.onBackPressed()
        }else{
            Toast.makeText(this,"사용자 정보를 입력하세요", Toast.LENGTH_SHORT).show()
        }
    }
}