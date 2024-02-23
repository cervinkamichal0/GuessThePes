package com.example.guessthepes.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.guessthepes.logic.DatabaseHelper;
import com.example.guessthepes.logic.Record;
import com.example.guessthepes.logic.User;

import junit.framework.TestCase;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.stream.StreamSupport;

public class DatabaseHelperTest extends TestCase {


    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    public void testAuthenticateUser() {
        User testUser = new User("test", "test");
        boolean authenticated = DatabaseHelper.authenticateUser(testUser, false);
        assertTrue(authenticated);

    }

    public void testAddRecord() {
        User testUser = new User("test", "test");
        Bitmap bMap = BitmapFactory.decodeFile("image.jpg");
        Record testRecord = new Record("Test", testUser, bMap);
        boolean added = DatabaseHelper.addRecord(testRecord);
        assertTrue(added);
    }

    public void testGetRecords() {
        User testUser = new User("x", "x");
        List<Record> records = DatabaseHelper.getRecords(testUser);
        assertTrue(records.size() > 0);
    }

    public void testChangePassword() {
        User testUser = new User("test", "test");
        boolean changed = DatabaseHelper.changePassword(testUser, "test");
        assertTrue(changed);
    }

    public void testAddUser() {
        User testUser = new User("test", "test");
        boolean added = DatabaseHelper.addUser(testUser);
        assertTrue(added);
    }

}