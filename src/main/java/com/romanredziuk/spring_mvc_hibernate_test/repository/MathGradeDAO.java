package com.romanredziuk.spring_mvc_hibernate_test.repository;

import com.romanredziuk.spring_mvc_hibernate_test.models.MathGrade;
import org.springframework.data.repository.CrudRepository;

public interface MathGradeDAO extends CrudRepository<MathGrade, Integer> {

    public Iterable<MathGrade> findGradeByStudentId(int id);
}
