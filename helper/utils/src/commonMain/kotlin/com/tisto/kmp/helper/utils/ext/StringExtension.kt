package com.tisto.kmp.helper.utils.ext

import kotlin.random.Random

fun String.remove(string: String): String = replace(string, "")

fun String.removeComma(): String = replace(",", "")

fun String.fixPhoneNumber(): String {
    val phone = this.remove("+")
    return when {
        take(1) == "0" -> "62${phone.substring(1, phone.length)}"
        take(2) == "62" -> phone
        isNullOrEmpty() -> phone
        else -> "62$phone"
    }
}

fun String?.uppercaseFirstChar(): String {
    val value = this?.lowercase()
    return value?.replaceFirstChar { it.uppercaseChar() } ?: ""
}

fun String.getYoutubeId(): String {
    return when {
        this.contains("youtu.be") -> this.split("/")[3]
        this.contains("youtube.com") -> this.substringAfter("watch?v=").substringBefore("&")
        else -> {
            this
        }
    }
}

//fun String.toRequestBody(): RequestBody {
//    return this.toRequestBody("text/plain".toMediaTypeOrNull())
//}
//
//fun Int.toRequestBody(): RequestBody {
//    return this.toString().toRequestBody("text/plain".toMediaTypeOrNull())
//}

fun String?.getInitial(): String {
    try {
        if (this.isNullOrEmpty()) return ""
        val array = this.split(" ")
        if (array.isEmpty()) return this
        var inisial = array[0].substring(0, 1)
        if (array.size > 1) inisial += array[1].substring(0, 1)
        return inisial.uppercase()
    } catch (e: Exception) {
        return "N"
    }
}

fun String?.toKFormat(): String {
    if (this == null) return ""
    return if (this.length > 4) {
        return when (this.length) {
            4 -> this.toRupiah(true).dropLast(2) + "K"
            in 5..6 -> this.dropLast(3) + "K"
            7 -> this.toRupiah(true).dropLast(6) + "M"
            in 8..9 -> this.dropLast(6) + "M"
            10 -> this.toRupiah(true).dropLast(6) + "M"
            in 11..100 -> this.dropLast(9).toRupiah(true) + "B"
            else -> this
        }
    } else this
}

fun String.searchQuery(): String {
    return "%$this%"
}

private val firstNames = listOf(
    "Agus", "Budi", "Citra", "Dedi", "Eko", "Fitri", "Gita", "Hari", "Indra", "Joko",
    "Kiki", "Lina", "Maya", "Nina", "Oka", "Putu", "Rian", "Susi", "Taufik", "Umar",
    "Vina", "Wahyu", "Yusuf", "Zahra", "Andi", "Bella", "Candra", "Dewi", "Edi", "Farhan",
    "Gilang", "Hendra", "Ika", "Joni", "Kurniawan", "Linda", "Meli", "Nadia", "Oni", "Pandu",
    "Rina", "Siti", "Toni", "Usman", "Viona", "Wira", "Yuli", "Zaki", "Adi", "Benny",
    "Cici", "Dian", "Endang", "Feri", "Gusman", "Hani", "Irfan", "Jaka", "Kartika", "Lutfi",
    "Mira", "Nino", "Olga", "Via", "Rendi", "Sandy", "Tirta", "Uli", "Vega", "Wulan",
    "Yogi", "Zulfi", "Asep", "Beni", "Dika", "Eva", "Faisal", "Guntur", "Heryanto", "Iwan",
    "Krisna", "Lala", "Oky", "Rani", "Sigit", "Tina", "Utami", "Vino", "Yulia", "Zainal"
)

private val lastNames = listOf(
    "Pratama",
    "Santoso",
    "Dewi",
    "Kusuma",
    "Saputra",
    "Ayu",
    "Sari",
    "Susanto",
    "Setiawan",
    "Riyadi",
    "Ananda",
    "Permata",
    "Puspita",
    "Wirawan",
    "Syahputra",
    "Susanti",
    "Hidayat",
    "Bakri",
    "Melinda",
    "Nugroho",
    "Mansur",
    "Putri",
    "Siregar",
    "Wijaya",
    "Lestari",
    "Supriyadi",
    "Ridwan",
    "Mahendra",
    "Gunawan",
    "Ramadhan",
    "Utami",
    "Maharani",
    "Amelia",
    "Andika",
    "Purnama",
    "Hakim",
    "Aditya",
    "Hartono",
    "Rahman",
    "Anggraini",
    "Akbar",
    "Widodo",
    "Septiani",
    "Hartati",
    "Abidin",
    "Fadhilah"
)

