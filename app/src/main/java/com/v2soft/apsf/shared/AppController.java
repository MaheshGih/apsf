package com.v2soft.apsf.shared;

import android.app.Application;
import android.location.Address;

import com.v2soft.apsf.model.ResultItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by srikanth.m on 3/1/2018.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private static AppController mInstance;
    public static String user = "";

    public static boolean isRunningTask = false;

    public static ArrayList<String> mListCountries = new ArrayList<>();
    public static ArrayList<ResultItem> mCountriesList = new ArrayList<>();
    public static HashMap<String, String> mHashCountries = new HashMap<>();

    public static ArrayList<String> mListStates = new ArrayList<>();
    public static ArrayList<ResultItem> mStatesList = new ArrayList<>();
    public static HashMap<String, String> mHashStates = new HashMap<>();

    public static ArrayList<String> mListDistricts = new ArrayList<>();
    public static ArrayList<ResultItem> mDistrictList = new ArrayList<>();
    public static HashMap<String, String> mHashDistricts = new HashMap<>();
    public static HashMap<String, ArrayList<ResultItem>> mHashDistrictsList = new HashMap<>();

    public static ArrayList<String> mListMandals = new ArrayList<>();
    public static ArrayList<ResultItem> mMandalsList = new ArrayList<>();
    public static HashMap<String, String> mHashMandals = new HashMap<>();
    public static HashMap<String, ArrayList<ResultItem>> mHashMandalsList = new HashMap<>();

    public static ArrayList<String> mListVillages = new ArrayList<>();
    public static ArrayList<ResultItem> mVillagesList = new ArrayList<>();
    public static HashMap<String, String> mHashVillages = new HashMap<>();
    public static HashMap<String, ArrayList<ResultItem>> mHashVillagesList = new HashMap<>();

    public static ArrayList<String> mListConstituences = new ArrayList<>();
    public static ArrayList<ResultItem> mConstituencesList = new ArrayList<>();
    public static HashMap<String, String> mHashConstituences = new HashMap<>();

    public static ArrayList<String> mListBloodGroups = new ArrayList<>();
    public static ArrayList<ResultItem> mBloodGroupsList = new ArrayList<>();
    public static HashMap<String, String> mHashBloodGroups = new HashMap<>();

    public static ArrayList<String> mListJobTypes = new ArrayList<>();
    public static ArrayList<ResultItem> mJobTypesList = new ArrayList<>();
    public static HashMap<String, String> mHashJobTypes = new HashMap<>();

    public static ArrayList<String> mListProfessions = new ArrayList<>();
    public static ArrayList<ResultItem> mProfessionsList = new ArrayList<>();
    public static HashMap<String, String> mHashProfessions = new HashMap<>();

    public static ArrayList<String> mListDepartments = new ArrayList<>();
    public static ArrayList<ResultItem> mDepartmentsList = new ArrayList<>();
    public static HashMap<String, String> mHashDepartments = new HashMap<>();

    public static ArrayList<String> mListSalaryRanges = new ArrayList<>();
    public static ArrayList<ResultItem> mSalaryRangesList = new ArrayList<>();
    public static HashMap<String, String> mHashSalaryRanges = new HashMap<>();

    public static ArrayList<String> mListPositions = new ArrayList<>();
    public static ArrayList<ResultItem> mPositionsList = new ArrayList<>();
    public static HashMap<String, String> mHashPositions = new HashMap<>();

    public static ArrayList<String> mListCities = new ArrayList<>();
    public static ArrayList<ResultItem> mCitiesList = new ArrayList<>();
    public static HashMap<String, String> mHashCities = new HashMap<>();

    public static ArrayList<String> mListSubCastes = new ArrayList<>();
    public static ArrayList<ResultItem> mSubCastesList = new ArrayList<>();
    public static HashMap<String, String> mHashSubCastes = new HashMap<>();
    public static HashMap<String, ArrayList<ResultItem>> mHashSubCastesList = new HashMap<>();

    public static ArrayList<String> mListCategories = new ArrayList<>();
    public static ArrayList<ResultItem> mCategoriesList = new ArrayList<>();
    public static HashMap<String, String> mHashCategories = new HashMap<>();

    public static ArrayList<String> mListQualifications = new ArrayList<>();
    public static ArrayList<ResultItem> mQualificationsList = new ArrayList<>();
    public static HashMap<String, String> mHashQualifications = new HashMap<>();

    public static HashMap<String, Address> mHashAddresses = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
}
