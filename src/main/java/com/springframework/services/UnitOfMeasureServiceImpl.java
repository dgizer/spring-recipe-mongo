package com.springframework.services;

import com.springframework.commands.UnitOfMeasureCommand;
import com.springframework.converters.UnitOfMeasureToCommand;
import com.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOfMeasureReactiveRepository uomRepo;
    private final UnitOfMeasureToCommand uomToCommand;

    public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository uomRepo,
                                    UnitOfMeasureToCommand uomToCommand) {
        this.uomRepo = uomRepo;
        this.uomToCommand = uomToCommand;
    }

    @Override
    public Flux<UnitOfMeasureCommand> listAllUoms() {
        return uomRepo
                .findAll()
                .map(uomToCommand::convert);
    }
}
