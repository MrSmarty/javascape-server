package com.javascape.chronjob;

public class ChronjobItem {

    String label;
    String labelAfter;
    String template;

    public ChronjobItem(String label, String template) {
        this.label = label;
        this.template = template;
    }

    public ChronjobItem(String label, String labelAfter, String template) {
        this.label = label;
        this.labelAfter = labelAfter;
        this.template = template;
    }

    public String getCommand(String... args) {
        return String.format(template, (Object[])args);
    }

    public String getLabel() {
        return label;
    }

    public String getAfterLabel() {
        return labelAfter;
    }

    @Override
    public String toString() {
        if (labelAfter != null)
            return label + " X " + labelAfter;
        else
            return label + " X ";
    }
}
