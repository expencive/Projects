package expencive.vk.com.recipes.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import expencive.vk.com.recipes.models.Recipe;
import expencive.vk.com.recipes.repositories.RecipeRepository;

public class RecipeListViewModel extends AndroidViewModel {

    public static final String TAG = "RecipeListViewModel";

    public enum ViewState {CATEGORIES, RECIPES}

    private MutableLiveData<ViewState> viewState;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
    }

    private void init(){
        if (viewState ==null){
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORIES);
        }
    }

    public LiveData<ViewState> getViewstate(){
        return viewState;
    }


//    private RecipeRepository mRecipeRepository;
//    private boolean mIsViewingRecipes;
//    private boolean mIsPerformingQuery;
//
//
//
//    public RecipeListViewModel() {
//        mRecipeRepository = RecipeRepository.getInstance();
//        mIsPerformingQuery = false;
//    }
//
//    public LiveData<List<Recipe>> getRecipes(){
//        return mRecipeRepository.getRecipes();
//    }
//
//    public LiveData<Boolean> isQueryExhausted(){
//        return mRecipeRepository.isQueryExhausted();
//    }
//
//    public void searchRecipesApi(String query, int pageNumber){
//        mIsViewingRecipes = true;
//        mIsPerformingQuery = true;
//
//        mRecipeRepository.searchRecipesApi(query, pageNumber);
//
//    }
//
//    public void searchNextPage(){
//        if(!mIsPerformingQuery && mIsViewingRecipes && !isQueryExhausted().getValue()){
//            mRecipeRepository.searchNextPage();
//        }
//    }
//
//    public boolean isViewingRecipes(){
//        return mIsViewingRecipes;
//    }
//
//    public void setIsViewingRecipes(boolean isViewingRecipes){
//        mIsViewingRecipes = isViewingRecipes;
//
//    }
//
//    public void setIsPerformingQuery(Boolean isPerformingQuery){
//        mIsPerformingQuery = isPerformingQuery;
//
//    }
//
//    public boolean isPerformingQuery(){
//        return mIsPerformingQuery;
//
//    }
//
//    public boolean onBackPressed(){
//        if (mIsPerformingQuery){
//            //cancell the query
//            mRecipeRepository.cancelRequest();
//            mIsPerformingQuery = false;
//        }
//
//        if (mIsViewingRecipes){
//            mIsViewingRecipes=false;
//            return false;
//        }
//        return true;
//    }
}
