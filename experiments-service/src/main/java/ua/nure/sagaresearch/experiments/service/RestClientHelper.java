package ua.nure.sagaresearch.experiments.service;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RestClientHelper {
    private final RestClient restClient;
    private final ExecutorService executorService;

    public  <T> void supplyAsyncAndWaitForAllEntityCreationTasks(Integer numberOfTasks,
                                                                 Supplier<T> entityCreationTask, Consumer<T> acceptConsumer) {
        allOf(IntStream.range(0, numberOfTasks)
                .mapToObj(i -> supplyAsync(entityCreationTask, executorService).thenAccept(acceptConsumer))
                .toArray(CompletableFuture[]::new)).join();
    }

    public String performPostRequest(String entityCreationUrl, Object... pathVariables) {
        return performPostRequest(entityCreationUrl, StringUtils.EMPTY, String.class, pathVariables);
    }

    public  <R> R performGetRequest(String entityRetrieveUrl, Class<R> responseType, Object... pathVariables) {
        return restClient.get()
                .uri(entityRetrieveUrl, pathVariables)
                .retrieve()
                .body(responseType);
    }

    public  <T, R> R performPostRequest(String entityCreationUrl, T body, Class<R> responseType, Object... pathVariables) {
        return restClient.post()
                .uri(entityCreationUrl, pathVariables)
                .body(body)
                .retrieve()
                .body(responseType);
    }
}
