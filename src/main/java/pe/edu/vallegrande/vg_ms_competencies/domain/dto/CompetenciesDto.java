package pe.edu.vallegrande.vg_ms_competencies.domain.dto;

import lombok.Data;

@Data
public class CompetenciesDto {
    private String competencyId;
    private String name;
    private String description;
    private String status;
    private DidacticUnit didacticUnitId;
}
