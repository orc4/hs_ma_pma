package de.hsmannheim.pma.run.storage;

import java.util.ArrayList;
import java.util.List;

import de.hsmannheim.pma.run.model.*;


/**
 * Created by aaron on 31.05.17.
 */

public interface WebConnection {
    //public List<Challenge> getChallangesForUser();
    //public boolean sendChallange(Challenge challange, String UserTo);
    //public List<String> getFriends();
    public Route getRoute(long routeId);
    public Profil getMyProfile();
    public int addChallange(Challenge challenge);
    public int addRoute(Route route);
    public int addRouteAnalyse(RouteAnalyse routeAnalyse);
    public ArrayList<Route> getMyRoutes();
    public ArrayList<RouteAnalyse> getMyRouteAnalyses();
    public ArrayList<Challenge> getAllChallanges();
    public void setChallengeChecked(Challenge challenge);
    public boolean checkLogin();
}