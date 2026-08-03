package ufpb.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufpb.hospital.model.Paciente;


public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
}