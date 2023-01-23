module zaman.ims {
    requires javafx.controls;
    requires javafx.fxml;

    opens zaman.ims.view to javafx.fxml;
    exports zaman.ims.controller;
    opens zaman.ims.controller to javafx.fxml;
    exports zaman.ims.app;
    opens zaman.ims.app to javafx.fxml;
    exports zaman.ims.model;
    opens zaman.ims.model to javafx.fxml;
}