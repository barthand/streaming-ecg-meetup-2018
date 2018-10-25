package com.example.streamingecg.reactive;

import com.example.streamingecg.domain.EcgGenerator;
import com.example.streamingecg.domain.EcgUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Date;

@Service
@Slf4j
public class EcgReactiveService {

    public static final Scheduler GENERATORS_POOL = Schedulers.newParallel("generators-pool");

    public Flux<EcgUnit> createInfiniteEcgStream() {
        Flux<EcgUnit> ecgUnitFlux = Mono.defer(() -> Mono.fromCallable(() -> new EcgGenerator().generate())
                                                         .subscribeOn(GENERATORS_POOL)
        ).flatMapMany(Flux::fromIterable);

        return ecgUnitFlux
                   .delayElements(Duration.ofMillis(1000 / EcgGenerator.SAMPLING_FREQ))
                   .sample(Duration.ofMillis(20))
                   .repeat()
                   .map(EcgReactiveService::withTimestamp)
                   .doFinally(signalType -> log.info("Stream completed with signalType {}...", signalType));
    }

    private static EcgUnit withTimestamp(EcgUnit ecgUnit) {
        return new EcgUnit(new Date().getTime(), ecgUnit.getVoltage());
    }

}
