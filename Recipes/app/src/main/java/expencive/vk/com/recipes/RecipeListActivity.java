package expencive.vk.com.recipes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import expencive.vk.com.recipes.models.Recipe;
import expencive.vk.com.recipes.requests.RecipeApi;
import expencive.vk.com.recipes.requests.ServiceGenerator;
import expencive.vk.com.recipes.requests.responses.RecipeResponse;
import expencive.vk.com.recipes.util.Constants;
import expencive.vk.com.recipes.util.Testing;
import expencive.vk.com.recipes.viewmodels.RecipeListViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeListActivity extends BaseActivity {
    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel mRecipeListVieModel;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecyclerView = findViewById(R.id.recipe_list);

        mRecipeListVieModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

       subscribeObservers();

    }

    private void subscribeObservers(){
        mRecipeListVieModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes!=null){
                    Testing.printRecipes(recipes, "recipes test");
                }

            }
        });
    }

    private void searchRecipesApi(String query, int pageNumber){

        mRecipeListVieModel.searchRecipesApi(query, pageNumber);

    }

    private void testRetrofitRequest() {
        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();
        searchRecipesApi("chicken breast", 1);

    }


}