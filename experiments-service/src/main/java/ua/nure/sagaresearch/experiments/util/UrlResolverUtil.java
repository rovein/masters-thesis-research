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

    private UrlResolverUtil() {
    }
}
