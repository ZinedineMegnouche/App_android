package com.ysifre.android_project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ysifre.android_project.R


class AdapterAlbums(private val mList: List<ItemAlbumViewModel>) : RecyclerView.Adapter<AdapterAlbums.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_album, parent, false)

        return ViewHolder(view)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun getItem(position: Int): ItemAlbumViewModel {
        return mList[position]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        holder.image.setImageResource(itemsViewModel.image)
        holder.albumName.text = itemsViewModel.albumName
        holder.albumDate.text = itemsViewModel.albumDate

        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener.onItemClick(
                position
            )
        })
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var image: ImageView = itemView.findViewById(R.id.imageAlbumCardView)
        val albumName: TextView = itemView.findViewById(R.id.albumNameCardViewAlbum)
        val albumDate: TextView = itemView.findViewById(R.id.albumDateCardViewAlbum)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}
