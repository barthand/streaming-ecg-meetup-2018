package com.example.streamingecg;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class ReactorFirstBabySteps {

    @Test
    public void createMono() {
        Mono.just(1)
            .map(s -> s * 5)
            .log()
            .subscribe(System.out::println);

        // without subscription, pipeline is not executed
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
        Flux<Integer> numbersSquared = Flux.just(1, 2, 3)
                                .map(s -> s * s)
                                .log();

        // let's use StepVerifier
        StepVerifier.create(numbersSquared)
                    .expectNext(1)
                    .expectNext(4)
                    .expectNext(9)
                    .verifyComplete();
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
        Mono<String> reduced = Flux.fromIterable(strings)
                                  .log()
                                  .map(String::toUpperCase)
                                  .reduce((s1, s2) -> s1 + " " + s2);

        StepVerifier.create(reduced)
                    .expectNext("HELLO MEETUP @ IT.ROCHE.PL !")
                    .verifyComplete();
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
        Mono<String> log = Mono.fromCallable(ReactorFirstBabySteps::blockingString)
                               .subscribeOn(Schedulers.elastic())
                               .log();

        // Since our stream is asynchronous and non-blocking, test thread finishes. Use Thread.sleep()
        // Thread.sleep(1000);

        // or better yet use StepVerifier, which will block, awaiting the result
        StepVerifier.create(log).expectNext("blocking result").verifyComplete();
    }

    private static String blockingString() throws InterruptedException {
        Thread.sleep(500);
        return "blocking result";
    }

}
