package com.everis.android.marvelkotlin.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.everis.android.marvelkotlin.R
import com.everis.android.marvelkotlin.data.network.error.NetworkFailure
import com.everis.android.marvelkotlin.databinding.FragmentHomeBinding
import com.everis.android.marvelkotlin.presentation.extension.hide
import com.everis.android.marvelkotlin.presentation.extension.observe
import com.everis.android.marvelkotlin.presentation.extension.show
import com.everis.android.marvelkotlin.presentation.home.adapter.CharacterAdapter
import com.everis.android.marvelkotlin.presentation.viewmodel.Result
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    private val homeViewModel by viewModel<HomeViewModel>()

    private var charactersAdapter: CharacterAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        charactersAdapter = CharacterAdapter(
            itemClick = { item ->
                homeViewModel.selectCharacter(item)
            }
        )

        with(binding.charactersRecycler) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(context, RecyclerView.VERTICAL).apply {
                    setDrawable(resources.getDrawable(R.drawable.separator, null))
                })
            adapter = charactersAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        homeViewModel.requestMoreCharacters()
                    }
                }
            })
        }
    }

    private fun initViewModel() {
        observe(homeViewModel.characters) { result ->
            when (result) {
                is Result.Success -> {
                    binding.run {
                        //swipeToRefresh.isRefreshing = false
                        progressBar.hide()
                    }
                    result.data?.let {
                        charactersAdapter?.run {
                            submitList(it)
                            notifyDataSetChanged()
                        }
                    }
                }
                is Result.Error -> {
                    binding.run {
                        //swipeToRefresh.isRefreshing = false
                        progressBar.hide()
                    }
                    val message = when (result.error) {
                        is NetworkFailure.NoInternetConnection -> "No connection"
                        else -> result.error.data.toString() ?: "ERROR"
                    }
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                }
                is Result.Loading<*> -> {
                    binding.progressBar.show()
                }
            }
        }
    }
}
