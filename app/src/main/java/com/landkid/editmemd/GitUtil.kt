package com.landkid.editmemd

import org.eclipse.jgit.api.Git
import java.io.File
import java.util.*
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.eclipse.jgit.internal.storage.file.FileRepository

import mu.KLogging

class GitUtil {

    private val cp: UsernamePasswordCredentialsProvider
    companion object: KLogging()

    constructor(id:String, pw:String){
        this.cp = UsernamePasswordCredentialsProvider(id, pw)

    }

    fun cloneRemote(remoteRepository : String, localStorage: File){

        val repositoryName = Regex("/([^/]*).git$").find(remoteRepository)!!.groups[1]!!.value

        val file =  File(localStorage, repositoryName)

        file.deleteRecursively()
        file.mkdir()

        logger.info("File = "+ file)
        logger.info("isDir = "+  file.isDirectory )

        Git.cloneRepository()
                .setURI(remoteRepository)
                .setCredentialsProvider(cp)
                .setDirectory(file)
                .call()

    }

    fun getLocalFileList(localRepository : String,remoteRepository: String, localStorage: File): Array<out File> {
        val repositoryName = Regex("/([^/]*).git$").find(remoteRepository)!!.groups[1]!!.value

        val file =  File(localStorage, repositoryName)

        logger.info("Files = "+ Arrays.toString(file.listFiles()))


        return file.listFiles()
    }

    fun commitLocal(localRepository: String,localStorage: File){


    }

    fun pushRemote(remoteRepository: String,localStorage: File){
        val repositoryName = Regex("/([^/]*).git$").find(remoteRepository)!!.groups[1]!!.value

        val db = FileRepository(File(localStorage, "$repositoryName/.git"))
        val git = Git(db)

        logger.info("workTree: "+ db.workTree)


        // run the add-call
        git.add().addFilepattern(".")
                .call()


        val commit = git.commit()
                .setMessage("EditMe.md commit")
                .call()

        val pushCommand = git.push()
                .setForce(true)
                .setPushAll()
                .setRemote(remoteRepository)
                .setCredentialsProvider(cp)
                .call()

    }
}