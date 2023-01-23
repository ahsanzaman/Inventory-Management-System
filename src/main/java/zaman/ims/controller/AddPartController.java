package zaman.ims.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zaman.ims.model.InHouse;
import zaman.ims.model.Inventory;
import zaman.ims.model.Outsourced;
import zaman.ims.model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {


    public Label machineIDCompanyName;
    public TextField nameAddPartTxt;
    public TextField invAddPartTxt;
    public TextField priceAddPartTxt;
    public TextField maxAddPartTxt;
    public TextField machineIDAddPartTxt;
    public RadioButton outsourcedAddPartBtn;
    public RadioButton inHouseAddPartBtn;
    public TextField minAddPartTxt;

    @FXML
    protected void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("Add Part: cancel button clicked.");

        Parent parent = FXMLLoader.load(getClass().getResource("/zaman/ims/view/MainForm.fxml"));
        Stage stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());

        Scene scene = new Scene(parent, 900, 360);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add part initialized.");
    }

    /**
     * When inhoucse radio is selected, the label is set to machine ID
     * @param actionEvent
     */
    public void inHouseSelected(ActionEvent actionEvent) {
        machineIDCompanyName.setText("Machine ID");
    }

    /**
     * When outsourced radio button is selected, label is set to Company Name
     * @param actionEvent
     */
    public void outsourcedSelected(ActionEvent actionEvent) {
        machineIDCompanyName.setText("Company Name");
    }

    /**
     * Generates id, validates inputs, and then adds the part to Inventory.
     * @param actionEvent
     */
    public void addPart(ActionEvent actionEvent) {
        // gets the current max ID which should be the last entry in list
        // if list is empty just set id to 1
        int id = 1;
        if(Inventory.getAllParts().size()!=0) {
            id = Inventory.getAllParts().get(Inventory.getAllParts().size()-1).getId()+1;
        }

        String name = null, companyName = null;
        int stock = 0, max = 0, min = 0, machineID = 0;
        double price = 0;
        try {
            name = nameAddPartTxt.getText();
            if(name.isEmpty())
                throw new Exception();
            stock = Integer.parseInt(invAddPartTxt.getText());
            price = Double.parseDouble(priceAddPartTxt.getText());
            max = Integer.parseInt(maxAddPartTxt.getText());
            min = Integer.parseInt(minAddPartTxt.getText());

            // input validation
            if(max<min || stock<min || stock>max)
                throw new Exception();

            // if inhouse get machineID, if outsourced get company name
            if(inHouseAddPartBtn.isSelected()) {
                machineID = Integer.parseInt(machineIDAddPartTxt.getText());
            } else {
                companyName = machineIDAddPartTxt.getText();
            }

            Part newPart;
            if(inHouseAddPartBtn.isSelected()) {
                newPart = new InHouse(id, name, price, stock, min, max, machineID);
            } else {
                newPart = new Outsourced(id, name, price, stock, min, max, companyName);
            }
            Inventory.addNewPart(newPart);

            Parent parent = FXMLLoader.load(getClass().getResource("/zaman/ims/view/MainForm.fxml"));
            Stage stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());

            Scene scene = new Scene(parent, 900, 360);
            stage.setTitle("Main Form");
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect value entered!");
            alert.setContentText("Please enter a correct value for each field and try again.");
            alert.showAndWait();
        }
    }
}
