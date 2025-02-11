package pe.edu.vallegrande.vg_ms_competencies.application.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vg_ms_competencies.domain.dto.*;
import pe.edu.vallegrande.vg_ms_competencies.domain.model.Competencies;
import pe.edu.vallegrande.vg_ms_competencies.domain.repository.CompetenciesRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CompetenciesService {

    private final CompetenciesRepository competenciesRepository;
    private final ExternalService externalService;
    private final ModelMapper modelMapper = new ModelMapper();

    public CompetenciesService(CompetenciesRepository competenciesRepository, ExternalService externalService) {
        this.competenciesRepository = competenciesRepository;
        this.externalService = externalService;
    }

    
    public Flux<CompetenciesDto> getByStatus(String status) {
        return competenciesRepository.findByStatus(status)
        .flatMap(this::converTo)
        .collectList()
        .flatMapMany(Flux::fromIterable);
    }

    
    public Mono<Competencies> create(CompetenciesCreateDto competenciesCreateDto) {
        if (competenciesCreateDto.getName() == null || competenciesCreateDto.getName().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre no puede estar vacío"));
        }
        competenciesCreateDto.setStatus("A");
        Competencies competencies = modelMapper.map(competenciesCreateDto, Competencies.class);
        return competenciesRepository.save(competencies);
    }
    

    
    public Mono<Competencies> update(String id, CompetenciesUpdateDto competenciesUpdateDto) {
        return competenciesRepository.findByCompetencyIdAndStatus(id, "A")
                .next()
                .flatMap(sp -> {
                    modelMapper.map(competenciesUpdateDto, sp);
                    sp.setCompetencyId(id); // Ensure the ID is not changed
                    return competenciesRepository.save(sp);
                });
    }

    
    public Mono<Competencies> changeStatus(String id, String status) {
        return competenciesRepository.findById(id)
                .flatMap(sp -> {
                    sp.setStatus(status);
                    return competenciesRepository.save(sp);
                });
    }

    
    public Mono<Competencies> getByIds(String id) {
        return competenciesRepository.findById(id);
    }

    
    public Mono<DidacticUnit> getById(String id) {
        return externalService.getByIdDidacticUnit(id);
    }

    
    public Flux<DidacticUnit> listActive() {
        return externalService.getDidacticUnit();
    }

    
    public Flux<Competencies> assignCompetenciesToDidacticUnit(String didacticUnitId, CompetenciesIdsDto competenciesIdsDto) {
        return Flux.fromIterable(competenciesIdsDto.getCompetenciesIds())
                .flatMap(competencyId -> competenciesRepository.findById(competencyId)
                        .flatMap(competency -> {
                            competency.setDidacticUnitId(didacticUnitId);
                            competency.setStatus("A");
                            return competenciesRepository.save(competency);
                        }));
    }

    
    public Flux<Competencies> getByDidacticUnitId(String didacticUnitId) {
        return competenciesRepository.findByDidacticUnitIdAndStatus(didacticUnitId, "A");
    }

    private Mono<CompetenciesDto> converTo(Competencies competencies) {
        CompetenciesDto dto = new CompetenciesDto();
        dto.setCompetencyId(competencies.getCompetencyId());
        dto.setName(competencies.getName());
        dto.setDescription(competencies.getDescription());
        dto.setStatus(competencies.getStatus());


        Mono<DidacticUnit> didacticUnit = externalService.getByIdDidacticUnit(competencies.getDidacticUnitId());
        
        return Mono.zip(didacticUnit , Mono.just(dto))
                .map(tuple -> {

                    DidacticUnit didacticUnits = tuple.getT1();

                    dto.setDidacticUnitId(didacticUnits);
                    return dto;
                });
    }
}
