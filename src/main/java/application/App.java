package application;

import databaseUtils.InsertionMySQL;
import databaseUtils.SelectionMySQL;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

public class App {

    private InsertionMySQL insertionMySQL;
    private SelectionMySQL selectionMySQL;
    public App(String xml, String csv) throws SQLException, ParserConfigurationException, SAXException, IOException {
        this.insertionMySQL = new InsertionMySQL(xml, csv);
        this.selectionMySQL = new SelectionMySQL();
    }

    public void addRecordsFromXML() throws SQLException, ParserConfigurationException, SAXException, IOException {
        insertionMySQL.insertRecordsFromXML();
    }

    public void addRecordsFromCSV() throws SQLException{
        insertionMySQL.insertRecordsFromCSV();
    }

    public void showAllContactRecords() throws SQLException {
        selectionMySQL.selectAllFromContacts();
    }

    public void showAllCustomerRecords() throws SQLException {
        selectionMySQL.selectAllFromCustomers();
    }


}
