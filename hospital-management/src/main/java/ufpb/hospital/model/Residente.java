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
@Table(name = "residente")
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class Residente extends Pessoa {

    @Column(name = "ano_residencia", nullable = false)
    private Integer anoResidencia;
}