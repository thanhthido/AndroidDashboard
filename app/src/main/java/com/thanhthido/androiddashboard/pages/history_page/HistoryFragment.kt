package com.thanhthido.androiddashboard.pages.history_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.thanhthido.androiddashboard.adapters.SensorDataListAdapter
import com.thanhthido.androiddashboard.adapters.SensorDataListLoadStateAdapter
import com.thanhthido.androiddashboard.base.BaseFragment
import com.thanhthido.androiddashboard.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    private val viewModel: HistoryViewModel by viewModels()

    private val dataListAdapter by lazy {
        SensorDataListAdapter()
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHistoryBinding.inflate(inflater, container, false)

    override fun initControls(savedInstanceState: Bundle?) {
        initRecyclerView()
        loadStateListener()
        subscribeGetSensorData()
    }

    override fun initEvents() {
        binding.btnRetry.setOnClickListener {
            dataListAdapter.retry()
        }
    }

    private fun initRecyclerView() {
        binding.rvHistoryData.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            itemAnimator = null
            adapter = dataListAdapter.withLoadStateHeaderAndFooter(
                header = SensorDataListLoadStateAdapter {
                    dataListAdapter.retry()
                },
                footer = SensorDataListLoadStateAdapter {
                    dataListAdapter.retry()
                }
            )
        }
    }

    private fun subscribeGetSensorData() {
        viewModel.sensorDataResponse.observe(viewLifecycleOwner) {
            dataListAdapter.submitData(requireActivity().lifecycle, it)
        }
    }

    private fun loadStateListener() {
        dataListAdapter.addLoadStateListener { loadState ->
            binding.pbLoading.isVisible = loadState.source.refresh is LoadState.Loading

            binding.rvHistoryData.isVisible =
                loadState.source.refresh is LoadState.NotLoading &&
                        loadState.source.refresh !is LoadState.Error

            binding.tvHistoryFail.isVisible = loadState.source.refresh is LoadState.Error
            binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached &&
                dataListAdapter.itemCount < 1
            ) {
                binding.rvHistoryData.isVisible = false
                binding.tvHistoryEmpty.isVisible = true
            } else {
                binding.tvHistoryEmpty.isVisible = false
            }

        }
    }

}