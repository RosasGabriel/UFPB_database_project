package ufpb.hospital.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "paciente")
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class Paciente extends Pessoa {

    @Column(name = "num_convenio", length = 50)
    private String numConvenio;

    @Column(name = "alergias", length = 255)
    private String alergias;

    @Column(name = "grupo_sanguineo", length = 5)
    private String grupoSanguineo;
}