/**
 * @author Ahsan Zaman
 * c482 Software 1 Project Submission. This is an Inventory Management System which allows users to
 * add, manage and track parts and products in inventory.
 */

package zaman.ims.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import zaman.ims.model.*;

import java.io.IOException;

/*
RUNTIME ERROR

One of the runtime errors I received while testing the project was a Null Pointer Exception.
This was after I deleted all parts added in testData and tried to add a new part. It was throwing
an exception because I was generating id by getting the id of the part in the last index. I added
a check which confirms if parts list or products list is empty in Inventory, just start the id
from scratch.
 */

/*
FUTURE ENHANCEMENT
This application can be improved in a number of ways. A login screen can be added to secure the
application and an accounts profile system can be added which would differentiate between regular
and admin users. Furthermore, this application can be made persistent by added a database. MVC
architecture applications generally have a DAO layer as well in which we can create data access.
 */

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // initializing application with test data
        addTestData();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/zaman/ims/view/MainForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 360);
        stage.setTitle("Inventory Management Application");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method will add test data at the start of the application.
     */
    public void addTestData() {
        // add testData here
        InHouse tvScreen = new InHouse(1,"TV Screen",499.99,7,1,20,101);
        InHouse tvShell = new InHouse(2,"TV Antenna",50.0,10,1,20,101);
        InHouse powerCord = new InHouse(3,"Power Cord", 5.99, 7, 1,20,101);
        Outsourced remote = new Outsourced(4, "Remote Control",29.99, 50, 30,100, "Samsung");

        Inventory.addNewPart(tvScreen);
        Inventory.addNewPart(tvShell);
        Inventory.addNewPart(powerCord);
        Inventory.addNewPart(remote);

        //Add sample product and add associated parts to the product
        Product television = new Product( 1000,"LCD Television", 499.99, 20, 20, 100);
        television.addAssociatedPart(tvScreen);
        television.addAssociatedPart(tvShell);
        television.addAssociatedPart(powerCord);
        television.addAssociatedPart(remote);
        Inventory.addNewProduct(television);
    }

    public static void main(String[] args) {
        launch();
    }
}