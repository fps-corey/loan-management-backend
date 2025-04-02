package com.example.loanmanagement.entity;

import com.example.loanmanagement.entity.enums.EducationLevel;
import com.example.loanmanagement.entity.enums.MaritalStatus;
import com.example.loanmanagement.entity.enums.ResidentialStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "members")
@Getter
@Setter
public class Member extends BaseEntity {
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    
    @Column(nullable = false)
    private String address;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaritalStatus maritalStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EducationLevel educationLevel;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResidentialStatus residentialStatus;
    
    @Column(nullable = false)
    private Integer numberOfDependents;
    
    @Column(nullable = false)
    private String occupation;
    
    @Column(nullable = false)
    private String employerName;
    
    @Column(nullable = false)
    private Double monthlyIncome;
    
    @Column(nullable = false)
    private String bankName;
    
    @Column(nullable = false)
    private String bankAccountNumber;
    
    @Column(nullable = false)
    private String bankSortCode;
    
    @Column(nullable = false)
    private String nextOfKinName;
    
    @Column(nullable = false)
    private String nextOfKinPhone;
    
    @Column(nullable = false)
    private String nextOfKinRelationship;
    
    @Column
    private String spouseName;
    
    @Column
    private String spouseOccupation;
    
    @Column
    private Double spouseMonthlyIncome;
    
    @Column(nullable = false)
    private String preferredLanguage;
    
    @Column(nullable = false)
    private String membershipChannel;
    
    @Column(nullable = false)
    private String referralSource;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Loan> loans = new HashSet<>();
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Document> documents = new HashSet<>();
    
    @Column(nullable = false)
    private boolean active = true;
} 