package com.landkid.editmemd

import android.os.Bundle
import android.app.Fragment
import android.content.ClipboardManager
import android.content.Context
import android.os.Environment
import android.os.StrictMode
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.fragment_register.*
import java.io.File
import android.content.ClipData
import android.text.Editable


class RegisterFragment : Fragment() {

    val gitUtil = null;
    private var mListener: OnNextButtonClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val policy  =  StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val item = clipboard.primaryClip.getItemAt(0)

// Gets the clipboard as text.
        val pasteData = item.text
        git_address.setText(pasteData)
        password.setOnEditorActionListener { v, actionId, event -> keyDown(v, actionId, event)}
    }

    private fun keyDown(v: View?, actionId: Int, event: KeyEvent?): Boolean {
        when(actionId) {
            EditorInfo.IME_ACTION_DONE -> gitClone()
        }

        return true;
    }

    private fun gitClone() {

        val filePath = Environment.getExternalStorageDirectory().absolutePath + "/EditMeMd"
        val file = File(filePath)

        val username = username.text.toString()
        val password = password.text.toString()
        val gitRepositoryURL = git_address.text.toString()
        GitUtil(username, password).cloneRemote(gitRepositoryURL, file)

        onButtonPressed()
    }


    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed() {
        if (mListener != null) {
            mListener!!.onButtonClick()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNextButtonClickListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnNextButtonClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnNextButtonClickListener {
        fun onButtonClick()
    }
}// Required empty public constructor
