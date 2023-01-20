package com.johaljicar.jfastchat

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.johaljicar.jfastchat.databinding.ActivityMainBinding


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

    private fun ValidateInputs(): Boolean {
        if(_binding.autoCompleteTextView.text.toString().isEmpty()) {
            Toast.makeText(this, R.string.emptyCountry, Toast.LENGTH_LONG).show()
            return false;
        }

        if(_binding.txtCellphoneNumber.text.toString().isEmpty()) {
            Toast.makeText(this, R.string.emptyNumber, Toast.LENGTH_LONG).show()
            return false;
        }

        return true;
    }

    private fun getCountryCode(): String {
        return _binding.autoCompleteTextView.text
            .split("+")[1]
            .replace(")", "")
            .replace(" ", "")
    }

    private fun getWhatsAppUri(): String {
        val countryCode = this.getCountryCode()
        return "https://api.whatsapp.com/send?phone=${countryCode}${_binding.txtCellphoneNumber.text}"
    }

    private fun isAppInstalled(packageName : String): Boolean {
        return try {
            val packageManager = this.packageManager

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            }
            else {
                @Suppress("DEPRECATION") packageManager.getPackageInfo(packageName, 0)
            }

            true
        } catch(e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun openSpecificPackageName(packageName: String) {
        val whatsAppIntent = Intent(Intent.ACTION_VIEW)
        whatsAppIntent.setPackage(packageName)

        val url = getWhatsAppUri()
        whatsAppIntent.data = Uri.parse(url)
        startActivity(whatsAppIntent)
    }

    fun openWhatsApp(view: View) {
        if(!this.ValidateInputs())
            return;

        val whatsAppPackageName = "com.whatsapp"

        if(isAppInstalled(whatsAppPackageName)) {
            openSpecificPackageName(whatsAppPackageName)
        }
        else {
            Toast.makeText(this, R.string.whatsAppNotInstalled, Toast.LENGTH_LONG).show()
        }
    }

    fun openWhatsAppBusiness(view: View) {
        if(!this.ValidateInputs())
            return;

        val whatsAppBusinessPackageName = "com.whatsapp.w4b"

        if(isAppInstalled(whatsAppBusinessPackageName)) {
            openSpecificPackageName(whatsAppBusinessPackageName)
        }
        else {
            Toast.makeText(this, R.string.whatsAppBusinessNotInstalled, Toast.LENGTH_LONG).show()
        }
    }
}