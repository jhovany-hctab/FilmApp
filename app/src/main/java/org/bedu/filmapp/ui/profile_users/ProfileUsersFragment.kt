package org.bedu.filmapp.ui.profile_users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.bedu.filmapp.R
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.ui.adapter.PostsAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileUsersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProfileUsersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var portedImageView: ImageView
    private lateinit var profileImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var myFavoriteRecyclerView: RecyclerView
    private lateinit var myActivityRecyclerView: RecyclerView
    private lateinit var previewFavoriteShimmerFrameLayout: ShimmerFrameLayout
    private lateinit var previewActivityShimmerFrameLayout: ShimmerFrameLayout
    private lateinit var followCardView: CardView
    private lateinit var followDeleteCardView: CardView
    private lateinit var followRelativeLayout: RelativeLayout
    private lateinit var followDeleteRelativeLayout: RelativeLayout

    private val viewModel by viewModels<ProfileUsersViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        portedImageView = view.findViewById(R.id.image_ported_iv)
        profileImageView = view.findViewById(R.id.image_profile_iv)
        nameTextView = view.findViewById(R.id.name_tv)
        descriptionTextView = view.findViewById(R.id.description_tv)
        myFavoriteRecyclerView = view.findViewById(R.id.post_favorite_rv)
        myActivityRecyclerView = view.findViewById(R.id.activity_rv)
        previewFavoriteShimmerFrameLayout = view.findViewById(R.id.preview_favorite_sfl)
        previewActivityShimmerFrameLayout = view.findViewById(R.id.preview_activity_sfl)
        followCardView = view.findViewById(R.id.follow_btn_cv)
        followDeleteCardView = view.findViewById(R.id.follow_delete_btn_cv)
        followRelativeLayout = view.findViewById(R.id.follow_btn_rl)
        followDeleteRelativeLayout = view.findViewById(R.id.follow_delete_btn_rl)

        followRelativeLayout.setOnClickListener { viewModel.follow() }
        followDeleteRelativeLayout.setOnClickListener { viewModel.followDelete() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.userDataResponse.collect() {
                        when(it) {
                            Response.Loading -> {}
                            is Response.Success -> {
                                if (it.data?.imagePortedProfile != null && it.data?.imagePortedProfile != ""){
                                    Picasso.get().load(it.data?.imagePortedProfile).into(portedImageView)
                                }
                                Picasso.get().load(it.data?.imageProfile).into(profileImageView)
                                nameTextView.text = it.data?.username
                                descriptionTextView.text = it.data?.description
                                if (it.data?.follow != null){
                                    if (it.data?.follow?.contains(viewModel.user) == true) {
                                        followDeleteCardView.visibility = View.VISIBLE
                                        followCardView.visibility = View.GONE
                                    } else {
                                        followDeleteCardView.visibility = View.GONE
                                        followCardView.visibility = View.VISIBLE
                                    }
                                } else {followCardView.visibility = View.VISIBLE}

                                viewModel.getFavoritePosts(it.data?.id)
                            }
                            is Response.Failure -> {
                                Toast.makeText(context, it.e.message ?: getString(R.string.auth_login_error), Toast.LENGTH_SHORT).show()
                            }
                            else -> {}
                        }
                    }
                }
                launch {
                    viewModel.postFavoriteResponse.collect() {
                        when(it) {
                            Response.Loading -> {}
                            is Response.Success -> {
                                previewFavoriteShimmerFrameLayout.stopShimmer()
                                previewFavoriteShimmerFrameLayout.visibility = View.GONE
                                val adapter = PostsAdapter(it.data) { post ->
                                    if (post != null) {
                                        val bundle = bundleOf("postId" to post.id)
                                        findNavController().navigate(R.id.action_profileUsersFragment_to_postDetailsFragment, bundle)
                                    }
                                }
                                myFavoriteRecyclerView.adapter = adapter
                                viewModel.getPosts()

                            }
                            is Response.Failure -> {
                                Toast.makeText(context, it.e.message ?: getString(R.string.auth_login_error), Toast.LENGTH_SHORT).show()
                            }
                            else -> {}
                        }
                    }
                }
                launch {
                    viewModel.postPopResponse.collect() {
                        when(it) {
                            Response.Loading -> {}
                            is Response.Success -> {
                                previewActivityShimmerFrameLayout.stopShimmer()
                                previewActivityShimmerFrameLayout.visibility = View.GONE
                                val adapter = PostsAdapter(it.data) { post ->
                                    if (post != null) {
                                        val bundle = bundleOf("postId" to post.id)
                                        findNavController().navigate(R.id.action_profileUsersFragment_to_postDetailsFragment, bundle)
                                    }
                                }
                                myActivityRecyclerView.adapter = adapter
                            }
                            is Response.Failure -> {
                                Toast.makeText(context, it.e.message ?: getString(R.string.auth_login_error), Toast.LENGTH_SHORT).show()
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileUsersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileUsersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}