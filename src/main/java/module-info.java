module org.voorraadbeheer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens org.voorraadbeheer to javafx.fxml;
    exports org.voorraadbeheer;
}