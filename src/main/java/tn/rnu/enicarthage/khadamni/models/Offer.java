package tn.rnu.enicarthage.khadamni.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String category;

    @NotNull
    private String title;

    @NotNull
    private String description;

    private Integer spots;

    @NotNull
    private String type;

    @NotNull
    private String experienceLowerBound;

    private String experienceUpperBound;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Company company;

    @JsonIgnore
    @OneToMany(mappedBy = "offer")
    private List<Application> applications;
}
