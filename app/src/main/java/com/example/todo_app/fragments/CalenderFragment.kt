package com.example.todo_app.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todo_app.R
import com.example.todo_app.SharedViewModel
import com.example.todo_app.adapters.CalenderListAdapter
import com.example.todo_app.databinding.FragmentCalenderBinding
import com.example.todo_app.dataclasses.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.Slider
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.text.NumberFormat
import java.util.Calendar
import java.util.Date

class CalenderFragment : Fragment() {

    private lateinit var binding: FragmentCalenderBinding
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var active=0
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_calender, container, false)
        binding.lifecycleOwner=viewLifecycleOwner
        this.requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator).let {
            it.visibility=View.VISIBLE
            it.findViewById<FloatingActionButton>(R.id.fab).visibility=View.VISIBLE
        }

        init_calenderview()


        val calenderListAdapter= CalenderListAdapter(
            ondeleteClicked = {
                sharedViewModel.deleteTask(it)
                sharedViewModel.changeCurrentTask(it.endDate)
                Toast.makeText(this.requireContext(), "delete", Toast.LENGTH_SHORT).show()
            },
            oneditClicked = {
                 this.findNavController().navigate(CalenderFragmentDirections.actionCalenderFragmentToAddTaskFragment().setTask(it))
            },
            onProgressChange = {
                progressChange(it)
            }
        )
        sharedViewModel.currenttasks.observe(viewLifecycleOwner){
            calenderListAdapter.submitList(it)
            binding.recyclerview.adapter=calenderListAdapter
        }




        return binding.root
    }

    private fun progressChange(it: Task) {
        val slider = Slider(this.requireContext()).apply {
            value = it.progressPercentage.toFloat()
            valueFrom = 0f
            valueTo = 100f
            stepSize = 5f
            labelBehavior = LabelFormatter.LABEL_VISIBLE
            setLabelFormatter {
                it.toInt().toString()+"%"
            }
        }

        MaterialAlertDialogBuilder(this.requireContext())
            .setView(slider)
            .setTitle("change task progress")


            .setNegativeButton("dismiss") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("save") { dialog, which ->
                val newtask = it
                newtask.progressPercentage = slider.value.toInt()
                if(newtask.progressPercentage==100) newtask.status="Completed"
                else if(newtask.progressPercentage==0) newtask.status="To Do"
                else newtask.status="In Progress"
                sharedViewModel.editTask(it, newtask)
                sharedViewModel.changeCurrentTask(it.endDate)
            }
            .show()
    }


    private fun init_calenderview() {
        // set current date to calendar and current month to currentMonth variable
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]



        val myCalendarViewManager = object :
            CalendarViewManager {
                // set calendar item layout
            override fun bindDataToCalendarView(
                    holder: SingleRowCalendarAdapter.CalendarViewHolder,
                    date: Date,
                    position: Int,
                    isSelected: Boolean
            ) {
                val daytextview = holder.itemView.findViewById<TextView>(R.id.tv_day_calendar_item)
                val monthtextview =
                    holder.itemView.findViewById<TextView>(R.id.tv_month_calendar_item)
                val datetextview =
                    holder.itemView.findViewById<TextView>(R.id.tv_date_calendar_item)

                datetextview.text = DateUtils.getDayNumber(date)
                monthtextview.text = DateUtils.getMonth3LettersName(date)
                daytextview.text = DateUtils.getDay3LettersName(date)

            }
            // set calendar item layout
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {

                return if (isSelected) R.layout.selected_calendar_item
                else R.layout.calendar_item

            }
        }


        // using calendar changes observer we can track changes in calendar
        val myCalendarChangesObserver = object :
            CalendarChangesObserver {
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {

                if (isSelected) {
                    sharedViewModel.changeCurrentTask(date)

                }
                super.whenSelectionChanged(isSelected, position, date)
            }
        }

        // selection manager is responsible for managing selection
        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                return true
            }
        }
        binding.singleRowCal.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            includeCurrentDate = true
            futureDaysCount = 7
            initialPositionIndex = 5
            pastDaysCount = 7
            init()
        }
        binding.singleRowCal.select(7)
    }

}