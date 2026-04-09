package com.tisto.kmp.helper.utils.ext

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.gson.internal.Primitives
import com.google.gson.reflect.TypeToken
import com.tisto.kmp.helper.utils.constants.AppConstants.TIME_STAMP_FORMAT
import retrofit2.Response
import java.lang.Exception
import java.lang.reflect.Type
import java.net.URLEncoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.HashMap
import java.util.UUID.randomUUID

fun <T> T.toJson(): String {
    return Gson().toJson(this)
}

fun <T> String?.toModel(classOfT: Class<T>): T? {
    if (this == null) return null
    val obj = Gson().fromJson<Any>(this, classOfT as Type)
    return Primitives.wrap(classOfT).cast(obj) ?: null
}

fun <T, R> T.toModel(classOfT: Class<R>): R? {
    return this.toJson().toModel(classOfT)
}

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)

data class ErrorResponse(
    val code: String? = null,
    val message: String? = null
)

fun <T> Response<T>.getErrorBody(): ErrorResponse? {
    return try {
        this.errorBody()?.string()?.let { error ->
            Gson().fromJson<ErrorResponse>(error)
        }
    } catch (exception: Exception) {
        null
    }
}

fun <T, S> Response<T>.getErrorBody(classOfT: Class<S>): S? {
    return this.errorBody()?.string()?.toModel(classOfT)
}

fun <T> T.toMap(): Map<String, String> {
    val map: Map<String, String> = HashMap()
    return Gson().fromJson(this.toJson(), map.javaClass)
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isOffline(): Boolean {
    return !isOnline()
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Fragment.isOffline(): Boolean {
    return !requireActivity().isOnline()
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Fragment.isOnline(): Boolean {
    return requireActivity().isOnline()
}

fun String.toQRCode(dimensions: Int): Bitmap {
    val bitMatrix = QRCodeWriter().encode(this, BarcodeFormat.QR_CODE, dimensions, dimensions)
    val bitmap = createBitmap(dimensions, dimensions, Bitmap.Config.RGB_565)
    for (x in 0 until dimensions) {
        for (y in 0 until dimensions) {
            bitmap[x, y] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
        }
    }
    return bitmap
}

fun Context.setToolbar(view: Toolbar, title: String) {
    (this as AppCompatActivity).setSupportActionBar(view)
    this.supportActionBar!!.title = title
    this.supportActionBar!!.setDisplayShowHomeEnabled(true)
    this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
}

fun Context.verticalLayoutManager(): LinearLayoutManager {
    val layoutManager = LinearLayoutManager(this)
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    return layoutManager
}

fun Fragment.verticalLayoutManager(): LinearLayoutManager {
    val layoutManager = LinearLayoutManager(requireActivity())
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    return layoutManager
}

fun Context.horizontalLayoutManager(): LinearLayoutManager {
    val layoutManager = LinearLayoutManager(this)
    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
    return layoutManager
}

fun Fragment.horizontalLayoutManager(): LinearLayoutManager {
    val layoutManager = LinearLayoutManager(requireActivity())
    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
    return layoutManager
}

fun Fragment.hideKeyboard() {
    val imm: InputMethodManager =
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
}

fun Activity.setTransparantStatusBar() {
    val w: Window = this.window
    w.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
}

fun Activity.hidenKeyboard() {
    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

fun Context.setError(editText: EditText, message: String = "Kolom tidak boleh kosong") {
    editText.error = message
    editText.requestFocus()
}

fun Activity.isCameraPermissionGranted(context: Context, REQUEST_PERMISSION_CAMERA: Int): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        if (context.checkSelfPermission(Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            // Show the permission request
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION_CAMERA
            )
            false
        }
    } else {
        true
    }
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.openWhatsApp(phone: String, message: String = "Hallo admin,") {
    try {
        val packageManager: PackageManager = packageManager
        val i = Intent(Intent.ACTION_VIEW)
        val url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode(
            message,
            "UTF-8"
        )
        i.setPackage("com.whatsapp")
        i.data = Uri.parse(url)
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i)
        } else {
            openWhatsappAlternate(phone, message)
        }
    } catch (e: kotlin.Exception) {
        openWhatsappAlternate(phone, message)
    }
}

fun Context.openWhatsappAlternate(phone: String, message: String) {
    try {
        var toNumber = phone // contains spaces.
        toNumber = toNumber.replace("+", "").replace(" ", "")
        val sendIntent = Intent("android.intent.action.MAIN")
        sendIntent.putExtra("jid", "$toNumber@s.whatsapp.net")
        sendIntent.putExtra(Intent.EXTRA_TEXT, message)
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.setPackage("com.whatsapp")
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    } catch (e: kotlin.Exception) {
        openWhatsAppBusiness(phone, message)
    }
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.openWhatsAppBusiness(phone: String, message: String = "Hallo admin,") {
    val text = URLEncoder.encode(message, "UTF-8")
    try {
        val packageManager: PackageManager = packageManager
        val i = Intent(Intent.ACTION_VIEW)
        val url = "https://api.whatsapp.com/send?phone=$phone&text=$text"
        i.setPackage("com.whatsapp.w4b")
        i.data = Uri.parse(url)
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i)
        } else {
            openBrowser("https://wa.me/$phone?text=$text")
        }
    } catch (e: kotlin.Exception) {
        openBrowser("https://wa.me/$phone?text=$text")
    }
}


