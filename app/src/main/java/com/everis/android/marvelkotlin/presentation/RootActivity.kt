package com.everis.android.marvelkotlin.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.everis.android.marvelkotlin.R
import com.everis.android.marvelkotlin.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.RootTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
