package com.sgp.spendsync.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sgp.spendsync.Activity.PDFViewActivity;
import com.sgp.spendsync.Adapter.TranscationAdapter;
import com.sgp.spendsync.Database.Databasehelper;
import com.sgp.spendsync.Model.ModelTransaction;
import com.sgp.spendsync.Others.ItemClick;
import com.sgp.spendsync.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPHeaderCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sgp.spendsync.Adapter.CatlistAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragTransaction extends Fragment implements ItemClick {

    RecyclerView rvTranscation;
    Databasehelper databasehelper;
    ArrayList<ModelTransaction> modelArrayList;
    SQLiteDatabase db;
    Cursor cursor;
    LinearLayout lPdf;
    TranscationAdapter mTranAdapter;
    public double sarvaloIncome, sarvaloExpense, Total;
    List<String> incomeTotal = new ArrayList<>();
    List<String> expenseTotal = new ArrayList<>();
    TextView txtTotalAmount;
    String from1, to1;
    SwipeRefreshLayout swipeRefreshTrans;
    InterstitialAd mInterstitialAd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transcation, container, false);

        findView(rootView);
        modelArrayList = new ArrayList<>();
        databasehelper = new Databasehelper(getActivity());
        loadAds();
        getAllData();
        swipeRefreshTrans.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshTrans.setRefreshing(false);
                        getAllData();

                    }
                }, 1000);
            }
        });



        lPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialogRange = new Dialog(getActivity(), R.style.WideDialog);
                dialogRange.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogRange.setCanceledOnTouchOutside(true);
                dialogRange.setCancelable(true);
                dialogRange.setContentView(R.layout.dialog_select_date);
                dialogRange.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                LinearLayout btnCancel = dialogRange.findViewById(R.id.btnCancel);
                LinearLayout btnDone = dialogRange.findViewById(R.id.btnDone);
                EditText etStartDate = dialogRange.findViewById(R.id.etStartDate);
                EditText etTodate = dialogRange.findViewById(R.id.etTodate);

                dialogRange.show();
                final int year, month, date;

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                date = calendar.get(Calendar.DAY_OF_MONTH);

                etStartDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int y, int m, int d) {
                                String ds = d + "", ms = m + "";
                                if (d < 10) {
                                    ds = "0" + d;
                                }
                                if (m < 10) {
                                    ms = "0" + (m + 1);
                                }

                                String date1 = ds + "-" + ms + "-" + y;
                                from1 = y + "" + ms + "" + ds;
                                etStartDate.setText(date1);

                            }
                        }, year, month, date);
                        datePickerDialog.show();

                    }
                });
                etTodate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int y, int m, int d) {
                                String ds = d + "", ms = m + "";
                                if (d < 10) {
                                    ds = "0" + d;
                                }
                                if (m < 10) {
                                    ms = "0" + (m + 1);
                                }

                                String date1 = ds + "-" + ms + "-" + y;
                                to1 = y + "" + ms + "" + ds;
                                etTodate.setText(date1);

                            }
                        }, year, month, date);
                        datePickerDialog.show();

                    }
                });


                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogRange.dismiss();
                    }
                });

                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etStartDate.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getActivity(), "Please select your FROM date", Toast.LENGTH_SHORT).show();
                        } else if (etTodate.getText().toString().trim().length() <= 0) {
                            Toast.makeText(getActivity(), "Please select your TO date", Toast.LENGTH_SHORT).show();
                        } else {
                            dialogRange.dismiss();
                            exportPdf();
                        }
                    }
                });


            }
        });


        return rootView;
    }
    private void loadAds() {
        InterstitialAd.load(getActivity(), getActivity().getString(R.string.interstitial_ID), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;

            }
        });
    }

    public void getAllData()
    {
        Total_Bal();

        txtTotalAmount.setText(String.format("%.2f", Total) + "");
        modelArrayList=new ArrayList<>();
        cursor = databasehelper.getData();
        while (cursor.moveToNext()) {
            ModelTransaction modelTransactions = new ModelTransaction();
            modelTransactions.setId(cursor.getInt(0));
            modelTransactions.setAmount(cursor.getString(1));
            modelTransactions.setNote(cursor.getString(2));
            modelTransactions.setTransaction_type(cursor.getString(3));
            modelTransactions.setCategory_type(cursor.getString(4));
            modelTransactions.setDatentime(cursor.getString(5));
            modelTransactions.setImage_pos(cursor.getInt(6));
            modelArrayList.add(modelTransactions);
        }
        cursor.close();

        mTranAdapter = new TranscationAdapter(getActivity(), modelArrayList,this);
        rvTranscation.setAdapter(mTranAdapter);

        rvTranscation.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    public void exportPdf() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Wait");
        progressDialog.setMessage("Creating...");
        progressDialog.show();
        cursor = databasehelper.dataExport(from1, to1);
        cursor = databasehelper.getData();
        boolean isempty = cursor.moveToFirst();
        Log.e("exportPdf: ", "" + isempty);
        if (isempty) {
            Document doc = new Document();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date d = new Date();
            String filename = format.format(d) + ".pdf";

            File dir = new File(getActivity().getExternalFilesDir(getActivity().getResources().getString(R.string.app_name)).toString());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File f = new File(dir, filename);

            try {
                FileOutputStream fout = new FileOutputStream(f);

                PdfWriter writer = PdfWriter.getInstance(doc, fout);
                doc.open();

                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{1.0f, 2.0f, 5.8f, 5.0f, 5.8f, 3.0f});

                Font header = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLD, BaseColor.BLUE);
                PdfPCell head = new PdfPCell(new Phrase("Budget Tracker Statement", header));
                head.setColspan(6);
                head.setHorizontalAlignment(Element.ALIGN_CENTER);
                head.setPaddingTop(15f);
                head.setPaddingBottom(15f);
                head.setBackgroundColor(BaseColor.WHITE);
                head.setBorder(0);
                head.setBorderWidthBottom(2f);
                table.addCell(head);

                PdfPHeaderCell headerCell;
                BaseColor color = new BaseColor(185, 147, 91);
                Font font = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, BaseColor.BLUE);

                headerCell = new PdfPHeaderCell();
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_CENTER);
                headerCell.setPaddingBottom(10f);
                headerCell.setBackgroundColor(BaseColor.WHITE);
                headerCell.addElement(new Chunk("ID", font));
                table.addCell(headerCell);


                headerCell = new PdfPHeaderCell();
                headerCell.setBackgroundColor(BaseColor.WHITE);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_CENTER);
                headerCell.setPaddingBottom(10f);
                headerCell.addElement(new Chunk("Notes", font));
                table.addCell(headerCell);

                headerCell = new PdfPHeaderCell();
                headerCell.setBackgroundColor(BaseColor.WHITE);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_CENTER);
                headerCell.setPaddingBottom(10f);
                headerCell.addElement(new Chunk("Transaction_type", font));
                table.addCell(headerCell);

                headerCell = new PdfPHeaderCell();
                headerCell.setBackgroundColor(BaseColor.WHITE);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_CENTER);
                headerCell.setPaddingBottom(10f);
                headerCell.addElement(new Chunk("Category Type", font));
                table.addCell(headerCell);

                headerCell = new PdfPHeaderCell();
                headerCell.setBackgroundColor(BaseColor.WHITE);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_CENTER);
                headerCell.setPaddingBottom(10f);
                headerCell.addElement(new Chunk("Date/Time", font));
                table.addCell(headerCell);

                headerCell = new PdfPHeaderCell();
                headerCell.setBackgroundColor(BaseColor.WHITE);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_CENTER);
                headerCell.setPaddingBottom(10f);
                headerCell.addElement(new Chunk("Amount", font));
                table.addCell(headerCell);

                double total = 0;
                PdfPCell cell = new PdfPCell();
                do {
                    BaseColor colordata = new BaseColor(56, 142, 60);
                    Font fontdata = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
                    cell = new PdfPCell();
                    cell.setBackgroundColor(BaseColor.WHITE);
                    cell.setPaddingBottom(10f);
                    cell.addElement(new Chunk(cursor.getInt(0) + "", fontdata));
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBackgroundColor(BaseColor.WHITE);
                    cell.setPaddingBottom(10f);
                    cell.addElement(new Chunk(cursor.getString(2), fontdata));
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBackgroundColor(BaseColor.WHITE);
                    cell.setPaddingBottom(10f);
                    cell.addElement(new Chunk(cursor.getString(3), fontdata));
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBackgroundColor(BaseColor.WHITE);
                    cell.setPaddingBottom(10f);

                    if (cursor.getString(3).equals("INCOME")) {
                        Font fontdata1 = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GREEN);
                        cell.addElement(new Chunk(cursor.getString(4), fontdata1));
                    }
                    else
                    {
                        Font fontdata1 = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
                        cell.addElement(new Chunk(cursor.getString(4), fontdata1));
                    }

                    table.addCell(cell);
                    cell = new PdfPCell();
                    String datenTime = cursor.getString(5);
                    String t = datenTime.replace("\n", "");
                    cell.setBackgroundColor(BaseColor.WHITE);
                    cell.setPaddingBottom(10f);
                    cell.addElement(new Chunk(t, fontdata));
                    table.addCell(cell);


                    if (cursor.getString(3).equals("INCOME")) {

                        Font fontdata1 = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GREEN);
                        cell = new PdfPCell();
                        cell.setBackgroundColor(BaseColor.WHITE);
                        cell.setPaddingBottom(10f);
                        cell.addElement(new Chunk(cursor.getString(1), fontdata1));
                        table.addCell(cell);
                        total += Double.parseDouble(cursor.getString(1));
                    } else {
                        Font fontdata1 = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
                        cell = new PdfPCell();
                        cell.setBackgroundColor(BaseColor.WHITE);
                        cell.setPaddingBottom(10f);
                        cell.addElement(new Chunk(cursor.getString(1), fontdata1));
                        table.addCell(cell);
                        total -= Double.parseDouble(cursor.getString(1));

                    }

                } while (cursor.moveToNext());
                PdfPCell blank = new PdfPCell();
                blank.setColspan(4);
                blank.setBorder(0);
                table.addCell(blank);

                BaseColor colorpdf = new BaseColor(185, 147, 91);
                Font fontpdf = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, BaseColor.BLUE);
                PdfPCell totalpdf = new PdfPCell(new Phrase("Total : ", fontpdf));
                totalpdf.setHorizontalAlignment(Element.ALIGN_CENTER);
                totalpdf.setBackgroundColor(BaseColor.WHITE);
                totalpdf.setPaddingTop(5f);
                totalpdf.setPaddingBottom(15f);
                totalpdf.setBorder(0);
