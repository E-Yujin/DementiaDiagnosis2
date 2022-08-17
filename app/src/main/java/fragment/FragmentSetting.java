package fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.cbnu.dementiadiagnosis.FirstActivity;
import com.cbnu.dementiadiagnosis.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import database.DBHelper;
import user.EducationAge;
import user.SharedPreference;
import user.User;

public class FragmentSetting extends Fragment implements View.OnClickListener {

    TextView inf_retrieve, inf_delete;
    TextView inf_name;
    DBHelper db;
    String serial_code;
    List<EducationAge> eduAgeList = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        db = new DBHelper(requireActivity());
        serial_code = SharedPreference.getSerialCode(requireActivity().getApplicationContext());
        inf_name = (TextView) view.findViewById(R.id.userIf_name);
        inf_retrieve = (TextView) view.findViewById(R.id.userIf_retrieve);
        inf_delete = (TextView) view.findViewById(R.id.userIf_delete);

        User user = db.getUserInf(serial_code);
        inf_name.setText(user.getName());

        // 수정 버튼
        inf_retrieve.setOnClickListener(this);
        // 삭제 버튼
        inf_delete.setOnClickListener(this);

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIf_retrieve:
                onClickRetrieve();
                break;
            case R.id.userIf_delete:
                onClickDelete();
                break;
        }
    }

    // 사용자 정보 수정 Dialog
    public void onClickRetrieve() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_retrieve, null);
        EditText nameEditText = (EditText) dialogView.findViewById(R.id.edit_name);
        EditText birthEditText = (EditText) dialogView.findViewById(R.id.edit_birth);
        Spinner spinnerSex = (Spinner) dialogView.findViewById(R.id.spinner_sex);
        Spinner spinnerEdu = (Spinner) dialogView.findViewById(R.id.spinner_edu);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(R.layout.dialog_retrieve);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.round_button);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(alertDialog.getWindow().getAttributes());
        params.width = 900;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setAttributes(params);

        // 수정 전, 기존 사용자 정보 출력
        User user = db.getUserInf(serial_code);
        String name = user.getName();
        String birth = user.getBirth();
        String sex = user.getSex();
        String edu = user.getEdu();

        nameEditText.setText(name);
        birthEditText.setText(birth);

        // 생년월일 dialog picker
        birthFun(birthEditText);

        String[] sexList = new String[2];
        sexList[0] = "남";
        sexList[1] = "여";

        ArrayAdapter spinnerAdapterSex;
        spinnerAdapterSex = new ArrayAdapter(requireActivity(), R.layout.spinner, sexList);
        spinnerSex.setAdapter(spinnerAdapterSex);

        spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(sex.equals("남")) {
            spinnerSex.setSelection(0);
        } else {
            spinnerSex.setSelection(1);
        }

        int eduNum = 0;
        switch(edu) {
            case "문맹":
                eduNum = 0;
                break;
            case "무학":
                eduNum = 1;
                break;
            case "초등졸업":
                eduNum = 2;
                break;
            case "중등졸업":
                eduNum = 3;
                break;
            case "고등졸업":
                eduNum = 4;
                break;
            case "대학졸업이상":
                eduNum = 5;
                break;
            default:
                break;

        }

        ArrayList<String> eduList = new ArrayList<>();
        eduList.add("문맹");
        eduList.add("무학");
        eduList.add("초등졸업");
        eduList.add("중등졸업");
        eduList.add("고등졸업");
        eduList.add("대학졸업이상");

        ArrayAdapter spinnerAdapterEdu;
        spinnerAdapterEdu = new ArrayAdapter(requireActivity(),R.layout.spinner, eduList);
        spinnerEdu.setAdapter(spinnerAdapterEdu);

        spinnerEdu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerEdu.setSelection(eduNum);

        Button cancelBtn = (Button) dialogView.findViewById(R.id.cancelBtn);
        Button retrieveBtn = (Button) dialogView.findViewById(R.id.retrieveBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // 수정 확인 버튼 클릭
        retrieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cname = nameEditText.getText().toString();
                String cbirth = birthEditText.getText().toString();
                String csex = spinnerSex.getSelectedItem().toString();
                String cedu = spinnerEdu.getSelectedItem().toString();
                int cage = getAge(cbirth);
                int cscore = getRefScore(cage, cedu);

                long result = db.updateData(serial_code, cname, cbirth, cage, csex, cedu, cscore);

                if(result > -1) {
                    Toast.makeText(requireActivity(), "수정되었습니다", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(requireActivity(), "수정에 실패하셨습니다", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });
    }

    // 사용자 정보 삭제 Dialog
    public void onClickDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("사용자 정보 삭제");
        builder.setMessage("서비스를 이용하는데 사용된 사용자의\n" +
                "개인정보와 기록된 진단 기록을 \n" +
                "삭제하시겠습니까? \n" +
                "\n" +
                "! 삭제하기 버튼을 누르시면 모든 정보가 삭제되고, 삭제된 정보를 복구할 수 없음을 알려드립니다.");

        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 취소 이벤트 처리
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("삭제",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 삭제 이벤트 처리
                        long del_user = db.deleteUser(serial_code);
                        long del_score = db.deleteScore(serial_code);
                        if(del_user > -1 && del_score > -1) {
                            dialog.dismiss();
                            Toast.makeText(requireActivity(), "사용자 정보 삭제 성공!!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), FirstActivity.class));
                        } else {
                            Toast.makeText(requireActivity(), "사용자 정보 삭제 실패ㅜㅜㅜ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Typeface face = ResourcesCompat.getFont(requireContext(),R.font.jejugothic);
        TextView title_of_dialog = new TextView(requireActivity().getApplicationContext());
        title_of_dialog.setText("사용자 정보 삭제");
        title_of_dialog.setTypeface(face);
        title_of_dialog.setTextSize(18f);
        title_of_dialog.setTextColor(Color.BLACK);
        title_of_dialog.setPadding(50, 50, 0, 30);
        builder.setCustomTitle(title_of_dialog);

        AlertDialog dialog = builder.show();
        TextView msg = (TextView) dialog.findViewById(android.R.id.message);
        msg.setTypeface(face);
        Button btnPositive = dialog.getButton(Dialog.BUTTON_POSITIVE);
        btnPositive.setTypeface(face);
        Button btnNegative = dialog.getButton(Dialog.BUTTON_NEGATIVE);
        btnNegative.setTypeface(face);
    }

    // 생년월일 datePicker
    public void birthFun(EditText birth) {
        birth.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    requireActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener, year, month, day);

            dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = null;
            String s_month = Integer.toString(month), s_day = Integer.toString(day);

            if(month < 10) {
                s_month = "0" + month;
            }
            if(day < 10) {
                s_day = "0" + day;
            }
            date = year + "/" + s_month + "/" + s_day;
            birth.setText(date);
        };
    }

    // 사용자 만나이 계산
    public int getAge(String birth) {
        int year, month, day;
        int age;

        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        year = Integer.parseInt(birth.substring(0, 4));

        if(birth.charAt(5) == '0') {
            month = Integer.parseInt(birth.substring(6, 7));
        } else {
            month = Integer.parseInt(birth.substring(5, 7));
        }

        if(birth.charAt(8) == '0') {
            day = Integer.parseInt(birth.substring(9, 10));
        } else {
            day = Integer.parseInt(birth.substring(8, 10));
        }

        age = currentYear - year;

        // 생일이 지나지 않은 경우 -1
        if(month * 100 + day > currentMonth * 100 + currentDay) {
            age--;
        }
        return age;
    }

    // 사용자 치매 기준 점수 구하기
    public int getRefScore(int age, String edu) {
        int age_index = 0;
        int ref_score = 0;

        if(age < 60) {
            age_index = 0;
        } else if(age < 70) {
            age_index = 1;
        } else if(age < 80) {
            age_index = 2;
        } else {
            age_index = 3;
        }

        eduAgeList = db.getAllEduTable();

        switch (edu) {
            case "문맹":
                ref_score = eduAgeList.get(age_index).getIlliterate();
                break;
            case "무학":
                ref_score = eduAgeList.get(age_index).getUneducated();
                break;
            case "초등졸업":
                ref_score = eduAgeList.get(age_index).getElementarySchool();
                break;
            case "중등졸업":
                ref_score = eduAgeList.get(age_index).getSecondarySchool();
                break;
            case "고등졸업":
                ref_score = eduAgeList.get(age_index).getHighSchool();
                break;
            case "대학졸업이상":
                ref_score = eduAgeList.get(age_index).getUniversity();
                break;
            default:
                break;
        }
        return ref_score;
    }
}
