package com.ysifre.android_project.vue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ysifre.android_project.R

class DetailAlbum : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.album, container, false)

            /*findViewById<View>(R.id.shapeableImageView).setOnClickListener{
                findNavController().navigate(
                    detailAlbumDirections.actionDetailAlbum6ToDetailArtiste42()
                )
            }
        }*/
    }
}