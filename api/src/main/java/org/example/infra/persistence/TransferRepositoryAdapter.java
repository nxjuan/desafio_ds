package org.example.infra.persistence;

import org.example.domain.model.Transfer;
import org.example.domain.ports.TransferRepository;
import org.example.infra.persistence.jpa.JpaTransferEntity;
import org.example.infra.persistence.jpa.SpringDataTransferRepository;
import org.example.infra.persistence.mapper.TransferMapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.infra.persistence.mapper.TransferMapper.*;

@Repository
public class TransferRepositoryAdapter implements TransferRepository{
    private final SpringDataTransferRepository jpa;

    public TransferRepositoryAdapter(SpringDataTransferRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Transfer save(Transfer transfer) {
        JpaTransferEntity saved = jpa.save(toJpa(transfer));
        return toDomain(saved);
    }

    @Override
    public List<Transfer> findAll() {
        return jpa.findAll().stream().map(TransferMapper::toDomain).collect(Collectors.toList());
    }
}
