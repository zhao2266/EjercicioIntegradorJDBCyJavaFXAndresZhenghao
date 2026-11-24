module org.example.ejerciciointegradorjdbcyjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.ejerciciointegradorjdbcyjavafx to javafx.fxml;
    exports org.example.ejerciciointegradorjdbcyjavafx;
}