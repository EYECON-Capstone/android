package com.example.eyecon.widget

import android.content.Intent
import android.util.Log
import android.widget.RemoteViewsService

class DiagnosaWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        Log.d("DiagnosaWidgetService", "Creating new RemoteViewsFactory")
        return DiagnosaWidgetFactory(applicationContext)
    }
}