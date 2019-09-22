package io.github.bradpatras.pacemonitor.ui.main

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.bradpatras.pacemonitor.R
import io.github.bradpatras.pacemonitor.services.speed.SpeedReportService
import io.github.bradpatras.pacemonitor.util.SpeedConversionHelper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var fiveSecondReportConsumer: Consumer<in Float> = Consumer(this::consumeFiveSecondSpeedReport)
    private var thirtySecondReportConsumer: Consumer<in Float> = Consumer(this::consumeThirtySecondSpeedReport)
    private var sixtySecondReportConsumer: Consumer<in Float> = Consumer(this::consumeSixtySecondSpeedReport)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startstop_btn.setOnClickListener(this::startStopClicked)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        compositeDisposable.addAll(
            viewModel.fiveSecondSpeedPublishSubject.subscribe(fiveSecondReportConsumer),
            viewModel.thirtySecondSpeedPublishSubject.subscribe(thirtySecondReportConsumer),
            viewModel.sixtySecondSpeedPublishSubject.subscribe(sixtySecondReportConsumer)
        )
    }

    private fun consumeFiveSecondSpeedReport(speed: Float) {
        val pace = SpeedConversionHelper.metersPerSecondToMilePace(speed)
        if (pace.first ?: 100 > 99) {
            short_avg_pace_view.paceMinutesConsumer.accept(null)
            short_avg_pace_view.paceSecondsConsumer.accept(null)
        } else {
            short_avg_pace_view.paceMinutesConsumer.accept(pace.first)
            short_avg_pace_view.paceSecondsConsumer.accept(pace.second)
        }

    }

    private fun consumeThirtySecondSpeedReport(speed: Float) {
        val pace = SpeedConversionHelper.metersPerSecondToMilePace(speed)

        if (pace.first ?: 100 > 99) {
            med_avg_pace_view.paceMinutesConsumer.accept(null)
            med_avg_pace_view.paceSecondsConsumer.accept(null)
        } else {
            med_avg_pace_view.paceMinutesConsumer.accept(pace.first)
            med_avg_pace_view.paceSecondsConsumer.accept(pace.second)
        }
    }

    private fun consumeSixtySecondSpeedReport(speed: Float) {
        val pace = SpeedConversionHelper.metersPerSecondToMilePace(speed)
        if (pace.first ?: 100 > 99) {
            long_avg_pace_view.paceMinutesConsumer.accept(null)
            long_avg_pace_view.paceSecondsConsumer.accept(null)
        } else {
            long_avg_pace_view.paceMinutesConsumer.accept(pace.first)
            long_avg_pace_view.paceSecondsConsumer.accept(pace.second)
        }
    }

    private fun startStopClicked(btn: View) {
        context?.let { ctx ->
            val intent = Intent(ctx, SpeedReportService::class.java)
            ctx.startService(intent)
        }
    }
}
