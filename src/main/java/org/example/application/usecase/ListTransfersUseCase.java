package org.example.application.usecase;

import org.example.domain.model.Transfer;
import org.example.domain.ports.TransferRepository;

import java.util.List;
public class ListTransfersUseCase {
    private final TransferRepository repository;

    public ListTransfersUseCase(TransferRepository repository) {
        this.repository = repository;
    }
    public List<Transfer> execute() { return repository.findAll(); }
}