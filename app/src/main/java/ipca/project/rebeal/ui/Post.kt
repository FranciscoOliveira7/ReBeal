package ipca.project.rebeal.ui

import org.json.JSONObject
import java.util.Date

data class Post (
    val postId: String,
    var username : String,
    var description : String?,
    var urlToImage : String?,
    var data : Date,
    var likes : Int
)
