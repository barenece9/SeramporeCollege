package com.lnsel.seramporecollege.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by apps2 on 5/5/2017.
 */
public class SharedManagerUtil {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String PREF_NAME = "SerampurPref";

    // All Shared Preferences Keys
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_EMPLOYEE_ID = "employeeId";
    public static final String KEY_USER_DEPARTMENT = "userDepartment";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_FIRST_NAME = "userFirstName";
    public static final String KEY_USER_LAST_NAME = "userLastName";
    public static final String KEY_USER_EMAIL = "userEmail";
    public static final String KEY_USER_CONTACT_NO = "userContactNo";
    public static final String KEY_USER_DESIGNATION = "userDesignation";
    public static final String KEY_USER_TYPE = "userType";
    public static final String KEY_USER_ACADEMIC_DEPARTMENT_ID = "userAcademicDepartmentId";
    public static final String KEY_USER_INSTITUTE_ID = "userInstituteId";
    public static final String KEY_USER_ACAD_DEPARTMENT = "userAcad_Department";



    //student module............................
    public static final String KEY_STUDENT_LOGIN_ID = "login_id";
    public static final String KEY_STUDENT_ID = "id";
    public static final String KEY_STUDENT_NAME = "name";
    public static final String KEY_STUDENT_UNIT_ID = "unit_id";
    public static final String KEY_STUDENT_ACAD_INST_ID = "academic_institute_id";
    public static final String KEY_STUDENT_ACAD_DEPT_NAME="academic_department_name";
    public static final String KEY_STUDENT_ACAD_DEPT_ID = "academic_department_id";

    public static final String KEY_STUDENT_ACAD_SECTION_NAME="academic_department_section_name";
    public static final String KEY_STUDENT_ACAD_SECTION_ID = "academic_department_section_id";

    public static final String KEY_STUDENT_ACAD_COURSE_ID = "academic_course_id";
    public static final String KEY_STUDENT_UNV_REG_NO = "university_registration_no";
    public static final String KEY_STUDENT_CLG_REG_NO = "college_registration_no";
    public static final String KEY_STUDENT_CURRENT_YEAR="current_year";
    public static final String KEY_STUDENT_ROLL_NO = "roll_no";
    public static final String KEY_STUDENT_SECTION = "section";
    public static final String KEY_STUDENT_DOB = "date_of_birth";

    public static final String KEY_STUDENT_SEX = "sex";
    public static final String KEY_STUDENT_PHONE = "phone";
    public static final String KEY_STUDENT_EMAIL = "email";
    public static final String KEY_STUDENT_IMAGE = "image";

    // Constructor
    public SharedManagerUtil(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void createTeacherLoginSession(
            String userId,
            String userName,
            String userType){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_TYPE, userType);

        editor.commit();
    }

    public void updateTeacherProfile(String employeeId,
                              String userName,
                              String userDesignation,
                              String userDepartment,
                              String academic_department_id,
                              String institute_id,
                              String academic_department_name){
        editor.putString(KEY_EMPLOYEE_ID,employeeId);
        editor.putString(KEY_USER_DEPARTMENT, userDepartment);
        editor.putString(KEY_USER_FIRST_NAME, userName);
        editor.putString(KEY_USER_DESIGNATION, userDesignation);

        editor.putString(KEY_USER_ACADEMIC_DEPARTMENT_ID, academic_department_id);
        editor.putString(KEY_USER_INSTITUTE_ID, institute_id);
        editor.putString(KEY_USER_ACAD_DEPARTMENT, academic_department_name);
        editor.commit();
    }


