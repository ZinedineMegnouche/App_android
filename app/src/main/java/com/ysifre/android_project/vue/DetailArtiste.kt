package com.ysifre.android_project.vue

import android.R.attr.defaultValue
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ysifre.android_project.Album
import com.ysifre.android_project.GetAlbumByIDAPI
import com.ysifre.android_project.GetAlbumIdNetwork
import com.ysifre.android_project.R
import com.ysifre.android_project.adapter.AdaptaterPopularTracks
import com.ysifre.android_project.adapter.CustomAdapter
import com.ysifre.android_project.adapter.ItemsPopularTrackModel
import com.ysifre.android_project.adapter.ItemsTrackViewModel
import com.ysifre.android_project.model.GetPopularTracksNetwork
import com.ysifre.android_project.model.GetTrendingTracksNetwork
import com.ysifre.android_project.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DetailArtiste : Fragment() {
    lateinit var backButton: ImageView
    lateinit var recyclerViewAlbum: RecyclerView
    lateinit var recyclerViewTrack: RecyclerView
    lateinit var idArtist: String
    lateinit var strArtist: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.artiste, container, false).apply {

            backButton = findViewById(R.id.backButtonToRanking)
            recyclerViewAlbum = findViewById(R.id.albumrecyclerViewDetailArtiste)
            recyclerViewTrack = findViewById(R.id.tracksRecyclerViewDetailArtist)

            recyclerViewAlbum.layoutManager = LinearLayoutManager(activity)
            recyclerViewTrack.layoutManager = LinearLayoutManager(activity)

            setFragmentResultListener("requestKey") { requestKey, bundle ->
                val result = bundle.getString("bundleKey")
                idArtist = result.toString()
                displayAlbum(idArtist)
            }

            setFragmentResultListener("artistNameKey") { requestKey, bundle ->
                val result = bundle.getString("bundleKeyArtistName")
                strArtist = result.toString()
                displayTracks(strArtist)
            }

            backButton.setOnClickListener{
                findNavController().navigate(
                    DetailArtisteDirections.actionDetailArtiste4ToClassement2()
                )
            }
        }
    }

    fun displayAlbum(id: String){
        val data = ArrayList<ItemsTrackViewModel>()
        var albums: ArrayList<Album> = ArrayList()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = GetAlbumIdNetwork.findAlbumById(id.toInt())
                Log.d("TRACK RESPONSE", response.toString())
                if (response != null) {
                    for(i in response.album){
                        albums.add(i)
                    }
                }
                for (i in albums) {
                    val myUri = Uri.parse(i.strAlbumThumb)
                    Log.d("MY URI", myUri.toString())
                    data.add(ItemsTrackViewModel(myUri, i.strAlbum, i.strArtist, "0"))
                }
                val adapter = CustomAdapter(data)
                recyclerViewAlbum.adapter = adapter
                adapter.setOnItemClickListener(object: CustomAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        Log.d("POSITION CLICKED 1", position.toString())
                    }
                })

            } catch(e: Exception) {
                Log.d("CATCH RETURN", e.message.toString())
            }
        }
    }

    fun displayTracks(name: String){
        val data = ArrayList<ItemsPopularTrackModel>()
        var tracks: ArrayList<Track> = ArrayList()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = GetPopularTracksNetwork.findPopularTracks(name)
                Log.d("TRACK POPULAR RESPONSE", response.toString())
                if (response != null) {
                    for(i in response.track){
                        tracks.add(i)
                    }
                }
                for (i in tracks) {
                    data.add(ItemsPopularTrackModel(i.strTrack))
                }
                val adapter = AdaptaterPopularTracks(data)
                recyclerViewTrack.adapter = adapter
                adapter.setOnItemClickListener(object: AdaptaterPopularTracks.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        Log.d("POSITION CLICKED 1", position.toString())
                    }
                })

            } catch(e: Exception) {
                Log.d("CATCH RETURN", e.message.toString())
            }
        }
    }
}
