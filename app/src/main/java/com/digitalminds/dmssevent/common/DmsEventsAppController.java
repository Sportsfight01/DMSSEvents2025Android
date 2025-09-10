package com.digitalminds.dmssevent.common;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.digitalminds.dmssevent.backendservice.WebService;
import com.digitalminds.dmssevent.models.AlbumsModel;
import com.digitalminds.dmssevent.models.DepartmentSearchModel;
import com.digitalminds.dmssevent.models.EventRatingModel;
import com.digitalminds.dmssevent.models.EventsDetailsModel;
import com.digitalminds.dmssevent.models.GetAllGamesModel;
import com.digitalminds.dmssevent.models.MyBookingsModel;
import com.digitalminds.dmssevent.models.NominationAwardsModel;
import com.digitalminds.dmssevent.models.NominationListModel;
import com.digitalminds.dmssevent.models.PendingBookimgsModel;
import com.digitalminds.dmssevent.models.PhotoDetailsModel;
import com.digitalminds.dmssevent.models.ResultListGamesModel;
import com.digitalminds.dmssevent.models.ScheduleDoublesListModel;
import com.digitalminds.dmssevent.models.ScheduleListGamesModel;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;
import com.digitalminds.dmssevent.models.SportsFightUserModel;
import com.digitalminds.dmssevent.models.TeamsListModel;
import com.digitalminds.dmssevent.models.TodayDoublesListModel;
import com.digitalminds.dmssevent.models.TodaySinglesListModel;
import com.digitalminds.dmssevent.models.UserProfileModel;
import com.google.firebase.FirebaseApp;
//import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * DmsEventsAppController.java -
 *
 * @author Jaya Krishna, sandeepkumar
 * @version 1.0
 * @see MultiDexApplication
 * @since 12-03-2017.
 */
public class DmsEventsAppController extends MultiDexApplication {
    DmsEventsAppController controller;
    Validation validation;
    String emailID = "";
    WebService webService;
    AlbumsModel selectedAlbum;
    PhotoDetailsModel photoDetailsModel;
    EventRatingModel eventRatingModel;
    boolean makeServiceCall = false;
    UserProfileModel userProfileModel;
    EventsDetailsModel selectedEvent;
    String remoteControllerKey="";
    int gamesItemSelectedPosition;
    FirebaseStorage storage;
    StorageReference storageRef;
    String[] names;
    ArrayList<TodaySinglesListModel> todayListModelArraySinglesList;
    ArrayList<TeamsListModel> teamsListModelArrayList;
    ArrayList<ScheduleListGamesModel> scheduleListGamesModelArrayList;
    ArrayList<ResultListGamesModel> resultListGamesModelArrayList;
    ArrayList<ResultListGamesModel> mainResultListGamesModelArrayList;
    ArrayList<TodayDoublesListModel> todayDoublesListOfGamesModelArrayList;
    ArrayList<TodayDoublesListModel> todayDoublesListOfGamesTypeTodayArrayList;
    ArrayList<TeamsListModel> teamsDoublesListOfGamesTypeTodayArrayList;
    ArrayList<ScheduleDoublesListModel> todayDoublesListOfGamesTypeScheduleArrayList;
    ArrayList<TodayDoublesListModel> todayDoublesListOfGamesTypeResultArrayList;
    ArrayList<GetAllGamesModel> getAllGamesModelArrayList;
    ArrayList<TodaySinglesListModel> myGamesModelArraySinglesList;
    ArrayList<TodayDoublesListModel> myGamesModelArrayDoublesList;
    ArrayList<NominationListModel> nominationListModelArrayList;
    ArrayList<PendingBookimgsModel> pendingBookimgsModelArrayList;
    ArrayList<SelectedPlayersResultModel> firstPlayersArrayList;
    ArrayList<SelectedPlayersResultModel> secondPlayersArrayList;
    int commentsListSizes;
    boolean cameraPermissionAvailable = false;
    boolean galleryPermissionAvailable = true;
    boolean guideLines;
    int nominationCount;
    NominationAwardsModel selectedNominationCriteria;
    int nomineeCount;

