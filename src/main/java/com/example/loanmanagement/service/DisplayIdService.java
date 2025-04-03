package com.example.loanmanagement.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class DisplayIdService {

    @PersistenceContext
    private EntityManager entityManager;

    private static final ConcurrentHashMap<String, String> PREFIX_MAP = new ConcurrentHashMap<>();

    static {
        PREFIX_MAP.put("loan", "LOAN-");
        PREFIX_MAP.put("member", "MEMBER-");
        PREFIX_MAP.put("document", "DOC-");
    }

    @Transactional
    public String getNextDisplayId(String type) {
        String sequenceName = "seq_display_id_" + type;
        Long nextVal = ((Number) entityManager.createNativeQuery("SELECT nextval('" + sequenceName + "')")
                .getSingleResult()).longValue();

        String prefix = PREFIX_MAP.getOrDefault(type.toLowerCase(), type.toUpperCase() + "-");
        return prefix + String.format("%04d", nextVal);
    }
}
