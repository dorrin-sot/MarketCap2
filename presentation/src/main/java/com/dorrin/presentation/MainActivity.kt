package com.dorrin.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dorrin.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  private var _binding: ActivityMainBinding? = null
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    _binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
      insets
    }

    setupBottomNavigation()
  }

  private fun setupBottomNavigation() {
    val navView = binding.bottomNav
    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
    val navController = navHostFragment.findNavController()
    val appBarConfiguration = AppBarConfiguration(
      setOf(R.id.navigation_conversion, R.id.navigation_list)
    )
    setupActionBarWithNavController(navController, appBarConfiguration)
    navView.setupWithNavController(navController)

    navView.setOnItemSelectedListener { item ->
      if (item.itemId != navView.selectedItemId) {
        navController.popBackStack(item.itemId, inclusive = true, saveState = false)
        navController.navigate(item.itemId)
      }
      true
    }
  }
}
