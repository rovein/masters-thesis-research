package ua.nure.sagaresearch.orders.domain.events;

public enum OrderState {
    PENDING,
    PENDING_PAYMENT_CONFIRMED,
    APPROVED,
    REJECTED,
    CANCELLATION_REQUESTED,
    CANCELLED
}
