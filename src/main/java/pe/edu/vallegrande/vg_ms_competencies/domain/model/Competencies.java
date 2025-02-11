package pe.edu.vallegrande.vg_ms_competencies.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "competencies")
public class Competencies {

    @Id
    private String competencyId;
    private String name;
    private String description;
    private String status;
    private String didacticUnitId;
}
