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
@Table(name = "unidade_saude")
public class UnidadeSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidade")
    private Long idUnidade;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "tipo_unidade", nullable = false, length = 50)
    private String tipoUnidade;

    @Column(name = "endereco", nullable = false)
    private String endereco;
}