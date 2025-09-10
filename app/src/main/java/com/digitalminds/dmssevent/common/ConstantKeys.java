package com.digitalminds.dmssevent.common;

/**
 * Created by Sandeep.kumar on 20-Mar-17.
 */
public class ConstantKeys {

    public static String versionCode = "Version 2.0";

    public static String fireBaseLinkKey = "gs://dmssevents-606bf.appspot.com";
    public static String fBGalleryAlbumName = "DMSS-Events-Images";
    public static String fBProfilePicsAlbumName = "Profile_pics";
    public static String fBAwardsAlbumName = "Milestone2017_awards";
    public static String fBAddNewFeed = "AddNewsFeedsFromUser";



    //public static String baseUrl = "http://www.dmss.co.in/dmssapp/api/"/*"http://192.168.100.92:1010/api/"*/;
    public static String getImagesUrl = "http://www.digitalminds.solutions/dmssapp/docs/";
    //public static String getImagesUrl = "http://www.digitalminds.solutions/DMSSApp/docs/";
    public static String baseUrl = "http://www.digitalminds.solutions/DMSSApp/api/"/*"http://192.168.100.92:1010/api/"*/;
    //public static String baseUrlLocal = "http://192.168.100.92:1010/api/";
    public static String loginUrl = baseUrl + "login/emplogin";
    public static String addingProfilePhotoUrl = baseUrl + "login/AddProfilePhoto";
    public static String eventList = baseUrl+"events/eventlist";
    public static String getAlbumsUrl = baseUrl + "events/getAlbums/";
    public static String createAlbumUrl = baseUrl + "events/CreateAlbum";
    public static String deleteAlbumUrl = baseUrl+"events/deleteAlbum/";
    public static String deleteImageUrl = baseUrl+"events/deleteImage/";
    public static String addImagesUrl = baseUrl+"events/addImages";
    public static String getPerformances = baseUrl+"events/GetPerformer/";
    public static String rating = baseUrl+"events/rating";
    public static String schedule = baseUrl+"events/eventschedule/";
    public static String checkUserUrl = baseUrl + "login/checkuser";
    public static String createPasswordUrl = baseUrl + "login/createpassword";
    public static String awards = baseUrl + "events/eventawards/";
    public static String forgotPassword = baseUrl + "login/forgetPwd?EmailId=";
    public static String statusJsonKey = "Status";
    public static String isPasswordSet = "IsPasswordSet";
    public static String resultKey = "Result";
    public static String formalAwardList = "FormalAwardsList";
    public static String informalAwardList = "InormalAwardsList";
    public static String messageKey = "Message";
    public static String uploadedImagesIdKey = "ids";
    public static String enableEvents =baseUrl+"events/enableEvent/";
    public static String disableEvents =baseUrl+"events/disableEvent/";
    public static String addNewFeed =baseUrl+"news/AddNews";
    public static String getAllNewsFeed =baseUrl+"news/getAllNews/";
    public static String clickFavourite =baseUrl+"news/addLikeComment";
    public static String clickComment =baseUrl+"news/GetCommentsByNewsID/";
    public static String getGameDetailsByID =baseUrl+"games/getGameDetailsById/";
    public static String getAllGames =baseUrl+"games/getAllGames/";
    public static String getAllGamesByEmpId =baseUrl+"games/getAllGamesByEmpId/";
    public static String getTeamMembers =baseUrl+"games/GetTeamMembers/";
    public static String getPerformanceForRemoteControll =baseUrl+"events/GetAllEventPerformer";
    public static String IsFavouriteKey = "IsFavouriteKey";
  //  public static String getImageUrl = "http://192.168.100.92:1010/docs/UserProfileimages/";
  //  public static String getAllImagesUrl = "http://192.168.100.92:1010/docs/Icons/";

    public static String getImageUrl = getImagesUrl+"UserProfileimages/";
    public static String getAllImagesUrl =getImagesUrl+"icons/";
    public static String getAllImagesNewsUrl =getImagesUrl+"images/";
    public static String getNominationAwards =baseUrl+"nomine/getMileStoneAwards";
    public static String getDepartmentList =baseUrl+"nomine/getdept";
    public static String getEmployeeListBasedOnDepart =baseUrl+"nomine/getemp";
    public static String addNomineeUrl =baseUrl+"nomine/addMileStoneNominees";
    public static String registerDevice= baseUrl+"news/refreshToken";
    public static String sportsFightloginUrl = baseUrl + "login/emploginFromSF/";


    //FIREBASE API
    public static String firebasebase_baseurl = "https://dmssevents-606bf.firebaseio.com/DMSSEmployees";
    public static String locationApi =baseUrl+ "login/AddEmpLocation";



    //http://www.digitalminds.solutions/DMSSApp/api/login/AddEmpLocation
}
