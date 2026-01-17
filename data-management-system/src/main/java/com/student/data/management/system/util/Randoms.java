package com.student.data.management.system.util;

import java.time.LocalDate;
import java.util.Random;

public class Randoms {
	  private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	  private static final String[] CLASSES = {"Class1","Class2","Class3","Class4","Class5"};
	  private static final Random R = new Random();

	  public static String randomName(int minLen, int maxLen) {
	    int len = minLen + R.nextInt(maxLen - minLen + 1);
	    StringBuilder sb = new StringBuilder(len);
	    for (int i = 0; i < len; i++) sb.append(LETTERS.charAt(R.nextInt(LETTERS.length())));
	    return sb.toString();
	  }

	  public static LocalDate randomDob() {
	    // between 2000-01-01 and 2010-12-31
	    LocalDate start = LocalDate.of(2000, 1, 1);
	    LocalDate end = LocalDate.of(2010, 12, 31);
	    int days = (int) (end.toEpochDay() - start.toEpochDay());
	    return start.plusDays(R.nextInt(days + 1));
	  }

	  public static String randomClassName() {
	    return CLASSES[R.nextInt(CLASSES.length)];
	  }

	  public static int randomScoreExcel() {
	    // 55..75 inclusive
	    return 55 + R.nextInt(21);
	  }
	}