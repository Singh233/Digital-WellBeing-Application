package timeline.android.digitalwellbeing.TYProject.rests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import timeline.android.digitalwellbeing.TYProject.model.ResponseModel;

public interface APIInterface {
    @GET("top-headlines")
    Call<ResponseModel> getLatestNews(@Query("sources") String source, @Query("apiKey") String apiKey);

    @GET("top-headlines")
    Call<ResponseModel> getBusinessNews(@Query("country") String country, @Query("category") String category, @Query("apiKey") String apiKey);


}
