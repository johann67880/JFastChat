package com.johaljicar.jfastchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.johaljicar.jfastchat.databinding.ActivityMainBinding
import android.content.Intent
import android.net.Uri
import com.google.android.gms.ads.AdRequest


class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        loadAds()
    }

    private fun loadAds() {
        val adRequest = AdRequest.Builder().build()
        _binding.banner.loadAd(adRequest)
    }

    override fun onResume() {
        super.onResume()
        val countries = resources.getStringArray(R.array.countries)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, countries)
        _binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    fun openWhatsApp(view: View) {
        val countryCode = _binding.autoCompleteTextView.text
            .split("+")[1]
            .replace(")", "")
            .replace(" ", "")

        val url = "https://api.whatsapp.com/send?phone=${countryCode}${_binding.txtCellphoneNumber.text}"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}