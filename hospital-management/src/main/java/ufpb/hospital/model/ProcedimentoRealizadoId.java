package ufpb.hospital.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProcedimentoRealizadoId implements Serializable {

    @Column(name = "id_atendimento")
    private Integer idAtendimento;

    @Column(name = "id_procedimento")
    private Integer idProcedimento;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcedimentoRealizadoId that = (ProcedimentoRealizadoId) o;
        return Objects.equals(idAtendimento, that.idAtendimento) &&
               Objects.equals(idProcedimento, that.idProcedimento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAtendimento, idProcedimento);
    }
}