package com.tech.sonet.ui.settings

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.work.WorkManager
import com.tech.sonet.R
import com.tech.sonet.data.local.CustomSharedPrefManager
import com.tech.sonet.data.model.SettingApiResponse
import com.tech.sonet.databinding.DeletePopupBinding
import com.tech.sonet.databinding.FragmentSettingsBinding
import com.tech.sonet.databinding.LogoutPopupBinding
import com.tech.sonet.databinding.ReportDialogBinding
import com.tech.sonet.ui.base.BaseCustomDialog
import com.tech.sonet.ui.base.BaseFragment
import com.tech.sonet.ui.base.BaseViewModel
import com.tech.sonet.ui.homepage.HomePageActivity
import com.tech.sonet.ui.web_view.WebActivity
import com.tech.sonet.ui.welcome.WelcomeActivity
import com.tech.sonet.utils.Constant
import com.tech.sonet.utils.ImageBindingUtil
import com.tech.sonet.utils.Status
import com.tech.sonet.utils.showSuccessToast
import com.tech.sonet.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(), BaseCustomDialog.Listener {
    private var locationIsActive: String? = null
    private val viewModel: SettingsFragmentVM by viewModels()
    lateinit var dialogLogout: BaseCustomDialog<LogoutPopupBinding>
    lateinit var dialogReport: BaseCustomDialog<ReportDialogBinding>
    lateinit var dialogDelete: BaseCustomDialog<DeletePopupBinding>
    private lateinit var  secondSharedPrefManager : CustomSharedPrefManager

    //private val shareText = "Hello, sharing via intent!"
    private var shareText : String ? =null
    var location: Int? = null

    override fun getLayoutResource(): Int {
        return R.layout.fragment_settings
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        initOnClick()
        initObserver()
        secondSharedPrefManager = CustomSharedPrefManager(requireContext())
        Log.i("sdfsdff", "location: ${sharedPrefManager.getLocation()}")

        val opinion  = sharedPrefManager.getOpinion()
        if (opinion?.isNotEmpty() == true){
            binding.tvOpinion.setText(opinion)
        }


        if (sharedPrefManager.getLocation() == true) {

            binding.btnlocation.isChecked = true
            binding.btnlocation.trackDrawable = context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.switch_button_background
                )
            }
        } else {
            binding.btnlocation.isChecked = false
            WorkManager.getInstance(requireContext()).cancelAllWork()
            binding.btnlocation.trackDrawable = context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.toggle_btn_background
                )
            }
        }

        Log.i("sdfsdff", "notification: ${sharedPrefManager.getPushNotification()}")

        if (sharedPrefManager.getPushNotification() == true) {
            binding.btnpushnotification.isChecked = true
            binding.btnpushnotification.trackDrawable = context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.switch_button_background
                )
            }
        } else {
            binding.btnpushnotification.isChecked = false
            binding.btnpushnotification.trackDrawable = context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.toggle_btn_background
                )
            }
        }

        binding.tvOpinion.addTextChangedListener(textWatcher)
        shareText

    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val count = s?.length ?: 0
            binding.textNumber.text = "$count/150"
        }
    }


    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.text_terms -> {
                    val intent = Intent(requireContext(), WebActivity::class.java)
                    intent.putExtra("url","https://sonet-singles-nearby.com/terms-and-conditions" )
                    intent.putExtra("heading", "Terms & Conditions")
                    startActivity(intent)
//                    val legal = Intent(
//                        "android.intent.action.VIEW", Uri.parse("https://sonet-singles-nearby.com/terms-and-conditions")
//                    )
//                    startActivity(legal)
                }
                R.id.text_tutorial ->{
                    val intent = Intent(requireContext(), WebActivity::class.java)
                    intent.putExtra("url","https://sonet-singles-nearby.com/quick-tutorial" )
                    intent.putExtra("heading", "Quick Tutorial")
                    startActivity(intent)
//                    val legal = Intent(
//                        "android.intent.action.VIEW", Uri.parse("https://sonet-singles-nearby.com/quick-tutorial/")
//                    )
//                    startActivity(legal)
                }

                R.id.text_Report -> {
                    reportDilog()
                }
                R.id.btnpushnotification -> {
                    if (binding.btnpushnotification.isChecked) {
                        notification(true)
                        binding.btnpushnotification.trackDrawable = context?.let {
                            ContextCompat.getDrawable(
                                it, R.drawable.switch_button_background
                            )
                        }

                    } else {
                        notification(false)
                        WorkManager.getInstance(requireContext()).cancelAllWork()
                        binding.btnpushnotification.trackDrawable = context?.let {
                            ContextCompat.getDrawable(
                                it, R.drawable.toggle_btn_background
                            )
                        }
                    }
                }
                R.id.text_Contact -> {
                    openGmailCompose()
/*                    viewModel.contact(
                        sharedPrefManager.getCurrentUser()?.id.toString(),
                        sharedPrefManager.getCurrentUser()?.session_key.toString(),
                        "contact"
                    )*/
                }
                R.id.text_Logout -> {
                    logOutDialog()
                }
                R.id.text_DeleteAc -> {
                    deleteDialog()
                }
                R.id.facebook_btn -> {
                    shareContent()
                }
                R.id.insta_btn -> {
                    shareContent()
                }
                R.id.pinterest_btn -> {
                    shareContent()
                }
                R.id.btnlocation -> {
                    if (binding.btnlocation.isChecked) {
                        checkFunction(true)
                        binding.btnlocation.trackDrawable = context?.let {
                            ContextCompat.getDrawable(
                                it, R.drawable.switch_button_background
                            )
                        }

                    } else {
                        checkFunction(false)
                        binding.btnlocation.trackDrawable = context?.let {
                            ContextCompat.getDrawable(
                                it, R.drawable.toggle_btn_background
                            )
                        }

                    }
                }
            }
        }
    }

    private fun notification(value: Boolean) {
        val map = HashMap<String, Any>()
        map["isNotification"] = value
        viewModel.checkNotificationAndLocationStatus(map,Constant.CHECK_STATUS)

//        viewModel.pushNoti(
//            map,
//            sharedPrefManager.getCurrentUser()?.id.toString(),
//            sharedPrefManager.getCurrentUser()?.session_key.toString()
//        )
    }

    private fun checkFunction(value: Boolean) {
        val map = HashMap<String, Any>()
        map["isLocation"] = value
        viewModel.checkNotificationAndLocationStatus(map, Constant.CHECK_STATUS)
//        viewModel.location(
//            map,
//            sharedPrefManager.getCurrentUser()?.id.toString(),
//            sharedPrefManager.getCurrentUser()?.session_key.toString()
//        )
    }

    private fun openGmailCompose() {
        val intent = Intent(Intent.ACTION_SEND)
        val recipients = arrayOf("info@sonet-singles-nearby.com")

        intent.putExtra(Intent.EXTRA_EMAIL, recipients)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Mail Subject")

        intent.putExtra(Intent.EXTRA_TEXT, "Enter your mail body here...")

        intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com")

        intent.putExtra(Intent.EXTRA_BCC, "mailbcc@gmail.com")

        intent.type = "text/html"
        intent.setPackage("com.google.android.gm")
        startActivity(Intent.createChooser(intent, "Send mail"))
    }

    private fun shareContent() {
        shareText = binding.tvOpinion.text.toString().trim()
        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareText)
        val packageManager: PackageManager? = context?.packageManager
        val resolvedActivityList: List<ResolveInfo> =
            packageManager?.queryIntentActivities(intent, 0) as List<ResolveInfo>

        val packageNames = arrayOf(
            "com.instagram.android",
            "com.facebook.katana",
            "com.facebook.orca",
            "com.google.android.gm"
        )
        val filteredActivities: ArrayList<ResolveInfo> = ArrayList()
        for (resolvedActivity in resolvedActivityList) {
            val packageName: String = resolvedActivity.activityInfo.packageName
            if (packageNames.contains(packageName)) {
                filteredActivities.add(resolvedActivity)
            }
        }

        val chooserIntent = Intent.createChooser(intent, "Share via")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, filteredActivities.toTypedArray())

        startActivity(chooserIntent)
    }

    /*** logout api *************/

    private fun initObserver() {

        viewModel.obrCommon.observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    showLoading("Loading")
                }

                Status.SUCCESS -> {
                    hideLoading()
                    when (it.message) {
                        "reportProblem" -> {
                            showSuccessToast("Thank you for your feedback")
                        }

                        "deleteAccount" -> {
                            sharedPrefManager.clear()
                            val intent = Intent(requireContext(), HomePageActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                            showSuccessToast("Please check email to verify email to delete account permanently")
                        }

                        "logout" -> {
                            sharedPrefManager.clear()
                            secondSharedPrefManager.clearUserData()
                            val intent = Intent(requireContext(), HomePageActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                            dialogLogout.dismiss()
                            showSuccessToast("Logout successfully")
                        }

                        "notificationLocation" -> {
                            val myDataModel: SettingApiResponse? =
                                ImageBindingUtil.parseJson(it.data.toString())
                            if (myDataModel != null) {
                                if (myDataModel.data != null) {
                                  Log.i("dsfsdfsd", "initObserver: ${myDataModel.data!!}")
                                        myDataModel.data?.isNotification?.let { it1 ->
                                            sharedPrefManager.savePushNotification(
                                                it1
                                            )

                                    }
                                    myDataModel.data?.isLocation?.let { it1 ->
                                        sharedPrefManager.saveLocation(
                                            it1
                                        )
                                    }
                                    myDataModel.data?.isLocation?.let { it1 ->
                                        secondSharedPrefManager.saveLocation(
                                            it1
                                        )
                                    }

                                }
                            }
                        }
                    }
                }

                Status.ERROR -> {
                    hideLoading()
                    showToast(it.message.toString())
                }
            }
        })


        /*** contact general *******/

        viewModel.observeContact.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showLoading("Loading")
                }

                Status.SUCCESS -> {
                    hideLoading()
                    showSuccessToast("Thank you, for contacting us")
                }

                Status.ERROR -> {
                    hideLoading()
                    showToast(it.message.toString())
                }
            }
        }


        /***** delete account ******/

