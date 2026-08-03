package ufpb.hospital.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "procedimento")
public class Procedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_procedimento")
    private Integer idProcedimento;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "tempo_medio_execucao", nullable = false)
    private Integer tempoMedioExecucao;
}