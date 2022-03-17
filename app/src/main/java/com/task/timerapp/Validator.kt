package com.task.timerapp

import java.util.regex.Pattern

object Validator {

    fun validateInput(hour: String, minute: String, second: String): Boolean {
        val regex = "[0-9]+"
        val p: Pattern = Pattern.compile(regex)
        if (hour.isEmpty().not() && p.matcher(hour).matches().not()) {
            return false
        } else if (minute.isEmpty().not() && p.matcher(minute).matches().not()) {
            return false
        } else if (second.isEmpty().not() && p.matcher(second).matches().not()) {
            return false
        } else if (hour.isEmpty() && minute.isEmpty() && second.isEmpty()) {
            return false
        } else if (hour.length > 2 || minute.length > 2 && second.length > 2) {
            return false
        }
        return true

    }
}
