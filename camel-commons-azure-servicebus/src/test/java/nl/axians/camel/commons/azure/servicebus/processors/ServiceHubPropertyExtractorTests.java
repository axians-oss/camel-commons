package nl.axians.camel.commons.azure.servicebus.processors;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the {@link ServiceHubPropertyExtractor}.
 */
public class ServiceHubPropertyExtractorTests extends CamelTestSupport {

    private ServiceHubPropertyExtractor extractor;
    private Exchange exchange;

    /**
     * Sets up the test environment.
     */
    @BeforeEach
    public void setUp()  {
        extractor = new ServiceHubPropertyExtractor(Arrays.asList("property1", "property2"));
        exchange = new DefaultExchange(new DefaultCamelContext());
    }

    /**
     * Tests the extraction of required properties from the application properties.
     */
    @Test
    public void shouldExtractRequiredProperties() {
        Map<String, String> applicationProperties = new HashMap<>();
        applicationProperties.put("property1", "value1");
        applicationProperties.put("property2", "value2");

        exchange.getIn().setHeader(ServiceHubPropertyExtractor.HEADER_APPLICATION_PROPERTIES, applicationProperties);

        extractor.process(exchange);

        assertEquals("value1", exchange.getProperty("property1"));
        assertEquals("value2", exchange.getProperty("property2"));
    }

    /**
     * Tests the extraction of required properties from the application properties when some properties are missing.
     */
    @Test
    public void shouldThrowExceptionWhenRequiredPropertyNotFound() {
        Map<String, String> applicationProperties = new HashMap<>();
        applicationProperties.put("property1", "value1");

        exchange.getIn().setHeader(ServiceHubPropertyExtractor.HEADER_APPLICATION_PROPERTIES, applicationProperties);

        assertThrows(IllegalStateException.class, () -> extractor.process(exchange));
    }

    /**
     * Tests the extraction of required properties from the application properties when no application properties are
     * found.
     */
    @Test
    public void shouldThrowExceptionWhenNoApplicationProperties() {
        assertThrows(IllegalStateException.class, () -> extractor.process(exchange));
    }

}
