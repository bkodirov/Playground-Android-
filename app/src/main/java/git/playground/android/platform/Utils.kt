package git.playground.android.platform

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

object Utils {
    fun hideKeyboard(activity: AppCompatActivity) {
        // Check if no view has focus:
        val view = activity.currentFocus
        view?.let { v ->
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
        }
    }

    fun hideKeyboard(focused: View) {
        // Check if no view has focus:
        val imm = focused.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.let { it.hideSoftInputFromWindow(focused.windowToken, 0) }
    }
}