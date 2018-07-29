package com.landkid.editmemd

import org.junit.Assert.*
import org.junit.Test

import java.io.File

class GitUtilTest{

    @Test
    fun gitUtilTest(){
        val git = GitUtil("id", "pw", "https://github.com/MaengDeokyoung/EditMeMd.git", File("./test"))
        assertTrue(git.cloneRemote().second)

    }



    @Test
    fun gitUtilExceptionTest(){
        val git = GitUtil("id", "pw", "https://github.com/foo/bar.git", File("./test"))
        assertFalse(git.cloneRemote().second)
    }
}

