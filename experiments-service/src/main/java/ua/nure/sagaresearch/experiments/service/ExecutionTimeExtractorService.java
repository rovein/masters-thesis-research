package ua.nure.sagaresearch.experiments.service;

import static java.util.Collections.reverse;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static ua.nure.sagaresearch.common.util.LoggingUtils.END_TIME;
import static ua.nure.sagaresearch.common.util.LoggingUtils.ID_MARKER;
import static ua.nure.sagaresearch.common.util.LoggingUtils.SAGA_PREFIX_CLOSING;
import static ua.nure.sagaresearch.common.util.LoggingUtils.START_TIME;
import static ua.nure.sagaresearch.common.util.LoggingUtils.TIME_AND_ID_SEPARATOR;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class ExecutionTimeExtractorService {

    public static final int NANO_TO_MILLI_SCALE = 1_000_000;

    public List<Long> extractExecutionTimesStatistic(Long numberOfTransactions, String startTimeLogsLocation, String endTimeLogsLocation, String sagaPrefix)
            throws IOException {
        List<String> startTimeLogs = extractLogs(startTimeLogsLocation);
        List<String> endTimeLogs = extractLogs(endTimeLogsLocation);

        reverse(startTimeLogs);
        reverse(endTimeLogs);

        List<String> exactStartLogs = filterBySagaAndLimit(startTimeLogs, sagaPrefix, START_TIME, numberOfTransactions);
        List<String> exactEndLogs = filterBySagaAndLimit(endTimeLogs, sagaPrefix, END_TIME, numberOfTransactions);

        List<String> experimentLogs = new ArrayList<>();
        experimentLogs.addAll(exactStartLogs);
        experimentLogs.addAll(exactEndLogs);

        return calculateTotalTimeForExperiments(experimentLogs);
    }

    public Double extractAverageExecutionTime(Long numberOfTransactions, String startTimeLogsLocation, String endTimeLogsLocation, String sagaPrefix)
            throws IOException {
        return extractExecutionTimesStatistic(numberOfTransactions, startTimeLogsLocation, endTimeLogsLocation, sagaPrefix)
                .stream()
                .mapToLong(time -> time)
                .average()
                .orElse(0d);
    }

    private static List<String> extractLogs(String logsLocation) throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(logsLocation))) {
            return lines.collect(toList());
        }
    }

    private static List<String> filterBySagaAndLimit(List<String> logs, String sagaPrefix, String timePrefix, Long numberOfTransactions) {
        return logs.stream()
                .filter(line -> line.contains(sagaPrefix))
                .filter(line -> line.contains(timePrefix))
                .limit(numberOfTransactions)
                .collect(toList());
    }

    private static ArrayList<Long> calculateTotalTimeForExperiments(List<String> experimentLogs) {
        var result = new ArrayList<Long>();

        groupStartEndTimesByLogId(experimentLogs).forEach((id, times) -> {
            var endTime = retrieveTime(times, END_TIME);
            var startTime = retrieveTime(times, START_TIME);
            var totalTimeInNano = endTime - startTime;
            result.add(totalTimeInNano / NANO_TO_MILLI_SCALE);
        });

        return result;
    }

    private static Map<String, List<String>> groupStartEndTimesByLogId(List<String> logs) {
        return logs.stream()
                .map(line -> substringAfterLast(line, SAGA_PREFIX_CLOSING).trim())
                .collect(groupingBy(line -> substringAfter(line, ID_MARKER).trim(),
                        mapping(line -> substringBefore(line, TIME_AND_ID_SEPARATOR).trim(), toList())));
    }

    private static long retrieveTime(List<String> times, String timePrefix) {
        return times.stream()
                .filter(log -> log.contains(timePrefix))
                .map(log -> substringAfter(log, timePrefix))
                .map(String::trim)
                .map(Long::parseLong)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unable to find %s in %s".formatted(timePrefix, times)));
    }
}
