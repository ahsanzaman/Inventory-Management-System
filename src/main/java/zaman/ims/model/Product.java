package zaman.ims.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    public ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    int id;
    String name;
    double price;
    int stock;
    int min;
    int max;


    /**
     * Overloaded constructor for initializing Product with values.
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    /**
     * Add part to associatedParts list.
     * @param part part to be added to associated parts list.
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Sets associatedParts list to the given list.
     * @param assocParts associated parts list to be set.
     */
    public void setAssociatedParts(ObservableList<Part> assocParts) {
        associatedParts = assocParts;
    }

    /**
     * Delete associated part from product.
     * @param part part to be deleted.
     * @return true or false based on if the part was deleted or not.
     */
    public boolean deleteAssociatedPart(Part part){
        if(associatedParts.contains(part)){
            associatedParts.remove(part);
            return true;
        }
        return false;
    }

    /**
     * Getter for all associated parts.
     * @return all associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }


}