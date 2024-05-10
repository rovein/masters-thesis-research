package ua.nure.sagaresearch.baskets.domain;

public class BasketIsEmptyException extends RuntimeException {
    public BasketIsEmptyException(String message) {
        super(message);
    }
}
