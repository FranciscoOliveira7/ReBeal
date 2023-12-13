package ipca.project.rebeal.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ipca.project.rebeal.R
import ipca.project.rebeal.databinding.FragmentHomeBinding
import ipca.project.rebeal.ui.Post
import ipca.project.rebeal.ui.toShortDateTime
import java.util.Date

class HomeFragment : Fragment() {

    var posts : List<Post> = arrayListOf(
        Post("Artur", "O Rui é Gay", "https://www.youtube.com/watch?v=dQw4w9WgXcQ", "https://en.wikipedia.org/wiki/African_wild_dog", Date())
    )
    val postsAdapter = PostsListAdapter()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ListViewPosts.adapter = postsAdapter

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
            val rootView = layoutInflater.inflate(R.layout.row_post,parent,false)
            val textViewUserName = rootView.findViewById<TextView>(R.id.UserNameID)
            val textViewDescription = rootView.findViewById<TextView>(R.id.descricaoID)
            val textViewDate = rootView.findViewById<TextView>(R.id.dataID)
            val imageView = rootView.findViewById<ImageView>(R.id.imageView)

            textViewUserName.text = posts[position].username
            textViewDescription.text = posts[position].descricao
            textViewDate.text = posts[position].data.toShortDateTime()




            return rootView
        }

    }
}