//                totalpdf.setBorderWidthBottom(2f);
                table.addCell(totalpdf);

                cell = new PdfPCell();
                cell.setBackgroundColor(BaseColor.WHITE);
                cell.addElement(new Chunk(total + ""));
                table.addCell(cell);

                doc.add(table);
                doc.close();

                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Statement Exported", Toast.LENGTH_SHORT).show();

                File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), getResources().getString(R.string.app_name));

                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File repostfile = new File(file1, filename);


                moveFile(f.getAbsolutePath(), repostfile.getAbsolutePath());

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(getActivity());

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            Intent intent = new Intent(getActivity(), PDFViewActivity.class);
                            intent.putExtra("filename", filename);
                            startActivity(intent);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Intent intent = new Intent(getActivity(), PDFViewActivity.class);
                            intent.putExtra("filename", filename);
                            startActivity(intent);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            mInterstitialAd = null;
                        }
                    });

                } else {
                    Intent intent = new Intent(getActivity(), PDFViewActivity.class);
                    intent.putExtra("filename", filename);
                    startActivity(intent);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveFile(String inputPath, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {

            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            new File(inputPath).delete();
            scanFile(outputPath);
        } catch (FileNotFoundException fnfe1) {
        } catch (Exception e) {
        }
    }
    private void scanFile(String path) {
        MediaScannerConnection.scanFile(getActivity(),
                new String[]{path}, null,
                (path1, uri) -> Log.e("TAG--)", "Finished scanning " + path1));
    }



    public void Total_Bal() {
        sarvaloIncome=0;
        sarvaloExpense=0;
        Total=0;
        incomeTotal=new ArrayList<>();
        expenseTotal=new ArrayList<>();
        Cursor cursorincome = databasehelper.getDataIncome();
        cursorincome.moveToFirst();
        for (int i = 0; i < cursorincome.getCount(); i++) {
            String total = cursorincome.getString(cursorincome.getColumnIndex("amount"));
            sarvaloIncome = sarvaloIncome + Integer.parseInt(total);
            incomeTotal.add(total);
            cursorincome.moveToNext();
        }

        Cursor cursorexpense = databasehelper.getDataExpense();
        cursorexpense.moveToFirst();
        for (int i = 0; i < cursorexpense.getCount(); i++) {
            String total = cursorexpense.getString(cursorexpense.getColumnIndex("amount"));
            sarvaloExpense = sarvaloExpense + Integer.parseInt(total);
            expenseTotal.add(total);
            cursorexpense.moveToNext();
        }
        Total = sarvaloIncome - sarvaloExpense;
        Log.e("Total", "onCreateView: " + Total);
    }



    private void findView(View rootView) {

        rvTranscation = rootView.findViewById(R.id.rvTranscation);
        txtTotalAmount = rootView.findViewById(R.id.txtTotalAmount);
        lPdf = rootView.findViewById(R.id.lPdf);
        swipeRefreshTrans = rootView.findViewById(R.id.swipeRefreshTran);

    }

    @Override
    public void onItemClick(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose an Option");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                View view = LayoutInflater.from(getActivity()).inflate(R.layout.bottomsheet_update_item, null);
                BottomSheetDialog bottomSheetDialogUpdate = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogThemeNew);
                bottomSheetDialogUpdate.setContentView(view);
                bottomSheetDialogUpdate.setCanceledOnTouchOutside(false);
                bottomSheetDialogUpdate.setDismissWithAnimation(true);


                ImageView ivCloseAdd=bottomSheetDialogUpdate.findViewById(R.id.ivCloseAdd);
                ImageView ivUpLogo=bottomSheetDialogUpdate.findViewById(R.id.ivUpLogo);
                EditText etUpAmount=bottomSheetDialogUpdate.findViewById(R.id.etUpAmount);
                EditText etUpNote=bottomSheetDialogUpdate.findViewById(R.id.etUpNote);
                TextView txtUpdate=bottomSheetDialogUpdate.findViewById(R.id.txtUpdate);
                TextView txtUpTitle=bottomSheetDialogUpdate.findViewById(R.id.txtUpTitle);

                final int id = modelArrayList.get(pos).getId();
                String amount = modelArrayList.get(pos).getAmount();
                String note = modelArrayList.get(pos).getNote();
                ivUpLogo.setImageResource(CatlistAdapter.image[modelArrayList.get(pos).getImage_pos()]);
                txtUpTitle.setText(modelArrayList.get(pos).getCategory_type());

                etUpAmount.setText(amount);
                etUpNote.setText(note);

                ivCloseAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialogUpdate.dismiss();
                    }
                });

                txtUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etUpAmount.getText().toString().trim().length() <= 0) {
                            etUpAmount.setError("Please Enter Amount");
                        } else {
                            bottomSheetDialogUpdate.dismiss();
                            databasehelper.updateTransactionData(id, etUpAmount.getText().toString(),
                                    etUpNote.getText().toString());
                            etUpAmount.setText("");
                            etUpNote.setText("");
                            mTranAdapter.notifyDataSetChanged();
                        }
                    }
                });

                bottomSheetDialogUpdate.show();

            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete!!");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String type = modelArrayList.get(pos).getTransaction_type();
                        final int ids = modelArrayList.get(pos).getId();
                        db = databasehelper.getReadableDatabase();
                        int result = databasehelper.deleteTransactionData(ids);
                        int amount = Integer.parseInt(modelArrayList.get(pos).getAmount());
                        if (result > 0) {
                            modelArrayList.remove(pos);
                            mTranAdapter.notifyDataSetChanged();
                            if (type.equals("EXPENSE")) {
                                Total += amount;
                            } else {
                                Total -= amount;
                            }
                            txtTotalAmount.setText(Total + "");
//                            txtAmountExpense.setText(sarvaloExpense + "");
//                            txtAmountIncome.setText(sarvaloIncome + "");
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        builder.show();
    }
}
