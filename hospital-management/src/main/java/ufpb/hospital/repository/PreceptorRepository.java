package ufpb.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufpb.hospital.model.Preceptor;


public interface PreceptorRepository extends JpaRepository<Preceptor, Integer> {
}