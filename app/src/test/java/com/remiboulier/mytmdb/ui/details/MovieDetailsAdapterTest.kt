package com.remiboulier.mytmdb.ui.details

import com.remiboulier.mytmdb.network.models.Part
import com.remiboulier.mytmdb.util.GlideRequests
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Created by Remi BOULIER on 19/11/2018.
 * email: boulier.r.job@gmail.com
 */
class MovieDetailsAdapterTest {

    @Test
    fun update_list_with_null_list() {

        val parts = MutableList(3) { mock(Part::class.java) }
        val adapter = spy(MovieDetailsAdapter(parts, mock(GlideRequests::class.java), {}))

        assert(parts.size == 3)

        doNothing().`when`(adapter).notifyDataSetChanged()
        adapter.update(null)

        assert(parts.size == 0)
    }

    @Test
    fun update_list_with_empty_list() {

        val parts = MutableList(3) { mock(Part::class.java) }
        val adapter = spy(MovieDetailsAdapter(parts, mock(GlideRequests::class.java), {}))

        assert(parts.size == 3)

        doNothing().`when`(adapter).notifyDataSetChanged()
        adapter.update(mutableListOf())

        assert(parts.size == 0)
    }

    @Test
    fun update_empty_list_with_list_of_part() {

        val parts = mutableListOf<Part>()
        val adapter = spy(MovieDetailsAdapter(parts, mock(GlideRequests::class.java), {}))

        assert(parts.size == 0)

        doNothing().`when`(adapter).notifyDataSetChanged()
        adapter.update(MutableList(4) { mock(Part::class.java) })

        assert(parts.size == 4)
    }

    @Test
    fun update_list_with_list_of_part() {

        val parts = MutableList(4) { mock(Part::class.java) }
        val adapter = spy(MovieDetailsAdapter(parts, mock(GlideRequests::class.java), {}))

        assert(parts.size == 4)

        doNothing().`when`(adapter).notifyDataSetChanged()
        adapter.update(MutableList(2) { mock(Part::class.java) })

        assert(parts.size == 2)
    }
}