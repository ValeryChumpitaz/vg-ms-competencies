package pe.edu.vallegrande.vg_ms_competencies.presentation.controller;

import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vg_ms_competencies.application.service.CompetenciesService;
import pe.edu.vallegrande.vg_ms_competencies.domain.dto.*;
import pe.edu.vallegrande.vg_ms_competencies.domain.model.Competencies;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/common/${api.version}/competencies")
public class CompetenciesController {

    private final CompetenciesService competenciesService;

    public CompetenciesController(CompetenciesService competenciesService) {
        this.competenciesService = competenciesService;
    }

    @GetMapping("/list/active")
    public Flux<CompetenciesDto> listActive() {
        return competenciesService.getByStatus("A");
    }

    @GetMapping("/list/inactive")
    public Flux<CompetenciesDto> listInactive() {
        return competenciesService.getByStatus("I");
    }

    @PostMapping("/create")
    public Mono<Competencies> create(@RequestBody CompetenciesCreateDto competenciesCreateDto) {
        return competenciesService.create(competenciesCreateDto);
    }

    @PutMapping("/update/{id}")
    public Mono<Competencies> update(@PathVariable String id, @RequestBody CompetenciesUpdateDto competenciesUpdateDto) {
        return competenciesService.update(id, competenciesUpdateDto);
    }

    @PutMapping("/activate/{id}")
    public Mono<Competencies> activate(@PathVariable String id) {
        return competenciesService.changeStatus(id, "A");
    }

    @PutMapping("/inactive/{id}")
    public Mono<Competencies> deactivate(@PathVariable String id) {
        return competenciesService.changeStatus(id, "I");
    }

    @GetMapping("/{id}")
    public Mono<Competencies> getById(@PathVariable String id) {
        return competenciesService.getByIds(id);
    }

    @GetMapping("/unit/{id}")
    public Mono<DidacticUnit> getByProgram(@PathVariable String id) {
        return competenciesService.getById(id);
    }

    @GetMapping("/unit/list")
    public Flux<DidacticUnit> listProgram() {
        return competenciesService.listActive();
    }

    @PostMapping("/{didacticUnitId}/competencies")
    public Flux<Competencies> assignCompetenciesToDidacticUnit(@PathVariable String didacticUnitId, @RequestBody CompetenciesIdsDto competenciesIdsDto) {
        return competenciesService.assignCompetenciesToDidacticUnit(didacticUnitId, competenciesIdsDto);
    }

    @GetMapping("/didactic-unit/{didacticUnitId}/competencies")
    public Flux<Competencies> getCompetenciesByDidacticUnitId(@PathVariable String didacticUnitId) {
        return competenciesService.getByDidacticUnitId(didacticUnitId);
    }
}
