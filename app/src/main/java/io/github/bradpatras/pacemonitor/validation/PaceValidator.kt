package io.github.bradpatras.pacemonitor.validation

import io.github.bradpatras.pacemonitor.models.Pace

// Pace slower than 60 minutes is considered invalid
fun Pace.validate(): Pace? {
    return if (this.minutes >= 60) null else this
}