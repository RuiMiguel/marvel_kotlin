package com.everis.android.marvelkotlin.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.everis.android.marvelkotlin.R
import com.everis.android.marvelkotlin.data.network.error.NetworkFailure
import com.everis.android.marvelkotlin.databinding.FragmentDetailBinding
import com.everis.android.marvelkotlin.presentation.extension.hide
import com.everis.android.marvelkotlin.presentation.extension.observe
import com.everis.android.marvelkotlin.presentation.extension.show
import com.everis.android.marvelkotlin.presentation.viewmodel.Result
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    val args: DetailFragmentArgs by navArgs()

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
        observe(detailViewModel.character) { result ->
            binding.run {
                when (result) {
                    is Result.Success -> {
                        progressBar.hide()

                        result.data?.let {
                            toolbar.title = it.name

                            val imageUrl = "${it.thumbnail.path}/landscape_xlarge.${it.thumbnail.extension}"
                            if (imageUrl.contains("image_not_available")) {
                                toolbarImage.load(R.drawable.placeholder)
                            } else {
                                toolbarImage.load(imageUrl) {
                                    crossfade(true)
                                    toolbarImage.scaleType = ImageView.ScaleType.CENTER
                                    placeholder(R.drawable.placeholder)
                                }
                            }

                            content.description.text = result.data.description
                        }
                    }
                    is Result.Error -> {
                        progressBar.hide()
                        val message = when (result.error) {
                            is NetworkFailure.NoInternetConnection -> "No connection"
                            else -> result.error.data.toString() ?: "ERROR"
                        }
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                    }
                    is Result.Loading<*> -> {
                        progressBar.show()
                    }
                }
            }
        }
        detailViewModel.requestForCharacter(args.characterId)
    }
}
