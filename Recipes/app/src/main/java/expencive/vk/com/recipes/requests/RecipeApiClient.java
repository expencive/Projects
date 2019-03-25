package expencive.vk.com.recipes.requests;

import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import expencive.vk.com.recipes.AppExecutors;
import expencive.vk.com.recipes.models.Recipe;
import expencive.vk.com.recipes.requests.responses.RecipeSearchResponse;
import expencive.vk.com.recipes.util.Constants;
import retrofit2.Call;
import retrofit2.Response;

import static expencive.vk.com.recipes.util.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetriveRecipesRunnable mRetriveRecipesRunnable;

    public static RecipeApiClient getInstance() {
        if (instance ==null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    public void searchRecipesApi(String query, int pageNumber) {
        if (mRetriveRecipesRunnable !=null){
            mRetriveRecipesRunnable = null;
        }
        mRetriveRecipesRunnable = new RetriveRecipesRunnable(query, pageNumber);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetriveRecipesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let the user know its timed out
                handler.cancel(true);

            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetriveRecipesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetriveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query, pageNumber).execute();
                if (cancelRequest){
                    return;
                }
                if (response.code() == 200) {
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body())
                            .getRecipes());
                    if (pageNumber==1){
                        mRecipes.postValue(list);
                    }else{
                        List<Recipe> currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(list);
                        mRecipes.postValue(currentRecipes);
                    }

                }else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mRecipes.postValue(null);

                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }


        }
        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber) {
            return ServiceGenerator.getRecipeApi().searchRecipe(Constants.API_KEY,
                    query, String.valueOf(pageNumber));
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: canceling the search request");
            cancelRequest = true;
        }
    }

    public void cancelRequest(){
        if (mRetriveRecipesRunnable!=null){
            mRetriveRecipesRunnable.cancelRequest();
        }
    }
}
