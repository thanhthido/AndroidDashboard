package com.thanhthido.androiddashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thanhthido.androiddashboard.R
import com.thanhthido.androiddashboard.data.remote.response.SensorData
import com.thanhthido.androiddashboard.databinding.ItemSensorDataBinding
import com.thanhthido.androiddashboard.utils.Constant.ERROR_MODE
import com.thanhthido.androiddashboard.utils.Constant.NORMAL_MODE
import java.text.SimpleDateFormat
import java.util.*

class SensorDataListAdapter :
    PagingDataAdapter<SensorData, SensorDataListAdapter.SensorDataViewHolder>(ITEM_COMPARATOR) {

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<SensorData>() {
            override fun areItemsTheSame(oldItem: SensorData, newItem: SensorData) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SensorData, newItem: SensorData) =
                oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorDataViewHolder {
        return SensorDataViewHolder(
            ItemSensorDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SensorDataViewHolder, position: Int) {
        val data = getItem(position) ?: return
        holder.bind(data, position)
    }


    inner class SensorDataViewHolder(private val binding: ItemSensorDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sensorData: SensorData, position: Int) {

            // binding text
            when (sensorData.type) {
                "temperature" -> {
                    binding.tvType.apply {
                        text = "Nhiệt độ"
                        setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.color_temp
                            )
                        )
                    }
                }
                "co" -> {
                    binding.tvType.apply {
                        text = "Nồng độ khí CO"
                        setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.color_co
                            )
                        )
                    }
                }
                "no2" -> {
                    binding.tvType.apply {
                        text = binding.root.context.getString(R.string.no2_title)
                        setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.color_no2
                            )
                        )
                    }
                }
                "ch4" -> {
                    binding.tvType.apply {
                        text = binding.root.context.getString(R.string.ch4_title)
                        setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.color_ch4
                            )
                        )
                    }
                }
                "pm1" -> {
                    binding.tvType.apply {
                        text = "Nồng độ bụi PM1.0"
                        setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.color_dust
                            )
                        )
                    }
                }
                "pm25" -> {
                    binding.tvType.apply {
                        text = "Nồng độ bụi PM2.5"
                        setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.color_dust
                            )
                        )
                    }
                }
                "pm10" -> {
                    binding.tvType.apply {
                        text = "Nồng độ bụi PM10.0"
                        setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.color_dust
                            )
                        )
                    }
                }
            }

            binding.tvId.text = "#${position + 1}"

            val date = Date(sensorData.time * 1000)
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa", Locale.getDefault())
            binding.tvDate.text = formatter.format(date)

            when (sensorData.event) {
                NORMAL_MODE -> {
                    binding.tvEvent.text = "Bình thường"
                    binding.tvEvent.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.color_event_normal_text
                        )
                    )
                    binding.containerTvEvent.background = ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.bg_normal_event
                    )
                    binding.containerItem.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.bg_card_normal
                        )
                    )
                }
                ERROR_MODE -> {
                    binding.tvEvent.text = "Bất thường"
                    binding.tvEvent.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.color_event_error_text
                        )
                    )
                    binding.containerTvEvent.background = ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.bg_error_event
                    )
                    binding.containerItem.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.bg_card_error
                        )
                    )
                }
            }

            binding.tvValue.text = "${sensorData.value}"
        }
    }


}