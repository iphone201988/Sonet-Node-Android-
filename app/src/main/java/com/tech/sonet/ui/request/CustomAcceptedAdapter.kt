package com.tech.sonet.ui.request

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tech.sonet.BR
import com.tech.sonet.R
import com.tech.sonet.data.model.AcceptedStatusApiResponse
import com.tech.sonet.databinding.AcceptedListBinding
import com.zerobranch.layout.SwipeLayout


class CustomAcceptedAdapter(val callback: CardCallback) :
    RecyclerView.Adapter<CustomAcceptedAdapter.RequestHolder>() {

    private var moreBeans: MutableList<AcceptedStatusApiResponse.AcceptedStatusApiResponseData> = ArrayList()
    var swipeLayout: SwipeLayout? = null

    public interface CardCallback {
        fun onItemClick(v: View?, m: AcceptedStatusApiResponse.AcceptedStatusApiResponseData, pos: Int?)
    }

    class RequestHolder(val layoutTodoListBinding: AcceptedListBinding) :
        RecyclerView.ViewHolder(layoutTodoListBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHolder {
        val layoutTodoListBinding: AcceptedListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.accepted_list, parent, false
        )
        layoutTodoListBinding.setVariable(BR.callback, callback)
        return RequestHolder(layoutTodoListBinding)
    }

    override fun getItemCount(): Int {
        return moreBeans.size

    }

    override fun onBindViewHolder(
        holder: RequestHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.layoutTodoListBinding.bean = moreBeans[position]
        holder.layoutTodoListBinding.pos = position
        holder.layoutTodoListBinding.swipeLayout.setOnActionsListener(object :
            SwipeLayout.SwipeActionsListener {
            override fun onOpen(direction: Int, isContinuous: Boolean) {
                if (direction == SwipeLayout.LEFT) {
                    if (swipeLayout != null && swipeLayout != holder.layoutTodoListBinding.swipeLayout) {
                        swipeLayout?.close(true)
                    }
                    swipeLayout = holder.layoutTodoListBinding.swipeLayout
                }
            }

            override fun onClose() {
                if (swipeLayout == holder.layoutTodoListBinding.swipeLayout) swipeLayout = null
            }
        })
    }

    fun setList(moreBeans: List<AcceptedStatusApiResponse.AcceptedStatusApiResponseData?>?) {
        this.moreBeans = moreBeans as MutableList<AcceptedStatusApiResponse.AcceptedStatusApiResponseData>
        notifyDataSetChanged()
    }

    fun clear() {
        moreBeans.isEmpty()
        notifyDataSetChanged()
    }

    fun getList(): MutableList<AcceptedStatusApiResponse.AcceptedStatusApiResponseData> {
        return moreBeans
    }


    fun addToList(newDataList: List<AcceptedStatusApiResponse.AcceptedStatusApiResponseData?>?) {
        if (newDataList == null || newDataList.isEmpty()) return

        val filteredList = newDataList.filterNotNull() // Remove null elements
        val positionStart = moreBeans.size
        val itemCount = filteredList.size
        moreBeans.addAll(filteredList)
        notifyItemRangeInserted(positionStart, itemCount)

    }
}