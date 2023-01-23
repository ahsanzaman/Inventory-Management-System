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

public class AddProductController implements Initializable {

    public TableView<Part> allPartsTable;
    public TableColumn allPartIdCol;
    public TableColumn allPartNameCol;
    public TableColumn allPartStockCol;
    public TableColumn allPartPriceCol;
    public TableView<Part> assocPartsTable;
    public TableColumn assocPartIdCol;
    public TableColumn assocPartNameCol;
    public TableColumn assocStockCol;
    public TableColumn assocPriceCol;
    public TextField searchAddProductTxt;
    public ObservableList<Part> assocParts;
    public TextField idAddProductTxt;
    public TextField nameAddProductTxt;
    public TextField invAddProductTxt;
    public TextField priceAddProductTxt;
    public TextField maxAddProductTxt;
    public TextField minAddProductTxt;

    /**
     * Exits out of AddProduct view and takes user to mainscreen.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    protected void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("Add Product: Cancel button clicked.");

        Parent parent = FXMLLoader.load(getClass().getResource("/zaman/ims/view/MainForm.fxml"));
        Stage stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());

        Scene scene = new Scene(parent, 900, 360);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add product initialized.");

        allPartsTable.setItems(Inventory.getAllParts());
        allPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocParts = FXCollections.observableArrayList();

        assocPartsTable.setItems(assocParts);
        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Searches part by name, if nothing gets returned, it searches by id instead.
     * If result is still empty, it displays an error alert and sets to display all parts.
     * @param actionEvent
     */
    public void getPartSearchResult(ActionEvent actionEvent) {
        System.out.println("Part Search entered in Add Product Screen.");
        String query = searchAddProductTxt.getText();
        ObservableList<Part> searchResult = Inventory.lookupPart(query);
        boolean resultReturned=true;

        // search by id if nothing was returned
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

        // if search result is still empty, display an error alert and set results to parts.
        if(!resultReturned) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No results found.");
            alert.setContentText("Your search did not return any results.");
            alert.showAndWait();

            allPartsTable.setItems(Inventory.getAllParts());
        }
        else {
            allPartsTable.setItems(searchResult);
        }
    }

    /**
     * Checks if a part has been selected, checks that part isnt already in associated parts.
     * Once these checks are passed, this method adds the selected part to the associatedParts table.
     * @param actionEvent
     */
    public void addAssociatedPartButtonClicked(ActionEvent actionEvent) {
        Part part = allPartsTable.getSelectionModel().getSelectedItem();
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
            assocParts.add(part);
        }
    }

    /**
     * Removes the selected part from associated parts list.
     * @param actionEvent
     */
    public void removeAssociatedPartButtonClicked(ActionEvent actionEvent) {
        assocParts.remove(allPartsTable.getSelectionModel().getSelectedItem());
    }

    /**
     * This method generates the id for new product, gets values from all fields,
     * and adds the new product to the Inventory along with its associated parts.
     * @param actionEvent
     */
    public void saveButtonClicked(ActionEvent actionEvent) {
        // getting max id and continuing from there
        // if size is 0, keep id at 1000
        int id = 1000;
        if(Inventory.getAllProducts().size()!=0) {
            id=Inventory.getAllProducts().get(Inventory.getAllProducts().size()-1).getId()+1;
        }

        String name=null;
        int stock=0, max=0, min=0;
        double price=0;
        try {
            name = nameAddProductTxt.getText();
            if(name.isEmpty())
                throw new Exception();
            stock = Integer.parseInt(invAddProductTxt.getText());
            price = Double.parseDouble(priceAddProductTxt.getText());
            max = Integer.parseInt(maxAddProductTxt.getText());
            min = Integer.parseInt(minAddProductTxt.getText());

            // input validation
            if(stock<min || min>max || stock>max)
                throw new Exception();

            Product newProduct = new Product(id, name, price, stock, min, max);
            newProduct.setAssociatedParts(assocParts);
            Inventory.addNewProduct(newProduct);

            // taking user back to main screen
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
