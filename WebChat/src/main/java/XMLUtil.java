import java.io.File;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;
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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class XMLUtil{
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm ");
    private static final String STORAGE_LOCATION = System.getProperty("user.home") +  File.separator + "history.xml";
    private static final String MESSAGES = "messages";
    private static final String MESSAGE = "message";
    private static final String AUTHOR = "author";
    private static final String TEXT = "text";
    private static final String DATE = "date";
    private static final String ID = "id";
    private static final String STATE = "state";

    public static ArrayList<State> readData()
            throws ParserConfigurationException, SAXException, IOException, TransformerException, java.text.ParseException {
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

    private static void addMessage(Document document, Message message, String state)
            throws ParserConfigurationException, SAXException, IOException, TransformerException {
        System.out.println("in add mess");
        Element rootEl = document.getDocumentElement();
        Element messageEl = document.createElement(MESSAGE);
        Element authorEl = document.createElement(AUTHOR);
        authorEl.appendChild(document.createTextNode(message.getAuthor()));
        messageEl.appendChild(authorEl);
        Element textEl = document.createElement(TEXT);
        textEl.appendChild(document.createTextNode(message.getText()));
        messageEl.appendChild(textEl);
        Element dateEl = document.createElement(DATE);
        dateEl.appendChild(document.createTextNode(dateFormat.format(message.getDate())));
        messageEl.appendChild(dateEl);
        Element IdEl = document.createElement(ID);
        IdEl.appendChild(document.createTextNode(message.getId()));
        messageEl.appendChild(IdEl);
        Element stateEl = document.createElement(STATE);
        stateEl.appendChild(document.createTextNode(state));
        messageEl.appendChild(stateEl);
        rootEl.appendChild(messageEl);
        System.out.println("mess add");
    }

    public static void save(List<State> list)
            throws ParserConfigurationException, SAXException, IOException, TransformerException, ParseException , java.text.ParseException{
        Document document = getDoc();
        for(int i = 0; i < list.size(); i++){
            Message message = new Message();
            System.out.println("in save");
            message.parseJSONValue(list.get(i).getJSONValue());
            addMessage(document, message, list.get(i).getState());
        }
        saveFile(document);
    }

    private static boolean doesFileExsist() {
        File file = new File(STORAGE_LOCATION);
        return file.exists();
    }

    private static ArrayList<State> read(Document document)
            throws ParserConfigurationException, SAXException, IOException, java.text.ParseException{
        NodeList nodeList = document.getElementsByTagName("message");

        System.out.println("in read");
        ArrayList<State> states = new ArrayList<State>();

        for(int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i).getChildNodes();
            String date = element.getElementsByTagName(DATE).item(0).getTextContent();
            System.out.println("ok1");
            String author = element.getElementsByTagName(AUTHOR).item(0).getTextContent();
            String text = element.getElementsByTagName(TEXT).item(0).getTextContent();
            String id =  element.getElementsByTagName(ID).item(0).getTextContent();
            String state = element.getElementsByTagName(STATE).item(0).getTextContent();
            Message message = new Message(dateFormat.parse(date), author, text, id);
            states.add(new State(state, message.getJSONValue()));
        }
        System.out.println("readed");
        return states;
    }

    private static void createFile()
            throws ParserConfigurationException, TransformerException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
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