//        viewModel.observeDelete.observe(viewLifecycleOwner) {
//            when (it?.status) {
//                Status.LOADING -> {
//                    showLoading("Loading")
//                }
//                Status.SUCCESS -> {
//                    hideLoading()
//                    sharedPrefManager.clear()
//                    val intent = Intent(requireContext(), HomePageActivity::class.java)
//                    startActivity(intent)
//                    requireActivity().finish()
//                    showSuccessToast("Please check email to verify email to delete account permanently")
//
//                }
//                Status.ERROR -> {
//                    hideLoading()
//                    it.message?.let {
//                        showToast(it)
//                    }
//                }
//            }
//        }

//        /***** Location ******/
//
//        viewModel.obsLocation.observe(viewLifecycleOwner) {
//            when (it?.status) {
//                Status.LOADING -> {
//                    showLoading("Loading")
//                }
//                Status.SUCCESS -> {
//                    val data1 = it.data?.data
//                    if (data1 != null) {
//                        sharedPrefManager.saveLocation(data1.is_location_active.toString())
//                    }
//
//                    hideLoading()
//                }
//                Status.ERROR -> {
//                    hideLoading()
//                    showToast(it.message.toString())
//
//                }
//            }
//        }
//        /***** push Notification ******/
//
//        viewModel.obsPushNotification.observe(viewLifecycleOwner) {
//            when (it?.status) {
//                Status.LOADING -> {
//                    showLoading("Loading")
//                }
//                Status.SUCCESS -> {
//                    val data2 = it.data?.data
//                    if (data2 != null) {
//                        sharedPrefManager.savePushNotification(data2.notification_is_active.toString())
//                    }
//                    hideLoading()
//                }
//                Status.ERROR -> {
//                    hideLoading()
//                    showToast(it.message.toString())
//
//                }
//            }
//        }
//    }
    }

    private fun reportDilog() {
        dialogReport = BaseCustomDialog(requireContext(), R.layout.report_dialog, this)
        dialogReport.show()
        dialogReport.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun logOutDialog() {
        dialogLogout = BaseCustomDialog(requireContext(), R.layout.logout_popup, this)
        dialogLogout.show()
        dialogLogout.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun deleteDialog() {
        dialogDelete = BaseCustomDialog(requireContext(), R.layout.delete_popup, this)
        dialogDelete.show()
        dialogDelete.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onViewClick(view: View?) {
        when (view?.id) {
            R.id.tvNo -> {
                dialogLogout.dismiss()
            }
            R.id.tvYes -> {
                viewModel.logout(Constant.LOG_OUT)


//                viewModel.logout(
//                    sharedPrefManager.getCurrentUser()?.id.toString(),
//                    sharedPrefManager.getCurrentUser()?.session_key.toString()
//                )
            }
            R.id.tvDismiss -> {
                dialogReport.dismiss()
            }
            R.id.tvspam -> {

                val data = HashMap<String, Any>()
                data["message"] = "It's spam"
                 viewModel.reportProblem(data , Constant.REPORT)
                dialogReport.dismiss()
//                viewModel.report(
//                    sharedPrefManager.getCurrentUser()?.id.toString(),
//                    sharedPrefManager.getCurrentUser()?.session_key.toString(),
//                    "It's spam"
//                )
            }
            R.id.tvviolation -> {

                val data = HashMap<String, Any>()
                data["message"] = "Intellectual property violation"
                viewModel.reportProblem(data , Constant.REPORT)
                dialogReport.dismiss()
//                viewModel.report(
//                    sharedPrefManager.getCurrentUser()?.id.toString(),
//                    sharedPrefManager.getCurrentUser()?.session_key.toString(),
//                    "Intellectual property violation"
//                )
            }
            R.id.tvharassment -> {

                val data = HashMap<String, Any>()
                data["message"] = "Bulling or harassment"
                viewModel.reportProblem(data , Constant.REPORT)
                dialogReport.dismiss()
//                viewModel.report(
//                    sharedPrefManager.getCurrentUser()?.id.toString(),
//                    sharedPrefManager.getCurrentUser()?.session_key.toString(),
//                    "Bulling or harassment"
//                )
            }
            R.id.tvspeech -> {

                val data = HashMap<String, Any>()
                data["message"] = "Hate speech or symbols"
                viewModel.reportProblem(data , Constant.REPORT)
                dialogReport.dismiss()
//                viewModel.report(
//                    sharedPrefManager.getCurrentUser()?.id.toString(),
//                    sharedPrefManager.getCurrentUser()?.session_key.toString(),
//                    "Hate speech or symbols"
//                )
            }
            R.id.tvviolence -> {

                val data = HashMap<String, Any>()
                data["message"] = "violence or dangerous organisations"
                viewModel.reportProblem(data , Constant.REPORT)
                dialogReport.dismiss()
//                viewModel.report(
//                    sharedPrefManager.getCurrentUser()?.id.toString(),
//                    sharedPrefManager.getCurrentUser()?.session_key.toString(),
//                    "violence or dangerous organisations"
//                )
            }
            R.id.tvillegal -> {

                val data = HashMap<String, Any>()
                data["message"] = "Sale of illegal or regulated goods"
                viewModel.reportProblem(data , Constant.REPORT)
                dialogReport.dismiss()
//                viewModel.report(
//                    sharedPrefManager.getCurrentUser()?.id.toString(),
//                    sharedPrefManager.getCurrentUser()?.session_key.toString(),
//                    "Sale of illegal or regulated goods"
//                )
            }
            R.id.tvscam -> {

                val data = HashMap<String, Any>()
                data["message"] = "Scam or Fraud"
                viewModel.reportProblem(data , Constant.REPORT)
                dialogReport.dismiss()
//                viewModel.report(
//                    sharedPrefManager.getCurrentUser()?.id.toString(),
//                    sharedPrefManager.getCurrentUser()?.session_key.toString(),
//                    "Scam or Fraud"
//                )
            }
            R.id.tvfalse_information -> {

                val data = HashMap<String, Any>()
                data["message"] = "False Information"
                viewModel.reportProblem(data , Constant.REPORT)
                dialogReport.dismiss()
//                viewModel.report(
//                    sharedPrefManager.getCurrentUser()?.id.toString(),
//                    sharedPrefManager.getCurrentUser()?.session_key.toString(),
//                    "False Information"
//                )
            }
            R.id.No -> {
                dialogDelete.dismiss()
            }
            R.id.Yes -> {
                dialogDelete.dismiss()
                viewModel.deleteAccount(Constant.DELETE_ACCOUNT)
            }
        }
    }

    override fun onResume() {
        Log.i("Ggg", "onResume: ")
        super.onResume()

    }

    override fun onPause() {
        Log.i("Ggg", "onPause: ")
        sharedPrefManager.saveOpinion(binding.tvOpinion.text.toString().trim())
        super.onPause()
    }

    override fun onDestroy() {
        Log.i("Ggg", "onDestroy: ")
        super.onDestroy()
    }
}