package com.everis.android.marvelkotlin.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.everis.android.marvelkotlin.databinding.FragmentDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding

    private val detailViewModel by viewModel<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {

    }

    private fun initViewModel() {

    }
}
