package com.example.vnprk.prisontrainings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by VNPrk on 17.07.2017.
 */

public class ClassCharts {

	List<ClassTraining> trainings = null;
	List<Entry> entries = null;
	LineData chartData = null;
	int colorLine = 0;
	
    public ClassCharts() {
		trainings = new ArrayList<ClassTraining>();
		entries = new ArrayList<Entry>();
	}
	
	public void setDataChart(List<ClassTraining> _trainings){
		trainings.clear();
		trainings = _trainings;
		initChart();
	}

	public void setColorLine(int color){
		colorLine=color;
	}
	
	private void initChart(){
		entries.clear();
		int i = 1;
		if(trainings.size()>0)
		for (ClassTraining data : trainings) {
			entries.add(new Entry(i/*data.getDateTraining().getTime()*/, data.getLvlTrening()));
			i++;
		}
		else
			entries.add(new Entry(1,0));
		/*
        entries.add(new Entry(1,10));
        entries.add(new Entry(2,23));
        entries.add(new Entry(3,16));
        entries.add(new Entry(4,15));
        entries.add(new Entry(5,30));*/
		LineDataSet dataSet = new LineDataSet(entries, "Уровень"); // add entries to dataset
		dataSet.setMode(LineDataSet.Mode.STEPPED);
        dataSet.setLineWidth(3f);
		dataSet.setColor(colorLine);
		//dataSet.setColor();
		//dataSet.setValueTextColor(...);
		chartData = new LineData(dataSet);
	}
	
	public LineData getDataChart(){
		return chartData;
	}
	
	private void setXAxisFormatter(){
	}
	
	private void setYAxisFormatter(){
	}
}
