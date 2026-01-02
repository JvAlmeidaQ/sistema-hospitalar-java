package br.ufjf.dcc025.model.repository;

import java.util.List;

public interface Repository<T> {

    String DIRECTORY = "data";
    public void save(List<T> itens);
    public List<T> findAll();
}