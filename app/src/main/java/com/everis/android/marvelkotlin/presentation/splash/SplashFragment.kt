package com.everis.android.marvelkotlin.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.everis.android.marvelkotlin.databinding.FragmentSplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding

    private val splashViewModel by viewModel<SplashViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        splashViewModel.load()
    }
}