    boolean introductionReadTrueOrFalse=false;
    boolean fromNotification = false;
    String token="";
    boolean notificationCalled=false;
    SportsFightUserModel sportsFightUserModel;
    String sportsFightDataString="";
    String sessionTokenSF="";
    boolean loggedInStatus=false;
    boolean callFromNotification=false;
    ArrayList<SelectedPlayersResultModel> showPlayersResultModelArrayList;
    ArrayList<SelectedPlayersResultModel> showPlayersFromAddPlayerActArrayList;
    SelectedPlayersResultModel showPlayersModelController;
    ArrayList<SelectedPlayersResultModel> appControlArrayListOfSelectedPlayers;
    ArrayList<DepartmentSearchModel> departmentSearchModelArrayList;
    boolean callingFromPendingNotif=false;
    int screenWidth=0;
    boolean addPlayerOrNot=false;

    ArrayList<MyBookingsModel> playersStatusForAddButton;

    @Override
    public void onCreate() {
        controller = this;
        System.out.println("initalizeAllElements:: "+controller);


        initalizeAllElements();
        super.onCreate();

    }

    private void initalizeAllElements() {
//        FirebaseInstanceId.getInstance().getToken();
        FirebaseApp init=FirebaseApp.initializeApp(controller);
//        FirebaseApp init1=FirebaseApp.initializeApp(this);


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl(ConstantKeys.fireBaseLinkKey);
        webService = new WebService(getContext());
        validation = new Validation(getContext());
        userProfileModel = DmsSharedPreferences.getUserDetails(getContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public StorageReference getStorageRef() {
        return storageRef;
    }

    public void setStorageRef(StorageReference storageRef) {
        this.storageRef = storageRef;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    public EventsDetailsModel getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(EventsDetailsModel selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Context getContext() {
        return getApplicationContext();
    }

    public DmsEventsAppController getInstance() {
        return controller;
    }

    public DmsEventsAppController getController() {
        return controller;
    }

    public void setController(DmsEventsAppController controller) {
        this.controller = controller;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public WebService getWebService() {
        return webService;
    }

    public void setWebService(WebService webService) {
        this.webService = webService;
    }

    public AlbumsModel getSelectedAlbum() {
        return selectedAlbum;
    }

    public void setSelectedAlbum(AlbumsModel selectedAlbum) {
        this.selectedAlbum = selectedAlbum;
    }

    public PhotoDetailsModel getPhotoDetailsModel() {
        return photoDetailsModel;
    }

    public void setPhotoDetailsModel(PhotoDetailsModel photoDetailsModel) {
        this.photoDetailsModel = photoDetailsModel;
    }

    public EventRatingModel getEventRatingModel() {
        return eventRatingModel;
    }

    public void setEventRatingModel(EventRatingModel eventRatingModel) {
        this.eventRatingModel = eventRatingModel;
    }

    public boolean isMakeServiceCall() {
        return makeServiceCall;
    }

    public void setMakeServiceCall(boolean makeServiceCall) {
        this.makeServiceCall = makeServiceCall;
    }

    public UserProfileModel getUserProfileModel() {
        return userProfileModel;
    }

    public void setUserProfileModel(UserProfileModel userProfileModel) {
        this.userProfileModel = userProfileModel;
    }

    public String getRemoteControllerKey() {
        return remoteControllerKey;
    }

    public void setRemoteControllerKey(String remoteControllerKey) {
        this.remoteControllerKey = remoteControllerKey;
    }

    public int getGamesItemSelectedPosition() {
        return gamesItemSelectedPosition;
    }

    public void setGamesItemSelectedPosition(int gamesItemSelectedPosition) {
        this.gamesItemSelectedPosition = gamesItemSelectedPosition;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public ArrayList<TodaySinglesListModel> getTodayListModelArraySinglesList() {
        return todayListModelArraySinglesList;
    }

    public void setTodayListModelArraySinglesList(ArrayList<TodaySinglesListModel> todayListModelArraySinglesList) {
        this.todayListModelArraySinglesList = todayListModelArraySinglesList;
    }

    public ArrayList<TeamsListModel> getTeamsListModelArrayList() {
        return teamsListModelArrayList;
    }

    public void setTeamsListModelArrayList(ArrayList<TeamsListModel> teamsListModelArrayList) {
        this.teamsListModelArrayList = teamsListModelArrayList;
    }

    public ArrayList<ScheduleListGamesModel> getScheduleListGamesModelArrayList() {
        return scheduleListGamesModelArrayList;
    }

    public void setScheduleListGamesModelArrayList(ArrayList<ScheduleListGamesModel> scheduleListGamesModelArrayList) {
        this.scheduleListGamesModelArrayList = scheduleListGamesModelArrayList;
    }

    public ArrayList<TodayDoublesListModel> getTodayDoublesListOfGamesModelArrayList() {
        return todayDoublesListOfGamesModelArrayList;
    }

    public void setTodayDoublesListOfGamesModelArrayList(ArrayList<TodayDoublesListModel> todayDoublesListOfGamesModelArrayList) {
        this.todayDoublesListOfGamesModelArrayList = todayDoublesListOfGamesModelArrayList;
    }

    public ArrayList<GetAllGamesModel> getGetAllGamesModelArrayList() {
        return getAllGamesModelArrayList;
    }

    public void setGetAllGamesModelArrayList(ArrayList<GetAllGamesModel> getAllGamesModelArrayList) {
        this.getAllGamesModelArrayList = getAllGamesModelArrayList;
    }

    public int getCommentsListSizes() {
        return commentsListSizes;
    }

    public void setCommentsListSizes(int commentsListSizes) {
        this.commentsListSizes = commentsListSizes;
    }

    public boolean isCameraPermissionAvailable() {
        return cameraPermissionAvailable;
    }

    public void setCameraPermissionAvailable(boolean cameraPermissionAvailable) {
        this.cameraPermissionAvailable = cameraPermissionAvailable;
    }

    public boolean isGalleryPermissionAvailable() {
        return galleryPermissionAvailable;
    }

    public void setGalleryPermissionAvailable(boolean galleryPermissionAvailable) {
        this.galleryPermissionAvailable = galleryPermissionAvailable;
    }

    public ArrayList<ResultListGamesModel> getResultListGamesModelArrayList() {
        return resultListGamesModelArrayList;
    }

    public void setResultListGamesModelArrayList(ArrayList<ResultListGamesModel> resultListGamesModelArrayList) {
        this.resultListGamesModelArrayList = resultListGamesModelArrayList;
    }

    public ArrayList<TodaySinglesListModel> getMyGamesModelArraySinglesList() {
        return myGamesModelArraySinglesList;
    }

    public void setMyGamesModelArraySinglesList(ArrayList<TodaySinglesListModel> myGamesModelArraySinglesList) {
        this.myGamesModelArraySinglesList = myGamesModelArraySinglesList;
    }

    public ArrayList<TodayDoublesListModel> getMyGamesModelArrayDoublesList() {
        return myGamesModelArrayDoublesList;
    }

    public void setMyGamesModelArrayDoublesList(ArrayList<TodayDoublesListModel> myGamesModelArrayDoublesList) {
        this.myGamesModelArrayDoublesList = myGamesModelArrayDoublesList;
    }

    public ArrayList<TodayDoublesListModel> getTodayDoublesListOfGamesTypeTodayArrayList() {
        return todayDoublesListOfGamesTypeTodayArrayList;
    }

    public void setTodayDoublesListOfGamesTypeTodayArrayList(ArrayList<TodayDoublesListModel> todayDoublesListOfGamesTypeTodayArrayList) {
        this.todayDoublesListOfGamesTypeTodayArrayList = todayDoublesListOfGamesTypeTodayArrayList;
    }

    public ArrayList<ScheduleDoublesListModel> getTodayDoublesListOfGamesTypeScheduleArrayList() {
        return todayDoublesListOfGamesTypeScheduleArrayList;
    }

    public void setTodayDoublesListOfGamesTypeScheduleArrayList(ArrayList<ScheduleDoublesListModel> todayDoublesListOfGamesTypeScheduleArrayList) {
        this.todayDoublesListOfGamesTypeScheduleArrayList = todayDoublesListOfGamesTypeScheduleArrayList;
    }

    public ArrayList<TodayDoublesListModel> getTodayDoublesListOfGamesTypeResultArrayList() {
        return todayDoublesListOfGamesTypeResultArrayList;
    }

    public void setTodayDoublesListOfGamesTypeResultArrayList(ArrayList<TodayDoublesListModel> todayDoublesListOfGamesTypeResultArrayList) {
        this.todayDoublesListOfGamesTypeResultArrayList = todayDoublesListOfGamesTypeResultArrayList;
    }

    public ArrayList<TeamsListModel> getTeamsDoublesListOfGamesTypeTodayArrayList() {
        return teamsDoublesListOfGamesTypeTodayArrayList;
    }

    public void setTeamsDoublesListOfGamesTypeTodayArrayList(ArrayList<TeamsListModel> teamsDoublesListOfGamesTypeTodayArrayList) {
        this.teamsDoublesListOfGamesTypeTodayArrayList = teamsDoublesListOfGamesTypeTodayArrayList;
    }

    public boolean isGuideLines() {
        return guideLines;
    }

    public void setGuideLines(boolean guideLines) {
        this.guideLines = guideLines;
    }

    public int getNominationCount() {
        return nominationCount;
    }

    public void setNominationCount(int nominationCount) {
        this.nominationCount = nominationCount;
    }

    public NominationAwardsModel getSelectedNominationCriteria() {
        return selectedNominationCriteria;
    }

    public void setSelectedNominationCriteria(NominationAwardsModel selectedNominationCriteria) {
        this.selectedNominationCriteria = selectedNominationCriteria;
    }

    public ArrayList<ResultListGamesModel> getMainResultListGamesModelArrayList() {
        return mainResultListGamesModelArrayList;
    }

    public void setMainResultListGamesModelArrayList(ArrayList<ResultListGamesModel> mainResultListGamesModelArrayList) {
        this.mainResultListGamesModelArrayList = mainResultListGamesModelArrayList;
    }

    public int getNomineeCount() {
        return nomineeCount;
    }

    public void setNomineeCount(int nomineeCount) {
        this.nomineeCount = nomineeCount;
    }

    public ArrayList<NominationListModel> getNominationListModelArrayList() {
        return nominationListModelArrayList;
    }

    public void setNominationListModelArrayList(ArrayList<NominationListModel> nominationListModelArrayList) {
        this.nominationListModelArrayList = nominationListModelArrayList;
    }

    public boolean isIntroductionReadTrueOrFalse() {
        return introductionReadTrueOrFalse;
    }

    public void setIntroductionReadTrueOrFalse(boolean introductionReadTrueOrFalse) {
        this.introductionReadTrueOrFalse = introductionReadTrueOrFalse;
    }

    public boolean isFromNotification() {
        return fromNotification;
    }

    public void setFromNotification(boolean fromNotification) {
        this.fromNotification = fromNotification;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isNotificationCalled() {
        return notificationCalled;
    }

    public void setNotificationCalled(boolean notificationCalled) {
        this.notificationCalled = notificationCalled;
    }

    public SportsFightUserModel getSportsFightUserModel() {
        return sportsFightUserModel;
    }

    public void setSportsFightUserModel(SportsFightUserModel sportsFightUserModel) {
        this.sportsFightUserModel = sportsFightUserModel;
    }

    public String getSportsFightDataString() {
        return sportsFightDataString;
    }

    public void setSportsFightDataString(String sportsFightDataString) {
        this.sportsFightDataString = sportsFightDataString;
    }

    public String getSessionTokenSF() {
        return sessionTokenSF;
    }

    public void setSessionTokenSF(String sessionTokenSF) {
        this.sessionTokenSF = sessionTokenSF;
    }

    public boolean isLoggedInStatus() {
        return loggedInStatus;
    }

    public void setLoggedInStatus(boolean loggedInStatus) {
        this.loggedInStatus = loggedInStatus;
    }

    public boolean isCallFromNotification() {
        return callFromNotification;
    }

    public void setCallFromNotification(boolean callFromNotification) {
        this.callFromNotification = callFromNotification;
    }

    public ArrayList<PendingBookimgsModel> getPendingBookimgsModelArrayList() {
        return pendingBookimgsModelArrayList;
    }

    public void setPendingBookimgsModelArrayList(ArrayList<PendingBookimgsModel> pendingBookimgsModelArrayList) {
        this.pendingBookimgsModelArrayList = pendingBookimgsModelArrayList;
    }

    public ArrayList<SelectedPlayersResultModel> getShowPlayersResultModelArrayList() {
        return showPlayersResultModelArrayList;
    }

    public void setShowPlayersResultModelArrayList(ArrayList<SelectedPlayersResultModel> showPlayersResultModelArrayList) {
        this.showPlayersResultModelArrayList = showPlayersResultModelArrayList;
    }

    public SelectedPlayersResultModel getShowPlayersModelController() {
        return showPlayersModelController;
    }

    public void setShowPlayersModelController(SelectedPlayersResultModel showPlayersModelController) {
        this.showPlayersModelController = showPlayersModelController;
    }

    public ArrayList<SelectedPlayersResultModel> getAppControlArrayListOfSelectedPlayers() {
        return appControlArrayListOfSelectedPlayers;
    }

    public void setAppControlArrayListOfSelectedPlayers(ArrayList<SelectedPlayersResultModel> appControlArrayListOfSelectedPlayers) {
        this.appControlArrayListOfSelectedPlayers = appControlArrayListOfSelectedPlayers;
    }

    public boolean isCallingFromPendingNotif() {
        return callingFromPendingNotif;
    }

    public void setCallingFromPendingNotif(boolean callingFromPendingNotif) {
        this.callingFromPendingNotif = callingFromPendingNotif;
    }

    public ArrayList<DepartmentSearchModel> getDepartmentSearchModelArrayList() {
        return departmentSearchModelArrayList;
    }

    public void setDepartmentSearchModelArrayList(ArrayList<DepartmentSearchModel> departmentSearchModelArrayList) {
        this.departmentSearchModelArrayList = departmentSearchModelArrayList;
    }


    public ArrayList<SelectedPlayersResultModel> getShowPlayersFromAddPlayerActArrayList() {
        return showPlayersFromAddPlayerActArrayList;
    }

    public void setShowPlayersFromAddPlayerActArrayList(ArrayList<SelectedPlayersResultModel> showPlayersFromAddPlayerActArrayList) {
        this.showPlayersFromAddPlayerActArrayList = showPlayersFromAddPlayerActArrayList;
    }

    public ArrayList<SelectedPlayersResultModel> getFirstPlayersArrayList() {
        return firstPlayersArrayList;
    }

    public void setFirstPlayersArrayList(ArrayList<SelectedPlayersResultModel> firstPlayersArrayList) {
        this.firstPlayersArrayList = firstPlayersArrayList;
    }

    public ArrayList<SelectedPlayersResultModel> getSecondPlayersArrayList() {
        return secondPlayersArrayList;
    }

    public void setSecondPlayersArrayList(ArrayList<SelectedPlayersResultModel> secondPlayersArrayList) {
        this.secondPlayersArrayList = secondPlayersArrayList;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public  ArrayList<MyBookingsModel> getPlayersStatusForAddButton() {
        return playersStatusForAddButton;
    }

    public void setPlayersStatusForAddButton( ArrayList<MyBookingsModel> playersStatusForAddButton) {
        this.playersStatusForAddButton = playersStatusForAddButton;
    }

    public boolean isAddPlayerOrNot() {
        return addPlayerOrNot;
    }

    public void setAddPlayerOrNot(boolean addPlayerOrNot) {
        this.addPlayerOrNot = addPlayerOrNot;
    }

}
