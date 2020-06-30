package filesUtils;

import exceptions.FileSizeToLargeException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLReader {

    public static NodeList getListFromXML(String pathToXML, String elementName) throws ParserConfigurationException, SAXException, IOException {
        return getDocument(pathToXML).getElementsByTagName(elementName);
    }

    private static Document getDocument(String pathToXML) throws IOException, SAXException, ParserConfigurationException {
        File file = new File(pathToXML);
        if(FileUtils.getSize(file)>1)
            try {
                throw new FileSizeToLargeException("file size too large");
            } catch (FileSizeToLargeException e) {
                e.printStackTrace();
            }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(file);
        document.getDocumentElement().normalize();
        return document;
    }
}
