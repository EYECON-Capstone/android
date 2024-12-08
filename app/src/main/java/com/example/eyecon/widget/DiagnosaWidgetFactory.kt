package com.example.eyecon.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.eyecon.R
import com.example.eyecon.data.dataclass.Diagnosa
import com.example.eyecon.ui.diagnosa.DetailDiagnosaActivity
import com.example.eyecon.ui.diagnosa.DiagnosaViewModel

class DiagnosaWidgetFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {
    companion object {
        private const val TAG = "DiagnosaWidgetFactory"
    }

    private var diagnosaList = mutableListOf<Diagnosa>()
    private val viewModel = DiagnosaViewModel()

    override fun onCreate() {
        Log.d(TAG, "onCreate called")
        loadDiagnosaData()
    }

    override fun onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged called")
        loadDiagnosaData()
    }

    private fun loadDiagnosaData() {
        Log.d(TAG, "Loading diagnosa data...")
        try {
            viewModel.loadDiagnosa(context)
            viewModel.diagnosaList.value?.let { list ->
                diagnosaList.clear()
                diagnosaList.addAll(list)
                Log.d(TAG, "Data loaded successfully. Size: ${diagnosaList.size}")
                diagnosaList.forEach { diagnosa ->
                    Log.d(TAG, "Loaded diagnosa: id=${diagnosa.id}, title=${diagnosa.title}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading data", e)
        }
    }

    override fun getViewAt(position: Int): RemoteViews {
        Log.d(TAG, "getViewAt position: $position")

        if (position < 0 || position >= diagnosaList.size) {
            Log.e(TAG, "Invalid position: $position")
            return RemoteViews(context.packageName, R.layout.diagnosa_widget_item).apply {
                setTextViewText(R.id.widget_title, "Data tidak tersedia")
            }
        }

        val diagnosa = diagnosaList[position]
        Log.d(TAG, "Creating view for diagnosa: id=${diagnosa.id}, title=${diagnosa.title}")

        return RemoteViews(context.packageName, R.layout.diagnosa_widget_item).apply {
            setImageViewResource(R.id.widget_image, diagnosa.imageResId)
            setTextViewText(R.id.widget_title, diagnosa.title)

            val fillInIntent = Intent().apply {
                putExtra(DetailDiagnosaActivity.EXTRA_DIAGNOSA_ID, diagnosa.id)
                putExtra("DIAGNOSA_TITLE", diagnosa.title)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            setOnClickFillInIntent(R.id.widget_item_container, fillInIntent)

            Log.d(TAG, "FillInIntent created with diagnosaId: ${diagnosa.id}")
        }
    }

    override fun getCount(): Int = diagnosaList.size
    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
        diagnosaList.clear()
    }
}