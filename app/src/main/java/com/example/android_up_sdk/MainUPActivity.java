package com.example.android_up_sdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.android_up_sdk.HomeAuxiliaries.CategoryHubFragment;
import com.example.android_up_sdk.HomeAuxiliaries.HomeUpFragment;
import com.example.android_up_sdk.HomeAuxiliaries.MerchantPinFragment;
import com.example.android_up_sdk.HomeAuxiliaries.WebServices.Authorization_WebHit_Post_getAuth;
import com.example.android_up_sdk.Utils.AppConfig;
import com.example.android_up_sdk.Utils.AppConstt;
import com.example.android_up_sdk.Utils.IWebCallback;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



public class MainUPActivity extends AppCompatActivity {

    private FragmentManager mFrgmgr;

    private static final String THEME_DEFAULT_FONT = "serif";//Must be same as used in styles.xml
    String DEFAULT_NORMAL_BOLD_FONT_FILENAME = "";
    String DEFAULT_NORMAL_BOLD_ITALIC_FONT_FILENAME = "";
    String DEFAULT_NORMAL_ITALIC_FONT_FILENAME = "";
    String DEFAULT_NORMAL_NORMAL_FONT_FILENAME = "";
    String DEFAULT_LIGHT_FONT_FILENAME = "";
    String DEFAULT_LIGHT_ITALIC_FONT_FILENAME = "";
    String DEFAULT_SANS_NORMAL_FONT_FILENAME = "";
    String DEFAULT_MONOSPACE_NORMAL_FONT_FILENAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_up);

        mFrgmgr = getSupportFragmentManager();
        AppConfig.initInstance(MainUPActivity.this);

        Intent intent = getIntent();
        String walletId;
        String env;
        String publicKey;
        if (intent.getExtras()!=null){
            walletId=intent.getStringExtra(AppConstt.BundleStrings.walletId);
            env=intent.getStringExtra(AppConstt.BundleStrings.environment);
            publicKey=intent.getStringExtra(AppConstt.BundleStrings.publicKey);
        }
        else {
            walletId="wallet_112233";
            env="live-om-prd";
            publicKey="EBEAB82C445855B4-OM-1";
        }

        AppConfig.getInstance().loadDefLanguage();
        if (AppConfig.getInstance().mLanguage.equalsIgnoreCase(AppConstt.AppLanguage.ARABIC)) {
            setDefLanguage(AppConstt.AppLanguage.ARABIC);
        }
        else {
            setDefLanguage(AppConstt.AppLanguage.ENGLISH);
        }

        String encryptedString = AppConfig.getInstance().encrypt(walletId, AppConstt.CHAR_SET);
        encryptedString = AppConfig.getInstance().applyHackForGSON(encryptedString);

        String decryptedPublicKeyInternal = AppConfig.getInstance().decrypt(AppConstt.PublicKey.Internal, AppConstt.CHAR_SET);
        String decryptedEnvironmentInternal = AppConfig.getInstance().decrypt(AppConstt.Enviornment.Internal, AppConstt.CHAR_SET);

        String decryptedPublicKeyProd = AppConfig.getInstance().decrypt(AppConstt.PublicKey.Prod, AppConstt.CHAR_SET);
        String decryptedEnvironmentProd = AppConfig.getInstance().decrypt(AppConstt.Enviornment.Prod, AppConstt.CHAR_SET);

        if (publicKey.equalsIgnoreCase(decryptedPublicKeyInternal) && env.equalsIgnoreCase(decryptedEnvironmentInternal)){
            AppConfig.getInstance().environment=false;
            requestAuthorization(encryptedString);
        }
        else if (publicKey.equalsIgnoreCase(decryptedPublicKeyProd) && env.equalsIgnoreCase(decryptedEnvironmentProd)){
            AppConfig.getInstance().environment=true;
            requestAuthorization(encryptedString);
        }
        else {
            Toast.makeText(this, "SDK Not Initialized Correctly", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void requestAuthorization(String wallet_id) {
        Authorization_WebHit_Post_getAuth authorization_webHit_post_getAuth = new Authorization_WebHit_Post_getAuth();
        authorization_webHit_post_getAuth.requestAuth(MainUPActivity.this, new IWebCallback() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                if (isSuccess) {
                    AppConfig.getInstance().mUser.setAuthorizationToken(Authorization_WebHit_Post_getAuth.responseObject.getData().getAuth_key());
                    AppConfig.getInstance().mUser.setUpAccess("5");
                    AppConfig.getInstance().saveUserData();
                    navToHomeFragment();
                } else {
                    Toast.makeText(MainUPActivity.this, strMsg, Toast.LENGTH_SHORT).show();
                    finish();

                }
            }

            @Override
            public void onWebException(Exception ex) {
                Toast.makeText(MainUPActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                finish();

            }

        }, wallet_id);
    }

    public void setDefLanguage(String _lang) {
        Locale locale = new Locale(_lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        if (_lang.equalsIgnoreCase(AppConstt.ARABIC)) {
            AppConfig.getInstance().saveDefLanguage(AppConstt.ARABIC);
            AppConfig.getInstance().isAppLangArabic = true;
        } else {
            AppConfig.getInstance().saveDefLanguage(AppConstt.ENGLISH);
            AppConfig.getInstance().isAppLangArabic = false;
        }
        setLanguageSpecificFonts(AppConfig.getInstance().isAppLangArabic);

    }

    public void setLanguageSpecificFonts(boolean isArabic) {
        if (isArabic) {
            //Arabic fonts
            DEFAULT_NORMAL_BOLD_FONT_FILENAME = "fonts/din_arabic_bold.otf";
            DEFAULT_NORMAL_BOLD_ITALIC_FONT_FILENAME = "fonts/din_arabic_bold.otf";
            DEFAULT_NORMAL_ITALIC_FONT_FILENAME = "fonts/din_arabic_regular.otf";
            DEFAULT_NORMAL_NORMAL_FONT_FILENAME = "fonts/din_arabic_regular.otf";
            DEFAULT_SANS_NORMAL_FONT_FILENAME = "fonts/din_arabic_regular.otf";
            DEFAULT_LIGHT_FONT_FILENAME = "fonts/din_arabic_regular.otf";
            DEFAULT_LIGHT_ITALIC_FONT_FILENAME = "fonts/din_arabic_regular.otf";
        } else {
            //English fonts
            DEFAULT_NORMAL_BOLD_FONT_FILENAME = "fonts/roboto_black.ttf";
            DEFAULT_NORMAL_BOLD_ITALIC_FONT_FILENAME = "fonts/roboto_black.ttf";
            DEFAULT_NORMAL_ITALIC_FONT_FILENAME = "fonts/roboto.ttf";
            DEFAULT_NORMAL_NORMAL_FONT_FILENAME = "fonts/roboto.ttf";
            DEFAULT_SANS_NORMAL_FONT_FILENAME = "fonts/roboto.ttf";
            DEFAULT_LIGHT_FONT_FILENAME = "fonts/roboto.ttf";
            DEFAULT_LIGHT_ITALIC_FONT_FILENAME = "fonts/roboto.ttf";
        }


        try {
            // The following code is only necessary if you are using the android:typeface attribute
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setThemeDefaultFonts();
                Log.d("LOG_SDK","Set Language" );

            }
        } catch (Exception e) {
            Log.d("LOG_SDK","Exception Language"+ e.toString());
            e.printStackTrace();
        }
    }

    private void setThemeDefaultFonts() throws NoSuchFieldException, IllegalAccessException {

        final Typeface normal = Typeface.createFromAsset(getAssets(), DEFAULT_NORMAL_NORMAL_FONT_FILENAME);
        final Typeface bold = Typeface.createFromAsset(getAssets(), DEFAULT_NORMAL_BOLD_FONT_FILENAME);
        final Typeface italic = Typeface.createFromAsset(getAssets(), DEFAULT_NORMAL_ITALIC_FONT_FILENAME);
        final Typeface boldItalic = Typeface.createFromAsset(getAssets(), DEFAULT_NORMAL_BOLD_ITALIC_FONT_FILENAME);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Map<String, Typeface> normalFontMap = new HashMap<String, Typeface>();
            normalFontMap.put(MainUPActivity.THEME_DEFAULT_FONT, normal);
            final Field staticField = Typeface.class.getDeclaredField("sSystemFontMap");
            staticField.setAccessible(true);
            staticField.set(null, normalFontMap);

        } else {
            Field defaultField = Typeface.class.getDeclaredField("DEFAULT");
            defaultField.setAccessible(true);
            defaultField.set(null, normal);

            Field defaultBoldField = Typeface.class.getDeclaredField("DEFAULT_BOLD");
            defaultBoldField.setAccessible(true);
            defaultBoldField.set(null, bold);

            Field sDefaults = Typeface.class.getDeclaredField("sDefaults");
            sDefaults.setAccessible(true);
            sDefaults.set(null, new Typeface[]{normal, bold, italic, boldItalic});

            final Typeface normal_sans = Typeface.createFromAsset(getAssets(), DEFAULT_NORMAL_NORMAL_FONT_FILENAME);
            Field sansSerifDefaultField = Typeface.class.getDeclaredField("SANS_SERIF");
            sansSerifDefaultField.setAccessible(true);
            sansSerifDefaultField.set(null, normal_sans);

            final Typeface normal_serif = Typeface.createFromAsset(getAssets(), DEFAULT_NORMAL_NORMAL_FONT_FILENAME);
            Field serifDefaultField = Typeface.class.getDeclaredField("SERIF");
            serifDefaultField.setAccessible(true);
            serifDefaultField.set(null, normal_serif);

            final Typeface normal_monospace = Typeface.createFromAsset(getAssets(), DEFAULT_MONOSPACE_NORMAL_FONT_FILENAME);
            Field monospaceDefaultField = Typeface.class.getDeclaredField("MONOSPACE");
            monospaceDefaultField.setAccessible(true);
            monospaceDefaultField.set(null, normal_monospace);
        }

    }

    public void redeemOffers(String _offerId, String _offername, String _merchantName, String _merchantAddr, String _outletId, String _categoryId, String _outletlogo, String _approxSavings) {

            Bundle bundle = new Bundle();
            bundle.putString(AppConstt.BundleStrings.offerId, _offerId + "");
            bundle.putString(AppConstt.BundleStrings.offerName, _offername);
            bundle.putString(AppConstt.BundleStrings.merchantName, _merchantName);
            bundle.putString(AppConstt.BundleStrings.merchantAddress, _merchantAddr);
            bundle.putString(AppConstt.BundleStrings.merchantId, _outletId);
            bundle.putString(AppConstt.BundleStrings.categoryId, _categoryId);
            bundle.putString(AppConstt.BundleStrings.merchantLogo, _outletlogo);
            bundle.putString(AppConstt.BundleStrings.approxSavings, _approxSavings);


            navToMerchantPinFragment(bundle, returnStackFragment());

    }

    public void navToMerchantPinFragment(Bundle _b, Fragment _previousFrg) {
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new MerchantPinFragment();
        frg.setArguments(_b);
        ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.MerchantPinFragment);
        ft.addToBackStack(AppConstt.FrgTag.MerchantPinFragment);
        ft.hide(_previousFrg);
        ft.commit();
    }

    public void navToHomeFragment() {
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new HomeUpFragment();
        ft.add(R.id.main_act_content_frame, frg, AppConstt.FrgTag.HomeUpFragment);
        ft.addToBackStack(AppConstt.FrgTag.HomeUpFragment);
        ft.commit();
    }

    public void navToMerchantDetailFragment(Bundle bundle) {
        Fragment previousFragment = returnStackFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fr = new MerchantDetailFragment();
        fr.setArguments(bundle);
        ft.add(R.id.main_act_content_frame, fr, AppConstt.FrgTag.MerchantDetailFragment);
        ft.addToBackStack(AppConstt.FrgTag.MerchantDetailFragment);
        ft.hide(previousFragment);
        ft.commit();

    }

    public void navToCategoryHubFragment(Bundle bundle) {
        Fragment previousFragment = returnStackFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (AppConfig.getInstance().isAppLangArabic)
            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                    R.anim.enter_from_right, R.anim.exit_to_left);
        else
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right);
        Fragment fr = new CategoryHubFragment();
        fr.setArguments(bundle);
        ft.add(R.id.main_act_content_frame, fr, AppConstt.FrgTag.CategoryHubFragment);
        ft.addToBackStack(AppConstt.FrgTag.CategoryHubFragment);
        ft.hide(previousFragment);
        ft.commit();

    }

    public void navToRulesOfPurchaseFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("page", AppConstt.PURCHASE_RULES);
        Fragment fr = new WebViewFragment();
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        fr.setArguments(bundle);
        ft.add(R.id.main_act_content_frame, fr, AppConstt.FrgTag.WebViewFragment);
        ft.addToBackStack(AppConstt.FrgTag.WebViewFragment);
        ft.hide(returnStackFragment());
        ft.commit();
    }

    public void navToOutletMenuWebViewFragment(String url, String page) {
        Bundle bundle = new Bundle();
        bundle.putString("page", page);
        bundle.putString("url", url);
        Fragment fr = new WebViewFragment();
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        fr.setArguments(bundle);
        ft.add(R.id.main_act_content_frame, fr, AppConstt.FrgTag.WebViewFragment);
        ft.addToBackStack(AppConstt.FrgTag.WebViewFragment);
        ft.hide(returnStackFragment());
        ft.commit();
    }

    public String returnStackFragmentTag() {
        int index = mFrgmgr.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry;
        String tag = "";
        if (index >= 0) {
            backEntry = mFrgmgr.getBackStackEntryAt(index);
            tag = backEntry.getName();
        }
        return tag;
    }

    public Fragment returnStackFragment() {
        int index = mFrgmgr.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry;
        String tag = "";
        if (index >= 0) {
            backEntry = mFrgmgr.getBackStackEntryAt(index);
            tag = backEntry.getName();
        }
        return mFrgmgr.findFragmentByTag(tag);
    }

    @Override
    public void onBackPressed() {
        whenBackPressed();
    }

    private void whenBackPressed() {
        String tag = returnStackFragmentTag();

        AppConfig.getInstance().closeKeyboard(this);
         if (tag.equalsIgnoreCase(AppConstt.FrgTag.HomeUpFragment)) {
            this.finish();
        } else {
            super.onBackPressed();
        }
    }
}