package ua.nure.sagaresearch.experiments.controller;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static ua.nure.sagaresearch.experiments.util.UrlResolverUtil.resolveCreateBasketUrl;
import static ua.nure.sagaresearch.experiments.util.UrlResolverUtil.resolveCreateProductUrl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import ua.nure.sagaresearch.experiments.config.ConfigProperties;
import ua.nure.sagaresearch.experiments.util.ExperimentType;
import ua.nure.sagaresearch.products.webapi.CreateProductRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/experiments")
@RequiredArgsConstructor
public class ExperimentsController {

    private final ConfigProperties configProperties;
    private final RestClient restClient;
    private final ExecutorService executorService;

    private List<String> basketIds = new ArrayList<>();
    private List<String> productIds = new ArrayList<>();

    @PostMapping(value = "/step-1/pre-setup-baskets")
    @Operation(summary = "Step 1, create N baskets", tags = "Experiments")
    public List<String> step1(@RequestParam Integer numberOfBaskets, @RequestParam ExperimentType experimentType) {
        basketIds.clear();
        String createBasketUrl = resolveCreateBasketUrl(experimentType, configProperties);
        supplyAsyncAndWaitForAllEntityCreationTasks(numberOfBaskets, () -> createEntity(createBasketUrl), basketIds::add);
        return basketIds;
    }

    @PostMapping(value = "/step-2/pre-setup-products")
    @Operation(summary = "Step 2, create N products", tags = "Experiments")
    public List<String> step2(@RequestParam Integer numberOfProducts, @RequestParam ExperimentType experimentType,
                              @RequestBody CreateProductRequest createProductRequest) {
        productIds.clear();
        String createProductUrl = resolveCreateProductUrl(experimentType, configProperties);
        supplyAsyncAndWaitForAllEntityCreationTasks(numberOfProducts, () -> createEntity(createProductUrl, createProductRequest), productIds::add);
        return productIds;
    }

    private <T> void supplyAsyncAndWaitForAllEntityCreationTasks(Integer numberOfTasks,
                                                                 Supplier<T> entityCreationTask, Consumer<T> acceptConsumer) {
        allOf(IntStream.range(0, numberOfTasks)
                .mapToObj(i -> supplyAsync(entityCreationTask, executorService).thenAccept(acceptConsumer))
                .toArray(CompletableFuture[]::new)).join();
    }

    private String createEntity(String entityCreationUrl) {
        return createEntity(entityCreationUrl, StringUtils.EMPTY);
    }

    private <T> String createEntity(String entityCreationUrl, T body) {
        return restClient.post()
                .uri(entityCreationUrl)
                .body(body)
                .retrieve()
                .body(String.class);
    }
}
