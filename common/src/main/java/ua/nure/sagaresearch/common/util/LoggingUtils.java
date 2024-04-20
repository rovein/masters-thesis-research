package ua.nure.sagaresearch.common.util;

import lombok.SneakyThrows;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

public final class LoggingUtils {
    public static final String ADD_PRODUCT_TO_BASKET_PREFIX = "[Add Product to Basket SAGA]";
    public static final String PLACE_ORDER_PREFIX = "[Place Order SAGA]";
    public static final String CONFIRM_PAYMENT_PREFIX = "[Confirm Payment SAGA]";

    /**
     * Util logging method that has small delay needed to correctly display the order of logs
     * in Kibana for local environment experiments.
     * @param logger logger from source class
     * @param format format message
     * @param arguments arguments for message
     */
    @SneakyThrows
    public static void log(Logger logger, String format, Object... arguments) {
        TimeUnit.MILLISECONDS.sleep(100L);
        logger.info(format, arguments);
    }
}
