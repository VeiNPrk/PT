package com.example.vnprk.prisontrainings;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by VNPrk on 03.03.2017.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(
                new FlowConfig.Builder(this)
                        .openDatabasesOnInit(true)
                        .build()
        );
        DataResCompound.initInstance(this);
    }
}
