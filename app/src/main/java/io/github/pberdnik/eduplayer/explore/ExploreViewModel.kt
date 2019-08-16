package io.github.pberdnik.eduplayer.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExploreViewModel : ViewModel() {

    private val _videos = MutableLiveData<String>("Hell world")
    val videos: LiveData<String> = _videos



}
