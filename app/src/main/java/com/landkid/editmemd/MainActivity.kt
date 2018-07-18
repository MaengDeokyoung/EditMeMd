package com.landkid.editmemd

import android.Manifest
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.landkid.editmemd.RegisterFragment.OnNextButtonClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnNextButtonClickListener, ProjectFragment.OnProjectItemClickListener{

    private val APP_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE = 20
    private var registerFragment: RegisterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAuthority()

        setSupportActionBar(toolbar)

        fragmentManager.inTransaction {
            val projectFragment: ProjectFragment? = ProjectFragment.newInstance()
            replace(R.id.fragment_content, projectFragment)
        }

    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func().commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        //noinspection SimplifiableIfStatement
        return if (id == R.id.add_repository) {

            if(registerFragment == null) {
                registerFragment = RegisterFragment.newInstance()

                fragmentManager.inTransaction {

                    addToBackStack("SignIn")
                            .add(R.id.fragment_content, registerFragment)
                }
            }

            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onButtonClick() {
        if(registerFragment != null) {

            if (fragmentManager.backStackEntryCount > 0) {
                fragmentManager.popBackStackImmediate()
                registerFragment = null
            }

        }
    }

    private fun getAuthority() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        APP_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            APP_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE -> {
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

    override fun onBackPressed() {
        if(fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
            registerFragment = null
        } else {
            super.onBackPressed()
        }
    }

    override fun onItemClick(url: String) {

        val markDownFragment = MarkDownFragment.newInstance(url)

        fragmentManager.inTransaction {
            addToBackStack("MarkDown")
                    .add(R.id.fragment_content, markDownFragment)
        }
    }
}
