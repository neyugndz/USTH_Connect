package com.usth_connect.vpn_server_backend_usth.service;

import com.usth_connect.vpn_server_backend_usth.dto.StudentDTO;
import com.usth_connect.vpn_server_backend_usth.entity.Student;
import com.usth_connect.vpn_server_backend_usth.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(student -> new StudentDTO(
                        student.getId(),
                        student.getFullName(),
                        student.getPassword(),
                        student.getEmail(),
                        student.getDob(),
                        student.getMajor(),
                        student.getGender(),
                        student.getPhoneNumber(),
                        student.getStudyYear()
                ))
                .toList();
    }

    public Optional<Student> updateStudent(String id, StudentDTO studentDTO) {
        return studentRepository.findById(id).map(existingStudent -> {
            existingStudent.setFullName(studentDTO.getFullName());
            existingStudent.setEmail(studentDTO.getEmail());
            existingStudent.setDob(studentDTO.getDob());
            existingStudent.setMajor(studentDTO.getMajor());
            existingStudent.setGender(studentDTO.getGender());
            existingStudent.setPhoneNumber(studentDTO.getPhoneNumber());
            existingStudent.setStudyYear(studentDTO.getStudyYear());
            return studentRepository.save(existingStudent);
        });
    }

    public Optional<Student> updateStudentPartially(String id, StudentDTO studentDTO) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();

            // Update only provided fields
            if (studentDTO.getPassword() != null) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                student.setPassword(encoder.encode(studentDTO.getPassword()));
            }
            if (studentDTO.getPhoneNumber() != null) {
                student.setPhoneNumber(studentDTO.getPhoneNumber());
            }
            // Update other fields as needed
            studentRepository.save(student);
            return Optional.of(student);
        }
        return Optional.empty();
    }


    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(id);
    }

    public void deleteStudent(String id) {
        studentRepository.deleteById(id);
    }
}
