package zaman.ims.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import zaman.ims.model.Inventory;
import zaman.ims.model.Part;
import zaman.ims.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyProductController implements Initializable {

    public TableView<Part> allPartsTableM;
    public TableColumn allPartIdCol;
    public TableColumn allPartNameCol;
    public TableColumn allPartStockCol;
    public TableColumn allPartPriceCol;
    public TableView<Part> assocPartTableM;
    public TableColumn assocPartIdCol;
    public TableColumn assocPartNameCol;
    public TableColumn assocPartStockCol;
    public TableColumn assocPartPriceCol;
    public TextField searchModifyProductTxt;
    public TextField idModifyProductTxt;
    public TextField nameModifyProductTxt;
    public TextField invModifyProductTxt;
    public TextField priceModifyProductTxt;
    public TextField maxModifyProductTxt;
    public TextField minModifyProductTxt;
    public ObservableList<Part> assocParts;

    /**
     * Exits ModifyProduct view back to MainScreen view.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    protected void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("Modify Product: cancel button clicked.");

        Parent parent = FXMLLoader.load(getClass().getResource("/zaman/ims/view/MainForm.fxml"));
        Stage stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());

        Scene scene = new Scene(parent, 900, 360);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Populates all fields from the selected value from MainScreen view.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify product initialized.");

        idModifyProductTxt.setText(MainScreenController.selectedProduct.getId()+"");
        nameModifyProductTxt.setText(MainScreenController.selectedProduct.getName());
        invModifyProductTxt.setText(MainScreenController.selectedProduct.getStock()+"");
        priceModifyProductTxt.setText(MainScreenController.selectedProduct.getPrice()+"");
        maxModifyProductTxt.setText(MainScreenController.selectedProduct.getMax()+"");
        minModifyProductTxt.setText(MainScreenController.selectedProduct.getMin()+"");

        allPartsTableM.setItems(Inventory.getAllParts());
        allPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // get selected product from main screen and associated part list
        assocParts = FXCollections.observableArrayList();
        for(Part part:MainScreenController.selectedProduct.getAllAssociatedParts()) {
            assocParts.add(part);
            System.out.println("Part id: "+part.getId());
        }
        assocPartTableM.setItems(assocParts);
        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Searches parts by name. If nothing is returned, it searches by id instead.
     * If nothing is returned still, it displays an error and then displays all parts.
     * @param actionEvent
     */
    public void getPartSearchResult(ActionEvent actionEvent) {
        System.out.println("Part Search entered in Product Modify screen.");
        String query = searchModifyProductTxt.getText();
        ObservableList<Part> searchResult = Inventory.lookupPart(query);
        boolean resultReturned=true;

        // searching with id instead
        if(searchResult.size()==0) {
            try {
                Part partReturned = Inventory.lookupPart(Integer.parseInt(query));
                searchResult.add(partReturned);

                if(partReturned==null)
                    resultReturned=false;
            } catch(Exception e) {
                System.out.println("Unable to parse int for part search by id.");
            }
        }

        // when no results were returned, display alert
        if(!resultReturned) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No results found.");
            alert.setContentText("Your search did not return any results.");
            alert.showAndWait();

            allPartsTableM.setItems(searchResult);
        }
        else {
            allPartsTableM.setItems(searchResult);
        }

    }

    /**
     * Checks if a part has been selected or not, then checks if the part already exists in assocParts list.
     * If these checks are passed, this method adds the part to the assocParts list.
     * @param actionEvent
     */
    public void addPartButtonClicked(ActionEvent actionEvent) {
        Part part = allPartsTableM.getSelectionModel().getSelectedItem();
        if(part==null) {
            // alert to select part to move it to associated parts
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No part has been selected!");
            alert.setContentText("Please select a part to add it to associated parts.");
            alert.showAndWait();
        } else if(assocParts.contains(part)) {
            // part already exists in list, display error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("Part already exists in associated parts.");
            alert.showAndWait();
        } else {
            assocParts.add(allPartsTableM.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Removes selected part from the assocParts list.
     * @param actionEvent
     */
    public void removeAssocPartClicked(ActionEvent actionEvent) {
        Part removePart = assocPartTableM.getSelectionModel().getSelectedItem();
        System.out.println("Remove part: "+ removePart.getId());
        assocParts.remove(removePart);
        assocPartTableM.setItems(assocParts);
    }

    /**
     * Populates values from fields, validates the inputs and then updates the product in Inventory.
     * @param actionEvent
     */
    public void saveButtonClicked(ActionEvent actionEvent) {
        int id = Integer.parseInt(idModifyProductTxt.getText());
        String name=null;
        int stock=0, max=0, min=0;
        double price=0;
        try {
            name = nameModifyProductTxt.getText();
            if(name.isEmpty())
                throw new Exception();
            stock = Integer.parseInt(invModifyProductTxt.getText());
            price = Double.parseDouble(priceModifyProductTxt.getText());
            max = Integer.parseInt(maxModifyProductTxt.getText());
            min = Integer.parseInt(minModifyProductTxt.getText());

            // input validation
            if(stock<min || max<min || stock>max)
                throw new Exception();

            Product newProduct = new Product(id, name, price, stock, min, max);
            newProduct.setAssociatedParts(assocParts);
            Inventory.updateProduct(id, newProduct);

            // Take user back to MainScreen view.
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
