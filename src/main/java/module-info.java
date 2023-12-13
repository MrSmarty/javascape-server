module com.javascape {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.javascape to com.google.gson;
    opens com.javascape.recievers to com.google.gson;
    opens com.javascape.chronjob to com.google.gson;

    //opens com.javascape.resources;

    exports com.javascape;
}
