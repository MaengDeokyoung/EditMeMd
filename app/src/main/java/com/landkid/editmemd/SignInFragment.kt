package com.landkid.editmemd

import android.os.Bundle
import android.app.Fragment
import android.os.Environment
import android.os.StrictMode
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_sign_in.*
import java.io.File


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SignInFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment() {

    val gitUtil = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        val policy  =  StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        view.findViewById<EditText>(R.id.password).setOnEditorActionListener { v, actionId, event -> keyDown(view, v, actionId, event)}

        return view
    }

    private fun keyDown(view: View, v: View?, actionId: Int, event: KeyEvent?): Boolean {
        when(actionId) {
            EditorInfo.IME_ACTION_DONE -> gitClone(view)
        }

        return true;
    }

    private fun gitClone(view: View) {

        val filePath = Environment.getExternalStorageDirectory().absolutePath + "/EditMeMd"
        val file = File(filePath)

        GitUtil(view.findViewById<EditText>(R.id.username).text.toString(), view.findViewById<EditText>(R.id.password).text.toString()).cloneRemote("", file)
    }


    companion object {
        fun newInstance(): SignInFragment {
            return SignInFragment()
        }
    }
}// Required empty public constructor
