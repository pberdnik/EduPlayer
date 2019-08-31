package io.github.pberdnik.eduplayer

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import io.github.pberdnik.eduplayer.database.PlaylistDao
import io.github.pberdnik.eduplayer.database.YoutubeDatabase
import io.github.pberdnik.eduplayer.repository.YoutubeRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var database: YoutubeDatabase
    private lateinit var playlistDao: PlaylistDao
    private lateinit var repository: YoutubeRepository

    @Before
    fun createDb() {

        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(appContext, YoutubeDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        playlistDao = database.playlistDao
        repository = YoutubeRepository(database)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun test() = runBlocking {
        repository.refreshPlaylists()
        repository.switchPlaylistExpansion("PLoFRvPlmME6E_nr_ulTL1-qO_-Y4I-hmk")
        println("EXPANSION:" + playlistDao.getExpandedPlaylistsItemsNoLD())
        repository.switchPlaylistExpansion("PLoFRvPlmME6E_nr_ulTL1-qO_-Y4I-hmk")
        println("EXPANSION:" + playlistDao.getExpandedPlaylistsItemsNoLD())
    }
}
