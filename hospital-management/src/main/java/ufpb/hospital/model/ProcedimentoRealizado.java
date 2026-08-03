package ufpb.hospital.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "procedimento_realizado")
public class ProcedimentoRealizado {

    @EmbeddedId
    private ProcedimentoRealizadoId id = new ProcedimentoRealizadoId();

    @ManyToOne
    @MapsId("idAtendimento")
    @JoinColumn(name = "id_atendimento")
    private Atendimento atendimento;

    @ManyToOne
    @MapsId("idProcedimento")
    @JoinColumn(name = "id_procedimento")
    private Procedimento procedimento;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "tempo_real_minutos", nullable = false)
    private Integer tempoRealMinutos;

    @Column(name = "observacao", length = 255)
    private String observacao;

    @Column(name = "pode_remover")
    private Boolean podeRemover = true;
}