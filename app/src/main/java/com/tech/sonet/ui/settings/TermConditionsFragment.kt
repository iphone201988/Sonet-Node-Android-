package com.tech.sonet.ui.settings

import android.view.View
import androidx.fragment.app.viewModels
import com.tech.sonet.R
import com.tech.sonet.databinding.TermAndConditionsBinding
import com.tech.sonet.ui.base.BaseFragment
import com.tech.sonet.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermConditionsFragment : BaseFragment<TermAndConditionsBinding>() {
    private val viewModel: TermConditionsFragmentVM by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.term_and_conditions
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.web->{

                }

            }
        }
    }
}