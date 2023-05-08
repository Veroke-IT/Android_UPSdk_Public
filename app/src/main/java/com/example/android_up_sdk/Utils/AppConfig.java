package com.example.android_up_sdk.Utils;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import androidx.core.content.ContextCompat;
import com.example.android_up_sdk.BuildConfig;
import com.example.android_up_sdk.Dialogs.CustomAlertDialog;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_CategorySorting;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_Recent_Outlet;
import com.example.android_up_sdk.HomeAuxiliaries.ModelClasses.DModel_User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AppConfig {
    Context mContext;
    public boolean environment;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    public ArrayList<String> lstInstoreRecentSearches;
    public ArrayList<DModel_Recent_Outlet> lstRecentViewed;
    public DModel_CategorySorting mCategorySorting;
    public static final int MAX_RECENT_SEARCHES = 5;
    public static final int MAX_RECENT_VIEWED = 10;
    public boolean isAppLangArabic;
    public String mLanguage;
    private static AppConfig ourInstance;
    public DModel_User mUser;

    public AppConfig(Context _mContext) {
        if (_mContext != null) {

            this.mContext = _mContext;

            this.sharedPref = mContext.getSharedPreferences(_mContext.getApplicationInfo().packageName + "_preferences", Context.MODE_PRIVATE);
            this.editor = sharedPref.edit();
            mLanguage = "";
            environment = false;
            mCategorySorting = new DModel_CategorySorting();
            mUser = new DModel_User();

            loadUserData();

        }
    }

    public static void initInstance(Context _mContext) {
        if (ourInstance == null) {
            // Create the instance
            ourInstance = new AppConfig(_mContext);

        }
    }

    public static AppConfig getInstance() {
        return ourInstance;
    }

    public String getBaseUrlImage() {
        if (environment)
            return AppConstt.ServerUrl.REl_URL_IMG;
        else
            return AppConstt.ServerUrl.DEB_URL_IMG;
    }
    public String getBaseUrlThumbs() {
        if (environment)
            return AppConstt.ServerUrl.REl_URL_THUMBS;
        else
            return AppConstt.ServerUrl.DEB_URL_THUMBS;
    }

    public String getBaseUrlApiMobile() {
        if (environment)
            return AppConstt.ServerUrl.REl_URL_API_MOBILE_QATAR_V2;
        else
            return AppConstt.ServerUrl.DEB_URL_API_MOBILE_QATAR_V2;
    }


    public String getBaseUrlApiEncrpt() {
        if (environment)
            return AppConstt.ServerUrl.REl_BASE_URL;
        else
            return AppConstt.ServerUrl.DEB_BASE_URL_V2;
    }



    public String getNetworkErrorMessage() {
        if (isAppLangArabic)
            return AppConstt.NetworkMsg.ERROR_NETWORK_AR;
        else
            return AppConstt.NetworkMsg.ERROR_NETWORK_EN;
    }

    public String getNetworkExceptionMessage(String _msg) {
        if (environment) {
            if (isAppLangArabic)
                return AppConstt.NetworkMsg.EXCEPTION_NETWORK_AR;
            else
                return AppConstt.NetworkMsg.EXCEPTION_NETWORK_EN;
        } else {
            return _msg;
        }
    }

    public int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    public void loadUserData() {

        mUser.setMasterMerchant(sharedPref.getString("key_merchint_pin", ""));
        mUser.setAuthorizationToken(sharedPref.getString("key_authorization", ""));
        mUser.setUpAccess(sharedPref.getString("key_up_access", ""));
        mUser.setUberRequired(sharedPref.getBoolean("key_is_uber_required", false));

    }

    public void saveUserData() {

        editor.putString("key_merchint_pin", mUser.getMasterMerchant());
        editor.putString("key_authorization", mUser.getAuthorizationToken());
        editor.putString("key_up_access", mUser.getUpAccess());
        editor.putBoolean("key_is_uber_required", mUser.isUberRequired());
        editor.commit();
        editor.apply();
    }

    public void saveDefLanguage(String lang) {
        editor.putString("upsa_def_lang", lang);
        editor.commit();
    }

    public String loadDefLanguage() {
        mLanguage = sharedPref.getString("upsa_def_lang", "en");
        return mLanguage;
    }

    public ArrayList<String> loadRecentSearches() {
        Gson gson = new Gson();
        String json = sharedPref.getString("key_instore_recent_searches", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        lstInstoreRecentSearches = gson.fromJson(json, type);

        if (lstInstoreRecentSearches == null) {
            lstInstoreRecentSearches = new ArrayList<>(MAX_RECENT_SEARCHES);
        }
        Collections.reverse(lstInstoreRecentSearches);

        return lstInstoreRecentSearches;
    }

    public void removeRecentSearch(int index) {
        if (lstInstoreRecentSearches != null && lstInstoreRecentSearches.size() > 0)
            lstInstoreRecentSearches.remove(index);
        Collections.reverse(lstInstoreRecentSearches);
        Gson gson = new Gson();
        String json = gson.toJson(lstInstoreRecentSearches);
        editor.putString("key_instore_recent_searches", json);
        editor.commit();

        Collections.reverse(lstInstoreRecentSearches);
    }

    public void updateRecentSearches(String strSearchTag) {
        if (lstInstoreRecentSearches.contains(strSearchTag))
            return;//To avoid saving same search string twice


        Collections.reverse(lstInstoreRecentSearches);

        if (lstInstoreRecentSearches != null && lstInstoreRecentSearches.size() == MAX_RECENT_SEARCHES)
            lstInstoreRecentSearches.remove(0);//To remove the oldest search string if the max size limit is reached

        lstInstoreRecentSearches.add(strSearchTag);
        Gson gson = new Gson();
        String json = gson.toJson(lstInstoreRecentSearches);
        editor.putString("key_instore_recent_searches", json);
        editor.commit();

        Collections.reverse(lstInstoreRecentSearches);

    }

    // RecentViewedSection
    public ArrayList<DModel_Recent_Outlet> loadRecentViewed() {

        Gson gson = new Gson();
        String json = sharedPref.getString("key_instore_recent_viewed", null);
        Type type = new TypeToken<ArrayList<DModel_Recent_Outlet>>() {
        }.getType();
        lstRecentViewed = gson.fromJson(json, type);

        if (lstRecentViewed == null) {
            lstRecentViewed = new ArrayList<>(MAX_RECENT_VIEWED);
        }
        Collections.reverse(lstRecentViewed);

        return lstRecentViewed;
    }

    public void removeRecentViewed(int index) {
        if (lstRecentViewed != null && lstRecentViewed.size() > 0){
            for (int i=0;i<lstRecentViewed.size();i++){
                if (lstRecentViewed.get(i).getId().equals(String.valueOf(index))){
                    lstRecentViewed.remove(i);
                }
            }

        }
        Collections.reverse(lstRecentViewed);
        Gson gson = new Gson();
        String json = gson.toJson(lstRecentViewed);
        editor.putString("key_instore_recent_viewed", json);
        editor.commit();

        Collections.reverse(lstRecentViewed);
    }

    public void updateRecentViewed(DModel_Recent_Outlet strSearchTag) {


        if (lstRecentViewed != null && lstRecentViewed.size()>0) {

            for (int i = 0; i < lstRecentViewed.size(); i++) {
                if (lstRecentViewed.get(i).getId().equals(strSearchTag.getId())){
                    removeRecentViewed(Integer.parseInt(lstRecentViewed.get(i).getId()));                }

            }
        }

        Collections.reverse(lstRecentViewed);

        if (lstRecentViewed != null && lstRecentViewed.size() == MAX_RECENT_VIEWED)
            lstRecentViewed.remove(0);//To remove the oldest search string if the max size limit is reached

        lstRecentViewed.add(strSearchTag);
        Gson gson = new Gson();
        String json = gson.toJson(lstRecentViewed);
        editor.putString("key_instore_recent_viewed", json);
        editor.commit();


        Collections.reverse(lstRecentViewed);

    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;

        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return locationMode != Settings.Secure.LOCATION_MODE_OFF;

    }



    public boolean checkPermission(Context mContext) {
        int result = -1;
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if ((ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                    result = 0;
                } else {
                    result = -1;
                }
            } else {
                result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result >= 0;
    }

    public static void slideView(View view, int currentHeight, int newHeight, long duration) {

        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(currentHeight, newHeight)
                .setDuration(duration);

        slideAnimator.addUpdateListener(animation1 -> {
            Integer value = (Integer) animation1.getAnimatedValue();
            view.getLayoutParams().height = value.intValue();
            view.requestLayout();
        });

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.play(slideAnimator);
        animationSet.start();
    }

    public void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window User_AccessToken from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window User_AccessToken from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void openKeyboard(Context _context) {
        InputMethodManager inputMethodManager = (InputMethodManager) _context.getSystemService(_context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public void showAlertDialog(Activity activity,final String _title, String _msgToShow, String _positiveBtnText,
                                 String _negativeBtnText, boolean _isNegativeButtonRequired, boolean _isCancellabe,
                                 final CustomAlertConfirmationInterface _customDialogConfirmationListener) {
        CustomAlertDialog cdd = new CustomAlertDialog(activity,
                    _title, _msgToShow, _positiveBtnText, _negativeBtnText, _isNegativeButtonRequired,
                    _customDialogConfirmationListener
        );
        cdd.show();
        cdd.setCancelable(_isCancellabe);

    }

    public String decrypt(String ciphertext, String passphrase) {
        if ((ciphertext != null && ciphertext.length() > 0) && (passphrase != null && passphrase.length() > 0)) {
            try {
                final int keySize = 256;
                final int ivSize = 128;

                // Decode from base64 text
                byte[] ctBytes = Base64.decode(ciphertext.getBytes("UTF-8"), Base64.DEFAULT);

                // Get salt
                byte[] saltBytes = Arrays.copyOfRange(ctBytes, 8, 16);

                // Get ciphertext
                byte[] ciphertextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);

                // Get key and iv from passphrase and salt
                byte[] key = new byte[keySize / 8];
                byte[] iv = new byte[ivSize / 8];
                EvpKDF(passphrase.getBytes("UTF-8"), keySize, ivSize, saltBytes, key, iv);

                // Actual decrypt
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
                byte[] recoveredPlaintextBytes = cipher.doFinal(ciphertextBytes);

                String strIs = new String(recoveredPlaintextBytes, "UTF-8");
                return strIs;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public String encrypt(String plaintext, String passphrase) {
        if ((plaintext != null && plaintext.length() > 0) && (passphrase != null && passphrase.length() > 0)) {
            try {
                final int keySize = 256;
                final int ivSize = 128;

                // Create empty key and iv
                byte[] key = new byte[keySize / 8];
                byte[] iv = new byte[ivSize / 8];

                // Create random salt
                byte[] saltBytes = generateSalt(8);

                // Derive key and iv from passphrase and salt
                EvpKDF(passphrase.getBytes("UTF-8"), keySize, ivSize, saltBytes, key, iv);

                // Actual encrypt
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
                byte[] cipherBytes = cipher.doFinal(plaintext.getBytes("UTF-8"));

                /**
                 * Create CryptoJS-like encrypted string from encrypted data
                 * This is how CryptoJS do:
                 * 1. Create new byte array to hold ecrypted string (b)
                 * 2. Concatenate 8 bytes to b
                 * 3. Concatenate salt to b
                 * 4. Concatenate encrypted data to b
                 * 5. Encode b using Base64
                 */
                byte[] sBytes = "Salted__".getBytes("UTF-8");
                byte[] b = new byte[sBytes.length + saltBytes.length + cipherBytes.length];
                System.arraycopy(sBytes, 0, b, 0, sBytes.length);
                System.arraycopy(saltBytes, 0, b, sBytes.length, saltBytes.length);
                System.arraycopy(cipherBytes, 0, b, sBytes.length + saltBytes.length, cipherBytes.length);

                byte[] base64b = Base64.encode(b, Base64.DEFAULT);

                String encrypted = new String(base64b);
                encrypted = encrypted.replaceAll("\n", "");
                encrypted = encrypted.replaceAll("=", AppConstt.GSON_SPCL_CHR);
                return encrypted;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static byte[] generateSalt(int length) {
        Random r = new SecureRandom();
        byte[] salt = new byte[length];
        r.nextBytes(salt);
        return salt;
    }
    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        return EvpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
    }
    public String applyHackForGSON(String _strJson) {
        return _strJson.replaceAll(AppConstt.GSON_SPCL_CHR, "=");
    }
    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        keySize = keySize / 32;
        ivSize = ivSize / 32;
        int targetKeySize = keySize + ivSize;
        byte[] derivedBytes = new byte[targetKeySize * 4];
        int numberOfDerivedWords = 0;
        byte[] block = null;
        MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
        while (numberOfDerivedWords < targetKeySize) {
            if (block != null) {
                hasher.update(block);
            }
            hasher.update(password);
            block = hasher.digest(salt);
            hasher.reset();

            // Iterations
            for (int i = 1; i < iterations; i++) {
                block = hasher.digest(block);
                hasher.reset();
            }

            System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4,
                    Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));

            numberOfDerivedWords += block.length / 4;
        }

        System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
        System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);

        return derivedBytes; // key + iv
    }

}
