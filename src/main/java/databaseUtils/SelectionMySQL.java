package databaseUtils;

import constans.ColumnName;
import constans.QueriesSQL;
import constans.TableName;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectionMySQL {

    private ConnectionMySQL connectionMySQL;

    public SelectionMySQL() throws SQLException {
        this.connectionMySQL = new ConnectionMySQL();
    }

    public void selectAllFromContacts() throws SQLException {
        Statement statement = connectionMySQL.getConnection().createStatement();
        ResultSet result = statement.executeQuery(QueriesSQL.SELECT_ALL_CONTACTS);
        int count = 0;

        while (result.next()){
            String idContact = result.getString(ColumnName.ID_CONTACT);
            String idCustomer = result.getString(ColumnName.ID_CUSTOMER);
            String type = result.getString(ColumnName.TYPE);
            String contact = result.getString(ColumnName.CONTACT);

            String output = "Contact #%s: %s - %s - %s - %s";
            System.out.println(String.format(output, ++count, idContact, idCustomer, type, contact));
        }
    }

   public void selectAllFromCustomers() throws SQLException {
        Statement statement = connectionMySQL.getConnection().createStatement();
        ResultSet result = statement.executeQuery(QueriesSQL.SELECT_ALL_CUSTOMERS);
        int count = 0;

        while (result.next()){
            String idCustomer = result.getString(ColumnName.ID_CUSTOMER);
            String name = result.getString(ColumnName.NAME);
            String surname = result.getString(ColumnName.SURNAME);
            String age = result.getString(ColumnName.AGE);
            String city = result.getString(ColumnName.CITY);

            String output = "Contact #%s: %s - %s - %s - %s - %s";
            System.out.println(String.format(output, ++count, idCustomer, name, surname, age,city));
        }
    }

}
