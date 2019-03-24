package expencive.vk.com.recipes;


import android.os.Bundle;
import java.util.List;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import expencive.vk.com.recipes.adapters.OnRecipeListener;
import expencive.vk.com.recipes.adapters.RecipeRecyclerAdapter;
import expencive.vk.com.recipes.models.Recipe;
import expencive.vk.com.recipes.requests.RecipeApi;
import expencive.vk.com.recipes.requests.ServiceGenerator;
import expencive.vk.com.recipes.util.Testing;
import expencive.vk.com.recipes.viewmodels.RecipeListViewModel;



public class RecipeListActivity extends BaseActivity implements OnRecipeListener {
    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel mRecipeListVieModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecyclerView = findViewById(R.id.recipe_list);

        mRecipeListVieModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        initRecyclerView();
       subscribeObservers();
       testRetrofitRequest();

    }

    private  void  initRecyclerView(){
        mAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void subscribeObservers(){
        mRecipeListVieModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes!=null){
                    Testing.printRecipes(recipes, "recipes test");
                    mAdapter.setRecipes(recipes);
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


    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

    }
}