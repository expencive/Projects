package expencive.vk.com.recipes.repositories;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import expencive.vk.com.recipes.models.Recipe;
import expencive.vk.com.recipes.requests.RecipeApiClient;

public class RecipeRepository {
    private static RecipeRepository instance;
    private RecipeApiClient mRecipeApiClient;
    private String mQuery;
    private int mPageNumber;

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository(){
        mRecipeApiClient = RecipeApiClient.getInstance();


    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeApiClient.getRecipes();
    }


    public LiveData<Recipe> getRecipe() {
        return mRecipeApiClient.getRecipe();
    }

    public LiveData<Boolean> isRecipeRequestTimedOut() {
        return mRecipeApiClient.isRecipeRequestTimedOut();
    }

    public void searchRecipeById(String recipeId){
        mRecipeApiClient.searchRecipeById(recipeId);
    }

    public void searchRecipesApi(String query, int pageNumber){
        if (pageNumber==0){
            pageNumber=1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mRecipeApiClient.searchRecipesApi(query, pageNumber);

    }

    public void searchNextPage(){
        searchRecipesApi(mQuery, mPageNumber+1);
    }

    public void cancelRequest(){
        mRecipeApiClient.cancelRequest();

    }
}
