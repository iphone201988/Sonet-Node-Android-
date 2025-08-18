package com.tech.sonet.ui
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tech.sonet.BR
import com.tech.sonet.R
import com.tech.sonet.data.model.LikeReceiveApiResponse
import com.tech.sonet.databinding.ItemLayoutLikeReceivedBinding

import com.zerobranch.layout.SwipeLayout

class CustomSwipeMenuAdapter(val callback: CardCallback):RecyclerView.Adapter<CustomSwipeMenuAdapter.RequestHolder>() {

    private var moreBeans: MutableList<LikeReceiveApiResponse.LikeReceiveApiResponseData> = ArrayList()
    var swipeLayout: SwipeLayout? = null

   public interface CardCallback {
        fun onItemClick(v: View?, m: LikeReceiveApiResponse.LikeReceiveApiResponseData?, pos: Int?)
    }
//    fun removeItem(i: Int) {
//        try {
//            if (i != -1) {
//                moreBeans.removeAt(i)
//                notifyItemRemoved(i)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHolder {
        val layoutTodoListBinding: ItemLayoutLikeReceivedBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_layout_like_received, parent, false
        )
        layoutTodoListBinding.setVariable(BR.callback, callback)
        return RequestHolder(layoutTodoListBinding)
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
    override fun getItemCount(): Int {
        return moreBeans.size
    }
    fun setList(moreBeans: List<LikeReceiveApiResponse.LikeReceiveApiResponseData?>?) {
        this.moreBeans = moreBeans as MutableList<LikeReceiveApiResponse.LikeReceiveApiResponseData>
        notifyDataSetChanged()
    }
    fun clear() {
        moreBeans.isEmpty()
        notifyDataSetChanged()
    }
    fun getList(): MutableList<LikeReceiveApiResponse.LikeReceiveApiResponseData> {
        return moreBeans
    }

    fun addToList(newDataList: List<LikeReceiveApiResponse.LikeReceiveApiResponseData?>?) {
        if (newDataList == null || newDataList.isEmpty()) return

        val filteredList = newDataList.filterNotNull() // Remove null elements
        val positionStart = moreBeans.size
        val itemCount = filteredList.size
        moreBeans.addAll(filteredList)
        notifyItemRangeInserted(positionStart, itemCount)
    }
    class RequestHolder(val layoutTodoListBinding: ItemLayoutLikeReceivedBinding) :
        RecyclerView.ViewHolder(layoutTodoListBinding.root)
}

