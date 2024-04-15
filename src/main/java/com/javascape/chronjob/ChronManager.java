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

    transient private final ArrayList<ChronjobItem> chronjobItems = new ArrayList<>();

    transient private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    private final ObservableList<Chronjob> repeatingJobs = FXCollections.observableArrayList();

    private final ObservableList<ConditionalJob> conditionalJobs = FXCollections.observableArrayList();

    private final ObservableList<Job> allJobs = FXCollections.observableArrayList();

    public void loadData() {
        chronjobItems.add(new ChronjobItem("Set pin", "to on", "setPin %1$s 1"));
        chronjobItems.add(new ChronjobItem("Set pin", "to off", "setPin %1$s 0"));
        chronjobItems.add(new ChronjobItem("Toggle value of pin", "togglePin %1$s"));
        chronjobItems.add(new ChronjobItem("Wait", "seconds", "wait %1$s"));

    }

    /**
     * Starts all the Chronjobs
     */
    public void startJobs() {
        for (Chronjob job : repeatingJobs) {
            job.setFuture(scheduler.scheduleAtFixedRate(job.getRunnable(), 0, job.getPeriod(), job.getTimeUnit()));
        }
    }

    /**
     *
     * @param job The job to add
     */
    public void newRepeating(Chronjob job) {

        job.setFuture(scheduler.scheduleAtFixedRate(job.getRunnable(), 0, job.getPeriod(), job.getTimeUnit()));

        repeatingJobs.add(job);
    }

    /**
     *
     * @param job The job to add
     * @param addtoList Whether or not to add the job to the joblist so that it
     * is saved.
     */
    public void newRepeating(Chronjob job, boolean addtoList) {

        job.setFuture(scheduler.scheduleAtFixedRate(job.getRunnable(), 0, job.getPeriod(), job.getTimeUnit()));

        if (addtoList) {
            repeatingJobs.add(job);
        }
    }

    /**
     * Creates a new repeating job from a json string
     *
     * @param json
     * @return true if the job was created succesfully
     */
    public boolean newRepeating(String json) {
        Logger.print(json);
        Chronjob j = (Chronjob) Server.getDataHandler().deserialize(json, Chronjob.class);
        if (j != null) {
            newRepeating(j);
            return true;
        }
        return false;
    }

    /**
     * Creates and initializes a new ChonditionalJob
     *
     * @param job The ConditionalJob to add
     */
    public void newConditional(ConditionalJob job) {
        job.setFuture(scheduler.scheduleAtFixedRate(job.getRunnable(), 0, Settings.conditionalCheckInterval, Settings.conditionalCheckUnit));

        conditionalJobs.add(job);
    }

    /**
     * Shuts down the ChronManager and kills the scheduler
     */
    public void quit() {
        scheduler.shutdownNow();
    }

    /**
     * Removes a job from the repeatingJobs list
     * @param job The job to remove
     * @return Returns true if the job was removed succesfully
     */
    public boolean remove(Chronjob job) {
        Logger.debug("Removing repeating job");
        boolean b = repeatingJobs.remove(job);
        if (b) {
            job.getFuture().cancel(true);
        } else {
            for (Chronjob j : repeatingJobs) {
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

    /**
     * Removes a job from the conditionalJobs list
     * @param job The job to remove
     * @return Returns true if the job was removed succesfully
     */
    public boolean remove(ConditionalJob job) {
        Logger.debug("Removing conditional job");
        boolean b = conditionalJobs.remove(job);
        if (b) {
            job.getFuture().cancel(true);
        }
        return b;
    }

    /**
     * Removes the job from whichever list it belongs to.
     * @param job Any job you would like ot remove from the ChronManager
     * @return Returns true if the job was removed succesfully
     */
    public boolean remove(Job job) {
        System.out.println(job.getClass());
        switch (job) {
            case Chronjob chronjob -> {
                return remove(chronjob);
            }
            case ConditionalJob conditionalJob -> {
                return remove(conditionalJob);
            }
            default -> {
            }
        }
        return false;
    }

    /**
     * @return Returns the list of repeating jobs 
     */
    public ObservableList<Chronjob> getRepeatingChronjobs() {
        return repeatingJobs;
    }

    /**
     * @return Returns the list of conditional jobs
     */
    public ObservableList<ConditionalJob> getConditionalJobs() {
        return conditionalJobs;
    }

    /**
     * @return Returns the list of all jobs
     */
    public ObservableList<Job> getAllJobs() {
        allJobs.clear();
        allJobs.addAll(repeatingJobs);
        allJobs.addAll(conditionalJobs);
        return allJobs;
    }

    /**
     * @return Returns the list of all chronJobItems
     */
    public ArrayList<ChronjobItem> getAllItems() {
        return chronjobItems;
    }

}
