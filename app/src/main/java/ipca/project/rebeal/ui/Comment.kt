package ipca.project.rebeal.ui

import org.json.JSONObject
import java.util.Date

data class Comment (
    var username : String,
    var description : String?,
    var urlToImage : String?,
    var data : Date,
) {
    companion object{
        fun fromJson ( jsonObject: JSONObject) : Comment {
            val username = jsonObject["username"] as String
            val description = jsonObject["description"] as String
            val urlToImage = jsonObject ["urlToImage"] as String
            val data = (jsonObject["data"] as String).toDate()

            return Comment(
                username,
                description,
                urlToImage,
                data
            )
        }
    }
}
