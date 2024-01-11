package ipca.project.rebeal.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import ipca.project.rebeal.R
import java.io.ByteArrayOutputStream
import java.util.UUID

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.row_post)
    }


}