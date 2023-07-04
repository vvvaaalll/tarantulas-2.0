package hr.vloboda.tarantulas.model.auth

import com.google.gson.annotations.SerializedName

class RegisterDao(
    @SerializedName("username")
    var username: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("password")
    var password: String = ""
) {}