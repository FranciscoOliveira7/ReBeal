package ipca.project.rebeal.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ipca.project.rebeal.LoginActivity
import ipca.project.rebeal.MainActivity
import ipca.project.rebeal.R
import ipca.project.rebeal.databinding.FragmentProfileBinding
import ipca.project.rebeal.databinding.LoginProfileBinding
import ipca.project.rebeal.ui.Post
import ipca.project.rebeal.ui.toShortDateTime
import java.util.Date
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    val posts: MutableList<Post> = mutableListOf()
    val postsAdapter = PostsListAdapter()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.GridViewProfilePosts.adapter = postsAdapter

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

        if (auth.currentUser == null) {
            lifecycleScope.launch(Dispatchers.IO) {
                Thread.sleep(1000L)
                withContext(Dispatchers.Main) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        } else {
            val uid = auth.currentUser?.uid
            val userDocRef = firestore.collection("users").document(uid!!)
            userDocRef.get().addOnSuccessListener { documentSnapshot ->
                val username = documentSnapshot.getString("username")
                binding.Utilizador.text = username
            }
            val userPostsRef = firestore.collection("posts").whereEqualTo("uid", uid)

            userPostsRef.get().addOnSuccessListener { querySnapshot ->
                posts.clear()

                for (document in querySnapshot) {
                    val descricao = document.getString("description")
                    val urlToImage = document.getString("imageUrl")

                    val post = Post("?", descricao, urlToImage, Date())
                    posts.add(post)
                }

                postsAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class PostsListAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return posts.size
        }

        override fun getItem(position: Int): Any {
            return posts[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.cell_post,parent,false)
            val textViewDescription = rootView.findViewById<TextView>(R.id.textViewGridTitle)
            val imageView = rootView.findViewById<ImageView>(R.id.imageViewPost)

            textViewDescription.text = posts[position].description

            Glide.with(requireContext())
                .load(posts[position].urlToImage)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView)


            return rootView
        }

    }
}