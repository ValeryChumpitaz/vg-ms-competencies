package pe.edu.vallegrande.vg_ms_competencies.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.vg_ms_competencies.domain.model.Competencies;
import reactor.core.publisher.Flux;

public interface CompetenciesRepository extends ReactiveMongoRepository<Competencies, String> {
    Flux<Competencies> findByStatus(String status);
    Flux<Competencies> findByCompetencyIdAndStatus(String competencyId, String status);
    Flux<Competencies> findByDidacticUnitIdAndStatus(String didacticUnitId, String status);
}

