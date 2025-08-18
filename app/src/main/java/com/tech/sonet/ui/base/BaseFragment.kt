package com.tech.sonet.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.tech.sonet.BR
import com.tech.sonet.data.local.SharedPrefManager
import com.tech.sonet.utils.hideKeyboard
import javax.inject.Inject

abstract class BaseFragment<Binding : ViewDataBinding> : Fragment() {
    lateinit var binding: Binding
    private val parentActivity: BaseActivity<*>?
        get() = activity as? BaseActivity<*>

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateView(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout: Int = getLayoutResource()
        binding = DataBindingUtil.inflate(layoutInflater, layout, container, false)
        val vm = getViewModel()
        binding.setVariable(BR.vm, vm)
        vm.onUnAuth.observe(viewLifecycleOwner) {
            val activity = requireActivity() as BaseActivity<*>
            activity.showUnauthorised()
        }
    //    binding.setVariable(BR.vm, getViewModel())
        return binding.root
    }

    protected abstract fun getLayoutResource(): Int
    protected abstract fun getViewModel(): BaseViewModel
    protected abstract fun onCreateView(view: View)
    override fun onPause() {
        super.onPause()
        activity?.hideKeyboard()
    }

    fun showLoading(s: String?) {
        parentActivity?.showLoading(s)
    }

    fun showLoading(s: Int) {
        parentActivity?.showLoading(getString(s))
    }

    fun hideLoading() {
        parentActivity?.hideLoading()
    }

}