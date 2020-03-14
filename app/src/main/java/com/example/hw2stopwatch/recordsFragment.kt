package com.example.hw2stopwatch

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.sql.Time

class recordsFragment : Fragment() {

    private var record: ArrayList<String>? = ArrayList<String>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private var timerecords: ArrayList<TimeRecords> = ArrayList<TimeRecords>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): View? {
        if (container == null) {

            return null
        }

        return inflater.inflate(R.layout.fragment_records, container, false)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        val model= activity?.run {
            ViewModelProviders.of(this).get(MyViewModel::class.java)
        }?: throw Exception("Invalid Activity")
        record = model.getlist()
        this.record?.forEach { temp ->
            var min = (temp.toLong() / 1000 / 60).toString()
            var sec = (temp.toLong() / 1000 % 60).toString()
            timerecords.add(TimeRecords("$min:$sec"))
        }


        recyclerView = view.findViewById(R.id.RecordsRecyclerView)


        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        viewAdapter = RecyclerViewAdapter(timerecords, activity as MainActivity)

        recyclerView.adapter = viewAdapter

    }
    fun display(time: Long)
    {
        var min = (time / 1000 / 60).toString()
        var sec = (time / 1000 % 60).toString()
        timerecords.add(TimeRecords("$min:$sec"))

//        recyclerView = view.findViewById(R.id.RecordsRecyclerView)
//
//
//        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        viewAdapter = RecyclerViewAdapter(timerecords, activity as MainActivity)

        recyclerView.adapter = viewAdapter
    }
    fun clear()
    {
        timerecords.clear()
        viewAdapter = RecyclerViewAdapter(timerecords, activity as MainActivity)

        recyclerView.adapter = viewAdapter
    }


}
class RecyclerViewAdapter(private val myDataset: ArrayList<TimeRecords>, private val activity: MainActivity) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerViewAdapter.ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview, parent, false)


        return ViewHolder(v, activity)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(myDataset[position])

    }

    class ViewHolder(private val view: View, private val activity: MainActivity) : RecyclerView.ViewHolder(view){
        fun bindItems(newsItem: TimeRecords) {
            val time:TextView = itemView.findViewById(R.id.time)
            time.text = newsItem.time
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}



