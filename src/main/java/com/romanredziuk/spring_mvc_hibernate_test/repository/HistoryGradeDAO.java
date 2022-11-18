package com.romanredziuk.spring_mvc_hibernate_test.repository;

import com.romanredziuk.spring_mvc_hibernate_test.models.HistoryGrade;
import com.romanredziuk.spring_mvc_hibernate_test.models.ScienceGrade;
import org.springframework.data.repository.CrudRepository;

public interface HistoryGradeDAO extends CrudRepository<HistoryGrade, Integer> {

    public Iterable<HistoryGrade> findGradeByStudentId(int id);
}
