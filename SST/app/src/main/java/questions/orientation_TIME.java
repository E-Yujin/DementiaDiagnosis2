package questions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class orientation_TIME extends question{
    private long getDate;
    private Date DateData;
    private SimpleDateFormat dFormat;
    private String Date;

    orientation_TIME(){
        getDate = System.currentTimeMillis();
        DateData = new Date(getDate);
        dFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date = dFormat.format(DateData);
    }

    String SetStartMent(){
        return "지금부터 시간 지남력을 검사하겠습니다.";
    }
}
