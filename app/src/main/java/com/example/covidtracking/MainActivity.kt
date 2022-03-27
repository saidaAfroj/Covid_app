package com.example.covidtracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val BASE_URl = "https://covidtracking.com/api/v1/"

private const val TAG = "MainActivity"
private const val All_STATES = "All ( NationWIde)"
class MainActivity : AppCompatActivity() {


    private lateinit var currentlyShownData: List<CovidData>
    private lateinit var adapter: CovidSparkAdapter
    private lateinit var perStateDailyData: Map<String, List<CovidData>>
    private lateinit var nationalDailyData: List<CovidData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val covidService = retrofit.create(CovidService::class.java)

        covidService.getNationalData().enqueue(object: Callback<List<CovidData>> {

            override fun onFailure(call: Call<List<CovidData>>, t: Throwable) {
                Log.e(TAG, "onFailure $t")
            }

            override fun onResponse(call: Call<List<CovidData>>, response: Response<List<CovidData>>) {
                Log.i(TAG, "onResponse $response")
                val nationalData = response.body()
                if (nationalData == null) {
                    Log.w(TAG, "Did not recieve a valid response body")
                    return
                }
                setupEventListners()
                nationalDailyData = nationalData.reversed()
                Log.i(TAG, "update graph with national data")
                updateDisplayWithData(nationalDailyData)
            }



        })

        covidService.getStatesData().enqueue(object: Callback<List<CovidData>> {

            override fun onFailure(call: Call<List<CovidData>>, t: Throwable) {
                Log.e(TAG, "onFailure $t")
            }

            override fun onResponse(call: Call<List<CovidData>>, response: Response<List<CovidData>>) {
                Log.i(TAG, "onResponse $response")
                val statesData = response.body()
                if (statesData == null) {
                    Log.w(TAG, "Did not recieve a valid response body")
                    return
                }

                perStateDailyData = statesData.reversed().groupBy { it.state }
                Log.i(TAG, "update spinner with state names")
                //Update spinner with state name
                updateSpineerWithStateData(perStateDailyData.keys)
            }



        })

    }
   //Display the state name in the top
    private fun updateSpineerWithStateData(stateNames: Set<String>) {
     val  stateAbbreviatioList = stateNames.toMutableList()//
        stateAbbreviatioList.sort()
       stateAbbreviatioList.add(0,All_STATES)
       //Add state list as data source for the spinner

    }

    private fun setupEventListners() {
        //Add a listner for the user scrubbing on the chart
        sparkView.isScrubEnabled = true
        sparkView.setScrubListener { itemData ->
            if (itemData is CovidData) {
                updateInfoForDate(itemData)
            }
        }
        //Respond to radio button selected events
        radioGroupTimeSelection.setOnCheckedChangeListener { _, checkedId ->
            adapter.daysAgo = when (checkedId) {
                R.id.radioButtonWeek -> TimeScale.WEEK
                R.id.radioButtonMonth -> TimeScale.MONTH
                else -> TimeScale.MAX
            }
            adapter.notifyDataSetChanged()
        }
       radioGroupMetricSelection.setOnCheckedChangeListener { _, checkedId ->
           when (checkedId){
               R.id.radioButtonNegative -> updateDisplayMetric(Metric.NEGATIVE)
               R.id.radioButtonPositive -> updateDisplayMetric(Metric.POSITIVE)
               R.id.radioButtonDeath -> updateDisplayMetric(Metric.DEATH)

           }
       }

    }

    private fun updateDisplayMetric(metric: Metric) {
        //update the color of the chart
        val colorRes = when (metric){
            Metric.DEATH ->R.color.colorDeath
            Metric.NEGATIVE ->R.color.colorNegative
            Metric.POSITIVE -> R.color.colorPositive

        }
       @ColorInt val colorInt = ContextCompat.getColor(this, colorRes)
      sparkView.lineColor = colorInt
        tvMetricLabel.setTextColor(colorInt)

        //Update the metric on the adapter
        adapter.metric = metric
        adapter.notifyDataSetChanged()

        //Rest number and date shown in the bottom text views
        updateInfoForDate(currentlyShownData.last())
    }

    private fun updateDisplayWithData(dailyData: List<CovidData>) {
        currentlyShownData = dailyData

        adapter = CovidSparkAdapter(dailyData)
        sparkView.adapter = adapter
        radioButtonPositive.isChecked = true
        radioButtonMax.isChecked = true

        updateDisplayMetric(Metric.POSITIVE)
    }
    private fun updateInfoForDate(covidData: CovidData) {
        val numCases = when (adapter.metric){
            Metric.NEGATIVE -> covidData.negativeIncrease
            Metric.POSITIVE -> covidData.positiveIncrease
            Metric.DEATH -> covidData.deathIncrease
        }

        tvMetricLabel.text = NumberFormat.getInstance().format(numCases)
        val outputdateFormat = SimpleDateFormat("MMM dd,yyyy", Locale.US)
        tvDateLabel.text = outputdateFormat.format(covidData.dateChecked)

    }
}