package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.Developer;

import java.util.List;

public interface DeveloperService {

    Developer create(Developer developer);

    Developer findById(int id);

    Developer findByName(String name);

    List<Developer> findAll();

    void update(Developer developer);

    void deleteById(int id);

}
