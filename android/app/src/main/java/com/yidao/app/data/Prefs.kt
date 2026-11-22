package com.yidao.app.data

import android.content.Context

/** 轻量持久化：首次启动引导状态 + 老人模式开关。 */
object Prefs {
    private const val NAME = "yidao_prefs"
    private const val KEY_ONBOARDED = "onboarded"
    private const val KEY_ELDERLY = "elderly_mode"

    fun isOnboarded(context: Context): Boolean =
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ONBOARDED, false)

    fun elderlyMode(context: Context): Boolean =
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ELDERLY, false)

    fun setMode(context: Context, elderly: Boolean) {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ELDERLY, elderly)
            .putBoolean(KEY_ONBOARDED, true)
            .apply()
    }
}
