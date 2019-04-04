package expencive.vk.com.recipes.requests;

import androidx.lifecycle.LiveData;
import expencive.vk.com.recipes.requests.responses.ApiResponse;
import expencive.vk.com.recipes.requests.responses.RecipeResponse;
import expencive.vk.com.recipes.requests.responses.RecipeSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    // SEARCH
    @GET("api/search")
    LiveData<ApiResponse<RecipeSearchResponse>> searchRecipe(
            @Query("key") String key,
            @Query("q") String query,
            @Query("page") String page
    );

    // GET SPECIFIC RECIPE
    @GET("api/get")
    LiveData<ApiResponse<RecipeSearchResponse>> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipe_id
    );

}
