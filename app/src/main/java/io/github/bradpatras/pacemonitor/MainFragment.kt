package io.github.bradpatras.pacemonitor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.github.bradpatras.pacemonitor.customviews.PaceView
import io.github.bradpatras.pacemonitor.events.PermissionEvents
import io.github.bradpatras.pacemonitor.services.speed.SpeedReportService
import io.github.bradpatras.pacemonitor.viewmodels.MainViewModel
import io.github.bradpatras.pacemonitor.util.SpeedConversionHelper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.main_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        compositeDisposable.addAll(
            viewModel.fiveSecondSpeedPublishSubject.subscribe(speedReportConsumerForPaceView(short_avg_pace_view)),
            viewModel.thirtySecondSpeedPublishSubject.subscribe(speedReportConsumerForPaceView(med_avg_pace_view)),
            viewModel.sixtySecondSpeedPublishSubject.subscribe(speedReportConsumerForPaceView(long_avg_pace_view))
        )
        viewModel.publishLastReports()
    }

    @Subscribe(sticky = true)
    fun onLocationPermissionGranted(event: PermissionEvents.LocationPermissionGrantedEvent) {
        EventBus.getDefault().removeStickyEvent(event)
        context?.let { ctx ->
            startSpeedReportService(ctx)
        }
    }

    private fun startSpeedReportService(context: Context) {
        val intent = Intent(context, SpeedReportService::class.java)
        context.startService(intent)
    }

    private fun speedReportConsumerForPaceView(paceView: PaceView): Consumer<in Float> {
        return Consumer { speedReport ->
            applySpeedToPaceView(speedReport, paceView)
        }
    }

    private fun applySpeedToPaceView(speed: Float, paceView: PaceView) {
        val pace = SpeedConversionHelper.metersPerSecondToMilePace(speed)
        if (pace.first ?: 100 > 99) {
            paceView.paceMinutesConsumer.accept(null)
            paceView.paceSecondsConsumer.accept(null)
        } else {
            paceView.paceMinutesConsumer.accept(pace.first)
            paceView.paceSecondsConsumer.accept(pace.second)
        }
    }
}
