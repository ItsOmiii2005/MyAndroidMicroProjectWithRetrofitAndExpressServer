package com.example.mymicroprojectmad.apiservices;

import com.example.mymicroprojectmad.student.Student;
import com.example.mymicroprojectmad.student.StudentResponse;

import retrofit2.Call;
import retrofit2.Callback;


public class StudentApiService extends BaseApiService {

    public void addStudent(Student student, Callback<Student> callback) {
        Call<Student> call = apiService.addStudent(student);
        call.enqueue(callback);
    }

    public void getStudent(String enrollmentNo, String token, Callback<StudentResponse> callback) {
        Call<StudentResponse> call = apiService.getStudent(enrollmentNo, "Bearer " + token);
        call.enqueue(callback);
    }

    public void deleteStudent(String enrollmentNo, Callback<Void> callback) {
        Call<Void> call = apiService.deleteStudent(enrollmentNo);
        call.enqueue(callback);
    }

    public void updateStudent(String enrollmentNo, Student studentToUpdate, Callback<Void> callback) {
        Call<Void> call = apiService.updateStudent(enrollmentNo, studentToUpdate);
        call.enqueue(callback);
    }
}
