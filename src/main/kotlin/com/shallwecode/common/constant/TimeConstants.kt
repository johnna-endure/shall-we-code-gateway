package com.shallwecode.common.constant

import java.time.ZoneId
import java.time.ZoneOffset

object TimeConstants {
    val KST_TIME_ZONE_ID: ZoneId = ZoneId.of("Asia/Seoul")
    val KST_TIME_ZONE_OFFSET: ZoneOffset = ZoneOffset.ofHours(9)
}