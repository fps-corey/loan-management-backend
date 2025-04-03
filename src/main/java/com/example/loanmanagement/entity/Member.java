package com.example.loanmanagement.entity;

import com.example.loanmanagement.entity.enums.EducationLevel;
import com.example.loanmanagement.entity.enums.MaritalStatus;
import com.example.loanmanagement.entity.enums.ResidentialStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "display_id", unique = true, nullable = false)
    private String displayId;

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
    private String gender;

    @Column(nullable = false)
    private String nationalId;

    @Column
    private Integer creditScore;

    @Column(nullable = false)
    private boolean documentsVerified = false;

    @Column(nullable = false)
    private String addressStreet;

    @Column(nullable = false)
    private String addressCity;

    @Column(nullable = false)
    private String addressRegion;

    @Column(nullable = false)
    private String addressPostalCode;

    @Column(nullable = false)
    private String addressCountry;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private String lastModifiedBy;

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
    @JsonManagedReference // For safe serialization
    private Set<Loan> loans = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Document> documents = new HashSet<>();

    @Column(nullable = false)
    private boolean active = true;
}
