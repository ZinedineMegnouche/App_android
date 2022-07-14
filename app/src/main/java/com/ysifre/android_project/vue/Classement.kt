package com.ysifre.android_project.vue

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
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


class Classement : Fragment(){

    lateinit var trendingRecyclerView: RecyclerView
    lateinit var tracksButton: Button
    lateinit var albumsButton: Button
    lateinit var viewTracks: View
    lateinit var viewAlbums: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.classement, container, false).apply {
            trendingRecyclerView = findViewById(R.id.trendingRecyclerView)
            tracksButton = findViewById(R.id.titleButton)
            albumsButton = findViewById(R.id.albumButton)
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

        }
    }

    private fun displayTrendingTrack(){
        val data = ArrayList<ItemsTrackViewModel>()
        var tracks: ArrayList<Track> = ArrayList()
        GlobalScope.launch(Dispatchers.Unconfined) {
            try {
                val response = GetTrendingTracksNetwork.findTrendingTracks("us", "itunes", "singles")
                Log.d("TRACK RESPONSE", response.toString())
                if (response != null) {
                    for(i in response.trending){
                        tracks.add(i)
                    }
                }
                for (i in tracks) {
                    val myUri = Uri.parse(i.strTrackThumb)
                    Log.d("MY URI", myUri.toString())
                    data.add(ItemsTrackViewModel(myUri, i.strTrack, i.strArtist, i.intChartPlace))
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

    private fun displayTrendingAlbum(){
        val data = ArrayList<ItemsTrackViewModel>()
        var albums: ArrayList<Album> = ArrayList()
        GlobalScope.launch(Dispatchers.Unconfined) {
            try {
                val response = GetTrendingAlbumsNetwork.findTrendingAlbums("us", "itunes", "albums")
                Log.d("TRACK RESPONSE", response.toString())
                if (response != null) {
                    for(i in response.trending){
                        albums.add(i)
                    }
                }
                for (i in albums) {
                    val myUri = Uri.parse(i.strAlbumThumb)
                    data.add(ItemsTrackViewModel(myUri, i.strAlbum, i.strArtist, i.intChartPlace))
                }
                val adapter = CustomAdapter(data)
                trendingRecyclerView.adapter = adapter
                adapter.setOnItemClickListener(object:CustomAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {
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

    fun newInstance(artistID: String): DetailArtiste? {
        val f = DetailArtiste()
        // Supply index input as an argument.
        val args = Bundle()
        args.putString("index", artistID)
        f.setArguments(args)
        return f
    }
}