package com.lnsel.seramporecollege.utils;

/**
 * Created by db on 1/1/2018.
 */

public class UrlUtil {

    //live url..............
   // public static String BASE_DOWNLOAD_URL="http://61.16.131.206";
   // public static String BASE_URL="http://61.16.131.206/erp_srcc/api/";

    //local url..............

    public static String BASE_URL="http://61.16.131.206/erp_srcc/api/";

    public static String BASE_IMAGE_URL="http://61.16.131.206/erp_srcc/employee/uploads/photo/";

    public static String BASE_DOWNLOAD_URL="http://61.16.131.206";

    public static String TEACHER_LOGIN_URL=BASE_URL+"teacher/login";

    public static String TEACHER_PROFILE_DETAILS_URL=BASE_URL+"teacher/employee_personal_details";

    public static String TEACHER_NOTICE_LIST_URL=BASE_URL+"teacher/notice_list";

    public static String TEACHER_NOTICE_DETAILS_URL=BASE_URL+"teacher/notice_details";

    public static String TEACHER_PAYSLIP_LIST_URL=BASE_URL+"teacher/payroll_emp_list_payslip";

    public static String TEACHER_PAYSLIP_DETAILS_URL=BASE_URL+"teacher/payroll_view_payslip";

    public static String TEACHER_LEAVE_LIST_URL=BASE_URL+"teacher/payroll_emp_leave_list";

    public static String TEACHER_LEAVE_TYPE_LIST_URL=BASE_URL+"teacher/payroll_leave_type_master";

    public static String TEACHER_APPLY_LEAVE_URL=BASE_URL+"teacher/payroll_emp_leave_add";

    public static String TEACHER_DOWNLOAD_PAYSLIP_URL=BASE_URL+"teacher/download_single_payslip";

    public static String TEACHER_TIME_TABLE_URL=BASE_URL+"teacher/routine_allocation";

    public static String TEACHER_DEPT_TIME_TABLE_URL=BASE_URL+"teacher/routine_allocation_department_wise";

    public static String TEACHER_LIST_URL=BASE_URL+"teacher/academic_department_all_teachers";

    public static String TEACHER_CHANGE_PASSWORD_URL=BASE_URL+"teacher/change_password";

    public static String ATTENDANCE_SUBJECT_LIST_URL=BASE_URL+"teacher/subject_list_by_academic_department_id";

    public static String ATTENDANCE_PAPER_LIST_URL=BASE_URL+"teacher/paper_list_by_subject_id";

    public static String ATTENDANCE_INSTITUTE_LIST_URL=BASE_URL+"teacher/academic_institute_master";

    public static String ATTENDANCE_TIME_SLOT_LIST_URL=BASE_URL+"teacher/time_master";

    public static String ATTENDANCE_SECTION_LIST_URL=BASE_URL+"teacher/academic_section_master";

    public static String ATTENDANCE_STUDENT_LIST_URL=BASE_URL+"teacher/academic_section_student_allocation";

    public static String ATTENDANCE_SUBMIT_URL=BASE_URL+"teacher/student_attendence_entry";

    public static String ATTENDANCE_ATTENDANCE_LIST_URL=BASE_URL+"teacher/student_attendence_list";

    public static String ATTENDANCE_UPDATED_URL=BASE_URL+"teacher/student_attendence_update";

    public static String STUDENT_LOGIN_URL=BASE_URL+"student/login";

    public static String STUDENT_PROFILE_DETAILS_URL=BASE_URL+"student/profile";

    public static String STUDENT_NOTICE_LIST_URL=BASE_URL+"student/notice_list";

    public static String STUDENT_STAFFS_CONTACT_URL=BASE_URL+"student/all_teaching_and_nonteaching_staffs";

    public static String STUDENT_SUBJECT_PAPER_LIST_URL=BASE_URL+"student/subjects_and_paper_by_student_id";

    public static String STUDENT_CLASS_WISE_ATTENDANCE_URL=BASE_URL+"student/class_wise_attendance_by_student_id";

    public static String STUDENT_TIME_TABLE_URL=BASE_URL+"student/time_table_by_student_id";

    public static String STUDENT_PAYMENT_LIST_URL=BASE_URL+"student/payment_list";

    public static String STUDENT_PAYMENT_DUE_LIST_URL=BASE_URL+"student/due_payments";

    public static String STUDENT_CHANGE_PASSWORD_URL=BASE_URL+"student/change_password";

    public static String STUDENT_INTERNAL_MARKS_LIST_URL=BASE_URL+"student/internal_marks";

    public static String STUDENT_PAYMENT_DETAILS_URL=BASE_URL+"student/download_payment_details";

    public static String STUDENT_RECEIPT_DELETE_URL=BASE_URL+"student/delete_voucher";

    public static String STUDENT_PAYMENT_RECEIPT_DOWNLOAD_URL=BASE_URL+"test/download_payment_details?collection_id=";

}
