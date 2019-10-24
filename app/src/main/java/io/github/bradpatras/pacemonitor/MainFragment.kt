package io.github.bradpatras.pacemonitor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.bradpatras.pacemonitor.customviews.PaceView
import io.github.bradpatras.pacemonitor.events.PermissionEvents
import io.github.bradpatras.pacemonitor.models.Pace
import io.github.bradpatras.pacemonitor.services.SpeedReportService
import io.github.bradpatras.pacemonitor.viewmodels.MainViewModel
import io.github.bradpatras.pacemonitor.utils.SpeedConversionHelper
import io.github.bradpatras.pacemonitor.validation.validate
import kotlinx.android.synthetic.main.main_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
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

        viewModel.fiveSecondSpeed.observe(this, speedReportObserverForPaceView(short_avg_pace_view))
        viewModel.thirtySecondSpeed.observe(this, speedReportObserverForPaceView(med_avg_pace_view))
        viewModel.sixtySecondSpeed.observe(this, speedReportObserverForPaceView(long_avg_pace_view))

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

    private fun speedReportObserverForPaceView(paceView: PaceView): Observer<Float> {
        return Observer { speedReport ->
            applySpeedToPaceView(speedReport, paceView)
        }
    }

    private fun applySpeedToPaceView(speed: Float, paceView: PaceView) {
        val pacePair = SpeedConversionHelper.metersPerSecondToMilePace(speed)
        val pace = Pace.fromPair(pacePair)?.validate()
        paceView.setPace(pace?.minutes, pace?.seconds)
    }
}
