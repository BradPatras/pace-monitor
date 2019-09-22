package io.github.bradpatras.pacemonitor.events

class SpeedReportEvents {
    data class FiveSecondReport(val speed: Float)
    data class ThirtySecondReport(val speed: Float)
    data class SixtySecondReport(val speed: Float)
}