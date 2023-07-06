package hr.vloboda.tarantulas.authentication.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.vloboda.tarantulas.TarantulasActivity
import hr.vloboda.tarantulas.authentication.viewModel.AuthState
import hr.vloboda.tarantulas.authentication.viewModel.AuthenticationViewModel
import hr.vloboda.tarantulas.databinding.FragmentLoginBinding
import hr.vloboda.tarantulas.model.auth.LoginDao
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthenticationViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            val loginDao = LoginDao(
                binding.Username.text.toString(),
                binding.Password.text.toString()
            )


            binding.loginProgressBar.visibility = View.VISIBLE
            viewModel.handleSignIn(loginDao)
        }

        binding.createText.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginProgressBar.visibility = View.INVISIBLE

        viewModel.handlePreviousLogin()

        viewModel.authState.observe(viewLifecycleOwner) { authState ->
            when (authState) {
                AuthState.Logout -> {

                }
                AuthState.Success -> {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    navigateToMainScreen()
                }
                else -> {
                    binding.loginProgressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(context, TarantulasActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
