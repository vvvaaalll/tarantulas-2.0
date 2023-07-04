package hr.vloboda.tarantulas.model.auth

import com.google.gson.annotations.SerializedName
import hr.vloboda.tarantulas.model.User

data class AuthDao (
    @SerializedName("auth-token")
    var token: AuthToken = AuthToken(),
    @SerializedName("user")
    var user: User = User()
) {}



data class AuthToken(
    @SerializedName("token")
    var token: String = ""){}