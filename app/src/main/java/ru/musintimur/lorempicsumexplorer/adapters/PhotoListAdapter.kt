package ru.musintimur.lorempicsumexplorer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.*
import kotlinx.android.synthetic.main.item_photo.view.*
import ru.musintimur.lorempicsumexplorer.R
import ru.musintimur.lorempicsumexplorer.room.photo.Photo
import ru.musintimur.lorempicsumexplorer.room.photo.PhotoDiffCallback

class PhotoListAdapter :
    PagedListAdapter<Photo, PhotoListAdapter.Companion.PhotosViewHolder>(PhotoDiffCallback()) {

    companion object {
        class PhotosViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }

    var onFavoriteClick: ((Photo, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotosViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        getItem(position)?.let { photo ->
            holder.itemView.apply {
                textViewAuthor.text = photo.author

                Picasso.get()
                    .load(photo.downloadUrl)
                    .placeholder(R.drawable.ic_insert_photo_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(imagePhoto)

                imageViewAddToFavorites.setImageDrawable(context.getDrawable(chooseDrawableId(photo.isFavorite)))

                imageViewAddToFavorites.setOnClickListener {
                    imageViewAddToFavorites.setImageDrawable(context.getDrawable(chooseDrawableId(!photo.isFavorite)))
                    onFavoriteClick?.invoke(photo, position)
                }
            }
        }
    }

    private fun chooseDrawableId(isFavorite: Boolean): Int =
        if (isFavorite) R.drawable.ic_favorite_black_24dp
        else R.drawable.ic_favorite_border_black_24dp

}