package com.dspread.demoui.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dspread.demoui.R;
import com.dspread.demoui.activity.MainActivity;
import com.dspread.demoui.activity.PaymentActivity;
import com.dspread.demoui.beans.Constants;
import com.dspread.demoui.utils.MoneyUtil;
import com.dspread.demoui.utils.SharedPreferencesUtil;
import com.dspread.demoui.utils.Utils;
import com.dspread.demoui.widget.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class Mydialog {
    private static SharedPreferencesUtil preferencesUtil;
    public interface OnMyClickListener {
        void onCancel();

        void onConfirm();
    }

    /**
     * loading
     */
    public static Dialog Ldialog;

    public static void loading(Activity mContext, String msg) {
        Ldialog = new Dialog(mContext);
        Ldialog.setContentView(R.layout.processing_dialog);
        Button confirmButton = Ldialog.findViewById(R.id.confirmButton);
        TextView messageTextView = Ldialog.findViewById(R.id.msgTextView);
        messageTextView.setText(msg);
        messageTextView.setTextSize(20);
        Window dialogWindow = Ldialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        Ldialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        lp.dimAmount = 0.4f;
        dialogWindow.setAttributes(lp);
        Ldialog.setCanceledOnTouchOutside(false);
//        Ldialog.setCancelable(false);
        if (!mContext.isFinishing()) {
            Ldialog.show();
        }
    }

    /*
     *Error message prompt box
     */
    public static AlertDialog ErrorDialog;

    public static void ErrorDialog(Activity mContext, String msg, OnMyClickListener listener) {
        View view = View.inflate(mContext, R.layout.alert_dialog, null);
        Button mbtnConfirm = view.findViewById(R.id.btnConfirm);
        TextView mtvInfo = view.findViewById(R.id.tvInfo);
        mtvInfo.setText(msg);
        Button mbtnCancel = view.findViewById(R.id.btnCancel);
        mbtnCancel.setVisibility(View.GONE);
        mbtnConfirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirm();
            } else {
                ErrorDialog.dismiss();
                if (!msg.equals(mContext.getString(R.string.bad_swipe))) {
                    mContext.finish();
                }
            }
        });

        ErrorDialog = new AlertDialog.Builder(mContext).create();
        ErrorDialog.setCanceledOnTouchOutside(false);
        ErrorDialog.setCancelable(false);
        if (!mContext.isFinishing()) {
            ErrorDialog.show();
        }
        //显示对话框
        Window window = ErrorDialog.getWindow();
        window.setWindowAnimations(R.style.popupAnimation);
        window.setBackgroundDrawable(null);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        Display d = wm.getDefaultDisplay();
        WindowManager.LayoutParams p = ErrorDialog.getWindow().getAttributes();
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        ErrorDialog.getWindow().setAttributes(p);
        window.setContentView(view);

    }


    public static AlertDialog manualExitDialog;

    public static void manualExitDialog(Activity mContext, String msg, OnMyClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.alert_dialog, null);
        View viewv = view.findViewById(R.id.view_v);
        viewv.setVisibility(View.VISIBLE);
        TextView mtvInfo = view.findViewById(R.id.tvInfo);
        mtvInfo.setText(msg);
        Button mbtnConfirm = view.findViewById(R.id.btnConfirm);
        Button mbtnCancel = view.findViewById(R.id.btnCancel);
        mbtnCancel.setVisibility(View.VISIBLE);
        mbtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirm();
            }
        });
        mbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
            }
        });

        manualExitDialog = builder.create();
        if (!mContext.isFinishing()) {
            manualExitDialog.show();
        }
        Window window = manualExitDialog.getWindow();
        window.setWindowAnimations(R.style.popupAnimation);
        window.setBackgroundDrawable(null);
        window.setGravity(Gravity.BOTTOM);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        WindowManager.LayoutParams p = manualExitDialog.getWindow().getAttributes();
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        manualExitDialog.getWindow().setAttributes(p);
        manualExitDialog.setCanceledOnTouchOutside(true);
        window.setContentView(view);
    }


    public static AlertDialog payTypeDialog;
    private static RecyclerView rvlist;
    private static String transactionTypeString = "GOODS";
    private static MyAdapter myAdapter;
    public static final int BLUETOOTH =1;
    public static final int UART =2;
    public static final int USB_OTG_CDC_ACM =3;

    public static void payTypeDialog(Activity mContext, String amount, long inputMoney, String[] data) {

        payTypeDialog = new AlertDialog.Builder(mContext).create();
        if (!mContext.isFinishing()) {
            payTypeDialog.show();
        }
        Window window = payTypeDialog.getWindow();
        window.setWindowAnimations(R.style.popupAnimation);
        window.setBackgroundDrawable(null);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        WindowManager.LayoutParams p = payTypeDialog.getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 0.6);
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        payTypeDialog.getWindow().setAttributes(p);
        View view = View.inflate(mContext, R.layout.paytype_dialog_view, null);
        rvlist = view.findViewById(R.id.rv_list);
        rvlist.setLayoutManager(new LinearLayoutManager(mContext.getApplicationContext()));
        myAdapter = new MyAdapter(getArrayList(data));
        rvlist.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener((view1, position, content) -> {
            if (Utils.islistFastClick()) {
                preferencesUtil = SharedPreferencesUtil.getInstance(mContext);
                String conType = (String) preferencesUtil.get(Constants.connType, "");
                if ("CASHBACK".equals(content)) {
                    String inputMoneyString = String.valueOf(inputMoney);
                    cashBackPaymentDialog(mContext,inputMoneyString);
                } else {
                    if (!"".equals(conType)) {
                        transactionTypeString = content;
                        Intent intent = new Intent(mContext, PaymentActivity.class);
                        String inputMoneyString = String.valueOf(inputMoney);
                        intent.putExtra("inputMoney", inputMoneyString);
                        intent.putExtra("paytype", transactionTypeString);
                        mContext.startActivity(intent);
                    }else {
                        ((MainActivity)mContext).switchFragment(1);
                    }
                }
                if(payTypeDialog != null) {
                    payTypeDialog.dismiss();
                }
                payTypeDialog = null;
                myAdapter = null;

            }
        });
        window.setContentView(view);
        payTypeDialog.setCanceledOnTouchOutside(true);
        payTypeDialog.setCancelable(true);
    }

    private static List<String> getArrayList(String[] data) {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            dataList.add(data[i]);
        }
        return dataList;
    }

    //Transaction confirmation information
    public static AlertDialog onlingDialog;

    public static Dialog cashBackPaymentDialog;

    public static void cashBackPaymentDialog(Activity mContext, String inputMoney) {
        View view = View.inflate(mContext, R.layout.cashback_dialog, null);
        TextView textView = view.findViewById(R.id.messageTextView);
        textView.setTextSize(20);

        EditText etInputMoney = view.findViewById(R.id.et_inputMoney);
        etInputMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (s.length() == 1 && s.toString().equals(".")) {
                    etInputMoney.setText("");
                }
                if (str.contains(".")) {
                    String[] strArr = str.split("\\.");
                    if (strArr.length > 1 && strArr[1].length() > 2) {
                        s.delete(str.length() - 1, str.length());
                    }
                }
                if (str.length() > 10 && !str.contains(".")) {
                    s.delete(str.length() - 1, str.length());
                }

            }
        });

        view.findViewById(R.id.confirmButton).setOnClickListener(
                v -> {
                    String cashbackAmounts = etInputMoney.getText().toString().trim();
                    if (!"".equals(cashbackAmounts) && !"0".equals(cashbackAmounts)) {
                        Double inputCashbackAmount = Double.valueOf(cashbackAmounts);
                        Long inputCashbackAmounts=MoneyUtil.yuan2fen(inputCashbackAmount);
                        String inputcashbackMoney = String.valueOf(inputCashbackAmounts);
                        preferencesUtil = SharedPreferencesUtil.getInstance(mContext);
                        String conType = (String) preferencesUtil.get( "conType", "");

                        if (conType != null) {
                            Intent intent = new Intent(mContext, PaymentActivity.class);
                            String inputMoneyString = String.valueOf(inputMoney);
                            intent.putExtra("inputMoney", inputMoneyString);
                            intent.putExtra("paytype", "CASHBACK");
                            intent.putExtra("cashbackAmounts", inputcashbackMoney);
                            intent.putExtra("connect_type", 2);
                            mContext.startActivity(intent);
                            cashBackPaymentDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.set_amount), Toast.LENGTH_SHORT).show();
                    }


                });


        cashBackPaymentDialog = new Dialog(mContext);
        cashBackPaymentDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        cashBackPaymentDialog.setCanceledOnTouchOutside(false);
        if (!mContext.isFinishing()) {
            cashBackPaymentDialog.show();
        }

        etInputMoney.setFocusable(true);
        etInputMoney.setFocusableInTouchMode(true);
        etInputMoney.requestFocus();
        etInputMoney.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etInputMoney, 0);
        }, 100);

        Window window = cashBackPaymentDialog.getWindow();
        window.setWindowAnimations(R.style.popupAnimation);
        window.setBackgroundDrawable(null);
        window.setGravity(Gravity.BOTTOM);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        WindowManager.LayoutParams p = cashBackPaymentDialog.getWindow().getAttributes();

        p.width = (int) (d.getWidth() * 1);

        cashBackPaymentDialog.getWindow().setAttributes(p);
        cashBackPaymentDialog.setContentView(view);
    }


}
