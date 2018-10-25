package com.example.streamingecg.api;

import com.example.streamingecg.domain.EcgUnit;
import com.example.streamingecg.reactive.EcgReactiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecg")
public class EcgController {

    private final EcgReactiveService ecgReactiveService;

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<EcgUnit> streamingEcgSignal() {
        return ecgReactiveService.createInfiniteEcgStream();
    }

}