fun generateRandomName(withNumber: Boolean = false): String {
    val first = firstNames.random()
    val last = lastNames.random()
    val suffix = if (withNumber) randomInt(10, 100).toString() else "" // 10..99
    return "$first $last$suffix"
}

fun getRandomName(withNumber: Boolean = false): String {
    return generateRandomName(withNumber)
}

fun randomInt(from: Int, to: Int): Int {
    require(from < to) { "from must be < to" }
    return Random.nextInt(from, to) // to exclusive
}

fun String?.clearJsonString(): String {
    return this?.replace("\"{", "{")?.replace("}\"", "}")?.replace("\\", "") ?: ""
}

fun String?.equalText(string: String?): Boolean {
    return this?.lowercase()?.contains(string?.lowercase() ?: "") ?: false
}

fun String.translateJson(): String {
    return this.replace("\\u003d", "=")
}


fun String?.startWithZero(): String {
    var result = this
    if (this?.startsWith("62") == true) {
        result = "0" + result?.substring(2)
    }
    return result ?: ""
}

fun String.isEmailValid(): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return emailRegex.matches(this)
}


fun String?.shorten(maxLength: Int = 10): String {
    val displaySearch = if (this?.length.def(0) > maxLength) {
        this?.take(10) + "..."
    } else {
        this.def()
    }
    return displaySearch
}

fun String.ellipsis(maxLength: Int = 10) = shorten(maxLength)

fun String?.startWithZeroPhone(): String? {
    if (this.isNullOrEmpty()) return this
    return if (this.startsWith("62")) {
        "0${this.substring(2)}"
    } else {
        this
    }
}

fun String.removeTrailingCommaZero(): String {
    return if (endsWith(",0") || endsWith(".0")) {
        dropLast(2)
    } else {
        this
    }
}

fun String?.ifZero(default: String = ""): String {
    return if (this == "0" || this == "0.0" || this == "0,0" || this == "null" || this == null) default
    else this.removeTrailingCommaZero()
}

fun Double?.ifZero(default: String = ""): String {
    return this.toString().ifZero(default)
}

fun Int?.ifZero(default: String = ""): String {
    return this.toString().ifZero(default)
}

inline fun <T> T.ifCondition(condition: (T) -> Boolean, ifTrue: () -> T): T {
    return if (this != null && condition(this)) ifTrue() else this
}


// ====================================
// Random Phone Number (Indonesia)
// ====================================

private val indonesianPrefixes = listOf(
    "0811", "0812", "0813", "0814", "0815", "0816", "0817", "0818", "0819", // Telkomsel
    "0821", "0822", "0823", "0852", "0853",                                 // Telkomsel
    "0831", "0832", "0833", "0838",                                         // Axis
    "0855", "0856", "0857", "0858",                                         // Indosat
    "0814", "0815", "0816",                                                 // Indosat
    "0877", "0878", "0879",                                                 // XL
    "0817", "0818", "0819", "0859",                                         // XL
    "0881", "0882", "0883", "0884", "0885", "0886", "0887", "0888", "0889", // Smartfren
    "0895", "0896", "0897", "0898", "0899",                                 // Three
)

fun randomPhoneNumber(): String {
    val prefix = indonesianPrefixes.random()
    val remaining = 12 - prefix.length // total 12 digits (08xx-xxxx-xxxx)
    val suffix = (1..remaining).map { (0..9).random() }.joinToString("")
    return "$prefix$suffix"
}

/** Formatted version: 0812-3456-7890 */
fun randomPhoneNumberFormatted(): String {
    val raw = randomPhoneNumber()
    return "${raw.substring(0, 4)}-${raw.substring(4, 8)}-${raw.substring(8)}"
}


// ====================================
// Random Car Name + Series
// ====================================

