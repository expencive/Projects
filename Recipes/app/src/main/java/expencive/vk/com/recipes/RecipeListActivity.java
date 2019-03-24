package expencive.vk.com.recipes;


import android.os.Bundle;
import android.widget.SearchView;

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
       initSearchView();

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



    private void initSearchView(){
        final androidx.appcompat.widget.SearchView searchView = findViewById(R.id.seach_view);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRecipeListVieModel.searchRecipesApi(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

    }
}