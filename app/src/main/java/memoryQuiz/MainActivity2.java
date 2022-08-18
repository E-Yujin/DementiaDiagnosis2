package memoryQuiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cbnu.dementiadiagnosis.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    String keyword = "";
    TextView resultView;
    TextView resultBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        resultBody = findViewById(R.id.searchResult2);
        resultView = findViewById(R.id.result);

        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");

        getResultSearch(keyword);
    }

    public void getResultSearch(String keyword) {
        ApiInterface apiInterface = ApiClient.getInstance().create(ApiInterface.class);
        String clientID = "EeyUFnx4V2vBUCESZorN";
        String clientSecret = "voTm1j9DwE";
        Call<String> call = apiInterface.getSearchResult(clientID, clientSecret, "encyc.json", keyword);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body();
                    resultBody.setText(result);
                    Log.e(TAG, "성공 : " + result);
                } else {
                    resultBody.setText(response.body());
                    Log.e(TAG, "실패 : " + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                resultBody.setText(t.getMessage());
                Log.e(TAG, "에러 : " + t.getMessage());
            }
        });
    }
}