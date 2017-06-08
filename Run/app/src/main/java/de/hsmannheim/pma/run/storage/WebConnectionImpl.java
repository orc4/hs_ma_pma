package de.hsmannheim.pma.run.storage;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.hsmannheim.pma.run.model.*;
import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.Profil;
import de.hsmannheim.pma.run.model.Route;
import de.hsmannheim.pma.run.utils.DownloadHandler;


/**
 * Created by aaron on 31.05.17.
 */

public class WebConnectionImpl implements WebConnection {
    //Example für decode und encode von json collections:
    /*
        Gson gson = new Gson();
        String json = gson.toJson(routes);
        Log.i(this.getClass().toString(), "onActivityResult: "+json);

        Type collectionType = new TypeToken<List<Route>>(){}.getType();
        List<Route> routes2= gson.fromJson(json, collectionType);

     */


    private final MyCredentials myCredentials;
    private final String SERVER_CONNECTION = "http://www.orc4.de/pma";
    private DownloadHandler downloadHandler;//lazy initialisation getDownloadHandler
    private Gson gson;

    public WebConnectionImpl(MyCredentials myCredentials) {
        this.myCredentials = myCredentials;
        initGson();
    }

    private DownloadHandler getDownloadHandler() {
        if (downloadHandler == null) {
            downloadHandler = new DownloadHandler();
        }
        return downloadHandler;
    }

    private Gson initGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }


    @Override
    public Route getRoute(long routeId) {
        String url = SERVER_CONNECTION + "/get_route.php?id=" + routeId;
        String json = this.getDownloadHandler().makeServiceCall(url);
        Route route = gson.fromJson(json, Route.class);
        return route;
    }

    @Override
    public Profil getMyProfile() {

        String url = SERVER_CONNECTION + "/get_user_profile.php?username=" + myCredentials.getUsername();
        String json = this.getDownloadHandler().makeServiceCall(url);
        Profil profil = gson.fromJson(json, Profil.class);
        return profil;
    }

    @Override
    public int addChallange(Challenge challenge) {
        String json = gson.toJson(challenge);
        String url = SERVER_CONNECTION + "/add_challenge.php";
        String result = DownloadHandler.makeRequest(url, json);
        int createId = Integer.parseInt(result);
        Log.i(this.getClass().toString(), "addChallenge: Return value: " + createId);
        return createId;
    }

    @Override
    public int addRoute(Route route) {
        //Setzen des Users
        route.setCreateUser(myCredentials.getUsername());
        String json = gson.toJson(route);
        String url = SERVER_CONNECTION + "/add_route.php";
        String result = DownloadHandler.makeRequest(url, json);
        int createId = Integer.parseInt(result);
        Log.i(this.getClass().toString(), "addRoute: Return value: " + createId);
        return createId;
    }

    @Override
    public List<Route> getMyRoutes() {
        String url = SERVER_CONNECTION + "/get_routes.php";
        String json = this.getDownloadHandler().makeServiceCall(url);
        Type collectionType = new TypeToken<List<Route>>() {
        }.getType();
        List<Route> routes = gson.fromJson(json, collectionType);
        return routes;
    }

    @Override
    public List<Challenge> getAllChallanges() {
        String url = SERVER_CONNECTION + "/get_challenges.php";
        String json = this.getDownloadHandler().makeServiceCall(url);
        Type collectionType = new TypeToken<List<Challenge>>() {
        }.getType();
        List<Challenge> challenges = gson.fromJson(json, collectionType);

        return challenges;
    }

    @Override
    public void setChallengeChecked(Challenge challenge) {
        //http://www.orc4.de/pma/add_challenge_user.php?username=aaron&challenge=5
        String url = SERVER_CONNECTION + "/add_challenge_user.php?username=" + myCredentials.getUsername()+"&challenge="+challenge.getId();
        this.getDownloadHandler().makeServiceCall(url);
    }

    @Override
    public boolean checkLogin() {
        String url = SERVER_CONNECTION + "/login.php?username=" + myCredentials.getUsername()+"&password="+myCredentials.getPassword();
        String result = this.getDownloadHandler().makeServiceCall(url);
        Log.i(this.getClass().toString(), "checkLogin: raw: "+result);
        if(result == null){
            return false;
        }
        result=result.trim(); //remove leading and following whitspaces
        if(result.equals("true")){
            return true;
        }else {
            return false;
        }
    }


}
