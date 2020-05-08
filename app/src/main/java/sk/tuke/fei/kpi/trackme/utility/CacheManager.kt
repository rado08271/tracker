package sk.tuke.fei.kpi.trackme.utility

import android.app.Activity
import android.content.Context

object CacheManager {
    private const val CACHE_NAME = "ACTIVITIES"
    fun addToCache(context: Context, tag: String, value: String) {
        context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE).edit().putString(tag, value).apply()
    }

    fun addToCache(context: Context, tag: String, value: Long) {
        context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE).edit().putLong(tag, value).apply()
    }

    fun addToCache(context: Context, tag: String, value: Boolean) {
        context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE).edit().putBoolean(tag, value).apply()
    }

    fun getValue(context: Context, tag: String, default: String): String {
        return context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE).getString(tag, default).orEmpty()
    }

    fun getValue(context: Context, tag: String, default: Long): Long {
        return context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE).getLong(tag, default)
    }

    fun getValue(context: Context, tag: String, default: Boolean): Boolean {
        return context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE).getBoolean(tag, default)
    }

}