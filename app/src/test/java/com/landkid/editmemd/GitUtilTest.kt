package com.landkid.editmemd

import org.junit.Test

import java.io.File

class GitUtilTest{

    @Test
    fun gitUtilTest(){
        val git = GitUtil("id", "pw")
        git.cloneRemote("https://github.com/MaengDeokyoung/EditMeMd.git", File("./test"))
        println(git.getLocalFileList("", "https://github.com/MaengDeokyoung/EditMeMd.git",File("./test")))
    }
}

