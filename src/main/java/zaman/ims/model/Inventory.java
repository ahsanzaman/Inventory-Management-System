package zaman.ims.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /**
     * Default Constructor
      */
    public Inventory(){}

    /**
     * Add new part to Inventory.
     * @param newPart part object to be added to parts list.
     */
    public static void addNewPart(Part newPart){
        allParts.add(newPart);
    }

    /** Add new Product to inventory
     *
     * @param newProduct product object to be added to products list.
     */
    public static void addNewProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * Search parts by ID.
     * @param partID part id to search with.
     * @return Part that matched with part id.
     */
    public static Part lookupPart(int partID) {
        for (Part part : allParts) {
            if (part.getId() == partID) {
                return part;
            }
        }
        return null;
    }

    /**
     * Case sensitive currently
     * @param partName requires a string or partial string to search through names
     * @return returns list of Parts that were a match for the string provided.
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> result = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if(part.getName().contains(partName)){
                result.add(part);
            }
        }
        return result;
    }

    /**
     * Searches product by using productID.
     * @param productID id to search with.
     * @return Product that matched.
     */
    public static Product lookupProduct(int productID) {
        for (Product product : allProducts) {
            if (product.getId() == productID) {
                return product;
            }
        }
        return null;
    }

    /**
     * Searches product by productName.
     * @param productName product name string to search with.
     * @return list of match in Product list.
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> result = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if(product.getName().contains(productName)){
                result.add(product);
            }
        }
        return result;
    }

    /**
     * Updates part by looking up index of the part with id.
     * @param id id of the part to be updated.
     * @param selectedPart the updated part.
     */
    public static void updatePart(int id, Part selectedPart){
        int index = -1;

        for(int i=0;i<allParts.size();i++) {
            System.out.println("ID: "+allParts.get(i).getId()+", index: "+i);
            if(allParts.get(i).getId() == id) {
                index = i;
            }
        }

        allParts.set(index, selectedPart);
    }

    /**
     * Updates product by searching the index of the product containing the id.
     * @param id id of product to be updated.
     * @param selectedProduct the updated product.
     */
    public static void updateProduct(int id, Product selectedProduct){
        int index = -1;

        for(int i=0;i<allProducts.size();i++) {
            System.out.println("ID: "+allProducts.get(i).getId()+", index: "+i);
            if(allProducts.get(i).getId() == id) {
                index = i;
            }
        }

        allProducts.set(index, selectedProduct);
    }

    /**
     * Delete part from inventory.
     * @param partSelected part to be deleted.
     * @return true or false if part was deleted.
     */
    public static boolean deletePart(Part partSelected){
        if(allParts.contains(partSelected)) {
            allParts.remove(partSelected);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Delete product from inventory
     * @param productSelected product to be deleted.
     * @return true or false if product was deleted.
     */
    public static boolean deleteProduct(Product productSelected){
        if(allProducts.contains(productSelected)) {
            allProducts.remove(productSelected);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Getter for all parts.
     * @return all parts.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Getter for all products.
     * @return all products.
     */
    public static ObservableList<Product>  getAllProducts() {
        return allProducts;
    }

}