private val carModels = listOf(
    "Toyota Avanza" to listOf("1.3 E", "1.3 G", "1.5 G", "1.5 Veloz"),
    "Toyota Innova" to listOf("2.0 G", "2.0 V", "2.4 G Diesel", "2.4 V Diesel", "Zenix HV"),
    "Toyota Rush" to listOf("1.5 S", "1.5 G", "1.5 GR Sport"),
    "Toyota Fortuner" to listOf("2.4 G", "2.4 VRZ", "2.8 GR Sport"),
    "Toyota Yaris" to listOf("1.5 S", "1.5 G", "1.5 TRD Sportivo"),
    "Toyota Calya" to listOf("1.2 E", "1.2 G"),
    "Honda Brio" to listOf("Satya E", "Satya S", "RS", "RS Urbanite"),
    "Honda Jazz" to listOf("1.5 S", "1.5 RS"),
    "Honda HR-V" to listOf("1.5 S", "1.5 E", "1.5 SE", "1.5 Turbo RS"),
    "Honda CR-V" to listOf("1.5 Turbo", "1.5 Turbo Prestige", "2.0 Hybrid"),
    "Honda City" to listOf("1.5 S", "1.5 E", "1.5 RS", "Hatchback RS"),
    "Honda Civic" to listOf("1.5 Turbo", "1.5 Turbo RS", "Type R"),
    "Daihatsu Xenia" to listOf("1.3 X", "1.3 R", "1.5 R", "1.5 R Deluxe"),
    "Daihatsu Ayla" to listOf("1.0 D", "1.0 X", "1.2 R", "1.2 R Deluxe"),
    "Daihatsu Terios" to listOf("1.5 X", "1.5 R", "1.5 R Deluxe"),
    "Daihatsu Sigra" to listOf("1.0 D", "1.0 M", "1.2 X", "1.2 R"),
    "Suzuki Ertiga" to listOf("GL", "GX", "Sport", "Hybrid SS"),
    "Suzuki XL7" to listOf("Zeta", "Beta", "Alpha"),
    "Suzuki APV" to listOf("Blind Van", "GA", "GE", "GX", "Arena"),
    "Suzuki Jimny" to listOf("4WD", "5-Door"),
    "Mitsubishi Xpander" to listOf("GLX", "GLS", "Ultimate", "Cross"),
    "Mitsubishi Pajero Sport" to listOf("GLX", "Exceed", "Dakar", "Dakar Ultimate"),
    "Mitsubishi Triton" to listOf("GLX", "GLS", "Exceed", "Athlete"),
    "Hyundai Creta" to listOf("Active", "Trend", "Prime", "Style"),
    "Hyundai Stargazer" to listOf("Active", "Trend", "Prime", "Style"),
    "Wuling Air EV" to listOf("Standard Range", "Long Range"),
    "Wuling Almaz" to listOf("1.5 SE", "1.5 LT", "RS Pro", "Hybrid"),
    "Nissan Livina" to listOf("1.5 EL", "1.5 VE", "1.5 VL"),
    "Nissan Kicks" to listOf("e-Power"),
    "Isuzu Panther" to listOf("LM Smart", "LS Turbo", "Grand Touring"),
    "Isuzu MU-X" to listOf("4x2", "4x4"),
)

fun randomCarName(): String {
    val (model, seriesList) = carModels.random()
    val series = seriesList.random()
    return "$model $series"
}

/** Returns Pair<model, series> if you need them separate */
fun randomCarPair(): Pair<String, String> {
    val (model, seriesList) = carModels.random()
    val series = seriesList.random()
    return model to series
}


// ====================================
// Random Color Name (ID & EN mix)
// ====================================

private val colorNames = listOf(
    // Indonesian
    "Merah", "Biru", "Hijau", "Kuning", "Putih", "Hitam", "Abu-Abu", "Coklat",
    "Oranye", "Ungu", "Merah Muda", "Emas", "Perak", "Krem", "Biru Tua",
    "Biru Muda", "Hijau Tua", "Hijau Muda", "Merah Maroon", "Biru Metalik",
    // English
    "Red", "Blue", "Green", "Yellow", "White", "Black", "Grey", "Silver",
    "Orange", "Purple", "Pink", "Gold", "Beige", "Navy Blue", "Dark Green",
    "Light Blue", "Midnight Black", "Pearl White", "Burgundy", "Titanium Grey",
    // Car-specific colors
    "Putih Mutiara", "Hitam Metalik", "Silver Metalik", "Merah Ferrari",
    "Biru Langit", "Bronze", "Champagne", "Graphite", "Lunar Silver",
    "Platinum White", "Attitude Black", "Celestial Blue",
)

fun randomColorName(): String {
    return colorNames.random()
}