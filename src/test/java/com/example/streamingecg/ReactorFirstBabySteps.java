package com.example.streamingecg;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;

public class ReactorFirstBabySteps {

    @Test
    public void createMono() {
        Mono.just(1)
            .map(s -> s * 5)
            .log();

        // where is my output?
    }

    @Test
    public void createEmptyMonoOrSwitch() {
        Mono.empty().switchIfEmpty(Mono.just(2))
            .log()
            .subscribe(System.out::println);
    }

    @Test
    public void createMonoFromCallable() {
        Mono.fromCallable(() -> "some result")
            .log()
            .subscribe(System.out::println);
    }

    @Test
    public void createFluxWithMap() {
        Flux.just(1, 2, 3)
            .map(s -> s * s)
            .log();

        // how to assert on values?
    }

    @Test
    public void reuseFlux() {
        Flux<Integer> just = Flux.just(1, 2, 3);

        just.map(s -> s * s)
            .log()
            .subscribe(System.out::println);

        just.filter(s -> s <= 2)
            .log()
            .subscribe(System.out::println);

        // unlike Java 8 streams, Reactive Streams can be subscribed to multiple times
    }

    @Test
    public void createFluxFromIterable() {
        List<String> strings = Arrays.asList("hello", "meetup", "@", "it.roche.pl", "!");
        Flux.fromIterable(strings)
            .log()
            .map(String::toUpperCase)
            .reduce((s1, s2) -> s1 + " " + s2)
            .subscribe(System.out::println);
    }

    @Test
    public void blockingFluxCalledOnSameThread() {
        // called on calling thread
        Mono.fromCallable(ReactorFirstBabySteps::blockingString)
            .log()
            .subscribe(System.out::println);
    }

    @Test
    public void blockingFluxScheduledOnAnotherThread() {
        // called on calling thread
        Mono.fromCallable(ReactorFirstBabySteps::blockingString)
            .subscribeOn(Schedulers.elastic())
            .log()
            .subscribe(System.out::println);

        // where is my output?
    }

    private static String blockingString() throws InterruptedException {
        Thread.sleep(500);
        return "blocking result";
    }

}
