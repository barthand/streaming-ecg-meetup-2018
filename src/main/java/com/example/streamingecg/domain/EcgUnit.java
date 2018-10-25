package com.example.streamingecg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EcgUnit {
    private long timestamp;
    private double voltage;

    public static EcgUnit withVoltage(double voltage) {
        return new EcgUnit(0, voltage);
    }
}
