package expencive.vk.com.recipes.repositories;

import android.content.Context;
import android.util.Log;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import expencive.vk.com.recipes.AppExecutors;
import expencive.vk.com.recipes.models.Recipe;
import expencive.vk.com.recipes.persistence.RecipeDao;
import expencive.vk.com.recipes.persistence.RecipeDatabase;
import expencive.vk.com.recipes.requests.RecipeApiClient;
import expencive.vk.com.recipes.requests.ServiceGenerator;
import expencive.vk.com.recipes.requests.responses.ApiResponse;
import expencive.vk.com.recipes.requests.responses.RecipeSearchResponse;
import expencive.vk.com.recipes.util.Constants;
import expencive.vk.com.recipes.util.NetworkBoundResource;
import expencive.vk.com.recipes.util.Resource;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeDao recipeDao;
    private static final String TAG = "RecipeRepository";

    public static RecipeRepository getInstance(Context context){
        if(instance == null){
            instance = new RecipeRepository(context);
        }
        return instance;
    }


    private RecipeRepository(Context context) {
        recipeDao = RecipeDatabase.getInstance(context).getRecipeDao();
    }

    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String query, final int pageNumber){
        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance() ){

            @Override
            public void saveCallResult(@NonNull RecipeSearchResponse item) {
                if (item.getRecipes() !=null){//recipe list wiil be null if apikey is expired

                    Recipe[] recipes = new Recipe[item.getRecipes().size()];

                    int index = 0;

                    for (long rowid: recipeDao.insertRecipes((Recipe[]) (item.getRecipes().toArray(recipes)))){
                        if (rowid==-1){
                            Log.d(TAG, "saveCallResult: CONFLICT... This recipe is already in the cache ");
                            //if recipe already exist i dont want to set ingridients or timestamp because
                            //they will be erased
                            recipeDao.updateRecipe(
                                    recipes[index].getRecipe_id(),
                                    recipes[index].getTitle(),
                                    recipes[index].getPublisher(),
                                    recipes[index].getImage_url(),
                                    recipes[index].getSocial_rank()
                            );
                        }
                        index++;
                    }

                }

            }

            @Override
            public boolean shouldFetch(@Nullable List<Recipe> data) {
                return true; // always query the network since the queries can be anything
            }

            @NonNull
            @Override
            public LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.searchRecipes(query, pageNumber);
            }

            @NonNull
            @Override
            public LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return ServiceGenerator.getRecipeApi()
                        .searchRecipe(
                                Constants.API_KEY,
                                query, String.valueOf(pageNumber)
                        );
            }

        }.getAsLiveData();
    }
}

    //    private static RecipeRepository instance;
//    private RecipeApiClient mRecipeApiClient;
//    private String mQuery;
//    private int mPageNumber;
//    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
//    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();
//
//    public static RecipeRepository getInstance() {
//        if (instance == null) {
//            instance = new RecipeRepository();
//        }
//        return instance;
//    }
//
//    private RecipeRepository(){
//        mRecipeApiClient = RecipeApiClient.getInstance();
//        initMediators();
//
//
//    }
//
//
//    private void initMediators(){
//        LiveData<List<Recipe>> recipeListApiSource = mRecipeApiClient.getRecipes();
//        mRecipes.addSource(recipeListApiSource, new Observer<List<Recipe>>() {
//            @Override
//            public void onChanged(List<Recipe> recipes) {
//                if (recipes !=null){
//                    mRecipes.setValue(recipes);
//                    doneQuery(recipes);
//                }else{
//                    //seach database cache
//                    doneQuery(null);
//                }
//            }
//        });
//    }
//    private void doneQuery(List<Recipe> list){
//        if (list!=null){
//            if (list.size() % 30 !=0){
//                mIsQueryExhausted.setValue(true);
//            }
//        }else{
//            mIsQueryExhausted.setValue(true);
//        }
//    }
//
//    public LiveData<Boolean> isQueryExhausted(){
//        return mIsQueryExhausted;
//    }
//
//    public LiveData<List<Recipe>> getRecipes() {
//        return mRecipes;
//    }
//
//
//    public LiveData<Recipe> getRecipe() {
//        return mRecipeApiClient.getRecipe();
//    }
//
//    public LiveData<Boolean> isRecipeRequestTimedOut() {
//        return mRecipeApiClient.isRecipeRequestTimedOut();
//    }
//
//    public void searchRecipeById(String recipeId){
//        mRecipeApiClient.searchRecipeById(recipeId);
//    }
//
//    public void searchRecipesApi(String query, int pageNumber){
//        if (pageNumber==0){
//            pageNumber=1;
//        }
//        mQuery = query;
//        mPageNumber = pageNumber;
//        mIsQueryExhausted.setValue(false);
//        mRecipeApiClient.searchRecipesApi(query, pageNumber);
//
//    }
//
//    public void searchNextPage(){
//        searchRecipesApi(mQuery, mPageNumber+1);
//    }
//
//    public void cancelRequest(){
//        mRecipeApiClient.cancelRequest();
//
//    }

