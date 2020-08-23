package ru.musintimur.lorempicsumexplorer.ui.favorite

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
import kotlinx.android.synthetic.main.fragment_favorite.*
import ru.musintimur.lorempicsumexplorer.R
import ru.musintimur.lorempicsumexplorer.adapters.PhotoListAdapter
import ru.musintimur.lorempicsumexplorer.ui.MainContract
import ru.musintimur.lorempicsumexplorer.ui.ThrowableCallback
import java.lang.RuntimeException

class FavoriteFragment : Fragment() {

    private lateinit var errorListener: ThrowableCallback
    private lateinit var mainActivity: MainContract
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var roomPhotosAdapter: PhotoListAdapter

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
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favoriteViewModel =
            ViewModelProvider(this).get(FavoriteViewModel::class.java)

        setupRecyclerView()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        mainActivity.setOnSettingsItemClick { openSettings() }
        errorListener.onSuccess()
    }

    private fun setupRecyclerView() {
        roomPhotosAdapter = PhotoListAdapter()
        recyclerViewFavoritePhotos.apply {
            layoutManager = GridLayoutManager(requireContext(), mainActivity.calculateColumns())
            adapter = roomPhotosAdapter
        }
        roomPhotosAdapter.onFavoriteClick = { photo, position ->
                favoriteViewModel.deleteFromFavorites(photo)
                roomPhotosAdapter.notifyItemRemoved(position)
        }
    }

    private fun setupObservers() {
        val owner = viewLifecycleOwner

        favoriteViewModel.run {
            getPhotos().observe(owner, Observer { photos ->
                roomPhotosAdapter.submitList(photos)
            })
            getLoading().observe(owner, Observer { isLoading ->
                val visibility =
                    if (isLoading) MainContract.ViewVisibility.VISIBLE
                    else MainContract.ViewVisibility.GONE
                mainActivity.setupProgressBarVisibility(visibility)
            })
            isDeleted().observe(owner, Observer { isDeleted ->
                if (isDeleted) roomPhotosAdapter.currentList?.dataSource?.invalidate()
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
        val action = FavoriteFragmentDirections.actionFavoritesToSettings()
        findNavController().navigate(action)
    }
}