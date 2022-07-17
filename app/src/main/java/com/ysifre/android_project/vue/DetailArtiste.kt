package com.ysifre.android_project.vue

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ysifre.android_project.Album
import com.ysifre.android_project.GetAlbumIdNetwork
import com.ysifre.android_project.R
import com.ysifre.android_project.adapter.AdapterAlbums
import com.ysifre.android_project.adapter.AdapterPopularTracks
import com.ysifre.android_project.adapter.ItemAlbumViewModel
import com.ysifre.android_project.adapter.ItemsPopularTrackModel
import com.ysifre.android_project.model.GetArtistByIdNetwork
import com.ysifre.android_project.model.GetPopularTracksNetwork
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
    lateinit var artistImage: ConstraintLayout
    lateinit var artistName: TextView
    lateinit var artistDesc: TextView
    lateinit var artistCountry: TextView
    lateinit var artistGenre: TextView
    lateinit var nbAlbum: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.artiste, container, false).apply {

            backButton = findViewById(R.id.backButtonToRanking)
            recyclerViewAlbum = findViewById(R.id.albumrecyclerViewDetailArtiste)
            recyclerViewTrack = findViewById(R.id.tracksRecyclerViewDetailArtist)
            artistImage = findViewById(R.id.constraintImage)
            artistDesc = findViewById(R.id.artistDescription)
            artistDesc.movementMethod = ScrollingMovementMethod()
            artistName = findViewById(R.id.artistName)
            artistCountry = findViewById(R.id.countryArtistTextView)
            artistGenre = findViewById(R.id.artistGenre)
            nbAlbum = findViewById(R.id.nbAlbumTextView)

            recyclerViewAlbum.layoutManager = LinearLayoutManager(activity)
            recyclerViewTrack.layoutManager = LinearLayoutManager(activity)

            //id de l'artiste quand l'utitlisateur a cliqué sur un track du classement
            setFragmentResultListener("requestKey") { requestKey, bundle ->
                val result = bundle.getString("bundleKey")
                idArtist = result.toString()
                diaplayInfo(idArtist)
                displayAlbum(idArtist)
            }

            //nom de l'artiste quand l'utitlisateur a cliqué sur un track du classement
            setFragmentResultListener("artistNameKey") { requestKey, bundle ->
                val result = bundle.getString("bundleKeyArtistName")
                strArtist = result.toString()
                displayTracks(strArtist)
            }

            //id de l'artiste quand l'utitlisateur a cliqué sur un album du classement
            setFragmentResultListener("requestKeyID") { requestKey, bundle ->
                val result = bundle.getString("bundleKeyID")
                idArtist = result.toString()
                diaplayInfo(idArtist)
                displayAlbum(idArtist)
            }

            //nom de l'artiste quand l'utitlisateur a cliqué sur un album du classement
            setFragmentResultListener("artistNameKeyAlbum") { requestKey, bundle ->
                val result = bundle.getString("bundleKeyArtistNameAlbum")
                strArtist = result.toString()
                displayTracks(strArtist)
            }

            //id de l'artiste quand l'utitlisateur a cliqué sur le boutton retour de la page de detail d'album
            setFragmentResultListener("requestKeyAlbumIdFromDetailAlbum") { requestKey, bundle ->
                val result = bundle.getString("bundleKeyAlbumIdFromDetailAlbum")
                idArtist = result.toString()
                diaplayInfo(idArtist)
                displayAlbum(idArtist)
            }

            //nom de l'artiste quand l'utitlisateur a cliqué sur le boutton retour de la page de detail d'album
            setFragmentResultListener("artistNameKeyArtistNameFromDetailAlbum") { requestKey, bundle ->
                val result = bundle.getString("bundleKeyArtistNameAlbumFromDetailAlbum")
                strArtist = result.toString()
                displayTracks(strArtist)
            }

            //id de l'artiste quand l'utitlisateur a cliqué sur l'artiste dans la recherche
            setFragmentResultListener("requestKeyArtistIdSearchView") { requestKey, bundle ->
                val result = bundle.getString("bundleKeyArtistIdSearchView")
                idArtist = result.toString()
                diaplayInfo(idArtist)
                displayAlbum(idArtist)
            }

            //nom de l'artiste quand l'utitlisateur a cliqué sur l'artiste dans la recherche
            setFragmentResultListener("requestKeyArtistNameSearchView") { requestKey, bundle ->
                val result = bundle.getString("bundleKeyArtistNameSearchView")
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

    private fun diaplayInfo(idArtist: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = GetArtistByIdNetwork.findArtist(idArtist.toInt())
                Log.d("TRACK POPULAR RESPONSE", response.toString())
                if (response != null) {
                    if (response.artists != null) {
                        artistName.text = response.artists[0].strArtist
                        artistCountry.text = response.artists[0].strCountry
                        artistDesc.text = response.artists[0].strBiographyEN
                        artistGenre.text = response.artists[0].strGenre
                        Glide.with(this@DetailArtiste).load(response.artists.get(0).strArtistThumb).centerCrop().placeholder(R.drawable.dark_placeholder).into(object : CustomTarget<Drawable?>() {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable?>?
                            ) {
                                artistImage.background = resource
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                    }
                }
            } catch(e: Exception) {
                Log.d("CATCH RETURN ARTIST", e.message.toString())
            }
        }
    }

    fun displayAlbum(id: String){
        val data = ArrayList<ItemAlbumViewModel>()
        var albums: ArrayList<Album> = ArrayList()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = GetAlbumIdNetwork.findAlbumByArtistId(id.toInt())
                Log.d("TRACK RESPONSE", response.toString())
                if (response != null) {
                    for(i in response.album){
                        albums.add(i)
                    }
                }
                for (i in albums) {
                    if(i.strAlbumThumb != null){
                        data.add(ItemAlbumViewModel(i.strAlbumThumb, i.strAlbum, i.intYearReleased))
                    }else{
                        data.add(ItemAlbumViewModel("", i.strAlbum, i.intYearReleased))
                    }

                }
                nbAlbum.text = "(" + albums.size.toString() + ")"
                val adapter = AdapterAlbums(data)
                recyclerViewAlbum.adapter = adapter
                adapter.setOnItemClickListener(object: AdapterAlbums.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        val idAlbum = albums.get(position).idAlbum
                        setFragmentResult("requestKeyAlbumID", bundleOf("bundleKeyAlbumID" to idAlbum))
                        findNavController().navigate(
                            DetailArtisteDirections.actionDetailArtiste4ToDetailAlbum6()
                        )
                    }
                })

            } catch(e: Exception) {
                Log.d("CATCH DISPLAY ALBUM", e.message.toString())
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
                var count = 1
                for (i in tracks) {
                    data.add(ItemsPopularTrackModel(count.toString(), i.strTrack))
                    count += 1
                }
                val adapter = AdapterPopularTracks(data)
                recyclerViewTrack.adapter = adapter
                adapter.setOnItemClickListener(object: AdapterPopularTracks.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        Log.d("POSITION CLICKED 1", position.toString())
                    }
                })

            } catch(e: Exception) {
                Log.d("CATCH DISPLAY TRACK", e.message.toString())
            }
        }
    }


}
