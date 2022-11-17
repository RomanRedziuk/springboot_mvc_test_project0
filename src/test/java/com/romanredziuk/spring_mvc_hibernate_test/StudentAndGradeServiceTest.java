package com.romanredziuk.spring_mvc_hibernate_test;

import com.romanredziuk.spring_mvc_hibernate_test.models.CollegeStudent;
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

    @BeforeEach
    public void setupDatabase(){
        jdbc.execute("insert into student(id,firstname,lastname,email_address) " +
                "values (1, 'Roman', 'Redziuk', 'roman.redziuk@gmail.com')");
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

        assertTrue(deletedCollegeStudent.isPresent(), "Return True");

        studentService.deleteStudent(1);

        deletedCollegeStudent = studentDAO.findById(1);

        assertFalse(deletedCollegeStudent.isPresent(), "Return False");
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

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("delete from student");
    }
}
