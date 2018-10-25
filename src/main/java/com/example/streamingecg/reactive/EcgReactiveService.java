package com.example.streamingecg.reactive;

import com.example.streamingecg.domain.EcgGenerator;
import com.example.streamingecg.domain.EcgUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class EcgReactiveService {

    public Flux<EcgUnit> createInfiniteEcgStream() {
        List<EcgUnit> sampleEcgData = new EcgGenerator().generate();

        return Flux.fromIterable(sampleEcgData)
                   .delayElements(Duration.ofMillis(500))
                   .repeat()
                   .map(EcgReactiveService::withTimestamp)
                   .doFinally(signalType -> log.info("Stream completed with signalType {}...", signalType));
    }

    private static EcgUnit withTimestamp(EcgUnit ecgUnit) {
        return new EcgUnit(new Date().getTime(), ecgUnit.getVoltage());
    }

}
