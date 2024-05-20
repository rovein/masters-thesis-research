package ua.nure.sagaresearch.experiments.controller;

import static ua.nure.sagaresearch.experiments.util.UrlResolverUtil.resolveCreateBasketUrl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import ua.nure.sagaresearch.experiments.config.ConfigProperties;
import ua.nure.sagaresearch.experiments.util.ExperimentType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/experiments")
@RequiredArgsConstructor
public class ExperimentsController {

    private final ConfigProperties configProperties;
    private final RestClient restClient;
    private final ExecutorService executorService;

    private List<String> basketIds = new ArrayList<>();

    @PostMapping(value = "/pre-setup")
    @Operation(summary = "Step 1, create N baskets", tags = "Experiments")
    public List<String> step1(@RequestParam Integer numberOfBaskets, @RequestParam ExperimentType experimentType) throws InterruptedException {
        basketIds.clear();
        String createBasketUrl = resolveCreateBasketUrl(experimentType, configProperties);
        CompletableFuture.allOf(IntStream.range(0, numberOfBaskets)
                .mapToObj(i ->
                        CompletableFuture.supplyAsync(() ->
                                        createBasket(createBasketUrl), executorService).thenAccept(basketIds::add))
                .toArray(CompletableFuture[]::new)).join();
        return basketIds;
    }

    private String createBasket(String entityCreationUrl) {
        return restClient.post()
                .uri(entityCreationUrl)
                .retrieve()
                .body(String.class);
    }
}
