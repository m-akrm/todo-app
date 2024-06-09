package com.example.todo_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.todo_app.databinding.FragmentCalenderBinding
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.util.Calendar
import java.util.Date

class CalenderFragment : Fragment() {

    private lateinit var binding:FragmentCalenderBinding
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_calender, container, false)
        binding.lifecycleOwner=viewLifecycleOwner

        // set current date to calendar and current month to currentMonth variable
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]


        // calendar view manager is responsible for our displaying logic
        val myCalendarViewManager = object :
            CalendarViewManager {
            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                val daytextview=holder.itemView.findViewById<TextView>(R.id.tv_day_calendar_item)
                val monthtextview=holder.itemView.findViewById<TextView>(R.id.tv_month_calendar_item)
                val datetextview=holder.itemView.findViewById<TextView>(R.id.tv_date_calendar_item)

                datetextview.text=DateUtils.getDayNumber(date)
                monthtextview.text=DateUtils.getMonth3LettersName(date)
                daytextview.text=DateUtils.getDay3LettersName(date)

            }

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
                // TODO: change recyclerview list with new date
                if(isSelected){
                    Toast.makeText(this@CalenderFragment.requireContext(), "${DateUtils.getMonthName(date)}, ${DateUtils.getDayNumber(date)} ", Toast.LENGTH_SHORT).show()

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
            includeCurrentDate=true
            futureDaysCount = 7
            initialPositionIndex=5
            pastDaysCount=7
            init()
        }
        binding.singleRowCal.select(7)


        return binding.root
    }

}