package com.example.studyapplication.service

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.studyapplication.databinding.RecyclerViewItemBinding
import com.example.studyapplication.model.SubjectData

class RecyclerViewAdapter(private val subjectData: ArrayList<SubjectData>) :
    RecyclerView.Adapter<RecyclerViewAdapter.SubjectItemViewHolder>() {

    private val hashCodeList = mutableListOf(0)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectItemViewHolder {
        val itemViewBinding = RecyclerViewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SubjectItemViewHolder(itemViewBinding)
    }


    override fun onBindViewHolder(holder: SubjectItemViewHolder, position: Int) {
        if((holder.hashCode() in hashCodeList).not()) {
            Log.i("Recycle", "new holder $position : ${holder.hashCode()}")
            hashCodeList.add(position, holder.hashCode())
        }
        else Log.i("Recycle", "holder recycled $position : ${holder.hashCode()}")

        holder.apply {
            marksText.text = subjectData[position].marks.toString()
            subjectText.text = subjectData[position].subject

            if(position == subjectData.lastIndex)
                separator.visibility = View.GONE

            holder.itemView.setOnClickListener {
                when(position) {
                    0 -> {
                        subjectData.removeAt(2)
                        notifyItemRemoved(2)
                    }
                    1 -> {
                        subjectData.add(2, SubjectData("newSubject", 6969))
                        notifyItemInserted(2)
                    }
                    else -> Toast.makeText(it.context, "item $position clicked", Toast.LENGTH_SHORT).show()

                }
            }
        }

    }

    override fun onViewRecycled(holder: SubjectItemViewHolder) {
        super.onViewRecycled(holder)
        Log.i("Recycle", "onViewRecycled ${holder.adapterPosition} : ${holder.hashCode()}")
    }

    override fun getItemCount(): Int {
        return subjectData.size
    }

    inner class SubjectItemViewHolder(viewItemBinding: RecyclerViewItemBinding) :
        RecyclerView.ViewHolder(viewItemBinding.root) {
        val subjectText = viewItemBinding.subject
        val marksText = viewItemBinding.marks
        val separator = viewItemBinding.horizontalSeparator
    }
}