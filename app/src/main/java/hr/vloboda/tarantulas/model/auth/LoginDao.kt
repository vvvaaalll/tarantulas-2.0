package hr.vloboda.tarantulas.model.auth

import com.google.gson.annotations.SerializedName

data class LoginDao(
    @SerializedName("username")
    var username: String = "",
    @SerializedName("password")
    var password: String = ""
) {}