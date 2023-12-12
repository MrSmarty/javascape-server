package com.javascape.chronjob;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.javascape.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChronManager {

    transient private ArrayList<ChronjobItem> chronjobItems = new ArrayList<ChronjobItem>();

    transient private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    private ObservableList<Chronjob> repeatingJobs = FXCollections.observableArrayList();

    private ObservableList<ConditionalJob> conditionalJobs = FXCollections.observableArrayList();

    public void loadData() {
        chronjobItems.add(new ChronjobItem("Set pin", "to on", "setPin %1$s 1"));
        chronjobItems.add(new ChronjobItem("Set pin", "to off", "setPin %1$s 0"));
        chronjobItems.add(new ChronjobItem("Toggle value of pin", "togglePin %1$s"));
        chronjobItems.add(new ChronjobItem("Wait", "seconds", "wait %1$s"));

        
    }

    public void startJobs() {
        for (Chronjob job : repeatingJobs)
            job.setFuture(scheduler.scheduleAtFixedRate(job.getRunnable(), 0, job.getPeriod(), job.getTimeUnit()));
    }

    public void newRepeating(Chronjob job) {

        job.setFuture(scheduler.scheduleAtFixedRate(job.getRunnable(), 0, job.getPeriod(), job.getTimeUnit()));

        repeatingJobs.add(job);
    }

    public void newRepeating(Chronjob job, boolean addtoList) {

        job.setFuture(scheduler.scheduleAtFixedRate(job.getRunnable(), 0, job.getPeriod(), job.getTimeUnit()));

        if (addtoList)
            repeatingJobs.add(job);
    }

    public void newConditional(ConditionalJob job) {
        job.setFuture(scheduler.scheduleAtFixedRate(job.getRunnable(), 0, Settings.conditionalCheckInterval, Settings.conditionalCheckUnit));

        conditionalJobs.add(job);
    }

    public void quit() {
        scheduler.shutdownNow();
    }

    public void remove(Chronjob job) {
        repeatingJobs.remove(job);
        job.getFuture().cancel(true);
    }

    public ObservableList<Chronjob> getRepeatingChronjobs() {
        return repeatingJobs;
    }

    public ObservableList<ConditionalJob> getConditionalJobs() {
        return conditionalJobs;
    }

    public ArrayList<ChronjobItem> getAllItems() {
        return chronjobItems;
    }

}
