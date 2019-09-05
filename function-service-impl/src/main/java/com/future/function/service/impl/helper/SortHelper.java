package com.future.function.service.impl.helper;

import java.util.Date;

public class SortHelper {

  public static int compareClosestDeadline(long date1, long date2) {
    long now = new Date().getTime();
    boolean is1Passed = date1 < now;
    boolean is2Passed = date2 < now;
    if (is1Passed && !is2Passed) {
      return 1;
    } else if(!is1Passed && is2Passed) {
      return -1;
    }
    return (int) (date1 - date2);
  }

}
