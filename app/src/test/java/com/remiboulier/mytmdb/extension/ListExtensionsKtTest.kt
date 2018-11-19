package com.remiboulier.mytmdb.extension

import com.remiboulier.mytmdb.network.models.Genre
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Remi BOULIER on 19/11/2018.
 * email: boulier.r.job@gmail.com
 */
class ListExtensionsKtTest {

    @Test
    fun genresToString_no_element() {
        val list = mutableListOf<Genre>()
        val ref = ""

        assertEquals(ref, genresToString(list))
    }

    @Test
    fun genresToString_one_element() {
        val list = mutableListOf(Genre(0, "Genre A"))
        val ref = "Genre A"

        assertEquals(ref, genresToString(list))
    }

    @Test
    fun genresToString_many_elements() {
        val list = mutableListOf("Genre A", "Genre B", "Genre C")
                .map { Genre(0, it) }
                .toMutableList()

        val ref = "Genre A, Genre B, Genre C"

        assertEquals(ref, genresToString(list))
    }
}