package hr.vloboda.tarantulas.authentication.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.vloboda.tarantulas.MainActivity
import hr.vloboda.tarantulas.authentication.viewModel.AuthState
import hr.vloboda.tarantulas.authentication.viewModel.AuthenticationViewModel
import hr.vloboda.tarantulas.databinding.ActivityAuthenticationBinding
import hr.vloboda.tarantulas.databinding.FragmentLoginBinding
import hr.vloboda.tarantulas.databinding.FragmentRegisterBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment(){

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthenticationViewModel by sharedViewModel()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (viewModel.authState.value == AuthState.Success){
            val intent = Intent(this.context, MainActivity::class.java)
            startActivity(intent)
        }

        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.createText.setOnClickListener {showRegisterFragment()}

        binding.loginButton.setOnClickListener {

            viewModel.handleSignIn(binding.EmailAdress.text.toString(),
                binding.Password.text.toString())
            if (viewModel.authState.value == AuthState.Success){
                val intent = Intent(this.context, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this.context, "Failed register", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }




    private fun showRegisterFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }
}