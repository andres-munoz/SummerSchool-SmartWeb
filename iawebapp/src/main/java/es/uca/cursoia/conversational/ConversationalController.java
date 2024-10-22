package es.uca.cursoia.conversational;

import dev.langchain4j.service.TokenStream;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class ConversationalController {

    /**
     * Este servicio te saluda
     *
     * @return un saludo
     */
    @GetMapping("/api/v1/hello")
    public String chat() {
        return "Hello World";
    }

    protected Flux<String> convertTokenStream2Flux(TokenStream streamingAssistant) {

        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        streamingAssistant
                .onNext(sink::tryEmitNext)
                .onComplete(c -> sink.tryEmitComplete())
                .onError(sink::tryEmitError)
                .start();

        return sink.asFlux();

    }
}
