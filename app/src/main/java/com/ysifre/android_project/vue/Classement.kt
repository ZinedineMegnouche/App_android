package com.ysifre.android_project.vue

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ysifre.android_project.Album
import com.ysifre.android_project.GetTrendingAlbumsNetwork
import com.ysifre.android_project.R
import com.ysifre.android_project.adapter.CustomAdapter
import com.ysifre.android_project.adapter.ItemsTrackViewModel
import com.ysifre.android_project.model.GetTrendingTracksNetwork
import com.ysifre.android_project.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class Classement : Fragment(){

    lateinit var trendingRecyclerView: RecyclerView
    lateinit var tracksButton: Button
    lateinit var albumsButton: Button
    lateinit var searchButton: ImageView
    lateinit var favButton: ImageView
    lateinit var viewTracks: View
    lateinit var viewAlbums: View

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.classement, container, false).apply {
            Log.d("LANGUE", Locale.getDefault().displayLanguage.toString())
            trendingRecyclerView = findViewById(R.id.trendingRecyclerView)
            tracksButton = findViewById(R.id.titleButton)
            albumsButton = findViewById(R.id.albumButton)
            searchButton = findViewById(R.id.search_button)
            favButton = findViewById(R.id.favButton)
            viewTracks = findViewById(R.id.view1)
            viewAlbums = findViewById(R.id.view2)

            viewAlbums.isVisible = false
            trendingRecyclerView.layoutManager = LinearLayoutManager(activity)
            displayTrendingTrack()

            tracksButton.setOnClickListener {
                Log.d("TRACK BUTTON CLICKED", "clicked")
                tracksButton.setTextColor(Color.BLACK)
                displayTrendingTrack()
                viewAlbums.isVisible = false
                viewTracks.isVisible = true
                albumsButton.setTextColor(Color.parseColor("#979797"))
            }

            albumsButton.setOnClickListener {
                Log.d("ALBUM BUTTON CLICKED", "clicked")
                albumsButton.setTextColor(Color.BLACK)
                displayTrendingAlbum()
                viewTracks.isVisible = false
                viewAlbums.isVisible = true
                tracksButton.setTextColor(Color.parseColor("#979797"))
            }

            searchButton.setOnClickListener {
                findNavController().navigate(
                    ClassementDirections.actionClassement2ToRecherche2()
                )
            }

            favButton.setOnClickListener {
                findNavController().navigate(
                    ClassementDirections.actionClassement2ToFavoris2()
                )
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun displayTrendingTrack(){
        val data = ArrayList<ItemsTrackViewModel>()
        var tracks: ArrayList<Track> = ArrayList()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = GetTrendingTracksNetwork.findTrendingTracks("us", "itunes", "singles")
                Log.d("TRACK RESPONSE", response.toString())
                if (response != null) {
                    for(i in response.trending.reversed()){
                        tracks.add(i)
                    }
                }

                for (i in tracks) {
                    if(i.strTrackThumb != null){
                        data.add(ItemsTrackViewModel(i.strTrackThumb, i.strTrack, i.strArtist, i.intChartPlace))
                    }else{
                        data.add(ItemsTrackViewModel("", i.strTrack, i.strArtist, i.intChartPlace))
                    }
                }

                val adapter = CustomAdapter(data)
                trendingRecyclerView.adapter = adapter
                adapter.setOnItemClickListener(object:CustomAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        Log.d("POSITION CLICKED 1", position.toString())
                        val idArtist = tracks.get(position).idArtist
                        val strArtist = tracks.get(position).strArtist

                        setFragmentResult("requestKey", bundleOf("bundleKey" to idArtist))
                        setFragmentResult("artistNameKey", bundleOf("bundleKeyArtistName" to strArtist))

                        findNavController().navigate(
                            ClassementDirections.actionClassement2ToDetailArtiste4()
                        )
                    }
                })

            } catch(e: Exception) {
                Log.d("CATCH RETURN", e.message.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun displayTrendingAlbum(){
        val data = ArrayList<ItemsTrackViewModel>()
        var albums: ArrayList<Album> = ArrayList()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = GetTrendingAlbumsNetwork.findTrendingAlbums("us", "itunes", "albums")
                Log.d("TRACK RESPONSE", response.toString())
                if (response != null) {
                    for(i in response.trending.reversed()){
                        albums.add(i)
                    }
                }
                for (i in albums) {
                    if(i.strAlbumThumb != null){
                        data.add(ItemsTrackViewModel(i.strAlbumThumb, i.strAlbum, i.strArtist, i.intChartPlace))
                    }else{
                        data.add(ItemsTrackViewModel("", i.strAlbum, i.strArtist, i.intChartPlace))
                    }
                }
                val adapter = CustomAdapter(data)
                Log.d("IMAGE URI", "TEST 1")
                trendingRecyclerView.adapter = adapter
                adapter.setOnItemClickListener(object:CustomAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        val idArtist = albums.get(position).idArtist
                        val strArtist = albums.get(position).strArtist
                        setFragmentResult("requestKeyID", bundleOf("bundleKeyID" to idArtist))
                        setFragmentResult("artistNameKeyAlbum", bundleOf("bundleKeyArtistNameAlbum" to strArtist))
                        Log.d("POSITION CLICKED 1", position.toString())
                        findNavController().navigate(
                            ClassementDirections.actionClassement2ToDetailArtiste4())
                    }
                })

            } catch(e: Exception) {
                Log.d("CATCH RETURN", e.message.toString())
            }
        }
    }
}