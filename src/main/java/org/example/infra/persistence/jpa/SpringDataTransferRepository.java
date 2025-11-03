package org.example.infra.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface SpringDataTransferRepository extends JpaRepository<JpaTransferEntity, UUID> {
}