package expencive.vk.com.recipes.repositories;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import expencive.vk.com.recipes.models.Recipe;

public class RecipeRepository {
    private static RecipeRepository instance;
    private MutableLiveData<List<Recipe>> mRecipes;

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository(){
        mRecipes = new MutableLiveData<>();

    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }
}
