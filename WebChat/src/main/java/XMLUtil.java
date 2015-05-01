import java.io.File;
import java.io.IOException;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUtil{
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm ");
    private static final String STORAGE_LOCATION = System.getProperty("user.home") +  File.separator + "history.xml";
    private static final String MESSAGES = "messages";
    private static final String MESSAGE = "message";
    private static final String AUTHOR = "author";
    private static final String TEXT = "text";
    private static final String DATE = "date";

    public static ArrayList<ArrayList<String> > readData()
            throws ParserConfigurationException, SAXException, IOException, TransformerException {
        return read(getDoc());
    }

    private static Document getDoc()
            throws ParserConfigurationException, SAXException, IOException, TransformerException {
        if(!doesFileExsist()) {
            createFile();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(STORAGE_LOCATION));
        return document;
    }

    public static void addMessage(Date date, String author, String text)
            throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document document = getDoc();
        Element rootEl = document.getDocumentElement();
        Element messageEl = document.createElement(MESSAGE);
        Element authorEl = document.createElement(AUTHOR);
        authorEl.appendChild(document.createTextNode(author));
        messageEl.appendChild(authorEl);
        Element textEl = document.createElement(TEXT);
        textEl.appendChild(document.createTextNode(text));
        messageEl.appendChild(textEl);
        Element dateEl = document.createElement(DATE);
        dateEl.appendChild(document.createTextNode(dateFormat.format(date)));
        messageEl.appendChild(dateEl);
        rootEl.appendChild(messageEl);
        saveFile(document);
    }

    private static boolean doesFileExsist() {
        File file = new File(STORAGE_LOCATION);
        return file.exists();
    }

    private static ArrayList<ArrayList<String> > read(Document document)
            throws ParserConfigurationException, SAXException, IOException {
        NodeList nodeList = document.getElementsByTagName("message");

        ArrayList<ArrayList<String> > messages = new ArrayList<ArrayList<String> >();

        for(int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i).getChildNodes();
            ArrayList<String> message = new ArrayList<String>(5);
            message.add(element.getElementsByTagName("date").item(0).getTextContent());
            message.add(element.getElementsByTagName("author").item(0).getTextContent());
            message.add(element.getElementsByTagName("text").item(0).getTextContent());
            messages.add(message);
        }
        return messages;
    }

    private static void createFile()
            throws ParserConfigurationException, TransformerException, SAXException, IOException {
        Document doc = getDoc();

        Element rootElement = doc.createElement(MESSAGES);
        doc.appendChild(rootElement);

        saveFile(doc);
    }

    private static void saveFile(Document doc)
            throws ParserConfigurationException, TransformerException {
        Transformer transformer = getTransformer();

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(STORAGE_LOCATION));
        transformer.transform(source, result);
    }

    private static Transformer getTransformer()
            throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        return transformer;
    }

}