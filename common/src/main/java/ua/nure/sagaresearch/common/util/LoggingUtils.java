package ua.nure.sagaresearch.common.util;

import io.eventuate.Aggregate;
import io.eventuate.Command;
import io.eventuate.Event;
import lombok.SneakyThrows;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

public final class LoggingUtils {
    public static final String ADD_PRODUCT_TO_BASKET_PREFIX = "[OUTBOX Add Product to Basket SAGA]";
    public static final String PLACE_ORDER_PREFIX = "[OUTBOX Place Order SAGA]";
    public static final String CONFIRM_PAYMENT_PREFIX = "[OUTBOX Confirm Payment SAGA]";
    public static final String CANCEL_ORDER_PREFIX = "[OUTBOX Cancel Order SAGA]";

    public static final String EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX = "[Add Product to Basket SAGA] Event Sourcing.";
    public static final String EVENT_SOURCING_PLACE_ORDER_PREFIX = "[Place Order SAGA] Event Sourcing.";
    public static final String EVENT_SOURCING_CONFIRM_PAYMENT_PREFIX = "[Confirm Payment SAGA] Event Sourcing.";
    public static final String EVENT_SOURCING_CANCEL_ORDER_PREFIX = "[Cancel Order SAGA] Event Sourcing.";

    public static final String START_TIME = "Start time:";
    public static final String END_TIME = "End time:";

    /**
     * Util logging method that has small delay needed to correctly display the order of logs
     * in Kibana for local environment experiments.
     *
     * @param logger    logger from source class
     * @param format    format message
     * @param arguments arguments for message
     */
    @SneakyThrows
    public static void log(Logger logger, String format, Object... arguments) {
        TimeUnit.MILLISECONDS.sleep(1L);
        logger.info(format, arguments);
    }

    public static void logAggregateProcessMethod(Logger logger, Class<? extends Aggregate<?>> aggregateClass, Command cmd, String sagaPrefix, Event event) {
        log(logger, "{} Aggregate {}, processing {}, propagating {}",
                sagaPrefix, aggregateClass.getSimpleName(), cmd.getClass().getSimpleName(), event.getClass().getSimpleName());
    }

    public static void logAggregateProcessMethod(Logger logger, Class<? extends Aggregate<?>> aggregateClass, Command cmd, String sagaPrefix, String message) {
        log(logger, "{} Aggregate {}, processing {}, {}",
                sagaPrefix, aggregateClass.getSimpleName(), cmd.getClass().getSimpleName(), message);
    }

    public static void logStartTime(Logger logger, String sagaPrefix, Object entityId) {
        logStartTime(logger, sagaPrefix, System.nanoTime(), entityId);
    }

    public static void logStartTime(Logger logger, String sagaPrefix, long startTime, Object entityId) {
        log(logger, "{} {} {} | ID: {}", sagaPrefix, START_TIME, startTime, entityId);
    }

    public static void logEndTime(Logger logger, String sagaPrefix, Object entityId) {
        log(logger, "{} {} {} | ID: {}", sagaPrefix, END_TIME, System.nanoTime(), entityId);
    }

    private LoggingUtils() {
        throw new UnsupportedOperationException();
    }
}
