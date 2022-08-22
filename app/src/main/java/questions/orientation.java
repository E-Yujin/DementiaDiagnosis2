package questions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cbnu.dementiadiagnosis.GeoVariable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class orientation extends question {
    private String Date[];
    private String day;
    double longitude, latitude;
    final LocationManager lm;
    final Geocoder geocoder;

    public orientation(AppCompatActivity context) {
        this.num = 5;
        this.quiz = new ArrayList<>();
        this.crr_ans = new ArrayList[6];
        for (int i = 0; i < 6; i++) {
            this.crr_ans[i] = new ArrayList<>();
        }
        this.user_ans = new String[6];
        this.scores = new int[10];
        this.Tscore = new Integer(0);

        GeoVariable geovariable = new GeoVariable();
        geocoder = new Geocoder(context);
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<Address> location = null;

        int permissionCheck1 = ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET);
        if(permissionCheck1 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(context, new String[] {Manifest.permission.INTERNET},1);

        int permissionCheck2 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissionCheck2 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(context, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},1);

        int permissionCheck3 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck3 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(context, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);

        final LocationListener mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                //여기서 위치값이 갱신되면 이벤트가 발생한다.
                //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

                Log.d("geocoding", "onLocationChanged, location:" + location);
                geovariable.setLatitude(location.getLatitude()); // 클래스 변수에 위도 대입
                geovariable.setLongitube(location.getLongitude());  // 클래스 변수에 경도 대입
                latitude = geovariable.getLatitude(); // 위도 경도 클래스변수에서 가져옴
                longitude = geovariable.getLongitube();
                reverseCoding();
            }

            public void onProviderDisabled(String provider) {
                // Disabled시
                Log.d("geocoding", "onProviderDisabled, provider:" + provider);
            }

            public void onProviderEnabled(String provider) {
                // Enabled시
                Log.d("geocoding", "onProviderEnabled, provider:" + provider);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                // 변경시
                Log.d("geocoding", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
            }
        };

        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
        } catch(SecurityException e){
            e.printStackTrace();
        }

        latitude = geovariable.getLatitude(); // 위도 경도 클래스변수에서 가져옴
        longitude = geovariable.getLongitube();


        if (permissionCheck2 == PackageManager.PERMISSION_GRANTED
                || permissionCheck3 == PackageManager.PERMISSION_GRANTED){
            if(!reverseCoding()){
                crr_ans[5].add("ERROR");
                crr_ans[5].add("ERROR");
            }
        }
        else {
            crr_ans[5].add("ERROR");
            crr_ans[5].add("ERROR");
        }

        long getDate = System.currentTimeMillis();
        Date DateData = new Date(getDate);
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        String temp = dFormat.format(DateData);
        Date = temp.split("-");

        Calendar cal = Calendar.getInstance();
        cal.setTime(DateData);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;

        }
        this.quiz.add("오늘 날짜를 말씀해주세요.");
        this.quiz.add("올해는 몇 년도입니까?");
        this.quiz.add("지금은 몇 월입니까?");
        this.quiz.add("오늘은 며칠입니까?");
        this.quiz.add("오늘은 무슨 요일입니까?");
        this.quiz.add("지금 있는 곳은 어디입니까?");

        this.crr_ans[0].add(Date[0]);
        this.crr_ans[0].add(returnMONTH());
        this.crr_ans[0].add(returnDATE());
        this.crr_ans[0].add(day);
        this.crr_ans[1].add(Date[0]);
        this.crr_ans[2].add(returnMONTH());
        this.crr_ans[3].add(returnDATE());
        this.crr_ans[4].add(day);

    }


    public String returnYEAR() {
        return Date[0];
    }

    public String returnMONTH() {
        if (Date[1].indexOf("0") == 0) return Date[1].replace("0", "");
        else return Date[1];
    }

    public String returnDATE() {
        if (Date[2].indexOf("0") == 0) return Date[2].replace("0", "");
        else return Date[2];
    }

    public String returnDAY() {
        return day;
    }

    public String KorTran(String num) {
        String input = num.replace("년", "")
                .replace("도", "")
                .replace("일", "")
                .replace("시월", "십월")
                .replace("유월", "육월")
                .replace("월", "");

        String splited[] = input.split(" ");

        StringTokenizer st;
        String token = "";
        int check;
        long result = 0;
        long tmpResult = 0;
        long number = 0;
        long total = 0;
        boolean have_to_num = false;
        boolean have_to_unit = false;
        final String NUMBER = "영일이삼사오육칠팔구";
        final String UNIT = "십백천만억조";
        final long[] UNIT_NUM = {10, 100, 1000, 10000, (long) Math.pow(10, 8), (long) Math.pow(10, 12)};
        for (String split : splited) {
            st = new StringTokenizer(split, UNIT, true);
            StringTokenizer tem = new StringTokenizer(split, UNIT, true);
            String tok = tem.nextToken();
            if (NUMBER.indexOf(tok) != -1 || (UNIT.indexOf(tok) != -1 && split.length() == 1)) {
                if (have_to_num && NUMBER.indexOf(tok) == -1) continue;
                if (have_to_unit && UNIT.indexOf(tok) == -1) continue;
                if (tem.hasMoreTokens()) {
                    tok = tem.nextToken();
                }
                if (!(split.length() == 1 && NUMBER.indexOf(tok) != -1)) { //한 글자의 숫자가 아니면 실행
                    while (st.hasMoreTokens()) {
                        token = st.nextToken();
                        check = NUMBER.indexOf(token);
                        //숫자인지, 단위(UNIT)인지 확인
                        if (check == -1) { //단위인 경우
                            if ("만억조".indexOf(token) == -1) {
                                tmpResult += (number != 0 ? number : 1) * UNIT_NUM[UNIT.indexOf(token)];
                            } else {
                                tmpResult += number;
                                result += (tmpResult != 0 ? tmpResult : 1) * UNIT_NUM[UNIT.indexOf(token)];
                                tmpResult = 0;
                            }
                            number = 0;
                        } else { //숫자인 경우
                            number = check;
                        }
                    }
                    total = result + tmpResult + number;
                    if (NUMBER.indexOf(token) != -1) {
                        have_to_unit = true;
                        have_to_num = false;
                    } else if (UNIT.indexOf(token) != -1) {
                        have_to_num = true;
                        have_to_unit = false;
                    }
                } else { // 한 글자의 숫자인 경우 실행
                    while (st.hasMoreTokens()) {
                        token = st.nextToken();
                        check = NUMBER.indexOf(token);
                        number = check;
                    }
                    total += number;
                    have_to_unit = true;
                    have_to_num = false;
                }
            }
        }
        return String.valueOf(total);
    }

    public boolean reverseCoding(){ // 위도 경도 넣어가지구 역지오코딩 주소값 뽑아낸다
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(latitude, longitude, 10); // 위도, 경도, 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test_", "입출력 오류 - 서버에서 주소변환시 에러발생");
            return false;
        }
        if (list != null) {
            if (list.size()==0) {
                Log.d("geocoding", "해당되는 주소 정보는 없습니다");
                return false;
            } else {
                // onWhere.setText(list.get(0).toString()); 원래 통으로 나오는 주소값 문자열

                // 문자열을 자르자!
                String cut[] = list.get(0).toString().split(" ");
                if(crr_ans[5].isEmpty()){ //아직 셋되지 않은 상태
                    for(int i=1; i<4; i++){
                        this.crr_ans[5].add(cut[i]);
                    }
                }
                else if(!cut[3].equals(crr_ans[5].get(2))){ //장소가 변경된 경우 업데이트
                    crr_ans[5].clear();
                    for(int i=1; i<4; i++){
                        this.crr_ans[5].add(cut[i]);
                    }
                }
                // cut[0] : Address[addressLines=[0:"대한민국
                // cut[1] : 서울특별시  cut[2] : 송파구  cut[3] : 오금동
                // cut[4] : cut[4] : 41-26"],feature=41-26,admin=null ~~~~

                String loca = "";
                loca = "";
                for(String crr : crr_ans[5]){
                    loca = loca + " " + crr;
                }
                Log.d("geocoding", loca);
                return true;
            }
        }
        else return false;
    }
}
