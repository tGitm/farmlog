package com.example.farmlog.chores.archive

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.farmlog.R
import com.example.farmlog.chores.models.Chores
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ArchiveAdapter(private var choresList: MutableList<Chores>, clickListener: ClickListener) : RecyclerView.Adapter<ArchiveAdapter.ChoresViewHolder>(), Filterable {

    private var choresListFiltered: MutableList<Chores> = arrayListOf()
    private var clickedListener: ClickListener = clickListener

    fun addItems(items: MutableList<Chores>) {
        choresList.clear()
        choresList.addAll(items)
        choresListFiltered = items
        Log.i("ChoresAdapter", choresList.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChoresViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.archive_item, parent, false)
    )

    override fun onBindViewHolder(holder: ChoresViewHolder, position: Int) {
        val chore = choresList[position]
        holder.bindView(chore)

        holder.itemView.setOnClickListener {
            clickedListener.clickedItem(chore)
        }
    }

    override fun getItemCount(): Int {
        return choresList.size
    }

    fun removeItem(position: Int) {
        choresList.removeAt(position)
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun clickedItem(choreModel: Chores)
    }

    class ChoresViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private var archiveTitle: TextView = view.findViewById(R.id.archiveTitleField)
        private var archiveLand: TextView = view.findViewById(R.id.archiveLandField)
        private var archiveDate: TextView = view.findViewById(R.id.archiveDateField)

        fun bindView(chore: Chores) {
            val current = chore.createdAt

            Log.i("Adapter", current)
            Log.i("Adapter", chore._id)
            Log.i("Adapter", chore.work_title)

            //val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            //val formatted = current.format(formatter)

            archiveTitle.text = chore.work_title
            archiveLand.text = chore.land_id
            archiveDate.text = chore.date

        }
    }

    override fun getFilter(): Filter {
        var filter = object: Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                var filterResults = FilterResults()
                if (p0 == null || p0.isEmpty()) {
                    filterResults.values = choresListFiltered
                    filterResults.count = choresListFiltered.size
                } else {
                    var searchChar = p0.toString().toLowerCase()
                    var filteredResults: MutableList<Chores> = ArrayList()

                    for (chore in choresListFiltered) {
                        if (chore.work_title.toLowerCase(Locale.getDefault()).contains(searchChar) || chore.accessories_used.toLowerCase(
                                Locale.getDefault()).contains(searchChar) || chore.date.toLowerCase(
                                Locale.getDefault()).contains(searchChar) || chore.land_id.toLowerCase(
                                Locale.getDefault()).contains(searchChar)) {
                                filteredResults.add(chore)
                        }
                    }

                    filterResults.values = filteredResults
                    filterResults.count = filteredResults.size
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                choresList = p1?.values as MutableList<Chores>
                notifyDataSetChanged()
            }
        }
        return filter
    }
}