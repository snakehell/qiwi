import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Tests{

    @Test
    public void testFindRateByCurrencyCode() throws Exception {
        String xmlResponse = "<?xml version=\"1.0\" encoding=\"windows-1251\"?><ValCurs Date=\"25.07.2023\" name=\"Foreign Currency Market\"><Valute ID=\"R01235\"><NumCode>840</NumCode><CharCode>USD</CharCode><Nominal>1</Nominal><Name>Доллар США</Name><Value>90,4890</Value></Valute></ValCurs>";
        InputStream inputStream = new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8));
        double rate = Currency.findRateByCurrencyCode(inputStream, "USD");
        assertEquals(90.4890, rate, 0.0001);
    }

    @Test
    public void testFindRateByInvalidCurrencyCode() throws Exception {
        String xmlResponse = "<?xml version=\"1.0\" encoding=\"windows-1251\"?><ValCurs Date=\"25.07.2023\" name=\"Foreign Currency Market\"><Valute ID=\"R01235\"><NumCode>840</NumCode><CharCode>USD</CharCode><Nominal>1</Nominal><Name>Доллар США</Name><Value>90,4890</Value></Valute></ValCurs>";
        InputStream inputStream = new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> {
            Currency.findRateByCurrencyCode(inputStream, "EUR");
        });
    }

    @Test
    public void testMain() {
        String[] args = {"--code=USD", "--date=2023-07-25"};
        Main.main(args);
    }

}