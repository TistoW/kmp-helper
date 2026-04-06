package com.tisto.kmp.helper.utils.ext

import android.graphics.*
import android.os.*
import android.util.Log
import java.net.URL
import androidx.core.graphics.createBitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

const val dateFormat = "yyyy-MM-dd"

fun logs(message: String? = "message") {
    Log.d("RESPONSE", message.def())
}

fun logs(tag: String?, message: String?) {
    Log.d(tag, message ?: "message")
}

fun logs(tag: String?, vararg str: String) {
    var message = ""
    for ((i, s) in str.withIndex()) {
        message += if (i == str.size - 1) s else "$s - "
    }
    Log.d(tag, message)
}

fun loge(message: String?) {
    Log.e("ERROR", message ?: "message")
}

fun loge(tag: String, message: String?) {
    Log.e(tag, message ?: "message")
}

fun longLogs(longString: String, tag: String = "RESPONS") {
    val maxLogSize = 3000
    for (i in 0..longString.length / maxLogSize) {
        val start = i * maxLogSize
        var end = (i + 1) * maxLogSize
        end = if (end > longString.length) longString.length else end
        logs(tag, longString.substring(start, end))
    }
}

fun delayFunction(r: Runnable, duration: Long = 300) {
    Handler(Looper.getMainLooper()).postDelayed(r, duration)
}

fun checkPrefix(mPhone: String): String {
    when (mPhone.substring(0, 4)) {
        "0831", "0832", "0833", "0835", "0836", "0837", "0838", "0839" -> {
            return "Axis"
        }

        "0817", "0818", "0819", "0859", "0877", "0878", "0879" -> {
            return "XL"
        }

        "0811", "0812", "0813", "0821", "0822", "0823", "0851", "0852", "0853", "0854" -> {
            return "Telkomsel"
        }

        "0814", "0815", "0816", "0855", "0856", "0857", "0858" -> {
            return "Indosat"
        }

        "0895", "0896", "0897", "0898", "0899" -> {
            return "Tri"
        }

        "0881", "0882", "0883", "0884", "0885", "0886", "0887", "0888", "0889" -> {
            return "Smartfren"
        }

        "0828" -> {
            return "Ceria"
        }

        else -> {
            return "non"
        }
    }
}

fun Int?.def(v: Int = 0): Int {
    return this ?: v
}

fun String?.def(v: String = ""): String {
    return this ?: v
}

fun Double?.def(v: Double = 0.0): Double {
    return this ?: v
}

fun Long?.def(v: Long = 0L): Long {
    return this ?: v
}

fun Bitmap.toBlackAndWhite(threshold: Int = 128): Bitmap {
    val w = width
    val h = height

    // config bisa null (mis. dari decodeStream tertentu), pakai fallback yang aman
    val outConfig = this.config ?: Bitmap.Config.ARGB_8888
    val out = createBitmap(w, h, outConfig)
    out.density = this.density

    val pixels = IntArray(w * h)
    getPixels(pixels, 0, w, 0, 0, w, h)

    // pakai bobot integer agar lebih cepat dan stabil
    // gray ~= (r*299 + g*587 + b*114) / 1000
    for (i in pixels.indices) {
        val p = pixels[i]
        val a = Color.alpha(p)
        val r = Color.red(p)
        val g = Color.green(p)
        val b = Color.blue(p)

        val gray = (r * 299 + g * 587 + b * 114) / 1000
        val bw = if (gray > threshold) 255 else 0
        pixels[i] = Color.argb(a, bw, bw, bw)
    }

    out.setPixels(pixels, 0, w, 0, 0, w, h)
    return out
}

fun Bitmap.addPaddingLeft(paddingLeft: Int): Bitmap {
    val outputBitmap = Bitmap.createBitmap(width + paddingLeft, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(outputBitmap)
    canvas.drawColor(Color.WHITE)
    canvas.drawBitmap(this, paddingLeft.toFloat(), 0f, null)
    return outputBitmap
}

fun String.convertUrlToBitmap(): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val url = URL(this)
        val connection = url.openConnection()
        connection.doInput = true
        connection.connect()
        val input = connection.getInputStream()
        bitmap = BitmapFactory.decodeStream(input)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bitmap
}

fun Int?.int(): Int {
    return this ?: 0
}

fun String?.string(): String {
    return this ?: ""
}

fun <T> T?.isNull(): Boolean {
    return this == null
}

fun <T> T?.isNotNull(): Boolean {
    return this != null
}

fun <T> LiveData<T>.observeOnce(
    lifecycleOwner: LifecycleOwner,
    observer: Observer<T>
) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(value: T) {
            observer.onChanged(value)
            removeObserver(this)
        }
    })
}


fun <T> List<T>.insertAt(value: T, index: Int = 1): List<T> {
    return when {
        index <= 0 -> listOf(value) + this
        index >= size -> this + value
        else -> take(index) + value + drop(index)
    }
}
