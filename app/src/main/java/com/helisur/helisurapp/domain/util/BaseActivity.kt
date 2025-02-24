package com.helisur.helisurapp.domain.util

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun next(activity: Class<*>?, bundle: Bundle?) {
        val intent = Intent(applicationContext, activity)
        if (bundle != null) {
            intent.putExtra("extra", bundle)
        }
        startActivity(intent)
    }

    fun isTablet():Boolean
    {
        var tablet = false
  //      val configuration = LocalConfiguration.current
    //    val expanded = configuration.screenWidthDp > 840

        return  tablet
    }






    fun isOnline(): Boolean {
        try {
            val p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com")
            val returnVal = p1.waitFor()
            val reachable = (returnVal == 0)
            return reachable
        } catch (e: Exception) {
            //  e.printStackTrace();
        }
        return false
    }


    fun showErrorDialog(message: String?) {
        val bundle = Bundle()
        bundle.putString("errorMessage", message)
        val df: ErrorMessageDialog = ErrorMessageDialog()
        df.setArguments(bundle)
        df.show(supportFragmentManager, "")
    }



}