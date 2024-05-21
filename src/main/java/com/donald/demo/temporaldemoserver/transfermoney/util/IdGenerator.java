package com.donald.demo.temporaldemoserver.transfermoney.util;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class IdGenerator {


    public static String generateWorkflowId() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(Calendar.getInstance().getTime());

        return String.format(
            "TRANSFER-%s%03d",
                timeStamp + "-"
                + (char) (Math.random() * 26 + 'A')
                + ""
                + (char) (Math.random() * 26 + 'A')
                + ""
                + (char) (Math.random() * 26 + 'A'),
            (int) (Math.random() * 999));
      }

    public static String generateTransferId() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(Calendar.getInstance().getTime());
        return String.format("TSFR-%s%03d",
                timeStamp + "-"
                + (char) (Math.random() * 26 + 'A')
                 + ""
                 + (char) (Math.random() * 26 + 'A')
                 + ""
                 + (char) (Math.random() * 26 + 'A'),
                (int) (Math.random() * 999));
    }  //End generateTransferId
}
