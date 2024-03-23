module ru.vladushik.vkusvillstoremanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens ru.vladushik.vkusvillstoremanagement to javafx.fxml;
    opens ru.vladushik.vkusvillstoremanagement.controllers to javafx.fxml;
    exports ru.vladushik.vkusvillstoremanagement;
}