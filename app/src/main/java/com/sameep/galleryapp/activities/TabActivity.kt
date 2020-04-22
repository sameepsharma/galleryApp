package com.sameep.galleryapp.activities

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.kogicodes.sokoni.models.custom.Status
import com.sameep.galleryapp.R
import com.sameep.galleryapp.viewmodel.TabViewModel

class TabActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    var serverCode : String=""
    var token: String? = null
    private val RC_GET_AUTH_CODE = 9003

    private lateinit var viewModel: TabViewModel
    private  var RC_SIGN_IN: Int=100
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mGoogleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        viewModel = TabViewModel(this.application)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()

            .requestServerAuthCode("1040742720426-627j6me6eumkl3evu0e6596gcnrett3q.apps.googleusercontent.com",false)
            .requestScopes(Scope("https://www.googleapis.com/auth/photoslibrary.readonly"))
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)

            .build()

        setUpTabs()
        signIn()

    }

    private fun setUpTabs() {

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    private fun signIn() {

        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_GET_AUTH_CODE)
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {

                token=null
            }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null){
            signOut()
        }else{
            //updateUI(null)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

        if (requestCode == RC_GET_AUTH_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)

        }


    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            val account = task?.getResult(ApiException::class.java)
            serverCode= account?.serverAuthCode.toString()
            Log.e("ServerCode>>> ", "$serverCode <<<")
            viewModel.getAccessToken(serverCode)

            val pref = getSharedPreferences("serverCode", Context.MODE_PRIVATE)
            var editor = pref.edit()
            editor.putString("gToken", serverCode)
            editor.commit()


        } catch (e: ApiException) {

             Log.e("sfs",e.toString())
        }

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

}
