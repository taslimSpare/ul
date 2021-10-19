package app.taslimoseni.abcdef.utils

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import app.taslimoseni.abcdef.R
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

object SimpleFunctions {

    fun Activity.showSnackBar(message: String) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackbar.view
        snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
            maxLines = 3
        }
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.app_gray))
        snackbar.show()
    }

    fun NavController.tryNavigate(@IdRes resId: Int, args: Bundle? = null) = try {
        navigate(resId, args)
    } catch (e: Exception) {
        Timber.e(e)
    }

    @Suppress("DEPRECATION")
    fun String.formatDateToHumanReadableTime(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant.parse(this).atZone(ZoneOffset.UTC).toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"))
        } else return SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(this))
    }

}