package org.bedu.filmapp.ui.post_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.bedu.filmapp.R
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.ui.profile.ProfileViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PostDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var progressLinearProgressIndicator: LinearProgressIndicator
    private lateinit var imagePortedImageView: ImageView
    private lateinit var imageImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var createByTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var reviewsRecyclerView: RecyclerView

    private val viewModel by viewModels<PostDetailsViewModel>()

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
        val view = inflater.inflate(R.layout.fragment_post_details, container, false)
        progressLinearProgressIndicator = view.findViewById(R.id.progress_bar)
        imagePortedImageView = view.findViewById(R.id.image_ported_iv)
        imageImageView = view.findViewById(R.id.image_iv)
        nameTextView = view.findViewById(R.id.name_tv)
        createByTextView = view.findViewById(R.id.created_by_tv)
        yearTextView = view.findViewById(R.id.year_tv)
        durationTextView = view.findViewById(R.id.duration_tv)
        genderTextView = view.findViewById(R.id.gender_tv)
        descriptionTextView = view.findViewById(R.id.description_tv)
        reviewsRecyclerView = view.findViewById(R.id.post_reviews_rv)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch{
            viewModel.postDetailsResponse.collect() {
                when(it) {
                    Response.Loading -> {progressLinearProgressIndicator.visibility = View.VISIBLE}
                    is Response.Success -> {
                        Picasso.get().load(it.data?.imagePortedPost).into(imagePortedImageView)
                        Picasso.get().load(it.data?.imagePost).into(imageImageView)
                        nameTextView.text = it.data?.name
                        createByTextView.text = it.data?.createdBy
                        yearTextView.text = it.data?.year.toString()
                        durationTextView.text = it.data?.duration
                        genderTextView.text = it.data?.gender
                        descriptionTextView.text = it.data?.description
                        progressLinearProgressIndicator.visibility = View.GONE
                    }
                    is Response.Failure -> {
                        Toast.makeText(context, it.e.message ?: getString(R.string.auth_login_error), Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
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
         * @return A new instance of fragment PostDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PostDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}