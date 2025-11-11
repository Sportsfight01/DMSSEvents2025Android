package com.digitalminds.dmssevent.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Utils {
    public static String CategoryList = "CategoryList";
    public static String SubCategories = "SubCategories";
    public static String ComplaintTypes = "ComplaintTypes";
    public static String StatusList = "StatusList";
    public static String AreaList = "AreaList";

    public static String AdminId = "198";
//    public static String statusListData = "198";
public static String[] statusArr = { "InProgress", "Pending","Closed"};

    public interface CallbackListner {
        public void onReturn(String response);
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static List<ComplaintStatusRes> parseComplaintsStatusData() {

        List<ComplaintStatusRes> complaintStatusResList = new ArrayList<ComplaintStatusRes>();

     /*  ComplaintStatusRes complaintStatusRes1 = new ComplaintStatusRes();
        ComplaintsResponse complaintStatusRes2 = new ComplaintsResponse();
        ComplaintsResponse complaintStatusRes3 = new ComplaintsResponse();*/


        for (int i = 0; i < statusArr.length; i++) {
            ComplaintStatusRes complaintStatusRes = new ComplaintStatusRes();
            complaintStatusRes.setStatusId(i + 2);
            complaintStatusRes.setStatusName(statusArr[i]);
            complaintStatusResList.add(complaintStatusRes);
        }

        return complaintStatusResList;
    }

    public static HashMap<String, List<?>> parseMasterData(String jsonData) {

        Gson gson = new Gson();
        ComplaintsRootData root = gson.fromJson(jsonData, ComplaintsRootData.class);

        List<Category> categories = root.data.CategoryList;
        List<SubCategory> subCategories = root.data.SubCategories;
        List<ComplaintType> complaintTypes = root.data.ComplaintTypes;
        List<Status> statusList = root.data.StatusList;
        List<AreaData> areaList = root.data.AreaList;


        HashMap<String, List<?>> dataMap = new HashMap<>();

        dataMap.put(CategoryList, categories);
        dataMap.put(SubCategories, subCategories);
        dataMap.put(ComplaintTypes, complaintTypes);
        dataMap.put(StatusList, statusList);
        dataMap.put(AreaList, areaList);

        System.out.println("Key Value: " + dataMap.get("CategoryList"));
//        List<Category> CategoryList= (List<Category>) dataMap.get("CategoryList");
        // Example: Print each list from the map
//        System.out.println("Key Value: " + CategoryList);
        /* for (Category item : CategoryList) {
                System.out.println(" Category:: " + item.Name);
            }*/
       /* for (String key : dataMap.keySet()) {
            System.out.println("Key: " + key);


        }*/

        return dataMap;
    }

    public static List<ComplaintsData> parseMyComplaintsData(String jsonData) {

        Gson gson = new Gson();
        ComplaintsResponse root = gson.fromJson(jsonData, ComplaintsResponse.class);

        List<ComplaintsData> complaintsDataList = root.getData();

        System.out.println("complaintsDataList:: " + complaintsDataList.size());

        return complaintsDataList;
    }

    public static List<SubCategory> filterSubCategories(List<SubCategory> allSubCategories, int selectedCategoryId) {
        List<SubCategory> filteredList = new ArrayList<>();
        for (SubCategory sub : allSubCategories) {
            if (sub.getCategoryId() == selectedCategoryId) {
                filteredList.add(sub);
            }
        }
        return filteredList;
    }
    public static List<Category> filterCategories(List<Category> allSubCategories, int selectedAreaId) {
        List<Category> filteredList = new ArrayList<>();
        for (Category sub : allSubCategories) {
            if (sub.getAreaId() == selectedAreaId) {
                filteredList.add(sub);
            }
        }
        return filteredList;
    }

    public void showConfirmationDialog(Context context, String message,CallbackListner callbackListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name)).setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        callbackListener.onReturn("Success");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        callbackListener.onReturn("Fail");
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void showSuccessDialog(Context context, String message,CallbackListner callbackListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name)).setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        callbackListener.onReturn("Success");
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public List<ComplaintsData> sortByDate(List<ComplaintsData> complaintsList){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Collections.sort(complaintsList, new Comparator<ComplaintsData>() {
            @Override
            public int compare(ComplaintsData o1, ComplaintsData o2) {
                try {
                    Date date1 = sdf.parse(o1.getLastUpdatedOn());
                    Date date2 = sdf.parse(o2.getLastUpdatedOn());
                    return date2.compareTo(date1); // descending order
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        return complaintsList;
    }
}


