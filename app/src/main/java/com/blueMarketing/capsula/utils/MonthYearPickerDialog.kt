package com.blueMarketing.capsula.utils

import android.app.Activity
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.blueMarketing.capsula.R
import java.util.*


class MonthYearPickerDialog(mContext: Activity, currentMonth: Int, currentYear: Int) :
    DialogFragment() {

    private val MAX_YEAR = 2099
    private var listener: OnDateSetListener? = null
    private var builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
    private var currentMonth = 0
    private var currentYear = 0

    fun setListener(listener: OnDateSetListener?) {
        this.listener = listener
    }

    init {
        this.currentMonth = currentMonth
        this.currentYear = currentYear
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Get the layout inflater
        val inflater = activity!!.layoutInflater
        val cal: Calendar = Calendar.getInstance()
        val dialog: View = inflater.inflate(R.layout.date_picker_dialog, null)
        val monthPicker = dialog.findViewById(R.id.picker_month) as NumberPicker
        val yearPicker = dialog.findViewById(R.id.picker_year) as NumberPicker
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = this.currentMonth
        val year: Int = this.currentYear
        yearPicker.minValue = cal.get(Calendar.YEAR)
        yearPicker.maxValue = MAX_YEAR
        yearPicker.value = year
        builder.setView(dialog) // Add action buttons
            .setPositiveButton(android.R.string.ok,
                DialogInterface.OnClickListener { dialog, id ->
                    run {
                        listener!!.onDateSet(
                            null,
                            yearPicker.value,
                            monthPicker.value,
                            0
                        )
                    }
                })
            .setNegativeButton(android.R.string.cancel,
                DialogInterface.OnClickListener { dialog, id -> this@MonthYearPickerDialog.dialog!!.cancel() })
        return builder.create()
    }

}