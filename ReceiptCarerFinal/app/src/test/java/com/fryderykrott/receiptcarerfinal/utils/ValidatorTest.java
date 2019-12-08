package com.fryderykrott.receiptcarerfinal.utils;

import androidx.annotation.NonNull;

import com.fryderykrott.receiptcarerfinal.MainView.MainActivity;
import com.fryderykrott.receiptcarerfinal.Model.Group;
import com.fryderykrott.receiptcarerfinal.Model.User;
import com.fryderykrott.receiptcarerfinal.Services.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ValidatorTest {

    ArrayList<Group> groups;
    String groupNameTest1 = "Gruop Name 1";
    String groupNameTest2 = "Gruop Name 2";

    @Before
    public void setUp() throws Exception {
        Group group_1 = new Group();
        group_1.setName(groupNameTest1);

        Group group_2 = new Group();
        group_2.setName(groupNameTest2);

        groups = new ArrayList<>(2);
        groups.add(group_1);
        groups.add(group_2);

        Utils.user = new User();
        Utils.user.setGroups(groups);
    }

    @Test
    public void validateEmail() {
        String email_1 = "adres";
        String email_2 = "adres@";
        String email_3 = "adres@fds";
        String email_4 = "adres@dsa.pl";
        String email_5 = "";

        assertFalse(Validator.validateEmail(email_1));
        assertFalse(Validator.validateEmail(email_2));
        assertFalse(Validator.validateEmail(email_3));
        assertTrue(Validator.validateEmail(email_4));
        assertFalse(Validator.validateEmail(email_5));
    }

    @Test
    public void validateEditGroupName() {
        Utils.user = new User();
        Utils.user.setGroups(groups);

        String group_test_1 = groupNameTest1;
        String group_test_2 = groupNameTest2;
        String group_test_3 = "";
        String group_test_4 = "Very Long Group Name Thet Shouldn't be possible to be added";
        String group_test_5 = "New Group name";

        assertFalse(Validator.validateGroupName(group_test_1));
        assertFalse(Validator.validateGroupName(group_test_2));
        assertFalse(Validator.validateGroupName(group_test_3));
        assertFalse(Validator.validateGroupName(group_test_4));
        assertTrue(Validator.validateGroupName(group_test_5));

    }

    @Test
    public void validateGroupName() {
        String group_test_1 = groupNameTest1;
        String group_test_2 = groupNameTest2;
        String group_test_3 = "New Group name";
        String group_test_4 = "";

        assertFalse(Validator.validateEditGroupName(group_test_1, group_test_1));
        assertFalse(Validator.validateEditGroupName(group_test_1, groupNameTest1));
        assertFalse(Validator.validateEditGroupName(group_test_2, groupNameTest2));
        assertFalse(Validator.validateEditGroupName(group_test_1, "Origal name"));
        assertFalse(Validator.validateEditGroupName(group_test_2, "Origal name"));
        assertTrue(Validator.validateEditGroupName(group_test_3, "Origal name"));
        assertFalse(Validator.validateEditGroupName(group_test_4, "Origal name"));
    }

    @Test
    public void validatePassword() {
        String password_test_1 = "short";
        String password_test_2 = "PerfectExample";
        String password_test_3 = "";

        assertFalse(Validator.validatePassword(password_test_1));
        assertFalse(Validator.validatePassword(password_test_3));
        assertTrue(Validator.validatePassword(password_test_2));
    }

    @Test
    public void validateRepeatedPassword() {
        String password_test_1 = "short";
        String password_test_2 = "PerfectExample";
        String password_test_3 = "";

        assertFalse(Validator.validateReapetedPassword(password_test_1, password_test_2));
        assertFalse(Validator.validateReapetedPassword(password_test_3, password_test_1));
        assertFalse(Validator.validateReapetedPassword(password_test_3, password_test_2));
        assertFalse(Validator.validateReapetedPassword(password_test_3, password_test_3));
        assertFalse(Validator.validateReapetedPassword(password_test_1, password_test_1));

        assertTrue(Validator.validateReapetedPassword(password_test_2, password_test_2));
    }

    @Test
    public void validateReceiptName() {
        String password_test_1 = "Receipt name";
        String password_test_2 = "";

        assertTrue(Validator.validateReceiptName(password_test_1));

        assertFalse(Validator.validateReceiptName(password_test_2));
    }

}