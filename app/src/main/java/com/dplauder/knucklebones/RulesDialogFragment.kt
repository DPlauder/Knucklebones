package com.dplauder.knucklebones

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import android.widget.TextView
import android.view.WindowManager

class RulesDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_rules, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rulesTextView: TextView = view.findViewById(R.id.rules_text_view)
        rulesTextView.text = getString(R.string.rules_text)

        val closeButton: View = view.findViewById(R.id.close_button)
        closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setDimAmount(0.7f)
    }
}

