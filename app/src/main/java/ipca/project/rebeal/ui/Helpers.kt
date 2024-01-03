package ipca.project.rebeal.ui


import android.text.TextUtils
import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Date

fun String.toDate() : Date{
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.parse(this)
}

fun Date.toShortDateTime() : String{
    val format = SimpleDateFormat("dd MMM - HH:mm")
    return format.format(this)
}

fun String.isPasswordValid()  : Boolean {
    return  this.length >= 6
}

fun  String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}