package ufpb.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufpb.hospital.model.Procedimento;


public interface ProcedimentoRepository extends JpaRepository<Procedimento, Integer> {
}