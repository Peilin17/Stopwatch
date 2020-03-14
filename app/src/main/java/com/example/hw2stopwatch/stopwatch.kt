package com.example.hw2stopwatch

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.ListView
import androidx.core.os.ConfigurationCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class stopwatch() : Fragment() {



    private lateinit var chronometer: Chronometer
    private var stopTime: Long = 0
    private var running: Boolean = false
    //private var recordsFromWatch = ArrayList<String>()

     override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
        //activity: MainActivity
    ): View? {
         val model= activity?.run {
             ViewModelProviders.of(this).get(MyViewModel::class.java)
         }?: throw Exception("Invalid Activity")

        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist. The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // isn't displayed. Note this isn't needed -- we could just
            // run the code below, where we would create and return the
            // view hierarchy; it would just never be used.
            return null
        }


        val view = inflater.inflate(R.layout.fragment_stopwatch, container, false)

         chronometer = view.findViewById(R.id.chronometer)

         running = model.getRun()
         if (running)
         {
             model.getBase().observe(viewLifecycleOwner, Observer<Long>{ time ->
                 view?.findViewById<Chronometer>(R.id.chronometer)?.base = time
             })
             chronometer.start()
         }
         else
         {

             model.getST().observe(viewLifecycleOwner, Observer<Long>{ time ->
                 stopTime = time
                 view?.findViewById<Chronometer>(R.id.chronometer)?.base = SystemClock.elapsedRealtime() - time
             })


         }

        view.findViewById<Button>(R.id.start).setOnClickListener {
            if (!running)
            {
                chronometer.base = SystemClock.elapsedRealtime() - stopTime
                chronometer.start()
                running = true
            }
        }
        view.findViewById<Button>(R.id.stop).setOnClickListener {
            if (running)
            {
                chronometer.stop()
                stopTime = SystemClock.elapsedRealtime() - chronometer.base
                running = false
            }
        }
        view.findViewById<Button>(R.id.reset).setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            stopTime = 0
            //running = false
            if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                model.clear()
            }
            else{
                (activity?.supportFragmentManager?.findFragmentById(R.id.records_fragment) as recordsFragment).clear()

            }
        }
        view.findViewById<Button>(R.id.lap).setOnClickListener {
            if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT){
                if (running) {
                    model.addRecord((SystemClock.elapsedRealtime() - chronometer.base).toString())
                }
                else
                {
                    model.addRecord(stopTime.toString())
                }
            }
            else
            {
                if (running) {
                    (activity?.supportFragmentManager?.findFragmentById(R.id.records_fragment) as recordsFragment).display(
                        SystemClock.elapsedRealtime() - chronometer.base
                    )
                    model.addRecord((SystemClock.elapsedRealtime() - chronometer.base).toString())
                }
                else
                {
                    (activity?.supportFragmentManager?.findFragmentById(R.id.records_fragment) as recordsFragment).display(
                        stopTime
                    )
                    model.addRecord(stopTime.toString())
                }
            }
        }
        view.findViewById<Button>(R.id.records).setOnClickListener {
            if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                view.findNavController().navigate(R.id.action_stopwatch_to_recordsFragment2)
            }


        }
        return view
    }

//    override fun onResume() {
//        super.onResume()
//        val model= activity?.run {
//            ViewModelProviders.of(this).get(MyViewModel::class.java)
//        }?: throw Exception("Invalid Activity")
//        view?.findViewById<Chronometer>(R.id.chronometer)?.base = SystemClock.elapsedRealtime() - model.getST()
//        running = model.getRun()
//        stopTime = model.getST()
//        if (running)
//        {
//            view?.findViewById<Chronometer>(R.id.chronometer)?.base = model.getBase()
//            view?.findViewById<Chronometer>(R.id.chronometer)?.start()
//
//        }
//
//    }


    override fun onPause() {
        super.onPause()
        val model= activity?.run {
            ViewModelProviders.of(this).get(MyViewModel::class.java)
        }?: throw Exception("Invalid Activity")
        if (view?.findViewById<Chronometer>(R.id.chronometer) != null) {
            if (running) {
                stopTime =
                    SystemClock.elapsedRealtime() - view?.findViewById<Chronometer>(R.id.chronometer)?.base!!
                model.saveStatus(running, stopTime)
                model.setBase(view?.findViewById<Chronometer>(R.id.chronometer)?.base!!)
            }
            else
            {
                model.saveStatus(running, stopTime)
                model.setBase(view?.findViewById<Chronometer>(R.id.chronometer)?.base!!)
            }

        }
    }




}
