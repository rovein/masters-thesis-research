package ua.nure.sagaresearch.experiments.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConfigProperties {

    @Value("${logs.location.basket}")
    private String basketLogsLocation;

    @Value("${logs.location.product}")
    private String productLogsLocation;

    @Value("${logs.location.order}")
    private String orderLogsLocation;

    @Value("${outbox.basket.service.url}")
    private String outboxBasketServiceUrl;

    @Value("${outbox.product.service.url}")
    private String outboxProductServiceUrl;

    @Value("${outbox.order.service.url}")
    private String outboxOrderServiceUrl;

    @Value("${sourcing.basket.service.url}")
    private String sourcingBasketServiceUrl;

    @Value("${sourcing.product.service.url}")
    private String sourcingProductServiceUrl;

    @Value("${sourcing.order.service.url}")
    private String sourcingOrderServiceUrl;

    @Value("${create.basket.endpoint}")
    private String createBasketEndpoint;

    @Value("${create.product.endpoint}")
    private String createProductEndpoint;

    @Value("${add.product.to.basket.endpoint}")
    private String addProductToBasketEndpoint;
}
