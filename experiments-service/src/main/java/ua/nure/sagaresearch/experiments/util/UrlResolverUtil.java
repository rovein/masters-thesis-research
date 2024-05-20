package ua.nure.sagaresearch.experiments.util;

import ua.nure.sagaresearch.experiments.config.ConfigProperties;

public final class UrlResolverUtil {

    public static String resolveCreateBasketUrl(ExperimentType experimentType, ConfigProperties configProperties) {
        String createBasketEndpoint = configProperties.getCreateBasketEndpoint();
        return switch (experimentType) {
            case OUTBOX -> configProperties.getOutboxBasketServiceUrl() + createBasketEndpoint;
            case EVENT_SOURCING -> configProperties.getSourcingBasketServiceUrl() + createBasketEndpoint;
        };
    }

    public static String resolveCreateProductUrl(ExperimentType experimentType, ConfigProperties configProperties) {
        String createProductEndpoint = configProperties.getCreateProductEndpoint();
        return switch (experimentType) {
            case OUTBOX -> configProperties.getOutboxProductServiceUrl() + createProductEndpoint;
            case EVENT_SOURCING -> configProperties.getSourcingProductServiceUrl() + createProductEndpoint;
        };
    }

    private UrlResolverUtil() {
    }
}
