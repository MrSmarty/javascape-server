module com.javascape {
    
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive com.google.gson;
    requires transitive javafx.base;
    requires transitive javafx.graphics;

    opens com.javascape to com.google.gson;
    opens com.javascape.recievers to com.google.gson;
    opens com.javascape.chronjob to com.google.gson;

    //opens com.javascape.resources;

    exports com.javascape;
    exports com.javascape.chronjob;
    exports com.javascape.recievers;
    exports com.javascape.gsonDeserializers;
    exports com.javascape.menuPopups;
    exports com.javascape.sensors;
}
