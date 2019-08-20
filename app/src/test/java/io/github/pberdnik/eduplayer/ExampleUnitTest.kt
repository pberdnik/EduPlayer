package io.github.pberdnik.eduplayer

import io.github.pberdnik.eduplayer.network.youtubeDataApiService
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        runBlocking {
            println(youtubeDataApiService
                .getPlaylistItemsForPlaylist("PLoFRvPlmME6E_nr_ulTL1-qO_-Y4I-hmk"))
        }
    }
}
