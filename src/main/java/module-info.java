module org.voorraadbeheer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.slf4j;


    opens org.voorraadbeheer to javafx.fxml;
    opens org.voorraadbeheer.PageController to javafx.fxml;
    exports org.voorraadbeheer;
}