package ufpb.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufpb.hospital.model.Residente;

public interface ResidenteRepository extends JpaRepository<Residente, Integer> {
}