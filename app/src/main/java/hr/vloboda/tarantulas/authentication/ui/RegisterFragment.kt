package hr.vloboda.tarantulas.authentication.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import hr.vloboda.tarantulas.Tarantulas
import hr.vloboda.tarantulas.TarantulasActivity
import hr.vloboda.tarantulas.authentication.viewModel.AuthState
import hr.vloboda.tarantulas.authentication.viewModel.AuthenticationViewModel
import hr.vloboda.tarantulas.databinding.FragmentRegisterBinding
import hr.vloboda.tarantulas.model.auth.AuthDao
import hr.vloboda.tarantulas.model.auth.RegisterDao
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: AuthenticationViewModel by sharedViewModel()

    val sharedPreferences = Tarantulas.application.getSharedPreferences(
        "TarantulasPrefs",
        Context.MODE_PRIVATE
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterBinding.inflate(layoutInflater)

        binding.registerButton.setOnClickListener {

            val registerDao = RegisterDao(
                binding.userName.text.toString(),
                binding.EmailAdress.text.toString(),
                binding.Password.text.toString()
            )

            viewModel.handleSignUp(registerDao)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.handlePreviousLogin()

        // Check if there is a non-expired token in shared preferences
        val authDaoJson = sharedPreferences.getString("authDao", null)
        val authDao: AuthDao? = Gson().fromJson(authDaoJson, AuthDao::class.java)
        if (authDao != null && authDao.token.token != null) {
            navigateToMainScreen()
        }

        viewModel.authState.observe(viewLifecycleOwner) { authState ->
            when (authState) {
                AuthState.Success -> {
                    Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                    navigateToMainScreen()
                }
                else -> {
                    Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.createText.setOnClickListener { showLoginFragment() }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(context, TarantulasActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Optional: Finish the current activity
    }

    private fun showLoginFragment() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(action)
    }
}
