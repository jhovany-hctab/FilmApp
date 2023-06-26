package org.bedu.filmapp.ui.home

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.bedu.filmapp.R
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.ui.adapter.PostsAdapter
import org.bedu.filmapp.ui.adapter.UsersAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collect

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var imageProfile: ImageView
    private lateinit var usersRecycler: RecyclerView
    private lateinit var postPopularRecycler: RecyclerView
    private lateinit var postFavoriteRecycler: RecyclerView
    private lateinit var usersShimmer: ShimmerFrameLayout
    private lateinit var postPopShimmer: ShimmerFrameLayout
    private lateinit var postFavoriteShimmer: ShimmerFrameLayout
    private lateinit var sunnyText: TextView

    //Obeto que obtiene la localización
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val viewModel by viewModels<HomeViewModel>()


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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageProfile = view.findViewById(R.id.image_profile_iv)
        usersRecycler = view.findViewById(R.id.users_rv)
        postPopularRecycler = view.findViewById(R.id.post_popular_rv)
        postFavoriteRecycler = view.findViewById(R.id.post_favorite_rv)
        usersShimmer = view.findViewById(R.id.preview_user_sfl)
        postPopShimmer = view.findViewById(R.id.preview_pop_sfl)
        postFavoriteShimmer = view.findViewById(R.id.preview_favorite_sfl)
        sunnyText = view.findViewById(R.id.sunny_text)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLocation()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.sunnyTimeResponse.collect() {
                        sunnyText.text = "${it?.weather?.firstOrNull()?.description}"
                    }
                }
                launch {
                    viewModel.userDataResponse.collect() {
                        when(it) {
                            Response.Loading -> {}
                            is Response.Success -> {
                                Picasso.get().load(it.data?.imageProfile).into(imageProfile)
                                viewModel.getUsers()
                            }
                            is Response.Failure -> {
                                Toast.makeText(context, it.e.message ?: getString(R.string.auth_login_error), Toast.LENGTH_SHORT).show()
                            }
                            else -> {}
                        }
                    }
                }
                launch {
                    viewModel.usersResponse.collect() {
                        when(it) {
                            Response.Loading -> {
                                usersShimmer.visibility = View.VISIBLE
                                usersShimmer.startShimmer()
                            }
                            is Response.Success -> {
                                usersShimmer.stopShimmer()
                                usersShimmer.visibility = View.GONE
                                val adapter = UsersAdapter(it.data) {user ->
                                    if (user != null) {
                                        val bundle = bundleOf("userId" to user.id)
                                        findNavController().navigate(R.id.action_homeFragment_to_profileUsersFragment, bundle)
                                    }
                                }
                                usersRecycler.adapter = adapter
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
                            Response.Loading -> {
                                postPopShimmer.visibility = View.VISIBLE
                                postPopShimmer.startShimmer()
                            }
                            is Response.Success -> {
                                postPopShimmer.stopShimmer()
                                postPopShimmer.visibility = View.GONE
                                val adapter = PostsAdapter(it.data) { post ->
                                    if (post != null) {
                                        val bundle = bundleOf("postId" to post.id)
                                        findNavController().navigate(R.id.action_homeFragment_to_postDetailsFragment, bundle)
                                    }
                                }
                                postPopularRecycler.adapter = adapter
                                viewModel.getFavoritePosts()

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
                            Response.Loading -> {
                                postFavoriteShimmer.visibility = View.VISIBLE
                                postFavoriteShimmer.startShimmer()
                            }
                            is Response.Success -> {
                                postFavoriteShimmer.stopShimmer()
                                postFavoriteShimmer.visibility = View.GONE
                                val adapter = PostsAdapter(it.data) { post ->
                                    if (post != null) {
                                        val bundle = bundleOf("postId" to post.id)
                                        findNavController().navigate(R.id.action_homeFragment_to_postDetailsFragment, bundle)
                                    }
                                }
                                postFavoriteRecycler.adapter = adapter

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
    private fun checkGranted(permission: String): Boolean{
        return ActivityCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }
    private fun checkPermissions() =
        checkGranted(Manifest.permission.ACCESS_COARSE_LOCATION) &&
                checkGranted(Manifest.permission.ACCESS_FINE_LOCATION)

    //Pedir los permisos requeridos para que funcione la localización
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkPermissions()) { //verificamos si tenemos permisos
            if (isLocationEnabled()) { //localizamos sólo si el GPS está encendido

                mFusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
                    viewModel.sunnyTime(location?.latitude!!, location?.longitude!!)
                    //tvLatitude.text = location?.latitude.toString()
                    //tvLongitude.text = location?.longitude.toString()

                }
            }
        } else{
            //si no se tiene permiso, pedirlo
            requestPermissions()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        const val PERMISSION_ID = 33
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}