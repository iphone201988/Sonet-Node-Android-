package com.tech.sonet.utils.event

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tech.sonet.R
import com.tech.sonet.databinding.SheetNoInternetBinding

class NoInternetSheet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SheetNoInternetBinding =
            DataBindingUtil.inflate(inflater, R.layout.sheet_no_internet, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        bottomSheetDialog.setOnShowListener { dialog ->
            val sheetDialog = dialog as BottomSheetDialog
            val bottomSheet =
                sheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                val layoutParams = sheet.layoutParams
                val windowHeight = getWindowHeight()

                layoutParams?.let {
                    it.height = windowHeight
                    sheet.layoutParams = it
                }

                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = false // disable dragging
            }
        }
    }


    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (requireActivity())!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

}