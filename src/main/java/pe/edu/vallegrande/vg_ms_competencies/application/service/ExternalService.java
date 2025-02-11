package pe.edu.vallegrande.vg_ms_competencies.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.vg_ms_competencies.domain.dto.DidacticUnit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ExternalService {

    @Value("${services.didactic-unit.url}")
    private String didacticUnitUrl;

    private final WebClient.Builder webClientBuilder;
    
    public ExternalService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<DidacticUnit> getByIdDidacticUnit(String didacticId) {
        return fetchData(didacticUnitUrl + "/", 
                    didacticId, DidacticUnit.class);
    }

    public Flux<DidacticUnit> getDidacticUnit() {
        return fetchDataList(didacticUnitUrl + "/list/active", 
        DidacticUnit.class);
    }


    private <T> Mono<T> fetchData(String baseUrl, String id, Class<T> responseType) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + id)
                .retrieve()
                .bodyToMono(responseType)
                .onErrorResume(e -> {
                    log.error("Error fetching data: ", e);
                    return Mono.empty();
                });
    }

    private <T> Flux<T> fetchDataList(String baseUrl, Class<T> responseType) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl) 
                .retrieve()
                .bodyToFlux(responseType) 
                .onErrorResume(e -> {
                    log.error("Error fetching data: ", e);
                    return Flux.empty(); 
                });
    }

}
