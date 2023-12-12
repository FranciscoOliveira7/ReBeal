package ipca.project.rebeal.ui

import org.json.JSONObject
import java.util.Date

data class Post (
    var descricao : String?,
    var url : String,
    var urlToImage : String?,
    var data : Date,
 ) {
    companion object{
        fun fromJson ( jsonObject: JSONObject) : Post {
            val descricao = jsonObject["descricao"] as String
            val url = jsonObject["url"] as String
            val urlToImage = jsonObject ["urlToImage"] as String
            val data = (jsonObject["data"] as String).toDate()

            return Post(
                descricao,
                url,
                urlToImage,
                data
            )
        }
    }
}

