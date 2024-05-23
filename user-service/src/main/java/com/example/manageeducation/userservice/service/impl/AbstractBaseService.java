package com.example.manageeducation.userservice.service.impl;

import com.example.manageeducation.exception.InternalServerException;
import com.example.manageeducation.exception.ResourceNotFoundException;
import com.example.manageeducation.mapper.AbstractMapper;
import com.example.manageeducation.service.BaseService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractBaseService<E, D, ID> implements BaseService<D, ID> {
    JpaRepository<E, ?> repository;
    AbstractMapper<E, D> mapper;
    Function<ID, Optional<E>> findByIdMethod;

    protected AbstractBaseService(JpaRepository<E, ?> repository, AbstractMapper<E, D> mapper, Function<ID, Optional<E>> findByIdMethod) {
        this.repository = repository;
        this.mapper = mapper;
        this.findByIdMethod = findByIdMethod;
    }

    @Override
    public D create(D dto) {
        try {
            E entity = mapper.mapDtoToEntity(dto);
            entity = repository.save(entity);
            return mapper.mapEntityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException(e);
        }
    }

    @Override
    public Optional<D> getById(ID id) {
        return mapper.mapOptEntityToOptDto(findByIdMethod.apply(id));
    }

    @Override
    public List<D> getAll() {
        return mapper.mapEntitiesToDtoes(repository.findAll());
    }

    @Override
    public D update(ID id, D dto) {
        Optional<E> optionalEntity = findByIdMethod.apply(id);
        if (optionalEntity.isPresent())
            throw new ResourceNotFoundException();

        try {
            E entity = optionalEntity.get();
            mapper.mapDtoToEntity(dto, entity);
            return mapper.mapEntityToDto(repository.save(entity));
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public void deleteById(ID id) {
        Optional<E> optionalEntity = findByIdMethod.apply(id);
        if (optionalEntity.isPresent())
            throw new ResourceNotFoundException();

        try {
            repository.delete(optionalEntity.get());
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }
}
