package com.example.backEnd.repositories;

import com.example.backEnd.datatables.qRepository.QDataTablesRepository;
import com.example.backEnd.models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository
    extends JpaRepository<Professor, Long>, QDataTablesRepository<Professor, Long> {}
