package com.javascape.chronjob;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.javascape.Logger;
import com.javascape.Server;
import com.javascape.receivers.Receiver;

public class Chronjob extends Job {

    private int period;

    private TimeUnit timeUnit;

    public Chronjob(String name, ArrayList<String> commands, int period, TimeUnit timeUnit) {
        super(name, commands);
        this.period = period;
        this.timeUnit = timeUnit;
    }

    public Runnable getRunnable() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    for (String s : commands) {
                        String[] ls = s.split(" - ");
                        String[] targets = ls[1].split(" ");
                        if (Server.getDataHandler() != null && !targets[0].equals("all"))
                            for (String current : targets) {
                                Receiver r = Server.getDataHandler().getReceiverHandler().getReceiver(current);

                                if (r.getCurrentThread() != null) {
                                    if (ls[0].startsWith("wait")) {
                                        String[] args = ls[0].split(" ");
                                        // System.out.println("Waiting " + args[1] + " Seconds");
                                        Thread.sleep(Integer.parseInt(args[1]));
                                    } else if (ls[0].startsWith("setPin")) {
                                        String[] args = ls[0].split(" ");
                                        r.setValue(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                                    } else if (ls[0].startsWith("togglePin")) {
                                        // System.out.println("Toggling");
                                        String[] args = ls[0].split(" ");
                                        r.setValue(Integer.parseInt(args[1]),
                                                r.getValues()[Integer.parseInt(args[1])] == 1 ? 0 : 1);
                                    } else if (ls[0].startsWith("getSensors")) {
                                        String temp = ls[0];
                                        for (int i = 0; i < r.getSensors().length; i++) {
                                            if (r.getSensors()[i] != null) {
                                                temp += " " + i;
                                            }
                                        }
                                        r.getCurrentThread().addCommand(temp, 0);
                                    } else if (ls[0].startsWith("getDigitalSensors")) {
                                        String temp = ls[0];
                                        for (int i = 0; i < r.getDigitalSensors().size(); i++) {
                                            temp += " " + r.getDigitalSensors().get(i).getCommand();
                                            System.out.println(r.getDigitalSensors().size());
                                        }
                                        r.getCurrentThread().addCommand(temp, 0);
                                    } else
                                        r.getCurrentThread().addCommand(ls[0], 0);
                                }

                            }
                        else if (Server.getDataHandler() != null)
                            // All active receivers
                            for (Receiver r : Server.getDataHandler().getReceiverHandler().getActiveReceiverList()) {

                                if (ls[0].startsWith("wait")) {
                                    String[] args = ls[0].split(" ");
                                    Thread.sleep(Integer.parseInt(args[1]));
                                } else if (ls[0].startsWith("setPin")) {
                                    String[] args = ls[0].split(" ");
                                    r.setValue(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                                } else if (ls[0].startsWith("togglePin")) {
                                    String[] args = ls[0].split(" ");
                                    r.setValue(Integer.parseInt(args[1]),
                                            r.getValues()[Integer.parseInt(args[1])] == 1 ? 0 : 1);
                                } else if (ls[0].startsWith("getSensors")) {
                                    String temp = ls[0];
                                    for (int i = 0; i < r.getSensors().length; i++) {
                                        if (r.getSensors()[i] != null) {
                                            temp += " " + i;
                                        }
                                    }
                                    r.getCurrentThread().addCommand(temp, 0);
                                } else if (ls[0].startsWith("getDigitalSensors")) {
                                    String temp = ls[0];
                                    for (int i = 0; i < r.getDigitalSensors().size(); i++) {
                                        temp += " " + r.getDigitalSensors().get(i).getCommand();
                                        System.out.println(r.getDigitalSensors().size());
                                    }
                                    r.getCurrentThread().addCommand(temp, 0);
                                } else
                                    r.getCurrentThread().addCommand(ls[0], 0);
                            }
                    }

                    // Server.getGUI().getReceiverView().update();
                } catch (Exception e) {
                    Logger.error(e.toString());
                    e.printStackTrace();
                }
            }
        };
        return runnable;

    }

    public int getPeriod() {
        return period;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
