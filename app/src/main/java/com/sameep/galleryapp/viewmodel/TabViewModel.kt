package com.sameep.galleryapp.viewmodel


import androidx.lifecycle.*
import android.app.Application
import android.content.Context
import android.os.SystemClock

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.photos.library.v1.PhotosLibraryClient

import com.google.photos.library.v1.proto.Filters
import com.google.photos.types.proto.Album
import com.google.photos.types.proto.MediaItem

import com.kogicodes.sokoni.models.custom.Resource
import com.sameep.galleryapp.utils.PhotosLibraryClientFactory
import com.squareup.okhttp.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class TabViewModel (application: Application) : AndroidViewModel(application){


    internal var client: PhotosLibraryClient? = null
    internal var applicationc: Application? = null

    internal   var mAccessToken: String = ""
    var mTokenExpired: Long = 0

    private val accountObserver = MutableLiveData<Resource<GoogleSignInAccount>>()
    private val albumsObservable = MutableLiveData<Resource<LinkedList<Album>>>()
    private val mediaItemObservable = MutableLiveData<Resource<List<MediaItem>>>()
    private val mediaItemFilteredObservable = MutableLiveData<Resource<List<MediaItem>>>()
    private val accessTokenObservable = MutableLiveData<Resource<String>>()

    init {
        applicationc=application

    }
    fun setIsSignedIn(account: GoogleSignInAccount) {

        accountObserver.value = Resource.success("",account)
    }
    fun setIsNotSignedIn() {
        accountObserver.value=Resource.error("Logged Out",null)

    }


    fun observeAccount(): LiveData<Resource<GoogleSignInAccount>> {
        return accountObserver
    }


    fun observeAlbums():LiveData<Resource<LinkedList<Album>>> {
        return albumsObservable
    }
    fun getAlbums(googleSignInAccount : String)  {
        albumsObservable.value= Resource.loading("Loading albums",null)

        GlobalScope.launch(context = Dispatchers.Main) {


            client = googleSignInAccount?.let  { it1 -> PhotosLibraryClientFactory.createClient(googleSignInAccount) }
            val response = client?.listAlbums()
            val albums = LinkedList<Album>()

            if (response != null) {
                for (album in response.iterateAll()) {
                    albums.add(album)
                }
                if (albums.size > 0) {
                    albumsObservable.postValue( Resource.success("",albums))
                } else {
                albumsObservable.postValue( Resource.error("No Albums Found", albums))

                }
            } else {
                albumsObservable.postValue( Resource.error("Response is null", albums))

            }
        }




    }






    fun getMediaItemFiltered(googleSignInAccount : String,filters : Filters)  {
        mediaItemObservable.value= Resource.loading("Loading Images",null)
        GlobalScope.launch(context = Dispatchers.Main) {

            client = googleSignInAccount?.let { it1 -> PhotosLibraryClientFactory.createClient(it1) }

            val response = client?.searchMediaItems(filters)

            val mediaItems = LinkedList<MediaItem>()

            if (response != null) {
                for (m in response.iterateAll()) {
                    mediaItems.add(m)
                }
            }
            if (mediaItems.size > 0) {
                mediaItemObservable.postValue(Resource.success("", mediaItems))
            } else {
                mediaItemObservable.postValue( Resource.error("No Media Found", mediaItems))

            }
        }


    }

    fun getAccessToken(googleSignInAccount : String){

            GlobalScope.launch(context = Dispatchers.Main) {

                val client = OkHttpClient()
                val requestBody = FormEncodingBuilder()
                        .add("grant_type", "authorization_code")
                        .add("client_id", "1040742720426-83ltuccetfmb7vn562os7rnfn8s23e9s.apps.googleusercontent.com")
                        .add("client_secret", "pf9p0AH1kvcLP3P7fK7NyScy")
                        .add("redirect_uri", "")
                        .add("code", googleSignInAccount)
                        .build()
                val request = Request.Builder()
                        .url("https://www.googleapis.com/oauth2/v4/token")
                        .post(requestBody)
                        .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(request: Request, e: IOException) {

                    }

                    @Throws(IOException::class)
                    override fun onResponse(response: Response) {
                        try {
                            val jsonObject = JSONObject(response.body().string())

                            mTokenExpired = SystemClock.elapsedRealtime() + jsonObject.optLong("expires_in") * 1000

                            accessTokenObservable.postValue(Resource.success("",jsonObject.optString("access_token")))
                            val pref = applicationc?.getSharedPreferences("accessToken", Context.MODE_PRIVATE)
                            val editor = pref?.edit()
                            editor?.putString("accessToken", jsonObject.optString("access_token"))


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                })


            }


    }
    fun observeAccessToken():LiveData<Resource<String>> {
        return accessTokenObservable
    }

}






