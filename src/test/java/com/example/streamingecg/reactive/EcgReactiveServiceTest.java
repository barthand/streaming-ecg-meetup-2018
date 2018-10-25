package com.example.streamingecg.reactive;

import com.example.streamingecg.domain.EcgUnit;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;

public class EcgReactiveServiceTest {

    @Test
    public void shouldProduceSomeEcgData() {
        EcgReactiveService service = new EcgReactiveService();

        Flux<EcgUnit> infiniteEcgStream = service.createInfiniteEcgStream();

        StepVerifier.create(infiniteEcgStream)
                    .recordWith(ArrayList::new)
                    .expectNextCount(10)
                    .consumeRecordedWith(ecgUnits ->
                            ecgUnits.forEach(System.out::println)
                    )
                    .thenCancel()
                    .verify();
    }

}