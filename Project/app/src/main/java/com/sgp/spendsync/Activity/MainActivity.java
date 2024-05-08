package com.sgp.spendsync.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sgp.spendsync.Database.Databasehelper;
import com.sgp.spendsync.Fragment.FragBank;
import com.sgp.spendsync.Fragment.FragExpense;
import com.sgp.spendsync.Fragment.FragIncome;
import com.sgp.spendsync.Fragment.FragTransaction;
import com.sgp.spendsync.Model.ModelBank;
import com.sgp.spendsync.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle;
    ImageView ivAdd,ivSetting;
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    Databasehelper databasehelper;
    public  String isFrom="INCOME";
    FrameLayout frameBanner;
    public static List<ModelBank> modelArrayList;
    String[] bankNamelist = {"Axis", "Allahabad Bank", "Bank of Baroda", "Bank of Maharashtra",
            "Canara Bank", "Central Bank of India", "Citi Bank", "Corporation Bank", "Deutsche Bank", "Dhanlaxmi Bank",
            "Federal Bank", "ICICI Bank", "IDBI Bank", "IndusInd Bank", "Karnataka Bank Ltd", "Karur Vysya Bank", "Kotak Bank",
            "Laxmi Vilas Bank", "Punjab National Bank", "South Indian Bank", "State Bank of India", "Syndicate Bank", "Union Bank of India",
            "Yes Bank Ltd", "HDFC Bank", "Varachha Bank"};

    String[] bankUrllist = {"https://retail.axisbank.co.in/wps/portal/rBanking/axisebanking/AxisRetailLogin/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOKNAzxMjIwNjLwsQp0MDBw9PUOd3HwdDQwMjIEKIoEKDHAARwNC-sP1o_ArMYIqwGNFQW6EQaajoiIAVNL82A!!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/?_ga=2.240083267.1524652374.1523376670-1258911885.1523376670",
            "https://www.allbankonline.in/", "https://www.bobibanking.com/", "https://www.mahaconnect.in/jsp/index.html",
            "https://netbanking.canarabank.in/entry/ENULogin.jsp#", "https://www.centralbank.net.in/jsp/startMain.jsp", "https://www.citibank.co.in/ibank/login/IQPin1.jsp",
            "https://www.corpretail.com/RetailBank/IceGate", "https://login.deutschebank.co.in/corp/AuthenticationController?__START_TRAN_FLAG__=Y&FORMSGROUP_ID__=AuthenticationFG&__EVENT_ID__=LOAD&FG_BUTTONS__=LOAD&ACTION.LOAD=Y&AuthenticationFG.LOGIN_FLAG=1&BANK_ID=200&LANGUAGE_ID=001",
            "https://www.dhanbank.com/personal/payment_services_faq.aspx", "https://www.fednetbank.com/", "https://infinity.icicibank.com/corp/AuthenticationController?FORMSGROUP_ID__=AuthenticationFG&__START_TRAN_FLAG__=Y&FG_BUTTONS__=LOAD&ACTION.LOAD=Y&AuthenticationFG.LOGIN_FLAG=1&BANK_ID=ICI",
            "https://www.idbi.com/idbi-bank-internet-banking.asp", "https://indusnet.indusind.com/corp/BANKAWAY?Action.RetUser.Init.001=Y&AppSignonBankId=234&AppType=corporate&CorporateSignonLangId=001", "https://moneyclick.karnatakabank.co.in/BankAwayRetail/AuthenticationController?__START_TRAN_FLAG__=Y&FORMSGROUP_ID__=AuthenticationFG&__EVENT_ID__=LOAD&FG_BUTTONS__=LOAD&ACTION.LOAD=Y&AuthenticationFG.LOGIN_FLAG=1&BANK_ID=KBL&LANGUAGE_ID=001",
            "https://www.kvbin.com/B001/ENULogin.jsp", "https://m.kotak.com/811/?Source=GoogleSEM&banner=Search&pubild=26&gclid=CjwKCAjwwbHWBRBWEiwAMIV7E5PnSCSsCnrs_uoFvK1-apJeOswe1kl5xTinAueT-oRFVGEfd519YBoCZwQQAvD_BwE#/login",
            "https://www.lvbank.com/netbanking.aspx", "https://www.netpnb.com/index.html", "https://sibernet.southindianbank.com/corp/AuthenticationController?FORMSGROUP_ID__=AuthenticationFG&__START_TRAN_FLAG__=Y&FG_BUTTONS__=LOAD&ACTION.LOAD=Y&AuthenticationFG.LOGIN_FLAG=1&BANK_ID=059",
            "https://www.onlinesbi.com/", "https://www.syndonline.in/B001/ENULogin.jsp", "https://www.unionbankonline.co.in/",
            "https://www.yesbank.in/digital-banking/online-banking/netbanking-services", "https://netbanking.hdfcbank.com/netbanking/", "https://netbanking.varachhabank.com/ib/login#"};

    int[] bankIconlist = {R.drawable.axis, R.drawable.allahabad, R.drawable.bob, R.drawable.bom,
            R.drawable.canarabank, R.drawable.cbi, R.drawable.citi, R.drawable.corporationbank, R.drawable.deutschebank,
            R.drawable.dhanlaxmibank, R.drawable.federalbank, R.drawable.icicibank, R.drawable.idbibank, R.drawable.indusindbank,
            R.drawable.karnatakabank, R.drawable.karurvysyabank, R.drawable.kotak, R.drawable.laxmivilasbank, R.drawable.pnb,
            R.drawable.sib, R.drawable.sbi, R.drawable.syndicatebank, R.drawable.ubi, R.drawable.yes, R.drawable.hdfcbank, R.drawable.varachhabank};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        modelArrayList = new ArrayList<ModelBank>();
        init();
        setData();
        setUpFragment();

        ivAdd.setOnClickListener(view -> {

            Intent intent=new Intent(MainActivity.this,IncomelistActivity.class);
            intent.putExtra("fromWhich",isFrom);
            startActivity(intent);
        });

        ivSetting.setOnClickListener(view -> {
            Intent intent=new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        });
    }

    private void setData() {
        for (int i = 0; i < bankIconlist.length; i++) {
            ModelBank modelBank = new ModelBank();
            modelBank.setBankName(bankNamelist[i]);
            modelBank.setBanklogo(bankIconlist[i]);
            modelBank.setBankUrl(bankUrllist[i]);
            modelArrayList.add(modelBank);
        }

        int categoryCount = databasehelper.getDataCategoryIncomeCount();

        if (categoryCount == 0) {
            databasehelper.categoryIncomeData("Salary", 0);
            databasehelper.categoryIncomeData("Awards", 1);
            databasehelper.categoryIncomeData("Grants", 2);
            databasehelper.categoryIncomeData("sale", 3);
            databasehelper.categoryIncomeData("Rental", 4);
            databasehelper.categoryIncomeData("Refunds", 5);
            databasehelper.categoryIncomeData("coupons", 6);
            databasehelper.categoryIncomeData("Lottery", 7);
            databasehelper.categoryIncomeData("Dividends", 8);
            databasehelper.categoryIncomeData("Investment", 9);
            databasehelper.categoryIncomeData("Other", 176);
        }

        int categoryCountEx = databasehelper.getDataCategoryExpenseCount();

        if (categoryCountEx == 0) {
            databasehelper.categoryExpenseData("Food", 10);
            databasehelper.categoryExpenseData("Bills", 11);
            databasehelper.categoryExpenseData("Transportation", 12);
            databasehelper.categoryExpenseData("Home", 13);
            databasehelper.categoryExpenseData("Car", 14);
            databasehelper.categoryExpenseData("Entertainment", 15);
            databasehelper.categoryExpenseData("Shopping", 16);
            databasehelper.categoryExpenseData("Clothing", 17);
            databasehelper.categoryExpenseData("Insurance", 18);
            databasehelper.categoryExpenseData("Tax", 19);
            databasehelper.categoryExpenseData("Telephone", 20);
            databasehelper.categoryExpenseData("Cigratee", 21);
            databasehelper.categoryExpenseData("Helath", 22);
            databasehelper.categoryExpenseData("Sport", 23);
            databasehelper.categoryExpenseData("Baby", 24);
            databasehelper.categoryExpenseData("Pet", 25);
            databasehelper.categoryExpenseData("Beauty", 26);
            databasehelper.categoryExpenseData("Electronics", 27);
            databasehelper.categoryExpenseData("Hamburger", 28);
            databasehelper.categoryExpenseData("Wine", 29);
            databasehelper.categoryExpenseData("Vegetables", 30);
            databasehelper.categoryExpenseData("Snacks", 31);
            databasehelper.categoryExpenseData("Gift", 32);
            databasehelper.categoryExpenseData("Social", 33);
            databasehelper.categoryExpenseData("Travel", 34);
            databasehelper.categoryExpenseData("Education", 35);
            databasehelper.categoryExpenseData("Fruits", 36);
            databasehelper.categoryExpenseData("Book", 37);
            databasehelper.categoryExpenseData("Office", 38);
            databasehelper.categoryExpenseData("Other", 176);
        }
    }

    private void setUpFragment() {

        viewPager = findViewById(R.id.viewpager_main);
        mTabLayout = findViewById(R.id.tabs_main);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                Log.e("Position--)",""+tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mTabLayout.addTab(mTabLayout.newTab().setText("Income"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Expense"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Bank"));
        mTabLayout.addTab(mTabLayout.newTab().setText("History"));

        viewPager.setAdapter(new TabPagerAdapter2(getSupportFragmentManager(), mTabLayout.getTabCount()));
        viewPager.setCurrentItem(0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                if (arg0==0)
                {
                    ivAdd.setVisibility(View.VISIBLE);
                    isFrom="INCOME";
                    txtTitle.setText("Income");
                }
                else if (arg0==1)
                {
                    ivAdd.setVisibility(View.VISIBLE);
                    isFrom="EXPENSE";
                    txtTitle.setText("Expense");
                }
                else if (arg0==2)
                {
                    ivAdd.setVisibility(View.GONE);
                    txtTitle.setText("Bank");
                }
                else if (arg0==3)
                {
                    ivAdd.setVisibility(View.GONE);
                    txtTitle.setText("Transaction");
                }
                Log.e("PositionView--)",""+arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

                System.out.println("onPageScrolled");
            }

            @Override
            public void onPageScrollStateChanged(int num) {
                // TODO Auto-generated method stub


            }
        });
    }

    public void init()
    {
        txtTitle=findViewById(R.id.txtTitle);
        ivAdd=findViewById(R.id.ivAdd);
        ivSetting=findViewById(R.id.ivSetting);
        frameBanner=findViewById(R.id.frameBanner);
        databasehelper = new Databasehelper(this);

        loadBanner();
    }

    public void loadBanner() {
        if (frameBanner != null) {
            frameBanner.setVisibility(View.VISIBLE);
            AdView adView = new AdView(MainActivity.this);
            adView.setAdUnitId(getResources().getString(R.string.banner_ID));
            frameBanner.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            AdSize adSize = getAdSize(MainActivity.this);
            adView.setAdSize(adSize);
            adView.loadAd(adRequest);
        }
    }
    private AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }
    class TabPagerAdapter2 extends FragmentStatePagerAdapter {

        String which;
        private String[] tabTitles = new String[]{"Income","Expense","Bank","History"};

        public TabPagerAdapter2(FragmentManager fragmentManager, int i) {
            super(fragmentManager);
        }
        @NonNull
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new FragIncome();
                case 1:
                    return new FragExpense();
                case 2:
                    return new FragBank();
                case 3:
                    return new FragTransaction();
                default:
                    return new FragIncome();
            }
        }

        public int getCount() {
            return tabTitles.length;
        }

    }

}
