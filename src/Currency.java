import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Currency{

    public static InputStream sendRequest(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return connection.getInputStream();
        } else {
            throw new IOException("Error: " + responseCode);
        }
    }


    public static double findRateByCurrencyCode(InputStream responseStream, String currencyCode) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(responseStream);
        NodeList nodeList = document.getElementsByTagName("Valute");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String charCode = element.getElementsByTagName("CharCode").item(0).getTextContent();
            if (charCode.equals(currencyCode)) {
                String rate = element.getElementsByTagName("Value").item(0).getTextContent();
                return Double.parseDouble(rate.replace(",", "."));
            }
        }
        throw new IllegalArgumentException("Currency code not found: " + currencyCode);
    }
}