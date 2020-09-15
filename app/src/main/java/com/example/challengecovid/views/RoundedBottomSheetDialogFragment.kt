package com.example.challengecovid.views

/**
 * BottomSheetDialog fragment that uses a custom theme which sets a rounded background to the dialog
 * and doesn't dim the navigation bar
 */
/*
open class RoundedBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)


    override fun onStart() {
        super.onStart()

        dialog?.let { dialog ->
            if ( dialog.window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                dialog.window?.findViewById<View>(com.google.android.material.R.id.container)?.fitsSystemWindows = false

                // set flag for dark navigation bar icons
                dialog.window?.decorView?.apply {
                    systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                }
            }
        }
    }
}

 */