package io.github.bradpatras.pacemonitor.util

class SpeedConversionHelper {
    companion object {
        fun metersPerSecondToMilePace(metersPerSec: Float): Pair<Int?, Int?> {
            // trim off small decimal places
            val temp = (metersPerSec * 10000).toInt()
            val rounded = temp / 10000f

            val mph = rounded * 2.23694f
            val paceMinutes = (60f / mph).toInt()
            val paceSeconds = (((60f / mph) % 1) * 60f).toInt()
            return Pair(paceMinutes, paceSeconds)
        }
    }
}