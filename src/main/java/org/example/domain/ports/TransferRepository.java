package org.example.domain.ports;

import org.example.domain.model.Transfer;

import java.util.List;
public interface TransferRepository {
    Transfer save(Transfer transfer);
    List<Transfer> findAll();
}