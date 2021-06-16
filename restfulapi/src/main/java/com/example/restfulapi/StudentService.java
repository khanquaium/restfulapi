package com.example.restfulapi;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public void addStudent(Student student){

        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if(studentOptional.isPresent()){
            throw new IllegalStateException("Student already exists");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId){
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if(studentOptional.isEmpty()){
            throw new IllegalStateException(("Student not found"));
        }
        studentRepository.deleteById(studentId);

    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email, LocalDate dob){
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("Student not found"));

        if(name != null){
            if(!student.getName().equals(name)){
                student.setName(name);
            }
        }
        Optional<Student> studentByEmail = studentRepository.findStudentByEmail(email);
        if(email != null){
            if (studentByEmail.isPresent()) {
                throw new IllegalStateException("Email address already in use by another student");
            } else if (!student.getEmail().equals(email)) {
                student.setEmail(email);
            }
        }
        if(dob !=null){
            if (student.getDob() != dob) {
                student.setDob(dob);
            }
        }
    }



}
