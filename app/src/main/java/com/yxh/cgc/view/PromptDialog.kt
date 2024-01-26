package com.yxh.cgc.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import com.yxh.cgc.R

class PromptDialog(private val promptDialogClickListener: PromptDialogClickListener): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_prompt, container, false)
        view.findViewById<Button>(R.id.close).setOnClickListener {
            promptDialogClickListener.onClick(this@PromptDialog, 1)
        }
        view.findViewById<Button>(R.id.cancel).setOnClickListener {
            promptDialogClickListener.onClick(this@PromptDialog, 2)
        }
        val drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.shape_rounded_white)
// val drawable = ColorDrawable(Color.TRANSPARENT) //创建透明颜色drawable
        dialog?.window?.setBackgroundDrawable(drawable)
        dialog?.window?.setDimAmount(0f)
        return view
    }
}