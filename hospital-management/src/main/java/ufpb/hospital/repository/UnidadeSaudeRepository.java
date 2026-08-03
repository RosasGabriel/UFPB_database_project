package ufpb.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufpb.hospital.model.UnidadeSaude;

public interface UnidadeSaudeRepository extends JpaRepository<UnidadeSaude, Integer> {
}