package com.example.farmlog.chores.archive

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.farmlog.R
import com.example.farmlog.chores.models.Chores
import java.time.format.DateTimeFormatter

class ArchiveAdapter(private val choresList : MutableList<Chores>) : RecyclerView.Adapter<ArchiveAdapter.ChoresViewHolder>() {

    fun addItems(items: MutableList<Chores>) {
        choresList.clear()
        choresList.addAll(items)
        Log.i("ChoresAdapter", choresList.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChoresViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.archive_item, parent, false)
    )

    override fun onBindViewHolder(holder: ChoresViewHolder, position: Int) {
        val chore = choresList[position]
        holder.bindView(chore)
    }

    override fun getItemCount(): Int {
        return choresList.size
    }

    class ChoresViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private var archiveTitle: TextView = view.findViewById(R.id.archiveTitleField)
        private var archiveDate: TextView = view.findViewById(R.id.archiveDateField)

        fun bindView(chore: Chores) {
            val current = chore.createdAt

            Log.i("Adapter", current)
            Log.i("Adapter", chore._id)
            Log.i("Adapter", chore.work_title)

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = current.format(formatter)

            archiveTitle.text = chore.work_title
            archiveDate.text = chore.createdAt

        }
    }
}