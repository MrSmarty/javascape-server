package com.javascape.chronjob;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.javascape.Logger;
import com.javascape.Server;
import com.javascape.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChronManager {

    transient private ArrayList<ChronjobItem> chronjobItems = new ArrayList<ChronjobItem>();

    transient private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    private ObservableList<Chronjob> repeatingJobs = FXCollections.observableArrayList();

    private ObservableList<ConditionalJob> conditionalJobs = FXCollections.observableArrayList();

    private ObservableList<Job> allJobs = FXCollections.observableArrayList();

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

    public boolean newRepeating(String json) {
        Logger.print(json);
        Chronjob j = (Chronjob) Server.getDataHandler().deserialize(json, Chronjob.class);
        if (j != null) {
            newRepeating(j);
            return true;
        }
        return false;
    }

    public void newConditional(ConditionalJob job) {
        job.setFuture(scheduler.scheduleAtFixedRate(job.getRunnable(), 0, Settings.conditionalCheckInterval, Settings.conditionalCheckUnit));

        conditionalJobs.add(job);
    }

    public void quit() {
        scheduler.shutdownNow();
    }

    public boolean remove(Chronjob job) {
        Logger.debug("Removing repeating job");
        boolean b = repeatingJobs.remove(job);
        if (b)
            job.getFuture().cancel(true);
        else {
            for (Job j : repeatingJobs) {
                if (j.getName().equals(job.getName()) && j.commands.equals(job.commands)) {
                    repeatingJobs.remove(j);
                    j.getFuture().cancel(true);
                    b = true;
                    break;
                }
            }
        }
        return b;
    }

    public boolean remove(ConditionalJob job) {
        Logger.debug("Removing conditional job");
        boolean b = conditionalJobs.remove(job);
        if (b)
            job.getFuture().cancel(true);
        return b;
    }

    public boolean remove(Job job) {
        System.out.println(job.getClass());
        if (job instanceof Chronjob)
            return remove((Chronjob) job);
        else if (job instanceof ConditionalJob)
            return remove((ConditionalJob) job);
        return false;
    }

    public ObservableList<Chronjob> getRepeatingChronjobs() {
        return repeatingJobs;
    }

    public ObservableList<ConditionalJob> getConditionalJobs() {
        return conditionalJobs;
    }

    public ObservableList<Job> getAllJobs() {
        allJobs.clear();
        allJobs.addAll(repeatingJobs);
        allJobs.addAll(conditionalJobs);
        return allJobs;
    }

    public ArrayList<ChronjobItem> getAllItems() {
        return chronjobItems;
    }

}
