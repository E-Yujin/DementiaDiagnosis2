package fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbnu.dementiadiagnosis.InfAdapter;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.RecyclerAdapter;

import java.util.Arrays;
import java.util.List;

import user.Data;

public class FragmentInf extends Fragment {

    View view;
    InfAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inf, container, false);

        init();
        getData();

        return view;
    }

    public void init() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplication());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new InfAdapter();
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getData() {
        List<String> listTitle = Arrays.asList("치매, 어떤 병인가요?", "치매, 얼마나 많나요?", "기억이 잘 안나요! 치매 아닌가요?", "치매진단은 어떻게 하는 건가요?",
                "치매, 예방할 수 있나요?", "치매 예방을 돕는 3가지의 즐길 거리!", "치매 예방을 돕는 3가지 금기 사항!", "치매 예방을 돕는 3가지 행동!");
        List<String> listContent = Arrays.asList(
                "치매는 어떤 하나의 질병 명이 아니라, 특정한 조건에서 여러 증상들이 함께 나타나는 증상들의 묶음입니다." +
                        "\t이러한 치매상태를 유발할 수 있는 질환 중 가장 대표적인 것이 알츠하이머병과 혈관성 치매이며, 그 외 루이체 치매, 전두측두엽 치매 등이 있습니다." +
                        "\n\n치매의 대표적인 초기 증상은 기억력 장애입니다. 누구나 나이가 들면서 젊었을 때에 비해 기억력이 저하되기 마련이지만, 치매에서의 기억력 저하는 이러한 정상적인 변화와는 다릅니다. " +
                        "\n\n치매는 나이가 들어서 생기는 자연스러운 결과가 아닙니다. 나이가 들면서 생기는 기억력 저하는 대개 사소한 일들에 국한되어 있으며, 개인의 일상생활을 심각하게 저해하지 않습니다. " +
                        "더 자세한 내용은 '정상노화와 치매의 차이' 부분을 참고하시기 바랍니다.\n\n다른 한편, 기억력의 저하가 가장 흔한 첫 증상이긴 하나, 언어, 판단력의 변화나 성격의 변화가 먼저 나타날 수도 있습니다.",
                "범세계적인 고령화로 인해 치매환자 수도 급격히 증가하고 있고 국내의 치매환자 수도 빠르게 증가하고 있습니다. 2016년 치매유병률조사'에 따르면, 2018년 우리나라 65세 이상 노인인구 중 약 75만 명이 치매를 앓고 있는 것으로 추정되었습니다. " +
                        "\n\n치매환자 수는 향후 17년마다 두 배씩 증가하여 2024년에는 100만, 2039년에는 200만을 넘어설 것으로 예상되었는데, 이는 ‘2012년 치매유병률’ 조사 당시 200만을 예측했던 2041년보다 2년이 앞당겨져 치매환자 증가 속도보다 더 가팔라진 수치입니다." +
                        "\n\n2016년 치매 역학조사 결과 2018년 현재 65~69세에서 약 1%, 70~74세에서 4%, 75~79세 12%, 80~84세 21%, 85세 이상에서 40% 정도의 유병률을 보여, 연령별 유병률은 대략 연령이 5세 증가할수록 유병률도 2배 가량 증가하는 추세를 보이고 있습니다." +
                        "\n\n2014년에 실시된 국내 치매 인식도 조사에서는 우리나라 노인들이 가장 두려워하는 질병은 치매(43%)로, 나이가 들수록 암보다 치매를 더 무서운 질병으로 인식하는 것으로 나타났습니다." +
                        "\n\n높아지는 치매 발병률에 대응하기 위해서는 우리 모두가 치매에 대한 많은 관심과 이에 따른 예방이 필수적입니다.",
                "-\t건망증\n" +
                        "치매와 혼동되는 건망증은 사건이나 경험의 내용 중 일부분을 잘 기억하지 못하는 증상으로 그러한 사건이나 경험이 있었다는 사실 자체를 기억하지 못하는 치매와는 다릅니다." +
                        "\n\n예를 들어, 할머니가 낮에 오후 7시까지 남편과 함께 저녁을 드시러 오라는 딸의 전화를 받았다고 가정해 봅시다. 할머니가 건망증이 있다면 “몇 시에 오라고 했더라?”하고 다시 딸에게 전화해서 묻게 되겠지만, " +
                        "만약 치매 환자라면 딸이 그런 전화를 했었다는 사실 자체를 잊어버린 채 남편 저녁을 준비하게 됩니다. 또 건망증의 경우에는 기억나지 않던 부분이 어느 순간 다시 떠오르는 경우가 많지만, " +
                        "치매 환자에서는 그런 경우가 거의 없습니다.\n\n아울러 치매는 건망증과는 달리 진행성 장애이기 때문에 기억력 장애는 점점 더 심해져 직무 수행이나 가정생활에 영향을 주게 됩니다. " +
                        "따라서 기억력이 계속 조금씩 나빠진다면 건망증보다는 치매를 의심해 보아야 합니다.",
                "기억돔에서 진행하는 자가진단 검사는 중앙치매센터에서 개발한 치매 진단 척도 CIST를 기반으로 해 빠르고 간단하지만 높은 정확도를 기대하실 수 있습니다. " +
                        "\n\n하지만 치매는 매우 다양한 원인에 의해 생기기 때문에 한 가지 검사로 진단을 내릴 수 없습니다. 자세한 병력 조사와 직접적인 진찰, 검사실에서의 검사가 동반되어야 합니다." +
                        "\n\n기억돔의 치매자가진단인 간이검사와 정규검사에서 지속적으로 정밀 검사를 요구하는 수준의 결과가 나오신다면 가까이에 있는 치매 센터나 병원에 방문하셔서 더욱 정밀한 진단을 받아보시기 바랍니다.",
                "다양한 치매의 원인 중 뇌종양, 심각한 우울증, 갑상선 질환, 약물 부작용, 영양문제 등은 일찍 발견할 경우 좋아질 수 있습니다. 그리고 이 중 5~10% 정도는 완치될 수도 있습니다. " +
                        "\n\n또한 치매의 원인 중 20~30% 정도를 차지하는 혈관성 치매는 고혈압이나 당뇨, 심장 질환 등 혈관성 위험 인자의 관리와 적절한 치료제의 사용으로 악화를 방지할 수 있습니다. " +
                        "\n\n치매의 가장 흔한 원인인 알츠하이머병도 조기에 발견하여 치료하면 인지기능의 저하를 더 늦출 수 있는 것으로 알려져 있습니다. " +
                        "따라서 치매는 조기 발견과 치료가 중요하며, 이미 치매가 진행되신 분이라 할지라도 적절한 평가와 치료를 통해 상당히 호전될 수 있습니다." +
                        "\n\n치매를 조기발견한다면 치매 진행을 늦출 수 있는 약물치료의 효과가 대폭 상승하며 환자의 수명도 유의미하게 증가시킬 수 있습니다. 또한 치매 악화로 인한 다양한 문제를 미리 대처해 더욱 건강한 삶을 영위할 수 있습니다." +
                        "\n\n이처럼 치매는 조기발견이 중요한 질병이기에 사용자님의 지속적인 관심과 건강한 식습관, 주기적인 운동, 신속한 자가진단 등으로 일찍 증상을 알게 된다면 충분히 예방할 수 있습니다.",
                "-\t운동 : 일주일에 3번 이상 걸으세요!\n" +
                        "알츠하이머병의 위험요인에 대한 한 연구에 의하면 20분의 고강도 운동을 주 3회 이상 또는 30분의 중강도 운동을 주 5회 이상 하는 성인의 경우 그러지 않는 성인과 비교하여 치매위험이 1.82배 감소한다고 보고하였습니다." +
                        "\n\n-\t식사 : 생선과 채소를 골고루 챙겨 드세요!\n" +
                        "노인의 인지건강에 영향을 주는 생활습관 요인에 대한 논문 150편 이상을 분석한 한 연구는 생선, 채소, 과일, 우유 등의 섭취가 인지건강에 긍정적인 영향을 끼치는 것으로 보인다고 보고했습니다. 하지만 이 연구는 육류 등의 고지방 섭취는 치매의 위험을 높이는 것으로 보인다고 명시합니다." +
                        "\n\n-\t독서 : 부지런히 읽고 쓰세요!\n" +
                        "Wilson 등은 독서, 도서관 이용, 연극 관람 등과 같은 지적 활동을 많이 하면 알츠하이머병의 발생 위험이 낮아진다고 보고했습니다. 낱말 맞추기, 편지쓰기, 독서 및 영화·공연 관람과 같은 문화·취미활동 등 뇌세포를 지속적으로 자극해줄 수 있는 두뇌활동을 꾸준히 즐겁게 지속하는 것이 중요합니다.",
                "-\t절주 : 술은 한 번에 3잔보다 적게 마시세요!\n" +
                        "당한 수준을 벗어난 과음과 폭음은 인지장애의 확률을 1.7배 높입니다. 또한 중년기부터 많은 음주를 한 사람의 경우 노년기에 인지장애를 보일 확률이 2.6배 높습니다. " +
                        "또한 과음이나 습관적인 음주는 인지기능손상으로 인한 알콜성 치매의 원인이 될 수도 있습니다. 다른 한편, 적당한 음주는 기억력, 반응속도와 같은 인지기능에 어느 정도 긍정적인 영향을 미친다는 연구 결과도 있습니다." +
                        "\n\n-\t금연 : 담배는 피지 마세요!\n" +
                        "흡연자의 치매 발병 위험은 비흡연자에 비해 1.59배 높습니다. 또한 현재 흡연을 하는 사람은 비흡연자에 비해 2년 후 알츠하이머 치매에 걸릴 확률이 3배 높습니다. " +
                        "하지만 과거에 흡연을 했더라도 금연을 시작하고 6년 이상 시간이 지나면 인지장애의 확률이 41% 감소합니다." +
                        "\n\n-\t뇌손상 예방 : 머리를 다치지 않도록 조심하세요!\n" +
                        "의식을 잃을 정도의 뇌손상을 경험해본 경우 그렇지 않은 경우에 비해 치매위험이 1.18배 높아집니다. 머리를 보호하기 위해 운동할 때에는 보호 장구를 반드시 착용하고, " +
                        "머리를 부딪쳤을 땐 바로 검사를 받아 보는 것이 좋습니다.",
                "-\t건강검진 : 혈압, 혈당, 콜레스테롤 3가지를 정기적으로 체크하세요!\n" +
                        "0-79세에 제 2형 당뇨를 진단받은 성인은 그렇지 않은 그룹에 비해 치매위험이 1.46배 높아집니다. 또한 35-64세에 고혈압을 앓게 된 사람과 BMI가 30kg/㎡ 이상으로 비만이 된 성인은 각각 치매위험이 1.61배, 1.6배 증가합니다." +
                        "\n\n-\t소통 : 가족과 친구를 자주 연락하고 만나세요!\n" +
                        "지속적으로 사회활동을 하고 사람들과 꾸준히 소통하며 만나는 것이 중요합니다. 중년에는 활발한 사회활동을 했으나 노년에 그 빈도가 떨어지는 사람의 경우 치매에 걸릴 확률이 1.9배 높습니다. " +
                        "자원봉사, 교회·성당 등에서의 종교 활동 및 교제, 복지관·경로당 프로그램 참여 등 사회활동에 더 많이 참여할수록 상대적으로 인지기능의 저하속도가 느리고 치매, 알츠하이머병의 발생률이 낮은 것으로 알려져 있습니다." +
                        "\n\n-\t치매조기발견 : 매년 보건소에서 치매 조기검진을 받으세요!\n" +
                        "치매를 조기에 발견하여 적극적으로 치료·관리할 경우 치매환자는 건강한 상태를 보다 오래 유지하여 삶의 질을 높일 수 있으며, 가족들은 돌봄에 대한 부담이 줄어듭니다. " +
                        "치매조기발견을 위해 보건소에서 무료로 진행하는 치매선별검사(대상: 만 60세 이상 누구나)를 이용하거나 돈 계산과 같은 추상적인 사고능력에 문제가 생기거나 자발성의 감소, 직업이나 일상생활에 영향을 줄 정도의 최근 기억력 상실 등과 같은 치매 의심증상에 대해 알아두는 것도 도움이 됩니다." +
                        "\n\n" +
                        "출처 : 중앙치매센터 치매대백과, 치매사전(https://www.nid.or.kr/info/diction_list1.aspx?gubun=0101)");
        List<Integer> listResId = Arrays.asList(R.drawable.ic_check, R.drawable.ic_account, R.drawable.ic_help, R.drawable.ic_forward, R.drawable.ic_home,
                R.drawable.ic_check, R.drawable.ic_account, R.drawable.ic_help);

        Log.e("title:", Integer.toString(listTitle.size()));
        Log.e("content:", Integer.toString(listContent.size()));


        for(int i = 0; i < listTitle.size(); i++) {
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));

            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }

}