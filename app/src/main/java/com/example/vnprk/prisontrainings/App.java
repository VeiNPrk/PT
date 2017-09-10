package com.example.vnprk.prisontrainings;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by VNPrk on 03.03.2017.
 */
public class App extends Application {
    List<ClassTraining> trainings;
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(
                new FlowConfig.Builder(this)
                        .openDatabasesOnInit(true)
                        .build()
        );
        CheckTreining();
        DataResCompound.initInstance(this);
    }

    private void CheckTreining(){
        trainings = new ArrayList<ClassTraining>();
        trainings = new Select().from(ClassTraining.class).where(ClassTraining_Table.lastTraining.is(0)).or(ClassTraining_Table.lastTraining.is(2)).orderBy(ClassTraining_Table.typeTrening, true).queryList();
        //trainings = new Select().from(ClassTraining.class).orderBy(ClassTraining_Table.typeTrening, true).queryList();
        int nowLevel = 1;
        if(trainings.size()>0) {
            for (ClassTraining training : trainings) {
                training.setMyAttempts();
                //if(!compareTrainingDates(training)) {
                    if (training.getMyStrAttempts().length() > 0 && training.getMyAttempts().size() > 0) {

                        nowLevel = training.getLvlTrening();
                        String strMyAtt = "";
                        if (training.checkAttempts()) {
                            if (nowLevel < 10)
                                nowLevel++;
                        } else
                            strMyAtt = training.getMyStrAttempts();
                        training.setLastTrain();
                        //training.setLastDateTraining();
                        training.save();
                        ClassTraining newTraining = new ClassTraining(training.getTypeTrening(), nowLevel);
                        newTraining.setDateTraining(training.getDateTraining());
                        newTraining.setOldStrAttempts(strMyAtt);
                        newTraining.save();
                    }
                    if(training.getLastTraining()==2) {
                        training.setLastTraining(0);
                        training.save();
                    }
                //}
            }
        }
    }
    private boolean compareTrainingDates(ClassTraining training){
        boolean tf = false;
        Calendar nowDate = Calendar.getInstance();
        Calendar trainingDate = Calendar.getInstance();
        nowDate.setTime(new Date());
        if(training.getDateTraining()!=null) {
            trainingDate.setTime(training.getDateTraining());
            tf = nowDate.get(Calendar.YEAR) == trainingDate.get(Calendar.YEAR) &&
                    nowDate.get(Calendar.DAY_OF_YEAR) == trainingDate.get(Calendar.DAY_OF_YEAR);
        }
        return tf;
        //return false;
    }
}
