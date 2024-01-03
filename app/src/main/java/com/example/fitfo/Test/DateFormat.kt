package com.example.fitfo.Test

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class DateFormat {
    fun toFormattedString(dateTimeString: String): String {
            val instant = Instant.parse(dateTimeString)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

            val currentDate = LocalDate.now()
            val inputDate = dateTime.toLocalDate()

            return when {
                currentDate == inputDate -> {
                    // Cùng một ngày, hiển thị giờ:phút
                    dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                }
                currentDate.year == inputDate.year -> {
                    // Khác ngày, nhưng cùng năm, hiển thị ngày/tháng
                    dateTime.format(DateTimeFormatter.ofPattern("dd/MM"))
                }
                else -> {
                    // Khác năm, hiển thị ngày/tháng/năm
                    dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                }
            }
        }
    }