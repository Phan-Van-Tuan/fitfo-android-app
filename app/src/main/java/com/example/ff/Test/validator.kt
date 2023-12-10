package com.example.ff.Test
class Validator {

    fun isEmail(input: String): String? {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        if(!emailRegex.matches(input)) {
            return "Invalid email address"
        }
        return null
    }

    fun isPhoneNumber(input: String): String? {
        val phoneRegex = Regex("^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$")
        if(!phoneRegex.matches(input)) {
            return "Invalid phone number"
        }
        return null
    }

    fun isStrongPassword(input: String): String? {
        val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$")
        if(!passwordRegex.matches(input)) {
            return "Is not strong password"
        }
        return null
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