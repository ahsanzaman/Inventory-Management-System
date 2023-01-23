package zaman.ims.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import zaman.ims.model.Inventory;
import zaman.ims.model.Part;
import zaman.ims.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {


    public TableView<Part> partsTableMain;
    public TableColumn mainPartIDCol;
    public TableColumn mainPartNameCol;
    public TableColumn mainPartInventLvlCol;
    public TableColumn mainPartPriceCol;
    
    public TableView<Product> productsTableMain;
    public TableColumn mainproductIDCol;
    public TableColumn mainProductNameCol;
    public TableColumn mainProductInventLevelCol;
    public TableColumn mainProductPriceCol;
    public Button deletePartMainBtn;
    public Button deleteProductMainBtn;
    public Button addProductMainBtn;
    public Button addPartMainBtn;
    public Button modifyPartMainBtn;
    public Button modifyProductMainBtn;
    public Button exitMainBtn;
    public TextField searchPartsMainTxt;
    public TextField searchProductsMainTxt;
    public static Part selectedPart;
    public static Product selectedProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Main Screen Initialized.");
        selectedPart = null;
        selectedProduct = null;

        partsTableMain.setItems(Inventory.getAllParts());
        mainPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainPartInventLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTableMain.setItems(Inventory.getAllProducts());
        mainproductIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainProductInventLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This method will search for part by name first, then by id if it doesnt find anything by name.
     * If the search is empty it will display an error and then display all parts in table.
     * @param actionEvent
     */
    @FXML
    protected void getPartSearchResult(ActionEvent actionEvent) {
        System.out.println("Part Search entered.");
        String query = searchPartsMainTxt.getText();
        ObservableList<Part> searchResult = Inventory.lookupPart(query);
        boolean resultReturned = true;

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

            partsTableMain.setItems(Inventory.getAllParts());
        }
        else {
            partsTableMain.setItems(searchResult);
        }
    }

    /**
     * This method will search product by name and then by id if name doesnt yield any results.
     * If nothing is found, it will display an error alert and then display all products table.
     * @param actionEvent
     */
    @FXML
    public void getProductSearchResult(ActionEvent actionEvent) {
        System.out.println("Product Search entered.");
        String query = searchProductsMainTxt.getText();
        ObservableList<Product> searchResult = Inventory.lookupProduct(query);
        boolean resultReturned = true;

        // searching with id instead
        if(searchResult.size()==0) {
            try {
                Product productReturned = Inventory.lookupProduct(Integer.parseInt(query));
                searchResult.add(productReturned);

                if(productReturned==null)
                    resultReturned=false;
            } catch(Exception e) {
                System.out.println("Unable to parse int for product search by id.");
            }
        }

        // when no results were returned, display alert
        if(!resultReturned) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No results found.");
            alert.setContentText("Your search did not return any results.");
            alert.showAndWait();

            productsTableMain.setItems(Inventory.getAllProducts());
        }
        else {
            productsTableMain.setItems(searchResult);
        }
    }

    /**
     * When delete part button is clicked, this method will display an alert to confirm the action.
     * Once confirmed, it will delete the part from Inventory.
     * @param actionEvent
     */
    @FXML
    protected void partDeleteButtonClicked(ActionEvent actionEvent) {
        System.out.println("Part delete button clicked.");
        Part deletePart = partsTableMain.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm action");
        alert.setContentText("You are about to delete a part. Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        // proceed to delete once user confirms to delete
        if (result.get() == ButtonType.OK){
            // in the event deletePart was not able to delete
            if(!Inventory.deletePart(deletePart)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText("Part could not be deleted successfully.");
                alert.showAndWait();
            }
        }
    }

    /**
     * This method will be run when delete product button is clicked.
     * It will cconfirm with user then, it will check if the product has any associated parts.
     * @param actionEvent
     */
    @FXML
    protected void productDeleteButtonClicked(ActionEvent actionEvent) {
        System.out.println("Product delete button clicked.");
        Product deleteProduct = productsTableMain.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm action");
        alert.setContentText("You are about to delete a product. Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        // proceed to delete product once user confirms
        if (result.get() == ButtonType.OK){
            // if there are parts associated display an error, do not delete product
            if(deleteProduct.getAllAssociatedParts().size()>0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText("Product has associated parts. Please delete them and then delete product.");
                alert.showAndWait();
            }
            else {
                // in the event that deleteProduct was not able to delete
                if(!Inventory.deleteProduct(deleteProduct)) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setContentText("Product could not be deleted successfully.");
                    alert.showAndWait();
                }
            }
        }
    }

    /**
     * Program will be exited.
     */
    @FXML
    protected void exitButtonClicked() {
        System.out.println("Exit button clicked.");
        System.exit(0);
    }

    /**
     * This method will display the AddPart view to allow user to add a part.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    protected void addPartButtonClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("Add part button clicked.");

        Parent parent = FXMLLoader.load(getClass().getResource("/zaman/ims/view/AddPart.fxml"));
        Stage stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());

        Scene scene = new Scene(parent, 600, 550);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method will get a selected part and allow the user to modify the select part in the ModifyPart view.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    protected void modifyPartButtonClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("Modify part button clicked.");
        selectedPart = partsTableMain.getSelectionModel().getSelectedItem();

        Parent parent = FXMLLoader.load(getClass().getResource("/zaman/ims/view/ModifyPart.fxml"));
        Stage stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());

        Scene scene = new Scene(parent, 600, 550);
        stage.setTitle("Modify Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method will load up and display the AddProduct view for the user.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    protected void addProductButtonClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("Add product button clicked.");

        Parent parent = FXMLLoader.load(getClass().getResource("/zaman/ims/view/AddProduct.fxml"));
        Stage stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());

        Scene scene = new Scene(parent, 800, 550);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method will save the selectedProduct and load up/display the ModifyProduct view for user.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    protected void modifyProductButtonClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("Modify product button clicked.");
        selectedProduct = productsTableMain.getSelectionModel().getSelectedItem();

        Parent parent = FXMLLoader.load(getClass().getResource("/zaman/ims/view/ModifyProduct.fxml"));
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());

        Scene scene = new Scene(parent, 800, 550);
        stage.setTitle("Modify Product");
        stage.setScene(scene);
        stage.show();
    }
}