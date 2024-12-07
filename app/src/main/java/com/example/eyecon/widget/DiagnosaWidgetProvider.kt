package com.example.eyecon.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.example.eyecon.R
import com.example.eyecon.ui.diagnosa.DetailDiagnosaActivity

class DiagnosaWidgetProvider : AppWidgetProvider() {
    companion object {
        private const val TAG = "DiagnosaWidgetProvider"
        const val ACTION_VIEW_DIAGNOSA = "com.example.eyecon.ACTION_VIEW_DIAGNOSA"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            Log.d(TAG, "Updating widget ID: $appWidgetId")
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.diagnosa_widget).apply {
            val serviceIntent = Intent(context, DiagnosaWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse("content://com.example.eyecon.widget/id/$appWidgetId")
            }
            setRemoteAdapter(R.id.widget_list_view, serviceIntent)

            val clickIntent = Intent(context, DetailDiagnosaActivity::class.java).apply {
                action = ACTION_VIEW_DIAGNOSA
                addCategory(Intent.CATEGORY_DEFAULT)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

            setPendingIntentTemplate(R.id.widget_list_view, pendingIntent)

            setEmptyView(R.id.widget_list_view, R.id.empty_view)
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
        Log.d(TAG, "Widget updated successfully")

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_VIEW_DIAGNOSA) {
            Log.d(TAG, "Received ACTION_VIEW_DIAGNOSA")
        }
    }
}