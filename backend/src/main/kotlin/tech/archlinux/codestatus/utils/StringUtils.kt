package tech.archlinux.codestatus.utils

import java.time.OffsetDateTime

object StringUtils {
    fun String.toOffsetDateTime(): OffsetDateTime = OffsetDateTime.parse(this)
}
