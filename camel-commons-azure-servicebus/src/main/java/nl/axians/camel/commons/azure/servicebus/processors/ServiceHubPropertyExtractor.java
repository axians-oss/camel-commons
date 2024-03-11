package nl.axians.camel.commons.azure.servicebus.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link Processor} to extract application properties from an Azure Service Hub message and set them as properties on
 * the exchange.
 */
public class ServiceHubPropertyExtractor implements Processor {

    public static final String HEADER_APPLICATION_PROPERTIES = "CamelAzureServiceBusApplicationProperties";

    private final List<String> requiredProperties;

    /**
     * Constructor to create a new instance of the ServiceHubPropertyExtractor. This processor will extract the
     * application properties from an Azure Service Bus  message and set them as properties on the exchange. Only the
     * properties in the list of required properties will be set on the exchange.
     *
     * @param theRequiredProperties The list of required properties to extract from the application properties.
     */
    public ServiceHubPropertyExtractor(final List<String> theRequiredProperties) {
        requiredProperties = theRequiredProperties != null ? theRequiredProperties : new ArrayList<>();
    }

    /**
     * Processes the exchange by extracting the application properties from the Azure Service Bus message and setting
     * them as properties on the exchange. Only the properties in the list of {@link #requiredProperties} will be set
     * on the exchange.
     *
     * @param exchange The exchange to process.
     * @throws IllegalStateException If a required application property is not found.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void process(final Exchange exchange)  {
        final Map<String, String> applicationProperties = (Map<String, String>) exchange.getIn()
                .getHeader(HEADER_APPLICATION_PROPERTIES, Map.class);

        if (applicationProperties == null)
            throw new IllegalStateException("No application properties found in the Azure Service Bus message");

        for (final String property : requiredProperties) {
            final String value = applicationProperties.get(property);
            if (value == null)
                throw new IllegalStateException("Required application property not found in Azure Service Bus message: " + property);

            exchange.setProperty(property, value);
        }
    }

}
