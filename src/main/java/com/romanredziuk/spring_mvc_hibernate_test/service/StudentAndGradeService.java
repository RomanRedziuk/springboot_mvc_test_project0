package com.romanredziuk.spring_mvc_hibernate_test.service;

import com.romanredziuk.spring_mvc_hibernate_test.models.*;
import com.romanredziuk.spring_mvc_hibernate_test.repository.HistoryGradeDAO;
import com.romanredziuk.spring_mvc_hibernate_test.repository.MathGradeDAO;
import com.romanredziuk.spring_mvc_hibernate_test.repository.ScienceGradeDAO;
import com.romanredziuk.spring_mvc_hibernate_test.repository.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private StudentGrades studentGrades;

    public void createStudent(String firstname, String lastname, String emailAddress) {
        CollegeStudent student = new CollegeStudent(firstname, lastname, emailAddress);
        student.setId(0);

        studentDAO.save(student);
    }

    public boolean checkIfStudentIsNull(int id) {

        Optional<CollegeStudent> student = studentDAO.findById(id);
        if (student.isPresent()) {
            return true;
        }
        return false;
    }

    public void deleteStudent(int id) {

        if (checkIfStudentIsNull(id)) {
            studentDAO.deleteById(id);
            mathGradeDAO.deleteStudentById(id);
            scienceGradeDAO.deleteStudentById(id);
            historyGradeDAO.deleteStudentById(id);
        }
    }

    public Iterable<CollegeStudent> getGradebook() {
        Iterable<CollegeStudent> collegeStudents = studentDAO.findAll();
        return collegeStudents;
    }

    public boolean createGrade(double grade, int studentId, String gradeType) {

        if (!checkIfStudentIsNull(studentId)) {
            return false;
        }

        if (grade >= 0 && grade <= 100) {
            if (gradeType.equals("math")) {
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(studentId);
                mathGradeDAO.save(mathGrade);
                return true;
            }
            if (gradeType.equals("science")) {
                scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(studentId);
                scienceGradeDAO.save(scienceGrade);
                return true;
            }

            if (gradeType.equals("history")) {
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(studentId);
                historyGradeDAO.save(historyGrade);
                return true;
            }
        }

        return false;
    }

    public int deleteGrade(int id, String gradeType) {
        int studentId = 0;

        if (gradeType.equals("math")) {
            Optional<MathGrade> grade = mathGradeDAO.findById(id);
            if (!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            mathGradeDAO.deleteById(id);
        }
        if (gradeType.equals("science")) {
            Optional<ScienceGrade> grade = scienceGradeDAO.findById(id);
            if (!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            scienceGradeDAO.deleteById(id);
        }
        if (gradeType.equals("history")) {
            Optional<HistoryGrade> grade = historyGradeDAO.findById(id);
            if (!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            historyGradeDAO.deleteById(id);
        }

        return studentId;
    }

    public GradebookCollegeStudent studentInformation(int id) {

        if(!checkIfStudentIsNull(id)){
            return null;
        }

        Optional<CollegeStudent> student = studentDAO.findById(id);
        Iterable<MathGrade> mathGrades = mathGradeDAO.findGradeByStudentId(id);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDAO.findGradeByStudentId(id);
        Iterable<HistoryGrade> historyGrades = historyGradeDAO.findGradeByStudentId(id);

        List<Grade> mathGradeList = new ArrayList<>();
        mathGrades.forEach(mathGradeList::add);

        List<Grade> scienceGradeList = new ArrayList<>();
        scienceGrades.forEach(scienceGradeList::add);

        List<Grade> historyGradeList = new ArrayList<>();
        historyGrades.forEach(historyGradeList::add);

        studentGrades.setMathGradeResults(mathGradeList);
        studentGrades.setScienceGradeResults(scienceGradeList);
        studentGrades.setHistoryGradeResults(historyGradeList);

        GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(student.get().getId(),
                student.get().getFirstname(), student.get().getLastname(),
                student.get().getEmailAddress(),
                studentGrades);

        return gradebookCollegeStudent;
    }
}
