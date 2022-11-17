package com.romanredziuk.spring_mvc_hibernate_test.repository;

import com.romanredziuk.spring_mvc_hibernate_test.models.CollegeStudent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface StudentDAO extends CrudRepository<CollegeStudent, Integer> {

    public CollegeStudent findByEmailAddress(String emailAddress);
}