    public void createStudentLoginSession(
            String login_id,
            String userName,
            String user_type){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_STUDENT_LOGIN_ID, login_id);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_TYPE,user_type);

        editor.commit();
    }


    public void updateStudentProfile(String id,
                                     String name,
                                     String unit_id,
                                     String academic_institute_id,
                                     String academic_department_id,
                                     String academic_department_name,
                                     String academic_section_id,
                                     String section_name,
                                     String academic_course_id,
                                     String university_registration_no,
                                     String college_registration_no,
                                     String current_year,
                                     String roll_no,
                                     String section,
                                     String date_of_birth,
                                     String sex,
                                     String phone,
                                     String email,
                                     String image){

        editor.putString(KEY_STUDENT_ID, id);
        editor.putString(KEY_STUDENT_NAME,name);
        editor.putString(KEY_STUDENT_UNIT_ID, unit_id);
        editor.putString(KEY_STUDENT_ACAD_INST_ID, academic_institute_id);
        editor.putString(KEY_STUDENT_ACAD_DEPT_ID, academic_department_id);
        editor.putString(KEY_STUDENT_ACAD_DEPT_NAME, academic_department_name);

        editor.putString(KEY_STUDENT_ACAD_SECTION_ID, academic_section_id);
        editor.putString(KEY_STUDENT_ACAD_SECTION_NAME, section_name);

        editor.putString(KEY_STUDENT_ACAD_COURSE_ID, academic_course_id);
        editor.putString(KEY_STUDENT_UNV_REG_NO, university_registration_no);
        editor.putString(KEY_STUDENT_CLG_REG_NO, college_registration_no);
        editor.putString(KEY_STUDENT_CURRENT_YEAR,current_year);
        editor.putString(KEY_STUDENT_ROLL_NO, roll_no);
        editor.putString(KEY_STUDENT_SECTION, section);

        editor.putString(KEY_STUDENT_DOB, date_of_birth);
        editor.putString(KEY_STUDENT_SEX, sex);
        editor.putString(KEY_STUDENT_PHONE, phone);
        editor.putString(KEY_STUDENT_EMAIL, email);
        editor.putString(KEY_STUDENT_IMAGE, image);
        editor.commit();
    }


    public void logoutUser(){
        editor.clear();
        editor.commit();
    }



    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getKeyUserID(){
        return pref.getString(KEY_USER_ID, null);
    }

    public String getKeyEmployeeId(){
        return pref.getString(KEY_EMPLOYEE_ID, null);
    }

    public String getKeyUserDepartment(){
        return pref.getString(KEY_USER_DEPARTMENT, null);
    }

    public String getKeyUserName(){
        return pref.getString(KEY_USER_NAME, null);
    }

    public String getKeyUserFirstName(){
        return pref.getString(KEY_USER_FIRST_NAME, null);
    }

    public String getKeyUserLastName(){
        return pref.getString(KEY_USER_LAST_NAME, null);
    }

    public String getKeyUserEmail(){
        return pref.getString(KEY_USER_EMAIL, null);
    }

    public String getKeyUserContactNo(){
        return pref.getString(KEY_USER_CONTACT_NO, null);
    }

    public String getKeyUserDesignation(){
        return pref.getString(KEY_USER_DESIGNATION, null);
    }

    public String getKeyUserType(){
        return pref.getString(KEY_USER_TYPE, null);
    }

    public String getKeyUserAcademicDepartmentId(){
        return pref.getString(KEY_USER_ACADEMIC_DEPARTMENT_ID, null);
    }

    public String getKeyUserInstituteId(){
        return pref.getString(KEY_USER_INSTITUTE_ID, null);
    }

    public String getKeyUserAcadDepartment(){
        return pref.getString(KEY_USER_ACAD_DEPARTMENT, null);
    }



    public String getKeyStudentId() {
        return pref.getString(KEY_STUDENT_ID, null);
    }

    public String getKeyStudentLoginId() {
        return pref.getString(KEY_STUDENT_LOGIN_ID, null);
    }

    public String getKeyStudentName() {
        return pref.getString(KEY_STUDENT_NAME, null);
    }

    public String getKeyStudentUnitId() {
        return pref.getString(KEY_STUDENT_UNIT_ID, null);
    }

    public String getKeyStudentAcadInstId() {
        return pref.getString(KEY_STUDENT_ACAD_INST_ID, null);
    }

    public String getKeyStudentAcadDeptId() {
        return pref.getString(KEY_STUDENT_ACAD_DEPT_ID, null);
    }
    public String getKeyStudentAcadDeptName() {
        return pref.getString(KEY_STUDENT_ACAD_DEPT_NAME, null);
    }

    public String getKeyStudentAcadSectionId() {
        return pref.getString(KEY_STUDENT_ACAD_SECTION_ID, null);
    }
    public String getKeyStudentAcadSectionName() {
        return pref.getString(KEY_STUDENT_ACAD_SECTION_NAME, null);
    }

    public String getKeyStudentAcadCourseId() {
        return pref.getString(KEY_STUDENT_ACAD_COURSE_ID, null);
    }

    public String getKeyStudentUnvRegNo() {
        return pref.getString(KEY_STUDENT_UNV_REG_NO, null);
    }

    public String getKeyStudentClgRegNo() {
        return pref.getString(KEY_STUDENT_CLG_REG_NO, null);
    }

    public String getKeyStudentRollNo() {
        return pref.getString(KEY_STUDENT_ROLL_NO, null);
    }

    public String getKeyStudentCurrentYear() {
        return pref.getString(KEY_STUDENT_CURRENT_YEAR, null);
    }

    public String getKeyStudentSection() {
        return pref.getString(KEY_STUDENT_SECTION, null);
    }

    public String getKeyStudentDob() {
        return pref.getString(KEY_STUDENT_DOB, null);
    }

    public String getKeyStudentSex() {
        return pref.getString(KEY_STUDENT_SEX, null);
    }

    public String getKeyStudentPhone() {
        return pref.getString(KEY_STUDENT_PHONE, null);
    }

    public String getKeyStudentEmail() {
        return pref.getString(KEY_STUDENT_EMAIL, null);
    }

    public String getKeyStudentImage() {
        return pref.getString(KEY_STUDENT_IMAGE, null);
    }

}
