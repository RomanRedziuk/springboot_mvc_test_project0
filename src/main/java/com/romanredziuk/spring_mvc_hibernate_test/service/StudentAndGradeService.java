package com.romanredziuk.spring_mvc_hibernate_test.service;

import com.romanredziuk.spring_mvc_hibernate_test.models.CollegeStudent;
import com.romanredziuk.spring_mvc_hibernate_test.models.HistoryGrade;
import com.romanredziuk.spring_mvc_hibernate_test.models.MathGrade;
import com.romanredziuk.spring_mvc_hibernate_test.models.ScienceGrade;
import com.romanredziuk.spring_mvc_hibernate_test.repository.HistoryGradeDAO;
import com.romanredziuk.spring_mvc_hibernate_test.repository.MathGradeDAO;
import com.romanredziuk.spring_mvc_hibernate_test.repository.ScienceGradeDAO;
import com.romanredziuk.spring_mvc_hibernate_test.repository.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    StudentDAO studentDAO;

    @Autowired
    @Qualifier("mathGrades")
    MathGrade mathGrade;

    @Autowired
    @Qualifier("scienceGrades")
    ScienceGrade scienceGrade;

    @Autowired
    @Qualifier("historyGrades")
    HistoryGrade historyGrade;

    @Autowired
    MathGradeDAO mathGradeDAO;

    @Autowired
    ScienceGradeDAO scienceGradeDAO;

    @Autowired
    HistoryGradeDAO historyGradeDAO;

    public void createStudent(String firstname, String lastname, String emailAddress) {
        CollegeStudent student = new CollegeStudent(firstname, lastname, emailAddress);
        student.setId(0);

        studentDAO.save(student);
    }

    public boolean checkIfStudentIsNull(int id) {

        Optional<CollegeStudent> student = studentDAO.findById(id);
        if(student.isPresent()){
            return true;
        }
        return false;
    }

    public void deleteStudent(int id) {

        if(checkIfStudentIsNull(id)){
            studentDAO.deleteById(id);
        }
    }

    public Iterable<CollegeStudent> getGradebook() {
        Iterable<CollegeStudent> collegeStudents = studentDAO.findAll();
        return collegeStudents;
    }

    public boolean createGrade(double grade, int studentId, String gradeType) {

        if(!checkIfStudentIsNull(studentId)){
            return false;
        }

        if(grade>= 0&&grade<=100){
            if(gradeType.equals("math")){
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(studentId);
                mathGradeDAO.save(mathGrade);
                return true;
            }
            if(gradeType.equals("science")){
                scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(studentId);
                scienceGradeDAO.save(scienceGrade);
                return true;
            }

            if(gradeType.equals("history")){
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(studentId);
                historyGradeDAO.save(historyGrade);
                return true;
            }
        }

        return false;
    }
}
