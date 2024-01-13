package ipca.project.rebeal.ui.addpost

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import ipca.project.rebeal.R
import ipca.project.rebeal.databinding.FragmentAddPostBinding
import ipca.project.rebeal.databinding.FragmentHomeBinding
import ipca.project.rebeal.databinding.FragmentProfileBinding
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddPostFragment : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSelectImage.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    fun storeBitmap(bitmap: Bitmap,description: String, isMap: Boolean, callback: (filename: String?) -> Unit) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val filename = (if (isMap) "map_" else "") + UUID.randomUUID().toString() + ".jpg"
        val photoRef = storageRef.child("Posts/${filename}")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val data = baos.toByteArray()

        val uploadTask = photoRef.putBytes(data)
        uploadTask.addOnFailureListener {
            callback.invoke(null)
        }.addOnSuccessListener { taskSnapshot ->
            photoRef.downloadUrl.addOnSuccessListener { uri ->
                savePostToFirestore(uri.toString(), description, callback)
            }.addOnFailureListener {
                callback.invoke(null)
            }
        }
    }

    private fun savePostToFirestore(urlImage: String, description: String, callback: (filename: String?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val db = FirebaseFirestore.getInstance()

            getUsernameFromUid(uid) { username ->
                if (username != null) {
                    val post = hashMapOf(
                        "uid" to uid,
                        "username" to username,
                        "imageUrl" to urlImage,
                        "description" to description,
                        "timestamp" to System.currentTimeMillis()
                    )

                    db.collection("posts")
                        .add(post)
                        .addOnSuccessListener { documentReference ->
                            callback.invoke(documentReference.id)
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Erro ao adicionar documento", e)
                            callback.invoke(null)
                        }
                } else {
                    callback.invoke(null)
                }
            }
        } else {
            callback.invoke(null)
        }
    }

    private fun getUsernameFromUid(uid: String, callback: (username: String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { userDocument ->
                val username = userDocument.getString("username")
                callback.invoke(username)
            }
            .addOnFailureListener {
                callback.invoke(null)
            }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let {
                Glide.with(requireContext())
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(binding.imageView2)

                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                val description = binding.editTextTextMultiLine2.text.toString()

                Log.d("Description", "Description: $description")

                storeBitmap(bitmap, description, false) { filename ->
                    if (filename != null) {
                        Log.d("Upload", "Image uploaded successfully. Filename: $filename")
                    } else {
                        Log.e("Upload", "Image upload failed.")
                    }
                }
            }
        }
    }
}