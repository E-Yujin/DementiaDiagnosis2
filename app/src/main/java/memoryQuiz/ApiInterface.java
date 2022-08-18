package memoryQuiz;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("search/{type}")
    Call<String> getSearchResult(
            @Header("X-Naver-Client-Id") String clientID,
            @Header("X-Naver-Client-Secret") String clientSecret,
            @Path("type") String type,
            @Query("query") String query
    );
}
