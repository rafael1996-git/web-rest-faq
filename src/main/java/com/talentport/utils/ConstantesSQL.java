package com.talentport.utils;

public class ConstantesSQL {
    public static final int DB_TIMEOUT = 12;
    public static final String FNINSERTUSERINFO = "{ call  PACKAGE_BODY_TALENTPORT( ?, ?, ? ,? ,?, ?,?)}";
    public static final String FINDBYEMAIL = "{? = call  PACKAGE_BODY_TALENTPORT_GET_EMAIL(?)}";
  
}