fun Context.openBrowser(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(browserIntent)
}

fun Context.openEmail(emailTo: String, text: String, subject: String = "") {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$emailTo"))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        println("Error open email: ${e.message}")
    }
}

fun Context.shareTo(message: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, message)
    intent.type = "text/plain"
    startActivity(Intent.createChooser(intent, "Share to.."))
}

fun Context.copyText(text: String, showToast: Boolean = true) {
    val copyManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val copyText = ClipData.newPlainText("text", text)
    copyManager.setPrimaryClip(copyText)

    if (showToast)
        Toast.makeText(this, "Text Berhasil di salin", Toast.LENGTH_LONG).show()
}

@RequiresPermission("android.permission.READ_PRIVILEGED_PHONE_STATE")
@SuppressLint("HardwareIds")
fun Context.getImei(): String {

    fun getDeviceID(): String {
        return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            .toString()
    }

    val imei: String
    val permisI = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
    imei = if (permisI == PackageManager.PERMISSION_GRANTED) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val telephonyManager =
                    this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                telephonyManager.imei
            } catch (e: kotlin.Exception) {
                getDeviceID()
            }
        } else getDeviceID()
    } else {
        "unknown"
    }

    return imei.uppercase()
}

fun TextView.setErrors(
    message: String = "Oopss, gagal memuat data!\nCoba lagi!",
    onClick: (() -> Unit?)? = null
) {
    this.text = message
    this.setOnClickListener {
        onClick?.invoke()
    }
}

@SuppressLint("SimpleDateFormat")
fun convertTglUTC(date: String, ygDimau: String): String {
    var hasil = ""
    val frmtlama = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val dateFormat = SimpleDateFormat(frmtlama)
    try {
        val dd = dateFormat.parse(date)
        // tambah 7 jam untuk indonesia
        val hour: Long = 3600 * 1000 // in milli-seconds.
        val newDate = Date(dd!!.time + 7 * hour)
        dateFormat.applyPattern(ygDimau)
        hasil = dateFormat.format(newDate)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return hasil
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toTimeStamp(format: String = TIME_STAMP_FORMAT): String {
    return DateTimeFormatter.ofPattern(format).format(this)
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isOnline(): Boolean {
    val context = this
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            println("Internet:NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            println("Internet:NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            println("Internet:NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    println("Internet:NetworkCapabilities.NO_INTERNET_CONNECTION")
    return false
}


inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

fun generateUUID(): String {
    return randomUUID().toString()
}

fun getFragmentWidthPercentage(percentage: Int): Int {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    return (rect.width() * percent).toInt()
}

fun visible() = View.VISIBLE
fun invisible() = View.INVISIBLE
fun gone() = View.GONE


