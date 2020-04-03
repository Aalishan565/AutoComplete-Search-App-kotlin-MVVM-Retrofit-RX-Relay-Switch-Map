package com.example.searchaddapp.apputils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

class AppUtils {
    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun showToastMessage(
            context: Context,
            message: String,
            duration: Int = Toast.LENGTH_SHORT
        ) {
            Toast.makeText(context, message, duration).show()
        }


        fun hideKeyboard(
            mContext: Context,
            v: View?
        ) {
            if (v != null) {
                val inputManager = mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }

    }
}