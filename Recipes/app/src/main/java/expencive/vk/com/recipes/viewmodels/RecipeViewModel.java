package expencive.vk.com.recipes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import expencive.vk.com.recipes.models.Recipe;
import expencive.vk.com.recipes.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private String mRecipeId;
    private Boolean mDidRetriveRecipe;

    public RecipeViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mDidRetriveRecipe = false;
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipeRepository.getRecipe();
    }

    public void searchRecipeById(String recipeId){
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeById(recipeId);
    }

    public LiveData<Boolean> isRecipeRequestTimedOut() {
        return mRecipeRepository.isRecipeRequestTimedOut();
    }

    public String getRecipeId() {
        return mRecipeId;
    }

    public void setRetivedRecipe(boolean retivedRecipe){
        mDidRetriveRecipe  = retivedRecipe;
    }

    public boolean didRetriveRecipe(){
        return mDidRetriveRecipe;
    }
}
