package com.springframework.services;

import com.springframework.commands.UnitOfMeasureCommand;
import com.springframework.converters.UnitOfMeasureToCommand;
import com.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOfMeasureRepository uomRepo;
    private final UnitOfMeasureToCommand uomToCommand;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository uomRepo,
                                    UnitOfMeasureToCommand uomToCommand) {
        this.uomRepo = uomRepo;
        this.uomToCommand = uomToCommand;
    }

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {
        return StreamSupport.stream(uomRepo.findAll()
                .spliterator(), false)
                .map(uomToCommand::convert)
                .collect(Collectors.toSet());
    }
}
