package com.romanredziuk.spring_mvc_hibernate_test.repository;

import com.romanredziuk.spring_mvc_hibernate_test.models.ScienceGrade;
import org.springframework.data.repository.CrudRepository;

public interface ScienceGradeDAO extends CrudRepository<ScienceGrade, Integer> {

    public Iterable<ScienceGrade> findGradeByStudentId(int id);
}
