package app.taslimoseni.abcdef.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import app.taslimoseni.abcdef.R
import app.taslimoseni.abcdef.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        navController = (supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment).navController

        binding.toolbar.setNavigationOnClickListener { navController.navigateUp()}

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.title.text = when (destination.id) {
                R.id.myLessonsFragment -> getString(R.string.my_lessons)
                else -> getString(R.string.live_lessons)
            }
        }

    }
}