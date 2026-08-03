package ufpb.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufpb.hospital.model.ProcedimentoRealizado;
import ufpb.hospital.model.ProcedimentoRealizadoId;


public interface ProcedimentoRealizadoRepository extends JpaRepository<ProcedimentoRealizado, ProcedimentoRealizadoId> {
}