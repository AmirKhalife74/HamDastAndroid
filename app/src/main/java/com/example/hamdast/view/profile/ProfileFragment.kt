package com.example.hamdast.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hamdast.R
import com.example.hamdast.data.models.settingsItem.SettingsModel
import com.example.hamdast.databinding.FragmentProfileBinding
import com.example.hamdast.view.profile.adapter.SettingsAdapter

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    lateinit var settingsMenuList: MutableList<SettingsModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        listen()
        observe()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    private fun init()
    {
       setRcSettings()
        binding.apply {

        }
    }

    private fun listen()
    {
        binding.apply {

        }
    }

    private fun observe()
    {
        binding.apply {

        }
    }


    private fun setSettingsMenu(){
        binding.apply {
            settingsMenuList = mutableListOf(
                SettingsModel(title = "حساب کاربری", subTitle = "ویرایش عکس٬ ویرایش نام کاربری ...", imgSrc = R.drawable.ic_profile) {goToProfile()},
                SettingsModel(title = "تنظیمات کلی", subTitle = "رنگ اصلی٬ سایز فونت ...", imgSrc = R.drawable.ic_setting) {goToSettings()},
                SettingsModel(title = "هم دست", subTitle = "تنظیمات هوش مصنوعی", imgSrc = R.drawable.ic_ai) {goToHamDastSettings()},
                SettingsModel(title = "اطلاع رسانی", subTitle = "تنظیمات نوتیفیکیشن", imgSrc = R.drawable.ic_bell) {goToNotificationSettings()},
                SettingsModel(title = "حریم شخصی", subTitle = "رمز عبور٬ ورود با اثر انگشت", imgSrc = R.drawable.ic_security) {goToPrivacySettings()},
                SettingsModel(title = "درباره ما", subTitle = "درباره توسعه دهنده", imgSrc = R.drawable.ic_about_us) {goToAboutUs()},
            )

        }
    }

    private fun setRcSettings(){
        binding.apply {
            setSettingsMenu()
            val adapter = SettingsAdapter(items = settingsMenuList, context = requireContext())
            rcSettings.layoutManager = LinearLayoutManager(requireContext())
            rcSettings.adapter = adapter
        }
    }


    private fun goToProfile(){}
    private fun goToNotificationSettings(){}
    private fun goToAboutUs(){}
    private fun goToSettings(){}
    private fun goToPrivacySettings(){}
    private fun goToHamDastSettings(){}



}