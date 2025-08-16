package com.dorrin.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dorrin.presentation.conversion.ConversionFragment
import com.dorrin.presentation.conversion.ConversionFragmentArgs
import com.dorrin.presentation.databinding.ActivityConvertBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConvertActivity : AppCompatActivity() {
  private var _binding: ActivityConvertBinding? = null
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    _binding = ActivityConvertBinding.inflate(layoutInflater)
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    handleIntent(intent)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    handleIntent(intent)
  }

  private fun handleIntent(intent: Intent) {
    val appLinkAction = intent.action
    val appLinkData: Uri? = intent.data

    Log.d("ConvertActivity", "appLinkAction: \"$appLinkAction\", appLinkData: \"$appLinkData\"")

    if (appLinkAction == Intent.ACTION_VIEW) {
      val bundle = bundleOf(
        *appLinkData
          ?.queryParameterNames
          ?.map { it to appLinkData.getQueryParameters(it)[0].replace("+", " ") }
          ?.toTypedArray()
          .let { it ?: emptyArray() }
      )
      Log.d("ConvertActivity", "bundle: \"$bundle\"")

      supportFragmentManager
        .beginTransaction()
        .add(
          binding.navHostFragmentContainer.id,
          ConversionFragment::class.java,
          bundle
        )
        .commit()
    }
  }
}