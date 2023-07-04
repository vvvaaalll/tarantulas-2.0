package hr.vloboda.tarantulas.authentication.viewModel

import RestClient
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import hr.vloboda.tarantulas.AuthenticationActivity
import hr.vloboda.tarantulas.Tarantulas
import hr.vloboda.tarantulas.TarantulasActivity
import hr.vloboda.tarantulas.model.auth.AuthDao
import hr.vloboda.tarantulas.model.auth.LoginDao
import hr.vloboda.tarantulas.model.auth.RegisterDao
import hr.vloboda.tarantulas.repository.common.AuthAPIInterface
import hr.vloboda.tarantulas.repository.common.AuthRestAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthenticationViewModel : ViewModel() {

    private val restClient = RestClient()
    val authApi = restClient.getClient().create(AuthAPIInterface::class.java)


    private val _authState by lazy { MutableLiveData<AuthState>(AuthState.Idle) }

    val authState: LiveData<AuthState> = _authState

    fun handlePreviousLogin(){
        val sharedPreferences = Tarantulas.application.getSharedPreferences(
            "TarantulasPrefs",
            Context.MODE_PRIVATE
        )
        // Check if there is a token in shared preferences
        val authDaoJson = sharedPreferences.getString("authDao", null)
        val authDao: AuthDao? = Gson().fromJson(authDaoJson, AuthDao::class.java)
        if (authDao != null && authDao.token.token != null) {
            _authState.value = AuthState.Success
        }
    }

    fun handleSignIn(loginDao: LoginDao) {
        if (TextUtils.isEmpty(loginDao.username)) {
            _authState.value = AuthState.AuthError("Email is required")
            return
        }
        if (TextUtils.isEmpty(loginDao.username)) {
            _authState.value = AuthState.AuthError("Password is required")
            return
        }
        if (loginDao.password.length < 6) {
            _authState.value = AuthState.AuthError("Password must be at least 6 characters")
        }

        _authState.value = AuthState.Loading

        val call: Call<AuthDao>? = authApi!!.logIn(loginDao)

        call?.enqueue(object : Callback<AuthDao?> {
            override fun onResponse(
                call: Call<AuthDao?>?,
                response: Response<AuthDao?>
            ) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            Log.i("onSuccess", response.body().toString())

                            val authDaoJson = Gson().toJson(response.body())
                            val sharedPreferences = Tarantulas.application.getSharedPreferences(
                                "TarantulasPrefs",
                                Context.MODE_PRIVATE
                            )

                            val editor = sharedPreferences.edit()
                            editor.putString("authDao", authDaoJson)
                            editor.apply()

                            _authState.value = AuthState.Success

                        } catch (e: Exception) {
                            e.printStackTrace()
                            _authState.value = AuthState.AuthError("An error occurred")
                        }
                    } else {
                        Log.i(
                            "onEmptyResponse",
                            "Returned empty response"
                        )
                        _authState.value = AuthState.AuthError("Login failed")
                    }
                } else {
                    _authState.value = AuthState.AuthError("Login failed")
                    Log.i("onErrorResponse", "Login failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AuthDao?>?, t: Throwable) {
                Log.d("LoginBHai_error", "onFailure: Throw$t")
                _authState.value = AuthState.AuthError("Login failed")
            }
        })
    }

    fun handleSignUp(registerDao: RegisterDao) {
        if (TextUtils.isEmpty(registerDao.email)) {
            _authState.value = AuthState.AuthError("Email is required")
            return
        }
        if (TextUtils.isEmpty(registerDao.username)) {
            _authState.value = AuthState.AuthError("Username is required")
            return
        }
        if (TextUtils.isEmpty(registerDao.password)) {
            _authState.value = AuthState.AuthError("Password is required")
            return
        }
        if (registerDao.password.length < 6) {
            _authState.value = AuthState.AuthError("Password must be at least 6 characters")
            return
        }

        _authState.value = AuthState.Loading

        val call: Call<Any>? = authApi!!.register(registerDao)

        call?.enqueue(object : Callback<Any?> {
            override fun onResponse(call: Call<Any?>?, response: Response<Any?>) {
                if (response.isSuccessful()) {
                    if (response.code() == 201) {
                        // Registration successful, call handleSignIn
                        handleSignIn(LoginDao(registerDao.username, registerDao.password))
                    } else {
                        Log.i(
                            "onErrorResponse",
                            "Registration failed with code: ${response.code()}"
                        )
                        _authState.value = AuthState.AuthError("Registration failed")
                    }
                } else {
                    Log.i("onErrorResponse", "Registration failed with code: ${response.code()}")
                    _authState.value = AuthState.AuthError("Registration failed")
                }
            }

            override fun onFailure(call: Call<Any?>?, t: Throwable) {
                Log.d("RegisterError", "onFailure: Throw$t")
                _authState.value = AuthState.AuthError("Registration failed")
            }
        })
    }

    fun handleSignOut() {
        val authDaoJson = Gson().toJson(AuthDao())
        val sharedPreferences =
            Tarantulas.application.getSharedPreferences("TarantulasPrefs", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putString("authDao", authDaoJson)
        editor.apply()

        val context = Tarantulas.application
        val intent = Intent(context, AuthenticationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    class AuthError(val message: String? = null) : AuthState()
}
