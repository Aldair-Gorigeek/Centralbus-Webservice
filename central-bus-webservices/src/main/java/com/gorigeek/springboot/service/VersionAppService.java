package com.gorigeek.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gorigeek.springboot.entity.VersionApp;
import com.gorigeek.springboot.repository.VersionAppRepository;

@Transactional
@Service
public class VersionAppService {
    
    @Autowired
    private VersionAppRepository repo;
    
    public List<VersionApp> obtenerVersion(String id){
        return repo.obtenerVersionApp(id);
    }

}
