package com.everis.android.marvelkotlin.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.everis.android.marvelkotlin.R
import com.everis.android.marvelkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MainTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}