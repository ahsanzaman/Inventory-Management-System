package zaman.ims.model;

public class InHouse extends Part {

    private int machineID;

    /**
     * Overloaded constructor to initialize Inhouse part.
     * @param partID the part id.
     * @param name name of the part.
     * @param price price of the part.
     * @param numInStock existing stock.
     * @param min minimum parts.
     * @param max maximum parts.
     * @param machineID machine id associated with the part.
     */
    public InHouse(int partID, String name, double price, int numInStock, int min, int max, int machineID) {
        super(partID, name, price, numInStock, min, max);
        this.machineID = machineID;
    }

    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

}
