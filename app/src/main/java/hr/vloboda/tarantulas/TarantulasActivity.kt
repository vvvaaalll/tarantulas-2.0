package hr.vloboda.tarantulas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import hr.vloboda.tarantulas.databinding.ActivityMainBinding
import hr.vloboda.tarantulas.databinding.ActivityTarantulasBinding

class TarantulasActivity : AppCompatActivity() {

    lateinit var binding: ActivityTarantulasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTarantulasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}