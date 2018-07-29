package com.landkid.editmemd


import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.os.Environment.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_project.*
import kotlinx.android.synthetic.main.item_project.view.*
import mu.KLogger
import mu.KLogging
import mu.KotlinLogging
import java.io.*


/**
 * A simple [Fragment] subclass.
 */
class ProjectFragment : Fragment() {

    private var filePath: String = ""
    private var mListener: OnProjectItemClickListener? = null


    private val logger = KotlinLogging.logger {}

    companion object {
        fun newInstance(): ProjectFragment {
            return ProjectFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //filePath = getExternalStorageDirectory().absolutePath + "/EditMeMd"
        filePath = context.filesDir.absolutePath

        logger.info { "Check $filePath "}

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_project, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val file = File(filePath)

        val list = file.listFiles()

        val projectList = ArrayList<File>()

        for (inFile in list) {
            if (inFile.isDirectory) {
                projectList.add(inFile)
            }
        }

        projects_view.layoutManager = LinearLayoutManager(activity)
        projects_view.adapter = ProjectRecyclerAdaptor(activity, projectList)
    }

    inner class ProjectRecyclerAdaptor(thisActivity: Activity?, private var projectList: ArrayList<File>) : RecyclerView.Adapter<ProjectViewHolder>() {

        private var thisActivity: Activity = thisActivity!!

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {

            val itemView = LayoutInflater.from(thisActivity).inflate(R.layout.item_project, parent, false)

            return ProjectViewHolder(itemView)
        }

        fun onItemClicked(url: String) {
            if (mListener != null) {
                mListener!!.onItemClick("$url/readme.md")
            }
        }

        override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
            holder.projectTitleView?.text = projectList[position].name
            holder.itemView.setOnClickListener {
                view -> onItemClicked(filePath + "/" + projectList[position].name)
            }
        }

        override fun getItemCount(): Int {
            return projectList.size
        }

    }

    class ProjectViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val projectTitleView = itemView?.project_title
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProjectItemClickListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnNextButtonClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnProjectItemClickListener {
        fun onItemClick(url: String)
    }

}// Required empty public constructor
