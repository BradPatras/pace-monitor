package io.github.bradpatras.pacemonitor.ui.main

import androidx.lifecycle.ViewModel
import io.github.bradpatras.pacemonitor.events.SpeedReportEvents
import io.reactivex.subjects.PublishSubject
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainViewModel : ViewModel() {
    private var fiveSpeed: Float = 0f
        set(value) {
            field = value
            fiveSecondSpeedPublishSubject.onNext(value)
        }
    private var thirtySpeed: Float = 0f
        set(value) {
            field = value
            thirtySecondSpeedPublishSubject.onNext(value)
        }
    private var sixtySpeed: Float = 0f
        set(value) {
            field = value
            sixtySecondSpeedPublishSubject.onNext(value)
        }

    val fiveSecondSpeedPublishSubject: PublishSubject<Float> = PublishSubject.create()
    val thirtySecondSpeedPublishSubject: PublishSubject<Float> = PublishSubject.create()
    val sixtySecondSpeedPublishSubject: PublishSubject<Float> = PublishSubject.create()

    init {
        EventBus.getDefault().register(this)
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
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
