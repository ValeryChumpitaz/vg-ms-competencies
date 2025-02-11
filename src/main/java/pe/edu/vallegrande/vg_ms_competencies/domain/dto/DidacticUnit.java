package pe.edu.vallegrande.vg_ms_competencies.domain.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class DidacticUnit {

    @Id
    private String didacticId;
    private String name;
    private int credit;
    private int hours;
    private String condition;
    private String correction;
    private String status;
}

