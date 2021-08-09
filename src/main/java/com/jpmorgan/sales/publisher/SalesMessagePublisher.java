package com.jpmorgan.sales.publisher;

import com.jpmorgan.sales.model.Operation;
import com.jpmorgan.sales.receiver.SalesMessageReceiver;
import com.jpmorgan.sales.report.SalesMessageConsoleReport;
import com.jpmorgan.sales.report.SalesMessageReport;
import com.jpmorgan.sales.store.SalesMessageMemoryStore;
import com.jpmorgan.sales.store.SalesMessageStore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

/**
 * SalesMessagePublisher acts as a third party sender sending sales into the system.
 * This class reads messages from a file 'message.xml' and publishes the message for the receiver to consume.
 */
public class SalesMessagePublisher {

    private final SalesMessageStore storage = new SalesMessageMemoryStore();
    private final SalesMessageReport reporter = new SalesMessageConsoleReport(storage);
    private final SalesMessageReceiver receiver = new SalesMessageReceiver(storage, reporter, 10, 50);

    private static final String FILENAME = "message.xml";

    public void start() {

        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(getClass().getClassLoader().getResource(FILENAME).getFile()));
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("message");
            IntStream.range(0,list.getLength()).forEach(count -> {
                if (!receiver.hasReachedMaximumNumberOfMessages()) {
                    Node node = list.item(count);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String type = element.getAttribute("productType");
                        BigDecimal price = new BigDecimal(element.getAttribute("price"));
                        Optional<Node> quantityNode = Optional.ofNullable(element.getElementsByTagName("qty").item(0));
                        Optional<Node> adjustmentNode = Optional.ofNullable(element.getElementsByTagName("command").item(0));
                        quantityNode.ifPresent(quantity -> receiver.receiveProduct(type, price, Integer.valueOf(quantity.getTextContent())));
                        adjustmentNode.ifPresent(adjustment -> receiver.receiveAdjustment(type, price, Operation.valueOf(adjustment.getTextContent())));
                        if (quantityNode.isEmpty() && adjustmentNode.isEmpty()) {
                            receiver.receiveProduct(type, price);
                        }
                    }
                } else {
                    System.out.println("No more messages allowed");
                    System.exit(1);
                }
            }
            );

        } catch (ParserConfigurationException | SAXException | IOException e) {
        }

    }


}
