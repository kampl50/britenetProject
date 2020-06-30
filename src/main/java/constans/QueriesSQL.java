package constans;

public class QueriesSQL {

    public static final String INSERT_TO_CONTACTS = "INSERT INTO contacts (idCustomer, type, contact) VALUES (?, ?, ?)";
    public static final String INSERT_TO_CUSTOMERS = "INSERT INTO customers (name, surname, age, city) VALUES (?, ?, ?, ?)";
    public static final String SELECT_ALL_CONTACTS = "SELECT * FROM contacts";
    public static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM customers";
}
