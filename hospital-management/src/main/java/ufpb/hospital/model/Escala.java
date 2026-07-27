package ufpb.hospital.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "escala")
public class Escala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_escala")
    private Long idEscala;

    @ManyToOne
    @JoinColumn(name = "id_unidade", nullable = false)
    private UnidadeSaude unidadeSaude;

    @ManyToOne
    @JoinColumn(name = "id_residente", nullable = false)
    private Residente residente;

    @ManyToOne
    @JoinColumn(name = "id_preceptor", nullable = false)
    private Preceptor preceptor;

    @Column(name = "dia_semana", nullable = false, length = 20)
    private String diaSemana;

    @Column(name = "turno", nullable = false, length = 20)
    private String turno;
}