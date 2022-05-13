package com.thanhthido.androiddashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thanhthido.androiddashboard.databinding.HeaderFooterListBinding

class SensorDataListLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<SensorDataListLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(private val binding: HeaderFooterListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                pbHeaderFooter.isVisible = loadState is LoadState.Loading
                if (loadState is LoadState.Error) {
                    btnRetry.isVisible = true

                    errorMsg.text = "Không có kết nối internet"
                    errorMsg.isVisible = true
                } else {
                    btnRetry.isVisible = false
                    errorMsg.isVisible = false
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            HeaderFooterListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


}