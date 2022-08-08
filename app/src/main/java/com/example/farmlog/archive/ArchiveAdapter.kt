package com.example.farmlog.archive

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.farmlog.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ArchiveAdapter : RecyclerView.Adapter<ArchiveAdapter.ChoresViewHolder>() {
    private var choresList: ArrayList<ChoresModel> = ArrayList()

    fun addItems(items: ArrayList<ChoresModel>) {
        this.choresList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChoresViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.activity_archive, parent, false)
    )

    override fun onBindViewHolder(holder: ChoresViewHolder, position: Int) {
        val chore = choresList[position]
        holder.bindView(chore)
    }

    override fun getItemCount(): Int {
        return choresList.size
    }


    class ChoresViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var archiveTitle: TextView = view.findViewById(R.id.archiveTitle)
        private var archiveDate: TextView = view.findViewById(R.id.archiveDate)


        @SuppressLint("NewApi")
        fun bindView(chore: ChoresModel) {
            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = current.format(formatter)

            for (i in 1..5) {
                archiveTitle.text = "Škropljenje sadovnjaka"
                archiveDate.text = formatted
            }


        }
    }
}