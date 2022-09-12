package com.example.farmlog.chores.archive

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.farmlog.R
import com.example.farmlog.chores.models.Chore
import java.time.format.DateTimeFormatter

class ArchiveAdapter : RecyclerView.Adapter<ArchiveAdapter.ChoresViewHolder>() {
    private var choresList: ArrayList<Chore?> = ArrayList()

    fun addItems(items: ArrayList<Chore?>) {
        this.choresList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChoresViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.activity_archive, parent, false)
    )

    override fun onBindViewHolder(holder: ChoresViewHolder, position: Int) {
        val chore = choresList?.get(position)
        if (chore != null) {
            holder.bindView(chore)
        }
    }

    override fun getItemCount(): Int {
        return choresList!!.size
    }


    class ChoresViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var archiveTitle: TextView = view.findViewById(R.id.archiveTitle)
        private var archiveDate: TextView = view.findViewById(R.id.archiveDate)


        @SuppressLint("NewApi")
        fun bindView(chore: Chore) {
            val current = chore.createdAt

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = current.format(formatter)

            archiveTitle.text = chore.work_title
            archiveDate.text = formatted

        }
    }
}