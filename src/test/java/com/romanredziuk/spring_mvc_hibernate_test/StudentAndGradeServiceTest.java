package com.romanredziuk.spring_mvc_hibernate_test;

import com.romanredziuk.spring_mvc_hibernate_test.models.*;
import com.romanredziuk.spring_mvc_hibernate_test.repository.HistoryGradeDAO;
import com.romanredziuk.spring_mvc_hibernate_test.repository.MathGradeDAO;
import com.romanredziuk.spring_mvc_hibernate_test.repository.ScienceGradeDAO;
import com.romanredziuk.spring_mvc_hibernate_test.repository.StudentDAO;
import com.romanredziuk.spring_mvc_hibernate_test.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    StudentAndGradeService studentService;

    @Autowired
    StudentDAO studentDAO;

    @Autowired
    MathGradeDAO mathGradeDAO;

    @Autowired
    ScienceGradeDAO scienceGradeDAO;

    @Autowired
    HistoryGradeDAO historyGradeDAO;

    @BeforeEach
    public void setupDatabase(){
        jdbc.execute("insert into student(id,firstname,lastname,email_address) " +
                "values (1, 'Roman', 'Redziuk', 'roman.redziuk@gmail.com')");

        jdbc.execute("insert into math_grade(id,student_id,grade) values (1,1,100.00)");

        jdbc.execute("insert into science_grade(id,student_id,grade) values (1,1,100.00)");

        jdbc.execute("insert into history_grade(id,student_id,grade) values (1,1,100.00)");
    }

    @Test
    public void createStudentService(){

        studentService.createStudent("George", "Bush", "george.bush@gmail.com");

        CollegeStudent student = studentDAO.findByEmailAddress("george.bush@gmail.com");

        assertEquals("george.bush@gmail.com", student.getEmailAddress(), "find by email");
    }

    @Test
    public void isStudentNullCheck(){

        assertTrue(studentService.checkIfStudentIsNull(1));

        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    public void deleteStudentService() {

        Optional<CollegeStudent> deletedCollegeStudent = studentDAO.findById(1);
        Optional<MathGrade> deletedMathGrade = mathGradeDAO.findById(1);
        Optional<ScienceGrade> deletedScienceGrade = scienceGradeDAO.findById(1);
        Optional<HistoryGrade> deletedHistoryGrade = historyGradeDAO.findById(1);

        assertTrue(deletedCollegeStudent.isPresent(), "Return True");
        assertTrue(deletedMathGrade.isPresent());
        assertTrue(deletedScienceGrade.isPresent());
        assertTrue(deletedHistoryGrade.isPresent());

        studentService.deleteStudent(1);

        deletedMathGrade = mathGradeDAO.findById(1);
        deletedScienceGrade = scienceGradeDAO.findById(1);
        deletedHistoryGrade = historyGradeDAO.findById(1);

        deletedCollegeStudent = studentDAO.findById(1);

        assertFalse(deletedCollegeStudent.isPresent(), "Return False");
        assertFalse(deletedMathGrade.isPresent());
        assertFalse(deletedScienceGrade.isPresent());
        assertFalse(deletedHistoryGrade.isPresent());

    }

    @Sql("/insertData.sql")
    @Test
    public void getGradebookService(){
        Iterable<CollegeStudent> iterableCollegeStudent = studentService.getGradebook();

        List<CollegeStudent> collegeStudentList = new ArrayList<>();

        for (CollegeStudent collegeStudent: iterableCollegeStudent
             ) {
            collegeStudentList.add(collegeStudent);
        }

        assertEquals(5, collegeStudentList.size());
    }

    @Test
    public void createGradeService(){
        assertTrue(studentService.createGrade(80.50, 1, "math"));
        assertTrue(studentService.createGrade(80.50, 1, "science"));
        assertTrue(studentService.createGrade(80.50, 1, "history"));

        Iterable<MathGrade> mathGrades = mathGradeDAO.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDAO.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradeDAO.findGradeByStudentId(1);

        assertTrue(((Collection<MathGrade>) mathGrades).size() == 2, "Student has math grades.");
        assertTrue(((Collection<ScienceGrade>) scienceGrades).size() == 2, "Student has science grades.");
        assertTrue(((Collection<HistoryGrade>) historyGrades).size() == 2, "Student has history grades.");
    }

    @Test
    public void createGradeServiceReturnFalse(){
        assertFalse(studentService.createGrade(105, 1, "math"));
        assertFalse(studentService.createGrade(-5, 1, "math"));
        assertFalse(studentService.createGrade(80.50, 2, "math"));
        assertFalse(studentService.createGrade(80.50, 2, "literature"));
    }

    @Test
    public void deleteGradeService() {
        assertEquals(1,studentService.deleteGrade(1, "math"), "Returns student id after delete");

        assertEquals(1,studentService.deleteGrade(1, "science"), "Returns student id after delete");

        assertEquals(1,studentService.deleteGrade(1, "history"), "Returns student id after delete");
    }

    @Test
    public void deleteGradeServiceReturnStudentIdOfZero(){
        assertEquals(0, studentService.deleteGrade(0, "science"), "No student should have 0");
        assertEquals(0, studentService.deleteGrade(1, "literature"), "No student should have literature class");
    }

    @Test
    public void studentInformation(){
        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(1);

        assertNotNull(gradebookCollegeStudent);
        assertEquals(1, gradebookCollegeStudent.getId());
        assertEquals("Roman", gradebookCollegeStudent.getFirstname());
        assertEquals("Redziuk", gradebookCollegeStudent.getLastname());
        assertEquals("roman.redziuk@gmail.com", gradebookCollegeStudent.getEmailAddress());
        assertTrue(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size()==1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size()==1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size()==1);
    }

    @Test
    public void studentInformationServiceReturnNull(){
        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(0);
        assertNull(gradebookCollegeStudent);
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("delete from student");
        jdbc.execute("delete from math_grade");
        jdbc.execute("delete from science_grade");
        jdbc.execute("delete from history_grade");

    }
}
