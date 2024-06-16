package com.example.backEnd.repositories;


import com.example.backEnd.datatables.qRepository.QDataTablesRepository;
import com.example.backEnd.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, QDataTablesRepository<Student, Long> {
}
