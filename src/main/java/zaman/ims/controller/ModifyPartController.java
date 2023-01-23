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

public class ModifyPartController implements Initializable {

    public Label machineIDCompanyName;
    public TextField idModifyPartTxt;
    public TextField nameModifyPartTxt;
    public TextField invModifyPartTxt;
    public TextField priceModifyPartTxt;
    public TextField maxModifyPartTxt;
    public TextField machineIDModifyPartTxt;
    public TextField minModifyPartTxt;
    public RadioButton inHouseModifyPartBtn;
    public RadioButton outsourcedModifyPartBtn;

    /**
     * Exits ModifyPart view back to MainScreen view.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    protected void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("Modify Part: cancel button clicked.");

        Parent parent = FXMLLoader.load(getClass().getResource("/zaman/ims/view/MainForm.fxml"));
        Stage stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());

        Scene scene = new Scene(parent, 900, 360);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify part initialized.");
        Part part = MainScreenController.selectedPart;

        // filling out Modify form with existing part values
        if(part  instanceof InHouse) {
            // set radio to inhouse, text field to machine ID and set machine id value
            inHouseModifyPartBtn.setSelected(true);
            machineIDCompanyName.setText("Machine ID");
            machineIDModifyPartTxt.setText(((InHouse)part).getMachineID()+"");
        } else if(part instanceof Outsourced) {
            // set radio to outsourced, text field to company name and set company name value
            outsourcedModifyPartBtn.setSelected(true);
            machineIDCompanyName.setText("Company Name");
            machineIDModifyPartTxt.setText(((Outsourced)part).getCompanyName());
        }

        // setting rest of values that are common to both inhouse and outsourced
        idModifyPartTxt.setText(part.getId()+"");
        nameModifyPartTxt.setText(part.getName());
        invModifyPartTxt.setText(part.getStock()+"");
        priceModifyPartTxt.setText(part.getPrice()+"");
        maxModifyPartTxt.setText(part.getMax()+"");
        minModifyPartTxt.setText(part.getMin()+"");
    }

    /**
     * Sets label to machine id if inhouse is selected.
     * @param actionEvent
     */
    public void inHouseSelected(ActionEvent actionEvent) {
        machineIDCompanyName.setText("Machine ID");
    }

    /**
     * Sets label to company name if outsourced is selected.
     * @param actionEvent
     */
    public void outSourcedSelected(ActionEvent actionEvent) {
        machineIDCompanyName.setText("Company Name");
    }

    /**
     * Gets all values from the fields and populates values after validating them.
     * @param actionEvent
     */
    public void saveButtonClicked(ActionEvent actionEvent) {
        // perform checks and then update part in inventory
        String name=null, companyName = null;
        int id = MainScreenController.selectedPart.getId();

        int stock = 0, max = 0, min = 0, machineID = 0;
        double price = 0;

        try {
            name = nameModifyPartTxt.getText();
            if(name.isEmpty())
                throw new Exception();
            stock = Integer.parseInt(invModifyPartTxt.getText());
            price = Double.parseDouble(priceModifyPartTxt.getText());
            max = Integer.parseInt(maxModifyPartTxt.getText());
            min = Integer.parseInt(minModifyPartTxt.getText());

            // input validation
            if(max<min || stock<min || stock>max)
                throw new Exception();

            if(inHouseModifyPartBtn.isSelected()) {
                machineID = Integer.parseInt(machineIDModifyPartTxt.getText());
            } else {
                companyName = machineIDModifyPartTxt.getText();
            }

            Part updatePart;
            if(inHouseModifyPartBtn.isSelected()) {
                updatePart = new InHouse(id, name, price, stock, min, max, machineID);
            } else {
                updatePart = new Outsourced(id, name, price, stock, min, max, companyName);
            }

            // Update part in inventory.
            Inventory.updatePart(id, updatePart);

            // Take user back to MainScreen
            Parent parent = FXMLLoader.load(getClass().getResource("/zaman/ims/view/MainForm.fxml"));
            Stage stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());

            Scene scene = new Scene(parent, 900, 360);
            stage.setTitle("Main Form");
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect value entered!");
            alert.setContentText(" "+e.getMessage());
            alert.showAndWait();
        }
    }
}
