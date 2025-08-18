package com.tech.sonet.ui.like_sent

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tech.sonet.BR
import com.tech.sonet.R
import com.tech.sonet.data.model.LikeSentApiResponse
import com.tech.sonet.data.model.LikeSentApiResponse.LikeSentApiResponseData
import com.tech.sonet.databinding.ItemLayoutLikeSentBinding
import com.zerobranch.layout.SwipeLayout


class CustomLikeSentAdapter(val callback: CardCallback):RecyclerView.Adapter<CustomLikeSentAdapter.RequestHolder>() {

    private var moreBeans: MutableList<LikeSentApiResponse.LikeSentApiResponseData> = ArrayList()
    var swipeLayout: SwipeLayout? = null

    public interface CardCallback {
        fun onItemClick(v: View?, m: LikeSentApiResponse.LikeSentApiResponseData, pos: Int?)
    }

    class RequestHolder(val layoutTodoListBinding: ItemLayoutLikeSentBinding) :
        RecyclerView.ViewHolder(layoutTodoListBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHolder {
        val layoutTodoListBinding: ItemLayoutLikeSentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_layout_like_sent, parent, false
        )
        layoutTodoListBinding.setVariable(BR.callback, callback)
        return RequestHolder(layoutTodoListBinding)
    }

    override fun getItemCount(): Int {
        return moreBeans.size

    }

    override fun onBindViewHolder(holder: RequestHolder, @SuppressLint("RecyclerView")position: Int) {
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

    fun setList(moreBeans: List<LikeSentApiResponse.LikeSentApiResponseData?>?) {
        this.moreBeans = moreBeans as MutableList<LikeSentApiResponse.LikeSentApiResponseData>
        notifyDataSetChanged()
    }
    fun clear() {
        moreBeans.isEmpty()
        notifyDataSetChanged()
    }
    fun getList(): MutableList<LikeSentApiResponse.LikeSentApiResponseData> {
        return moreBeans
    }




    fun addToList(newDataList: List<LikeSentApiResponse.LikeSentApiResponseData?>?) {
        if (newDataList == null || newDataList.isEmpty()) return

        val filteredList = newDataList.filterNotNull() // Remove null elements
        val positionStart = moreBeans.size
        val itemCount = filteredList.size
        moreBeans.addAll(filteredList)
        notifyItemRangeInserted(positionStart, itemCount)
    }

}