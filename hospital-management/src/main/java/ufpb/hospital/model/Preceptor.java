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
@Table(name = "preceptor")
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class Preceptor extends Pessoa {

    @Column(name = "titulacao", nullable = false, length = 50)
    private String titulacao;
}