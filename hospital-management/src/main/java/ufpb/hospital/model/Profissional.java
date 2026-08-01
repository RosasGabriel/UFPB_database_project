package ufpb.hospital.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "profissional")
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class Profissional extends Pessoa {

    @Column(name = "crm", nullable = false, unique = true, length = 20)
    private String crm;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "especialidade", nullable = false, length = 100)
    private String especialidade;
}