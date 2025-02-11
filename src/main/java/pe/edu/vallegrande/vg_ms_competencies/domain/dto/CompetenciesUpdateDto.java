package pe.edu.vallegrande.vg_ms_competencies.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompetenciesUpdateDto {

    private String name;
    private String description;
    private String didacticUnitId;

}
