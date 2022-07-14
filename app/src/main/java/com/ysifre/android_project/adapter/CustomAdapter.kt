package com.ysifre.android_project.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ysifre.android_project.R
import kotlin.coroutines.coroutineContext


class CustomAdapter(private val mList: List<ItemsTrackViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_track, parent, false)

        return ViewHolder(view)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun getItem(position: Int): ItemsTrackViewModel {
        return mList[position]
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val itemsViewModel = mList[position]
        holder.imageView.setImageURI(itemsViewModel.image)
        holder.trackname.text = itemsViewModel.trackName
        holder.artistname.text = itemsViewModel.artistName
        holder.ranking.text = itemsViewModel.ranking

        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener.onItemClick(
                position
            )
        })
    }


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageTrackCardView)
        val artistname: TextView = itemView.findViewById(R.id.artistNameCardView)
        val trackname: TextView = itemView.findViewById(R.id.trackNameCardView)
        val ranking: TextView = itemView.findViewById(R.id.placeTrackCardView)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
