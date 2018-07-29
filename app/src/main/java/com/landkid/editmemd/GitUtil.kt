package com.landkid.editmemd

import org.eclipse.jgit.api.Git
import java.io.File
import java.util.*
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.eclipse.jgit.internal.storage.file.FileRepository

import mu.KotlinLogging
import org.eclipse.jgit.api.errors.TransportException
import org.eclipse.jgit.errors.NoRemoteRepositoryException

class GitUtil {

    /*

     */

    private val logger = KotlinLogging.logger {}
    private val cp: UsernamePasswordCredentialsProvider
    private val remoteRepository : String?
    private val localStorage : File?


    constructor(id:String, pw:String, localStorage: File){
        this.cp = UsernamePasswordCredentialsProvider(id, pw)
        this.localStorage = localStorage
        this.remoteRepository = null

    }

    constructor(id:String, pw:String, remoteRepository : String, localStorage: File){
        this.cp = UsernamePasswordCredentialsProvider(id, pw)
        this.remoteRepository = remoteRepository
        this.localStorage = localStorage

    }

    fun cloneRemote() : Pair<String, Boolean> {
        return if (remoteRepository!=null){
            cloneRemote(remoteRepository)
        }
        else {
            Pair("Something went wrong [$remoteRepository] ", false)
        }
    }

    fun cloneRemote(remoteRepository : String) : Pair<String, Boolean> {

        val repositoryName = Regex("/([^/]*).git$").find(remoteRepository)!!.groups[1]!!.value

        val file =  File(localStorage, repositoryName)

        file.deleteRecursively()
        file.mkdir()

        logger.info("File = $file")
        logger.info("isDir = ${file.isDirectory}" )
        return try {
            Git.cloneRepository()
                    .setURI(remoteRepository)
                    .setCredentialsProvider(cp)
                    .setDirectory(file)
                    .call()
            Pair(file.absolutePath, true)
        } catch (ex: Exception) {
            when(ex) {
                is NoRemoteRepositoryException,
                is TransportException -> {
                    Pair("Couldn't reach to [$remoteRepository]", false)
                }
                else -> {
                    Pair(ex.toString(), false)
                }
            }

        }
    }

    fun getLocalFileList() :  Array<out File>  {
        return if (remoteRepository != null){
            getLocalFileList(remoteRepository)
        }
        else {
            emptyArray()
        }
    }
    fun getLocalFileList(remoteRepository: String): Array<out File> {
        val repositoryName = Regex("/([^/]*).git$").find(remoteRepository)!!.groups[1]!!.value

        val file =  File(localStorage, repositoryName)

        logger.info("Files = "+ Arrays.toString(file.listFiles()))


        return file.listFiles()
    }

    fun pushRemote() : Pair<String, Boolean> {
        return if (remoteRepository != null){
            pushRemote(remoteRepository)
        }
        else {
            Pair("Something wrong [$remoteRepository]  ", false)
        }
    }
    fun pushRemote(remoteRepository: String, commitMessage:String = "EditMe.md commit") : Pair<String, Boolean> {

        try {
            val repositoryName = Regex("/([^/]*).git$").find(remoteRepository)!!.groups[1]!!.value

            val db = FileRepository(File(localStorage, "$repositoryName/.git"))
            val git = Git(db)

            // run the add-call
            git.add().addFilepattern(".")
                    .call()

            val commit = git.commit()
                    .setMessage(commitMessage)
                    .call()

            val pushCommand = git.push()
                    .setForce(true)
                    .setPushAll()
                    .setRemote(remoteRepository)
                    .setCredentialsProvider(cp)
                    .call()

            return Pair("OK", true)

        }catch (e : Exception) {
            val msg :String = if (e.message.isNullOrBlank()) e.toString() else e.message!!
            return Pair(msg, false)
        }

    }
}