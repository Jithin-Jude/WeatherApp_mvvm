package com.jithin.weatherapp

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jithin.weatherapp.databinding.LayoutWeatherBottomSheetBinding

class ForecastBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: LayoutWeatherBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var forecastDays: List<WeatherForecastDay>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutWeatherBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forecastDays = arguments?.getParcelableArrayList(ARG_FORECAST_DAYS) ?: emptyList()

        val adapter = WeatherForecastAdapter(forecastDays)
        binding.recyclerViewWeatherForecast.adapter = adapter
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCanceledOnTouchOutside(false)
        dialog.behavior.isDraggable = false
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_FORECAST_DAYS = "forecast_days"
        fun newInstance(forecastDays: List<WeatherForecastDay>): ForecastBottomSheetDialogFragment {
            val fragment = ForecastBottomSheetDialogFragment()
            val args = Bundle().apply {
                putParcelableArrayList(ARG_FORECAST_DAYS, ArrayList(forecastDays))
            }
            fragment.arguments = args
            return fragment
        }
    }
}