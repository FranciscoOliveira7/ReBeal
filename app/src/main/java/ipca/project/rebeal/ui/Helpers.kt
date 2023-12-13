package ipca.project.rebeal.ui


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