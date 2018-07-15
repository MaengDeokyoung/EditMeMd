package com.landkid.editmemd

import android.Manifest
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    val APP_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAuthority()

        setSupportActionBar(toolbar);

        val signInFragment = SignInFragment.newInstance()

        fragmentManager.inTransaction {
            add(R.id.fragment_content, signInFragment)
        }

    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func().commit()
    }

    private fun getAuthority() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        APP_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            APP_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }
}
