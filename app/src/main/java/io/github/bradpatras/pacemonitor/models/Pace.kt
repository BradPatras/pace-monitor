package io.github.bradpatras.pacemonitor.models

class Pace(val minutes: Int, val seconds: Int) {
    companion object {
        fun fromPair(pair: Pair<Int?, Int?>): Pace? {
            val minutes = pair.first ?: return null
            val seconds = pair.second ?: return null
            return Pace(minutes, seconds)
        }
    }
}

