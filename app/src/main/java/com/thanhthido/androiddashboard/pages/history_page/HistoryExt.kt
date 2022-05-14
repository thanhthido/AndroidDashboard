package com.thanhthido.androiddashboard.pages.history_page

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.thanhthido.androiddashboard.R
import com.thanhthido.androiddashboard.adapters.SensorDataListAdapter
import com.thanhthido.androiddashboard.adapters.SensorDataListLoadStateAdapter

fun HistoryFragment.subscribeGetSensorData(
    viewModel: HistoryViewModel,
    dataListAdapter: SensorDataListAdapter
) {
    viewModel.sensorDataResponse.observe(viewLifecycleOwner) {
        dataListAdapter.submitData(requireActivity().lifecycle, it)
    }
}

fun HistoryFragment.initRecyclerView(dataListAdapter: SensorDataListAdapter) {
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

fun HistoryFragment.initBottomSheet(): Dialog {
    val dialog = Dialog(requireContext())

    dialog.apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.bottom_sheet_filter)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.attributes?.windowAnimations = R.style.BottomSheetAnimation
        window?.setGravity(Gravity.BOTTOM)
        setCanceledOnTouchOutside(true)
    }

    return dialog
}


fun HistoryFragment.initDropDown(
    autoCompleteDropdownType: AutoCompleteTextView,
    autoCompleteDropdownEvent: AutoCompleteTextView
) {
    val historyType = resources.getStringArray(R.array.history_type)
    val typeAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, historyType)
    autoCompleteDropdownType.setAdapter(typeAdapter)

    val historyEvent = resources.getStringArray(R.array.history_event)
    val eventAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, historyEvent)
    autoCompleteDropdownEvent.setAdapter(eventAdapter)
}