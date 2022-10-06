package hr.vloboda.tarantulas.authentication.viewModel

import android.content.ContentValues.TAG
import android.text.TextUtils
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel : ViewModel() {



    private val _authState by lazy { MutableLiveData<AuthState>(AuthState.Idle) }

    val authState: LiveData<AuthState> = _authState

    fun handleSignIn(email: String, password: String) {
        if (TextUtils.isEmpty(email)) {
            _authState.value = AuthState.AuthError("Email is required")
            return
        }
        if (TextUtils.isEmpty(password)) {
            _authState.value = AuthState.AuthError("Password is required")
            return
        }
        if (password.length < 6) {
            _authState.value = AuthState.AuthError("Password must me at least 6 characters")
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i(TAG, "Email signup is successful")
                _authState.value = AuthState.Success
            } else {
                task.exception?.let {
                    Log.i(TAG, "Email signup failed with error ${it.localizedMessage}")
                    _authState.value = AuthState.AuthError(it.localizedMessage)
                }
            }
        }
    }

    fun handleSignUp(email: String, password: String) {
        if (TextUtils.isEmpty(email)) {
            _authState.value = AuthState.AuthError("Email is required")
            return
        }
        if (TextUtils.isEmpty(password)) {
            _authState.value = AuthState.AuthError("Password is required")
            return
        }
        if (password.length < 6) {
            _authState.value = AuthState.AuthError("Password must me at least 6 characters")
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email, password
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "Email signup is successful")
                    _authState.value = AuthState.Success
                } else {
                    task.exception?.let {
                        Log.i(TAG, "Email signup failed with error ${it.localizedMessage}")
                        _authState.value = AuthState.AuthError(it.localizedMessage)
                    }
                }
            }
    }

    fun resetPassword(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "reset link sent")
                } else {
                    task.exception?.let {
                        Log.i(TAG, "sending reset mail failed")

                    }
                }

            }

    }

}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    class AuthError(val message: String? = null) : AuthState()
}