package databaseUtils;

import constans.*;
import enums.ContactType;
import filesUtils.CSVReader;
import filesUtils.ContactValidator;
import filesUtils.XMLReader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class InsertionMySQL {

    private List<List<String>> recordsToWriteFromCSV;
    private ConnectionMySQL connectionMySQL;
    private NodeList contactsList;
    private NodeList customerList;

    public InsertionMySQL(String pathToXML, String pathToCSV) throws SQLException, IOException, SAXException, ParserConfigurationException {
        this.recordsToWriteFromCSV = CSVReader.getRecords(pathToCSV);
        this.connectionMySQL = new ConnectionMySQL();
        this.contactsList = XMLReader.getListFromXML(pathToXML, TableName.CONTACTS_TABLE);
        this.customerList = XMLReader.getListFromXML(pathToXML, TableName.CUSTOMERS_TABLE);
    }

    public void insertRecordsFromCSV() throws SQLException {
        Connection conn = connectionMySQL.getConnection();
        String[] generatedColumns = {"ID"};
        PreparedStatement statementInsertToContacts = conn.prepareStatement(QueriesSQL.INSERT_TO_CONTACTS);
        PreparedStatement statementInsertToCustomers = conn.prepareStatement(QueriesSQL.INSERT_TO_CUSTOMERS, generatedColumns);

        saveToDataBaseFromCSV(statementInsertToContacts, statementInsertToCustomers);
    }

    public void insertRecordsFromXML() throws SQLException, ParserConfigurationException, SAXException, IOException {
        Connection conn = connectionMySQL.getConnection();
        String generatedColumns[] = {"ID"};
        PreparedStatement statementInsertToContacts = conn.prepareStatement(QueriesSQL.INSERT_TO_CONTACTS);
        PreparedStatement statementInsertToCustomers = conn.prepareStatement(QueriesSQL.INSERT_TO_CUSTOMERS, generatedColumns);

        saveToDataBaseFromXML(statementInsertToContacts, statementInsertToCustomers);
    }

    private void saveToDataBaseFromXML(PreparedStatement statementInsertToContacts, PreparedStatement statementInsertToCustomers) throws SQLException, SAXException, ParserConfigurationException {
        List<Long> customersIds = new LinkedList<>();
        long globalCustomerId = 0;

        for (int temp = 0; temp < customerList.getLength(); temp++) {
            Node nNode = customerList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                try {
                    statementInsertToCustomers.setString(ColumnIndexes.INDEX_CUSTOMER_NAME, eElement.getElementsByTagName(TagNames.NAME_TAG).item(ColumnIndexes.INDEX_FIRST_CHILD_OF_NODE).getTextContent());
                    statementInsertToCustomers.setString(ColumnIndexes.INDEX_CUSTOMER_SURNAME, eElement.getElementsByTagName(TagNames.SURNAME_TAG).item(ColumnIndexes.INDEX_FIRST_CHILD_OF_NODE).getTextContent());
                    statementInsertToCustomers.setString(ColumnIndexes.INDEX_CUSTOMER_AGE, eElement.getElementsByTagName(TagNames.AGE_TAG).item(ColumnIndexes.INDEX_FIRST_CHILD_OF_NODE).getTextContent());
                    statementInsertToCustomers.setString(ColumnIndexes.INDEX_CUSTOMER_CITY, eElement.getElementsByTagName(TagNames.CITY_TAG).item(ColumnIndexes.INDEX_FIRST_CHILD_OF_NODE).getTextContent());
                } catch (NullPointerException ex) {
                    statementInsertToCustomers.setString(ColumnIndexes.INDEX_CUSTOMER_AGE, null);
                }

                int rowsInsertedCustomers = statementInsertToCustomers.executeUpdate();

                if (rowsInsertedCustomers > 0) {
                    System.out.println("A new [CUSTOMER] was inserted successfully!");
                }

                ResultSet rs = statementInsertToCustomers.getGeneratedKeys();
                if (rs.next()) {
                    long id = rs.getLong(1);
                    globalCustomerId = id;
                    customersIds.add(id);
                } else
                    customersIds.add(++globalCustomerId);
            }
        }

        for (int temp = 0; temp < contactsList.getLength(); temp++) {
            boolean UNKNOWN_FLAG = false;
            Node nNode = contactsList.item(temp);

            for (int j = 0; j < nNode.getChildNodes().getLength(); j++) {

                if (nNode.getChildNodes().item(j).getNodeName().equals("#text"))
                    continue;

                if ((!nNode.getChildNodes().item(j).getNodeName().equals(TagNames.PHONE_TAG) && !nNode.getChildNodes().item(j).getNodeName().equals(TagNames.EMAIL_TAG) &&
                        !nNode.getChildNodes().item(j).getNodeName().equals(TagNames.JABBER_TAG))) {
                    UNKNOWN_FLAG = true;
                }
            }
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

                if (eElement.getElementsByTagName(TagNames.PHONE_TAG).getLength() > 0) {
                    int sizeOfElements = eElement.getElementsByTagName(TagNames.PHONE_TAG).getLength();
                    for (int i = 0; i < sizeOfElements; i++) {
                        statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_TYPE, ContactValidator.getType(eElement.getElementsByTagName(TagNames.PHONE_TAG).item(i).getTextContent()));
                        statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_ID_CUSTOMER, customersIds.get(temp).toString());
                        statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_CONTACT, eElement.getElementsByTagName(TagNames.PHONE_TAG).item(i).getTextContent());
                        int rowsInsertedContacts = statementInsertToContacts.executeUpdate();

                        if (rowsInsertedContacts > 0) {
                            System.out.println("A new [CONTACT] was inserted successfully!");
                        }
                    }
                }
                if (eElement.getElementsByTagName(TagNames.EMAIL_TAG).getLength() > 0) {
                    int sizeOfElements = eElement.getElementsByTagName(TagNames.EMAIL_TAG).getLength();
                    for (int i = 0; i < sizeOfElements; i++) {
                        statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_TYPE, ContactValidator.getType(eElement.getElementsByTagName(TagNames.EMAIL_TAG).item(i).getTextContent()));
                        statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_ID_CUSTOMER, customersIds.get(temp).toString());
                        statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_CONTACT, eElement.getElementsByTagName(TagNames.EMAIL_TAG).item(i).getTextContent());
                        int rowsInsertedContacts = statementInsertToContacts.executeUpdate();

                        if (rowsInsertedContacts > 0) {
                            System.out.println("A new [CONTACT] was inserted successfully!");
                        }
                    }
                }
                if (eElement.getElementsByTagName(TagNames.JABBER_TAG).getLength() > 0) {
                    int sizeOfElements = eElement.getElementsByTagName(TagNames.JABBER_TAG).getLength();
                    for (int i = 0; i < sizeOfElements; i++) {
                        statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_TYPE, ContactValidator.getType(eElement.getElementsByTagName(TagNames.JABBER_TAG).item(i).getTextContent()));
                        statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_ID_CUSTOMER, customersIds.get(temp).toString());
                        statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_CONTACT, eElement.getElementsByTagName(TagNames.JABBER_TAG).item(i).getTextContent());
                        int rowsInsertedContacts = statementInsertToContacts.executeUpdate();

                        if (rowsInsertedContacts > 0) {
                            System.out.println("A new [CONTACT] was inserted successfully!");
                        }
                    }
                }

                if (UNKNOWN_FLAG) {
                    statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_TYPE, ContactType.UNKNOWN.toString());
                    statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_ID_CUSTOMER, customersIds.get(temp).toString());
                    statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_CONTACT, TagNames.UNKNOWN_TAG);
                    int rowsInsertedContacts = statementInsertToContacts.executeUpdate();

                    if (rowsInsertedContacts > 0) {
                        System.out.println("A new [CONTACT] was inserted successfully!");
                    }
                }
            }
        }
        statementInsertToContacts.close();
        statementInsertToCustomers.close();

    }

    private void saveToDataBaseFromCSV(PreparedStatement statementInsertToContacts, PreparedStatement statementInsertToCustomers) throws SQLException {
        long globalId = 0;
        for (List<String> list : recordsToWriteFromCSV) {

            try {
                statementInsertToCustomers.setString(ColumnIndexes.INDEX_CUSTOMER_NAME, list.get(ListIndexes.INDEX_OF_NAME));
                statementInsertToCustomers.setString(ColumnIndexes.INDEX_CUSTOMER_SURNAME, list.get(ListIndexes.INDEX_OF_SURNAME));
                Integer.valueOf(list.get(ListIndexes.INDEX_OF_AGE));
                statementInsertToCustomers.setString(ColumnIndexes.INDEX_CUSTOMER_AGE, list.get(ListIndexes.INDEX_OF_AGE));
                statementInsertToCustomers.setString(ColumnIndexes.INDEX_CUSTOMER_CITY, list.get(ListIndexes.INDEX_OF_CITY));
            } catch (NumberFormatException e) {
                statementInsertToCustomers.setString(ColumnIndexes.INDEX_CUSTOMER_AGE, null);
            }
            int rowsInsertedCustomers = statementInsertToCustomers.executeUpdate();

            if (rowsInsertedCustomers > 0) {
                System.out.println("A new [CUSTOMER] was inserted successfully!");
            }
            ResultSet rs = statementInsertToCustomers.getGeneratedKeys();

            ListIterator<String> iterator = list.listIterator(ListIndexes.FIRST_INDEX_OF_CONTACT_LIST);
            while (iterator.hasNext()) {
                String value = iterator.next();
                if (rs.next()) {
                    long id = rs.getLong(1);
                    globalId = id;
                    statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_ID_CUSTOMER, String.valueOf(id));
                    statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_TYPE, ContactValidator.getType(value));
                    statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_CONTACT, value);
                } else {
                    statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_ID_CUSTOMER, String.valueOf(globalId));
                    statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_TYPE, ContactValidator.getType(value));
                    statementInsertToContacts.setString(ColumnIndexes.INDEX_CONTACTS_CONTACT, value);
                }
                int rowsInsertedContacts = statementInsertToContacts.executeUpdate();
                if (rowsInsertedContacts > 0) {
                    System.out.println("A new [CONTACT] was inserted successfully!");
                }
            }

        }
        statementInsertToContacts.close();
        statementInsertToCustomers.close();
    }
}

