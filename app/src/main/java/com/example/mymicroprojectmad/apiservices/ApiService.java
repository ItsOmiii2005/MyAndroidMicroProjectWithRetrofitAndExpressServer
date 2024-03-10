package com.example.mymicroprojectmad.apiservices;
import com.example.mymicroprojectmad.auth.AuthToken;
import com.example.mymicroprojectmad.auth.RegisterUser;
import com.example.mymicroprojectmad.auth.ResponseMessage;
import com.example.mymicroprojectmad.student.Student;
import com.example.mymicroprojectmad.student.StudentResponse;
import com.example.mymicroprojectmad.auth.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.GET;
public interface ApiService {



    @POST("/register")
    Call<ResponseMessage> register(@Body RegisterUser user);

    @POST("/login")
    Call<AuthToken> login(@Body User user);

    @POST("/addstudent")
    Call<Student> addStudent(@Body Student student);

    @GET("getStudent/{enrollmentNo}")
    Call<StudentResponse> getStudent(@Path("enrollmentNo") String enrollmentNo, @Header("Authorization") String token);
    @GET("/validatetoken")
    Call<ResponseMessage> validateToken( @Header("Authorization") String token);


    @DELETE("deletestudent/{enrollmentNo}")
    Call<Void> deleteStudent(@Path("enrollmentNo") String enrollmentNo);
@PUT("/updatestudent/{enrollmentNo}")
    Call<Void> updateStudent(@Path("enrollmentNo") String enrollmentNo, @Body Student studentToUpdate);

@POST("/loginstd")
    Call<AuthToken> loginStd(@Body User user);
}
