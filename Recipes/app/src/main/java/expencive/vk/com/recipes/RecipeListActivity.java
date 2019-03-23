package expencive.vk.com.recipes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import expencive.vk.com.recipes.models.Recipe;
import expencive.vk.com.recipes.requests.RecipeApi;
import expencive.vk.com.recipes.requests.ServiceGenerator;
import expencive.vk.com.recipes.requests.responses.RecipeResponse;
import expencive.vk.com.recipes.util.Constants;
import expencive.vk.com.recipes.viewmodels.RecipeListViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeListActivity extends BaseActivity {
    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel mRecipeListVieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecipeListVieModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

       subscribeObservers();
    }

    private void subscribeObservers(){
        mRecipeListVieModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {

            }
        });
    }
    private void testRetrofitRequest() {
        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();

//        Call<RecipeSearchResponse> responseCall = recipeApi.searchRecipe(Constants.API_KEY,
//                "chicken breast", "1");
//
//        responseCall.enqueue(new Callback<RecipeSearchResponse>() {
//            @Override
//            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
//                Log.d(TAG, "onResponse: server response: " + response.toString());
//                if (response.code() == 200) {
//                    Log.d(TAG, "onResponse: " + response.body().toString());
//                    List<Recipe> recipes = new ArrayList<>(response.body().getRecipes());
//
//                    for (Recipe recipe: recipes) {
//                        Log.d(TAG, "onResponse: " + recipe.getTitle());
//                    }
//                }else {
//                    try {
//                        Log.d(TAG, "onResponse: " + response.errorBody().string());
//
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
//
//            }
//        });

        Call<RecipeResponse> responseCall = recipeApi.getRecipe(Constants.API_KEY,
                "8c0314");

        responseCall.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                Log.d(TAG, "onResponse: Server Response: " + response.toString());
                if (response.code() == 200) {
                    Recipe recipe = response.body().getRecipe();
                    Log.d(TAG, "onResponse: " + recipe.toString());
                }
                else{
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.d(TAG, "onResponse: ERROR: " + t.getMessage());
            }
        });
    }

}