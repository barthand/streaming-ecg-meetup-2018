package com.example.streamingecg.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class EcgGenerator {

    public static final int SAMPLING_FREQ = 250;

    public List<EcgUnit> generate() {
        try {
            log.info("Starting to collect ECG data...");
            Thread.sleep(500);
            try (InputStream resource = this.getClass().getResourceAsStream("/data/ecg.data")) {

                List<EcgUnit> units = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8))
                        .lines()
                        .map(Double::valueOf)
                        .map(EcgUnit::withVoltage)
                        .collect(Collectors.toList());
                log.info("ECG data collected...");
                return units;
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error ouccured while loading ECG data", e);
            throw new RuntimeException(e);
        }
    }

}
