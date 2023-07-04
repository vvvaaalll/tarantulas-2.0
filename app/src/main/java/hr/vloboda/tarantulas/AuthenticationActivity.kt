package hr.vloboda.tarantulas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import hr.vloboda.tarantulas.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}