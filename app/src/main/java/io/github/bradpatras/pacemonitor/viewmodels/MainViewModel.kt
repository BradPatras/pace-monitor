package io.github.bradpatras.pacemonitor.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.bradpatras.pacemonitor.events.SpeedReportEvents
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainViewModel : ViewModel() {
    private var fiveSpeed: Float = 0f
        set(value) {
            field = value
            fiveSecondSpeed.postValue(value)
        }
    private var thirtySpeed: Float = 0f
        set(value) {
            field = value
            thirtySecondSpeed.postValue(value)
        }
    private var sixtySpeed: Float = 0f
        set(value) {
            field = value
            sixtySecondSpeed.postValue(value)
        }

    val fiveSecondSpeed: MutableLiveData<Float> by lazy { MutableLiveData<Float>() }
    val thirtySecondSpeed: MutableLiveData<Float> by lazy { MutableLiveData<Float>() }
    val sixtySecondSpeed: MutableLiveData<Float> by lazy { MutableLiveData<Float>() }

    init {
        EventBus.getDefault().register(this)
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    fun publishLastReports() {
        fiveSpeed = fiveSpeed
        thirtySpeed = thirtySpeed
        sixtySpeed = sixtySpeed
    }

    @Subscribe
    fun onFiveSecondReportEvent(event: SpeedReportEvents.FiveSecondReport) {
        fiveSpeed = event.speed
    }

    @Subscribe
    fun onThirtySecondReportEvent(event: SpeedReportEvents.ThirtySecondReport) {
        thirtySpeed = event.speed
    }

    @Subscribe
    fun onSixtySecondReportEvent(event: SpeedReportEvents.SixtySecondReport) {
        sixtySpeed = event.speed
    }
}
