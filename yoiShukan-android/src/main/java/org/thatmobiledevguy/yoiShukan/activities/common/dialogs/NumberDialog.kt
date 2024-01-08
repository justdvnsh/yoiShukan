package org.thatmobiledevguy.yoiShukan.activities.common.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.method.DigitsKeyListener
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import org.thatmobiledevguy.yoiShukan.YoiShukanApplication
import org.thatmobiledevguy.yoiShukan.R
import org.thatmobiledevguy.yoiShukan.core.models.Entry
import org.thatmobiledevguy.yoiShukan.databinding.CheckmarkPopupBinding
import org.thatmobiledevguy.yoiShukan.utils.InterfaceUtils
import org.thatmobiledevguy.yoiShukan.utils.requestFocusWithKeyboard
import org.thatmobiledevguy.yoiShukan.utils.sres
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.ParseException

class NumberDialog : AppCompatDialogFragment() {

    var onToggle: (Double, String) -> Unit = { _, _ -> }
    var onDismiss: () -> Unit = {}

    private var originalNotes: String = ""
    private var originalValue: Double = 0.0
    private lateinit var view: CheckmarkPopupBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val appComponent = (requireActivity().application as YoiShukanApplication).component
        val prefs = appComponent.preferences
        view = CheckmarkPopupBinding.inflate(LayoutInflater.from(context))
        arrayOf(view.yesBtn, view.skipBtn).forEach {
            it.setTextColor(requireArguments().getInt("color"))
        }
        arrayOf(view.noBtn, view.unknownBtn).forEach {
            it.setTextColor(view.root.sres.getColor(R.attr.contrast60))
        }
        arrayOf(view.yesBtn, view.noBtn, view.skipBtn, view.unknownBtn).forEach {
            it.typeface = InterfaceUtils.getFontAwesome(requireContext())
        }
        if (!prefs.isSkipEnabled) view.skipBtnNumber.visibility = View.GONE
        view.numberButtons.visibility = View.VISIBLE
        fixDecimalSeparator(view)
        originalNotes = requireArguments().getString("notes")!!
        originalValue = requireArguments().getDouble("value")
        view.notes.setText(originalNotes)
        view.value.setText(
            when {
                originalValue < 0.01 -> "0"
                else -> DecimalFormat("#.##").format(originalValue)
            }
        )
        view.value.setOnKeyListener { _, keyCode, event ->
            if (event.action == MotionEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                save()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        view.saveBtn.setOnClickListener {
            save()
        }
        view.skipBtnNumber.setOnClickListener {
            view.value.setText((Entry.SKIP.toDouble() / 1000).toString())
            save()
        }
        view.notes.setOnEditorActionListener { v, actionId, event ->
            save()
            true
        }
        view.value.requestFocusWithKeyboard()
        val dialog = Dialog(requireContext())
        dialog.setContentView(view.root)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        dialog.setOnDismissListener { onDismiss() }
        return dialog
    }

    private fun fixDecimalSeparator(view: CheckmarkPopupBinding) {
        // https://stackoverflow.com/a/34256139
        val separator = DecimalFormatSymbols.getInstance().decimalSeparator
        view.value.keyListener = DigitsKeyListener.getInstance("0123456789$separator")
    }

    fun save() {
        var value = originalValue
        try {
            val numberFormat = NumberFormat.getInstance()
            val valueStr = view.value.text.toString()
            value = numberFormat.parse(valueStr)!!.toDouble()
        } catch (e: ParseException) {
            // NOP
        }
        val notes = view.notes.text.toString()
        onToggle(value, notes)
        requireDialog().dismiss()
    }
}
