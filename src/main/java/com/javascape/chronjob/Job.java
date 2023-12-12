package com.javascape.chronjob;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;

public abstract class Job {

    protected String name;

    protected transient Runnable runnable;

    protected ArrayList<String> commands;

    transient protected ScheduledFuture<?> future;

    public Job(String name, ArrayList<String> commands) {
        this.name = name;
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    public ScheduledFuture<?> getFuture() {
        return future;
    }

    @Override
    public String toString() {
        return name;
    }

}
