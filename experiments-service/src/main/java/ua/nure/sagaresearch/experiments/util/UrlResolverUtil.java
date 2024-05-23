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

    public static String resolveAddProductToBasketUrl(ExperimentType experimentType, ConfigProperties configProperties) {
        String addProductToBasketEndpoint = configProperties.getAddProductToBasketEndpoint();
        return switch (experimentType) {
            case OUTBOX -> configProperties.getOutboxBasketServiceUrl() + addProductToBasketEndpoint;
            case EVENT_SOURCING -> configProperties.getSourcingBasketServiceUrl() + addProductToBasketEndpoint;
        };
    }

    public static String resolvePlaceOrderUrl(ExperimentType experimentType, ConfigProperties configProperties) {
        String placeOrderEndpoint = configProperties.getPlaceOrderEndpoint();
        return switch (experimentType) {
            case OUTBOX -> configProperties.getOutboxOrderServiceUrl() + placeOrderEndpoint;
            case EVENT_SOURCING -> configProperties.getSourcingOrderServiceUrl() + placeOrderEndpoint;
        };
    }

    public static String resolveRetrieveOrderUrl(ExperimentType experimentType, ConfigProperties configProperties) {
        String retrieveOrderEndpoint = configProperties.getRetrieveOrderEndpoint();
        return switch (experimentType) {
            case OUTBOX -> configProperties.getOutboxOrderServiceUrl() + retrieveOrderEndpoint;
            case EVENT_SOURCING -> configProperties.getSourcingOrderServiceUrl() + retrieveOrderEndpoint;
        };
    }

    public static String resolveConfirmPaymentUrl(ExperimentType experimentType, ConfigProperties configProperties) {
        String confirmPaymentEndpoint = configProperties.getConfirmPaymentEndpoint();
        return switch (experimentType) {
            case OUTBOX -> configProperties.getOutboxOrderServiceUrl() + confirmPaymentEndpoint;
            case EVENT_SOURCING -> configProperties.getSourcingOrderServiceUrl() + confirmPaymentEndpoint;
        };
    }

    public static String resolveCancelOrderUrl(ExperimentType experimentType, ConfigProperties configProperties) {
        String cancelOrderUrl = configProperties.getCancelOrderEndpoint();
        return switch (experimentType) {
            case OUTBOX -> configProperties.getOutboxOrderServiceUrl() + cancelOrderUrl;
            case EVENT_SOURCING -> configProperties.getSourcingOrderServiceUrl() + cancelOrderUrl;
        };
    }

    private UrlResolverUtil() {
    }
}
