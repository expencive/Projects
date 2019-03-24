package expencive.vk.com.recipes.util;

import android.util.Log;

import java.util.List;

import expencive.vk.com.recipes.models.Recipe;

public class Testing {

    public static void printRecipes(List<Recipe> list, String tag) {
        for (Recipe recipe: list){
            Log.d(tag, "onChanged: " + recipe.getTitle());
        }
    }
}
