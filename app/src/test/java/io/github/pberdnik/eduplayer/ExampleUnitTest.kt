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
                .getVideosById("Ks-_Mh1QhMc,c0KYU2j0TM4,eIho2S0ZahI"))
        }
    }
}
