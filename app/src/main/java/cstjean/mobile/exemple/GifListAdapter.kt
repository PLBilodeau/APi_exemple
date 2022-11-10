package cstjean.mobile.exemple

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cstjean.mobile.exemple.api.Gif
import cstjean.mobile.exemple.databinding.ListGifGalleryBinding

class GifViewHolder(
    private val binding: ListGifGalleryBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(gif: Gif) {
        binding.itemImageView.load(gif.images.fixedHeight.url) {
            placeholder(R.drawable.ic_baseline_insert_photo_24)
        }
    }
}

class GifListAdapter(
    private val gifs: List<Gif>
) : RecyclerView.Adapter<GifViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GifViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListGifGalleryBinding.inflate(inflater, parent, false)
        return GifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val item = gifs[position]
        holder.bind(item)
    }

    override fun getItemCount() = gifs.size
}
