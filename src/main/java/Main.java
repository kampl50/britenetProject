import application.App;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws SQLException, IOException, SAXException, ParserConfigurationException {

        System.out.println("WITAJ W APLIKACJI DODAJĄCEJ DANE Z PLIKOW XML I CSV DO BAZY MySQL :)");
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj sciezke do pliku xml.");
        String xmlPath = scan.nextLine();
        System.out.println("Podaj sciezke do pliku csv.");
        String csvPath = scan.nextLine();
        App app = new App(xmlPath, csvPath);
        int option;
        do {
            System.out.println("#####-MENU-#####");
            System.out.println("0.Wyjscie");
            System.out.println("1.Wczytaj z pliku CSV i zapisz do bazy.");
            System.out.println("2.Wczytaj z pliku XML i zapisz do bazy.");
            System.out.println("3.Pokaz wszystkie kontakty.");
            System.out.println("4.Pokaz wszystkich klientow.");

            option = scan.nextInt();

            switch (option) {
                case 1:
                    app.addRecordsFromCSV();
                    System.out.println("DANE ODCZYTANO I ZAPISANO Z PLIKU CSV.");
                    break;

                case 2:
                    app.addRecordsFromXML();
                    System.out.println("DANE ODCZYTANO I ZAPISANO Z PLIKU XML.");
                    break;
                case 3:
                    app.showAllContactRecords();
                    System.out.println("ODCZYTANO WSZYSTKIE REKORDY W CONTACTS.");
                    break;
                case 4:
                    app.showAllCustomerRecords();
                    System.out.println("ODCZYTANO WSZYSTKIE REKORDY W CUSTOMERS.");
                    break;
                default:
                    System.out.println("WYSZEDLES Z APLIKACJI");
                    break;
            }
        } while (option != 0);

    }
}



