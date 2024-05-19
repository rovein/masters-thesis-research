package ua.nure.sagaresearch.experiments.service;

import static java.util.Collections.reverse;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static ua.nure.sagaresearch.common.util.LoggingUtils.END_TIME;
import static ua.nure.sagaresearch.common.util.LoggingUtils.START_TIME;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExecutionTimeExtractorService {

    public static final int NANO_TO_MILLI_SCALE = 1_000_000;

    public List<Long> extractExecutionTimesStatistic(Long numberOfTransactions, String startTimeLogsLocation, String endTimeLogsLocation, String sagaPrefix)
            throws IOException {
        List<String> startTimeLogs = Files.lines(Path.of(startTimeLogsLocation)).collect(Collectors.toList());
        List<String> endTimeLogs = Files.lines(Path.of(endTimeLogsLocation)).collect(Collectors.toList());

        reverse(startTimeLogs);
        reverse(endTimeLogs);

        var startTimes = extractStartTimes(startTimeLogs, numberOfTransactions, START_TIME, sagaPrefix);
        var endTimes = extractStartTimes(endTimeLogs, numberOfTransactions, END_TIME, sagaPrefix);

        var result = new ArrayList<Long>();
        for (int i = 0; i < startTimes.size(); i++) {
            var endTime = endTimes.get(i);
            var startTime = startTimes.get(i);
            var totalTimeInNano = endTime - startTime;
            result.add(totalTimeInNano / NANO_TO_MILLI_SCALE);
        }

        return result;
    }

    public Double extractAverageExecutionTime(Long numberOfTransactions, String startTimeLogsLocation, String endTimeLogsLocation, String sagaPrefix)
            throws IOException {
        return extractExecutionTimesStatistic(numberOfTransactions, startTimeLogsLocation, endTimeLogsLocation, sagaPrefix)
                .stream()
                .mapToLong(time -> time)
                .average()
                .orElse(0d);
    }

    private static List<Long> extractStartTimes(List<String> logs, Long numberOfTransactions, String timeMarker, String sagaPrefix) {
        return logs.stream()
                .filter(line -> line.contains(sagaPrefix))
                .filter(line -> line.contains(timeMarker))
                .limit(numberOfTransactions)
                .map(line -> substringAfter(line, timeMarker).trim())
                .map(Long::parseLong)
                .toList();
    }
}
