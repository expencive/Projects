package expencive.vk.com.recipes;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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
import expencive.vk.com.recipes.util.VerticalSpacingItemDecorator;
import expencive.vk.com.recipes.viewmodels.RecipeListViewModel;



public class RecipeListActivity extends BaseActivity implements OnRecipeListener {
    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel mRecipeListVieModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecyclerView = findViewById(R.id.recipe_list);
        mSearchView = findViewById(R.id.seach_view);

        mRecipeListVieModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        initRecyclerView();
       subscribeObservers();
       initSearchView();
       if (!mRecipeListVieModel.isViewingRecipes()) {
           //display seach categories
           displaySeachCategories();
       }
       setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

    }

    private  void  initRecyclerView(){
        mAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (mRecyclerView.canScrollVertically(1)){
                    //search next page
                    mRecipeListVieModel.searchNextPage();
                }
            }
        });


    }

    private void subscribeObservers(){
        mRecipeListVieModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes!=null){
                    if (mRecipeListVieModel.isViewingRecipes()){
                    Testing.printRecipes(recipes, "recipes test");
                    mRecipeListVieModel.setIsPerformingQuery(false);
                    mAdapter.setRecipes(recipes);
                    }
                }


            }
        });
    }



    private void initSearchView(){

        mSearchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mAdapter.displayLoading();
                mRecipeListVieModel.searchRecipesApi(query, 1);
                mSearchView.clearFocus();
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
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", mAdapter.getSelectedRecipe(position));
        startActivity(intent);

    }

    @Override
    public void onCategoryClick(String category) {
        mAdapter.displayLoading();
        mRecipeListVieModel.searchRecipesApi(category, 1);
        mSearchView.clearFocus();

    }

    private void displaySeachCategories(){
        mRecipeListVieModel.setIsViewingRecipes(false);
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {

        if (mRecipeListVieModel.onBackPressed()) {
            super.onBackPressed();
        }
        else {
            displaySeachCategories();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_catigories) {
            displaySeachCategories();
        }
        return super.onOptionsItemSelected(item);
    }
}