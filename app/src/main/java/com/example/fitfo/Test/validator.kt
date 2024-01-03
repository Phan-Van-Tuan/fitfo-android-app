package com.example.fitfo.Test

class Validator {

    fun isEmail(input: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(input)
    }

    fun isPhoneNumber(input: String): Boolean {
        val phoneRegex =
            Regex("^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$")
        return phoneRegex.matches(input)
    }

    //    fun isStrongPassword(input: String): Boolean? {
//        val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$")
//        return passwordRegex.matches(input)
//    }
    fun isStrongPassword(input: String): Boolean {
        return hasMinimumLength(input) &&
                containsUppercaseLetter(input) &&
                containsDigit(input) &&
                containsSpecialCharacter(input)
    }

    fun hasMinimumLength(input: String): Boolean {
        val minLength = 8
        return input.length >= minLength
    }

    fun containsUppercaseLetter(input: String): Boolean {
        val uppercaseRegex = Regex("[A-Z]")
        return uppercaseRegex.containsMatchIn(input)
    }

    fun containsDigit(input: String): Boolean {
        val digitRegex = Regex("\\d")
        return digitRegex.containsMatchIn(input)
    }

    fun containsSpecialCharacter(input: String): Boolean {
        val specialCharRegex = Regex("[@\$!%*#?&]")
        return specialCharRegex.containsMatchIn(input)
    }


    fun isString(input: String): Boolean {
        return input.isNotBlank()
    }

    fun isNumber(input: String): Boolean {
        return input.toDoubleOrNull() != null
    }

    fun isRequired(input: String?): Boolean {
        return !input.isNullOrBlank()
    }
}