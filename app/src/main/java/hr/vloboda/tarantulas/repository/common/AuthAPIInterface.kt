package hr.vloboda.tarantulas.repository.common

import hr.vloboda.tarantulas.model.auth.AuthDao
import hr.vloboda.tarantulas.model.auth.LoginDao
import hr.vloboda.tarantulas.model.auth.RegisterDao
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST



interface AuthAPIInterface {

        @POST("/api/token/generate-token")
        fun logIn(@Body logInDao: LoginDao): Call<AuthDao>

        @POST("/api/users/signup")
        fun register(@Body registerDao: RegisterDao): Call<ResponseBody>

}