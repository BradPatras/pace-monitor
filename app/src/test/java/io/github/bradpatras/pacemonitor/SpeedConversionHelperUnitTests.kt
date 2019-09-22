package io.github.bradpatras.pacemonitor

import io.github.bradpatras.pacemonitor.util.SpeedConversionHelper
import org.junit.Assert
import org.junit.Test

class SpeedConversionHelperUnitTests {
    @Test
    fun metersPerSecondToMilePace() {
        Assert.assertEquals(SpeedConversionHelper.metersPerSecondToMilePace(1.34112f), Pair(20, 0))
        Assert.assertEquals(SpeedConversionHelper.metersPerSecondToMilePace(2.68224f), Pair(10, 0))
        Assert.assertEquals(SpeedConversionHelper.metersPerSecondToMilePace(2.248611f), Pair(11, 55))
    }
}