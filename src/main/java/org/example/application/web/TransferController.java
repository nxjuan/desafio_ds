package org.example.application.web;

import org.example.application.usecase.ListTransfersUseCase;
import org.example.application.usecase.ScheduleTransferUseCase;
import org.example.domain.model.Transfer;
import org.example.domain.ports.TransferRepository;
import org.example.domain.policy.FeePolicy;
import org.example.application.web.dto.ScheduleTransferInput;
import org.example.application.web.dto.TransferResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

    private final ScheduleTransferUseCase scheduleUseCase;
    private final ListTransfersUseCase listUseCase;

    public TransferController(TransferRepository repository, FeePolicy feePolicy) {
        this.scheduleUseCase = new ScheduleTransferUseCase(repository, feePolicy);
        this.listUseCase = new ListTransfersUseCase(repository);
    }

    @PostMapping
    public TransferResponse schedule(@Valid @RequestBody ScheduleTransferInput in) {
        Transfer t = scheduleUseCase.execute(
                in.getSourceAccount(),
                in.getDestinationAccount(),
                in.getAmount(),
                in.getTransferDate());

        return toResponse(t);
    }

    @GetMapping
    public List<TransferResponse> list() {
        return listUseCase.execute().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private TransferResponse toResponse(Transfer t) {
        TransferResponse r = new TransferResponse();
        r.id = t.getId();
        r.sourceAccount = t.getSource().value();
        r.destinationAccount = t.getDestination().value();
        r.amount = t.getAmount();
        r.fee = t.getFee();
        r.total = t.total();
        r.scheduledDate = t.getScheduledDate();
        r.transferDate = t.getTransferDate();
        r.status = t.getStatus().name();
        return r;
    }
}