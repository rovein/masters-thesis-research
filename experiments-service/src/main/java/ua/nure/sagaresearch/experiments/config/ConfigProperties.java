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

}
