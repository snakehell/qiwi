import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main {
    public static void main(String[] args) {

        String currencyCode = null;
        String dateString = null;

        for (String arg : args) {
            if (arg.startsWith("--code=")) {
                currencyCode = arg.substring(7);
            } else if (arg.startsWith("--date=")) {
                dateString = arg.substring(7);
            }
        }

        if (currencyCode == null || dateString == null) {
            System.out.println("Usage: currency_rates --code=<currency_code> --date=<date>");
            return;
        }

        try {
            boolean isDateValid;
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            dateString = new SimpleDateFormat("dd/MM/yyyy").format(date);
            String url = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + dateString;

            InputStream responseStream = Currency.sendRequest(url);
            double rate = Currency.findRateByCurrencyCode(responseStream, currencyCode);
            String currencyName = getCurrencyName(currencyCode);
            System.out.printf("%s (%s): %.4f\n", currencyCode, currencyName, rate);
        } catch (IOException | ParseException | ParserConfigurationException | SAXException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static String getCurrencyName(String currencyCode) throws IOException, ParserConfigurationException, SAXException  {
        String url = "https://www.cbr.ru/scripts/XML_valFull.asp";
        InputStream responseStream = Currency.sendRequest(url);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(responseStream);
        NodeList nodeList = document.getElementsByTagName("Item");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String charCode = element.getElementsByTagName("ISO_Char_Code").item(0).getTextContent();
            if (charCode.equals(currencyCode)) {
                return element.getElementsByTagName("Name").item(0).getTextContent();
            }
        }
        return "Unknown Currency";
    }
}