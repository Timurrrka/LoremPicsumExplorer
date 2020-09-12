package ru.musintimur.lorempicsumexplorer.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import ru.musintimur.lorempicsumexplorer.R
import ru.musintimur.lorempicsumexplorer.adapters.PhotoListAdapter
import ru.musintimur.lorempicsumexplorer.ui.MainContract
import ru.musintimur.lorempicsumexplorer.ui.ThrowableCallback
import java.lang.RuntimeException

class HomeFragment : Fragment() {

    private lateinit var errorListener: ThrowableCallback
    private lateinit var mainActivity: MainContract
    private lateinit var networkPhotosAdapter: PhotoListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is ThrowableCallback) {
            throw RuntimeException("Activity must implement ThrowableCallback.")
        }
        if (context !is MainContract) {
            throw RuntimeException("Activity must implement MainContract.")
        }
        errorListener = context
        mainActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        setupRecyclerView(homeViewModel)
        setupObservers(homeViewModel)
    }

    override fun onResume() {
        super.onResume()
        mainActivity.setOnSettingsItemClick { openSettings() }
        errorListener.onSuccess()
        if (::networkPhotosAdapter.isInitialized) networkPhotosAdapter.currentList?.dataSource?.invalidate()
    }

    private fun setupRecyclerView(vm: HomeViewModel) {
        networkPhotosAdapter = PhotoListAdapter()
        val gridLayoutManager = GridLayoutManager(requireContext(), mainActivity.calculateColumns())
        recyclerViewRandomPhotos.apply {
            layoutManager = gridLayoutManager
            adapter = networkPhotosAdapter
        }
        networkPhotosAdapter.onFavoriteClick = { photo, _ ->
            if (photo.isFavorite) vm.deleteFromFavorites(photo)
            else vm.saveToFavorites(photo, requireContext())
        }
    }

    private fun setupObservers(vm: HomeViewModel) {
        val owner = viewLifecycleOwner

        vm.run {
            getPhotos().observe(owner, Observer { photos ->
                networkPhotosAdapter.submitList(photos)
            })
            getLoading().observe(owner, Observer { isLoading ->
                val visibility =
                    if (isLoading) MainContract.ViewVisibility.VISIBLE
                    else MainContract.ViewVisibility.GONE
                mainActivity.setupProgressBarVisibility(visibility)
            })
            getError().observe(owner, Observer { error ->
                if (error == null)
                    errorListener.onSuccess()
                else
                    errorListener.onError(error)
            })
        }
    }

    private fun openSettings() {
        val action = HomeFragmentDirections.actionHomeToSettings()
        findNavController().navigate(action)
    }
}