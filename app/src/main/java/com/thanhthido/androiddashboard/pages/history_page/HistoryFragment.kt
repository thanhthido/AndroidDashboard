package com.thanhthido.androiddashboard.pages.history_page

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.google.gson.Gson
import com.thanhthido.androiddashboard.R
import com.thanhthido.androiddashboard.adapters.SensorDataListAdapter
import com.thanhthido.androiddashboard.base.BaseFragment
import com.thanhthido.androiddashboard.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    private val viewModel: HistoryViewModel by viewModels()

    private val dataListAdapter by lazy {
        SensorDataListAdapter()
    }

    private lateinit var bottomSheetDialog: Dialog

    private lateinit var autoCompleteDropdownType: AutoCompleteTextView
    private lateinit var autoCompleteDropdownEvent: AutoCompleteTextView
    private lateinit var btnSearch: Button

    private val listener: (CombinedLoadStates) -> Unit = { loadState ->
        binding.pbLoading.isVisible = loadState.source.refresh is LoadState.Loading
        binding.refreshRv.isRefreshing = false

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

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHistoryBinding.inflate(inflater, container, false)

    override fun initControls(savedInstanceState: Bundle?) {

        bottomSheetDialog = initBottomSheet()
        autoCompleteDropdownType = bottomSheetDialog.findViewById(R.id.dropdown_type)
        autoCompleteDropdownEvent = bottomSheetDialog.findViewById(R.id.dropdown_event)
        btnSearch = bottomSheetDialog.findViewById(R.id.btn_search_history)

        initRecyclerView(dataListAdapter)
        loadStateListener()
        subscribeGetSensorData(viewModel, dataListAdapter)
    }

    override fun onResume() {
        super.onResume()
        initDropDown(autoCompleteDropdownType, autoCompleteDropdownEvent)
    }

    override fun initEvents() {
        binding.btnRetry.setOnClickListener {
            dataListAdapter.retry()
        }
        binding.btnFilter.setOnClickListener {
            bottomSheetDialog.show()
        }
        btnSearch.setOnClickListener {
            binding.rvHistoryData.smoothScrollToPosition(0)
            filterData()
            bottomSheetDialog.dismiss()
        }
        binding.refreshRv.setOnRefreshListener {
            binding.refreshRv.isRefreshing = true
            dataListAdapter.refresh()
        }
    }

    private fun filterData() {
        val typeVietnamese = autoCompleteDropdownType.text.toString()
        val eventVietnamese = autoCompleteDropdownEvent.text.toString()

        var type = "all"
        var event = "all"

        when (typeVietnamese) {
            "Tất cả thông số" -> {
                type = "all"
            }
            "Nhiệt độ" -> {
                type = "temperature"
            }
            "Nồng độ khí CO" -> {
                type = "co"
            }
            "Nồng độ khí NO2" -> {
                type = "no2"
            }
            "Nồng độ khí CH4" -> {
                type = "ch4"
            }
            "Nồng độ PM2.5" -> {
                type = "pm25"
            }
            "Nồng độ PM1.0" -> {
                type = "pm1"
            }
            "Nồng độ PM10.0" -> {
                type = "pm10"
            }
        }

        when (eventVietnamese) {
            "Tất cả trạng thái" -> {
                event = "all"
            }
            "Bình thường" -> {
                event = "normal"
            }
            "Bất thường" -> {
                event = "error"
            }
        }

        val queryString = Gson().toJson(Pair(type, event))
        viewModel.searchSensorDataBasedOnType(queryString)
    }

    private fun loadStateListener() {
        dataListAdapter.addLoadStateListener(listener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataListAdapter.removeLoadStateListener(listener)
    }

}