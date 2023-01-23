package zaman.ims.model;

public class Outsourced extends Part{
    String companyName;

    /**
     * Overloaded constructor for populating outsourced part values.
     * @param id the part id.
     * @param name name of the part.
     * @param price price of the part.
     * @param stock existing stock.
     * @param min minimum parts.
     * @param max maximum parts.
     * @param companyName string for the company name.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Sets company name.
     * @param companyName String object for setting the name.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Gets company name.
     * @return companyName string.
     */
    public String getCompanyName() {
        return companyName;
    }
}
