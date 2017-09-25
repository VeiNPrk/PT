package com.example.vnprk.prisontrainings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by VNPrk on 22.07.2017.
 */

public class ActivityStatistics extends AppCompatActivity implements
        OnChartGestureListener, OnChartValueSelectedListener {
    public static final String TYPE_TREN_KEY = "type_trening_key";
    LineChart mChart;
    Spinner spnTrain;
    int typeTrening = 1;
    int isStart=0;
    List<ClassTraining> trainings = null;
    DataResCompound dataRes;
    ClassCharts chartsData;

    public static void openActivity(Context context, int type){
        Intent intent = new Intent(context, ActivityStatistics.class);
        intent.putExtra(TYPE_TREN_KEY, type);
        /*if (!(context instanceof Activity))
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);*/
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        dataRes = DataResCompound.getInstance();
        chartsData = new ClassCharts();
        chartsData.setColorLine(ContextCompat.getColor(this,R.color.colorAccent));
        typeTrening = getIntent().getIntExtra(TYPE_TREN_KEY,1);
        initViews();
        initData();

        setSpinner();
        initChart();
    }

    private void initViews(){
        mChart = (LineChart) findViewById(R.id.chart);
        spnTrain = (Spinner)findViewById(R.id.spn_train);
    }

    private void initData(){
        trainings = new ArrayList<ClassTraining>();
        //getTrainList();
    }

    private void getTrainList(){
        trainings.clear();
        trainings = new Select().from(ClassTraining.class).where(ClassTraining_Table.typeTrening.is(typeTrening)).and(ClassTraining_Table.dateTraining.isNotNull()).orderBy(ClassTraining_Table.typeTrening, true).queryList();
        if(trainings.size()!=0){
            for (ClassTraining train : trainings) {
                train.setNeedAttempts(dataRes.getTextRes(train.getIdAttempts()));
            }
        }


    }

    private void setSpinner() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.trainings, android.R.layout.simple_spinner_dropdown_item);
        spnTrain.setAdapter(adapter);
        spnTrain.setSelection(typeTrening-1);
        spnTrain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isStart>0) {
                    typeTrening=position+1;
                    refreshChart();
                }
                isStart++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initStartData(){
        ClassTraining t1 = new ClassTraining(1,1);
        t1.save();
        ClassTraining t2 = new ClassTraining(2,1);
        t2.save();
        ClassTraining t3 = new ClassTraining(3,1);
        t3.save();
        ClassTraining t4 = new ClassTraining(4,1);
        t4.save();
        ClassTraining t5 = new ClassTraining(5,1);
        t5.save();
        ClassTraining t6 = new ClassTraining(6,1);
        t6.save();
    }

    private void refreshChart(){
        getTrainList();
        //if(trainings.size()>0){
            chartsData.setDataChart(trainings);
            mChart.setData(chartsData.getDataChart());
        /*}
        else {
            //chartsData.setDataChart(null);
            mChart.setData(null);
        }*/
        mChart.invalidate(); // refresh
        mChart.moveViewToX(0f);
        mChart.animateY(500);
    }
    private void initChart(){
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //xAxis.setSpaceMin(1);

        //xAxis.setLabelCount(trainings.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM.yy");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                /*long millis = (long) value;
                Date d = new Date(millis);
                String s = mFormat.format(d);*/
                int index = (int)value;
                String s="";
                if(trainings.size()>0 && index<=trainings.size() && index>0)
                    s = mFormat.format(trainings.get(index-1).getDateTraining());
                else
                    s = "0";
                return s;
            }
        });
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12f);
        xAxis.setAvoidFirstLastClipping(true);
        //xAxis.setXR
        //xAxis.setLabelCount(5,true);
        //xAxis.setSpaceMax(5f);
        //xAxis.setAxisMaximum(5f);
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line


        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        /*leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);*/
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        leftAxis.setLabelCount(10);
        leftAxis.setTextSize(15f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        //setData(45, 100);
        refreshChart();
//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        //mChart.animateX(500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setTextSize(20f);
        l.setForm(Legend.LegendForm.LINE);
        mChart.setVisibleXRangeMaximum(4f);
        /*mChart.setExtraLeftOffset(15);
        mChart.setExtraRightOffset(15);*/

        //mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        mChart.animateY(1500);
    }
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleX() + ", high: " + mChart.getHighestVisibleX());
        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }


   /* private void initChart(){
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        // add data
        //setData(100, 30);
        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        //xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(170f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }*/
}
