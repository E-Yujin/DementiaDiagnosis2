package fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cbnu.dementiadiagnosis.FirstActivity;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.RegisterActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

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
        EditText sexEditText = (EditText) dialogView.findViewById(R.id.edit_sex);
        EditText eduEditText = (EditText) dialogView.findViewById(R.id.edit_edu);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.round_button);
        alertDialog.show();

        // 수정 전, 기존 사용자 정보 출력
        User user = db.getUserInf(serial_code);
        String name = user.getName();
        String birth = user.getBirth();
        String sex = user.getSex();
        String edu = user.getEdu();

        nameEditText.setText(name);
        birthEditText.setText(birth);
        sexEditText.setText(sex);
        eduEditText.setText(edu);

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
                String csex = sexEditText.getText().toString();
                String cedu = eduEditText.getText().toString();
                int cage = getAge(cbirth);
                int cscore = getRefScore(cage, cedu);

                long result = db.updateData(serial_code, cname, cbirth, cage, csex, cedu, cscore);

                if(result > -1) {
                    Toast.makeText(requireActivity(), "사용자 정보 수정 성공!!!", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(requireActivity(), "사용자 정보 수정 실패ㅜㅜㅜ", Toast.LENGTH_SHORT).show();
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
        builder.show();
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
