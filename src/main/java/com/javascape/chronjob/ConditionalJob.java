package com.javascape.chronjob;

import java.util.ArrayList;

import com.javascape.Logger;
import com.javascape.ReceiverHandler;
import com.javascape.Server;
import com.javascape.receivers.Receiver;

public class ConditionalJob extends Job {

    // Store each condition as:
    // ReceiverUID:SensorIndex:ValueIndex Operator value
    ArrayList<String> conditions;
    ArrayList<String> elseCommands;

    public ConditionalJob(String name, ArrayList<String> commands, ArrayList<String> conditions) {
        super(name, commands);
        this.conditions = conditions;
    }

    public ConditionalJob(String name, ArrayList<String> commands, ArrayList<String> conditions,
            ArrayList<String> elseCommands) {
        super(name, commands);
        this.conditions = conditions;
        this.elseCommands = elseCommands;
    }

    public Runnable getRunnable() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    if (checkConditions()) {

                        for (String s : commands) {
                            String[] ls = s.split(" - ");
                            String[] targets = ls[1].split(" ");
                            if (!targets[0].equals("all"))
                                for (String current : targets) {
                                    Receiver r = Server.getDataHandler().getReceiverHandler().getReceiver(current);

                                    if (r.getCurrentThread() != null) {
                                        if (ls[0].startsWith("wait")) {
                                            String[] args = ls[0].split(" ");
                                            System.out.println("Waiting " + args[1] + " Seconds");
                                            Thread.sleep(Integer.parseInt(args[1]));
                                        } else if (ls[0].startsWith("setPin")) {
                                            String[] args = ls[0].split(" ");
                                            r.setValue(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                                        } else if (ls[0].startsWith("togglePin")) {
                                            System.out.println("Toggling");
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
                                        } else
                                            r.getCurrentThread().addCommand(ls[0], 0);
                                    }

                                }
                            else
                                // All active receivers
                                for (Receiver r : Server.getDataHandler().getReceiverHandler()
                                        .getActiveReceiverList()) {

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
                                    } else
                                        r.getCurrentThread().addCommand(ls[0], 0);
                                }
                        }
                    } else {
                        for (String s : elseCommands) {
                            String[] ls = s.split(" - ");
                            String[] targets = ls[1].split(" ");
                            if (!targets[0].equals("all"))
                                for (String current : targets) {
                                    Receiver r = Server.getDataHandler().getReceiverHandler().getReceiver(current);

                                    if (r.getCurrentThread() != null) {
                                        if (ls[0].startsWith("wait")) {
                                            String[] args = ls[0].split(" ");
                                            System.out.println("Waiting " + args[1] + " Seconds");
                                            Thread.sleep(Integer.parseInt(args[1]));
                                        } else if (ls[0].startsWith("setPin")) {
                                            String[] args = ls[0].split(" ");
                                            r.setValue(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                                        } else if (ls[0].startsWith("togglePin")) {
                                            System.out.println("Toggling");
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
                                        } else
                                            r.getCurrentThread().addCommand(ls[0], 0);
                                    }

                                }
                            else
                                // All active receivers
                                for (Receiver r : Server.getDataHandler().getReceiverHandler()
                                        .getActiveReceiverList()) {

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
                                    } else
                                        r.getCurrentThread().addCommand(ls[0], 0);
                                }
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

    private boolean checkConditions() {
        ReceiverHandler handlerReference = Server.getDataHandler().getReceiverHandler();
        for (String s : conditions) {
            String[] args = s.split("[ :]");
            Double currentSensorValue;
            Receiver r = handlerReference.getReceiver(args[0]);
            if (Integer.parseInt(args[1]) > r.getGPIO().length) {
                // Analog Sensor
                currentSensorValue = r.getSensor(Integer.parseInt(args[1]) - r.getGPIO().length)
                        .getCurrentValueAsDouble();

                System.out.println("Current Sensor Value: " + currentSensorValue);
                if (currentSensorValue == null) {
                    return false;
                }

                if (args[2].equals("==")) {
                    if (currentSensorValue == Double.parseDouble(args[3])) {
                        return true;
                    }
                } else if (args[2].equals(">")) {
                    if (currentSensorValue > Double.parseDouble(args[3])) {
                        return true;
                    }
                } else if (args[2].equals(">=")) {
                    if (currentSensorValue >= Double.parseDouble(args[3])) {
                        return true;
                    }
                } else if (args[2].equals("<")) {
                    if (currentSensorValue < Double.parseDouble(args[3])) {
                        return true;
                    }
                } else if (args[2].equals("<=")) {
                    if (currentSensorValue <= Double.parseDouble(args[3])) {
                        return true;
                    }
                } else if (args[2].equals("!=")) {
                    if (currentSensorValue != Double.parseDouble(args[3])) {
                        return true;
                    }
                } else {
                    System.out.println("Invalid Operator: " + args[2]);
                }

            } else {
                // Digital Sensor
                currentSensorValue = Double.parseDouble(
                        r.getGPIO()[Integer.parseInt(args[1])].getSensor().getValue(args[2]));
                System.out.println("Current Sensor Value: " + currentSensorValue);

                if (args[3].equals("==")) {
                    if (currentSensorValue == Double.parseDouble(args[4])) {
                        return true;
                    }
                } else if (args[3].equals(">")) {
                    if (currentSensorValue > Double.parseDouble(args[4])) {
                        return true;
                    }
                } else if (args[3].equals(">=")) {
                    if (currentSensorValue >= Double.parseDouble(args[4])) {
                        return true;
                    }
                } else if (args[3].equals("<")) {
                    if (currentSensorValue < Double.parseDouble(args[4])) {
                        return true;
                    }
                } else if (args[3].equals("<=")) {
                    if (currentSensorValue <= Double.parseDouble(args[4])) {
                        return true;
                    }
                }

            }

        }
        return false;
    }
}
