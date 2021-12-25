package com.room.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.room.demo.database.DatabaseHelper;
import com.room.demo.database.DatabaseObserver;
import com.room.demo.database.Purchase;
import com.room.demo.utils.Constants;
import com.room.demo.utils.SharedPreferencesHelper;
import com.room.demo.utils.UiUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DatabaseObserver {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;

    private DatabaseHelper mDbHelper;

    private LinearLayout llStatement;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private float mCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llStatement = findViewById(R.id.ll_statement);
        mSwipeRefreshLayout = findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this::readAllElements);

        mContext = getApplicationContext();
        mDbHelper = new DatabaseHelper(mContext, this);

        FloatingActionButton fab = findViewById(R.id.fab_new_event);
        fab.setOnClickListener(v -> showAddItemAlertMessage());

        float credit = SharedPreferencesHelper.getFloat(mContext, Constants.KEY_CREDIT_VALUE);
        if (credit < 0) {
            Log.d(TAG, "User does not have credit saved!");
            showRequestCreditAlertMessage();
        } else {
            Log.d(TAG, "Credit saved! Value: " + credit);
            mCredit = credit;
        }
    }

    // Database methods
    private void readAllElements() {
        Log.d(TAG, "readAll");
        mDbHelper.getAllElements();
    }

    private void addItem(String description, float price) {
        mDbHelper.insertElement(new Purchase(price, description));
    }

    private void deleteElementById(int id) {
        mDbHelper.deleteElementById(id);
    }

    private void populateStatement(List<Purchase> purchaseList) {
        runOnUiThread(() -> llStatement.removeAllViews());
        float spendCredit = 0;
        for (Purchase purchase : purchaseList) {
            int id = purchase.getId();
            String description = purchase.getDescription();
            String price = getString(R.string.price_symbol_negative, purchase.getPrice());

            spendCredit += purchase.getPrice();

            RelativeLayout relativeLayout = UiUtils.createRelativeLayout(mContext);
            relativeLayout.setOnClickListener(v -> showItemSelectedAlertMessage(id, description, price));

            TextView tvPrice =
                    UiUtils.createDefaultTextView(mContext, RelativeLayout.ALIGN_PARENT_RIGHT, price);
            TextView tvDescription =
                    UiUtils.createDefaultTextView(mContext, RelativeLayout.ALIGN_PARENT_LEFT, description);

            relativeLayout.addView(tvDescription);
            relativeLayout.addView(tvPrice);

            runOnUiThread(() -> llStatement.addView(relativeLayout));
        }
        addTotalValue(spendCredit);
    }

    private void addTotalValue(float spendCredit) {
        RelativeLayout relativeLayout = UiUtils.createRelativeLayout(mContext);
        relativeLayout.setBackgroundColor(getColor(R.color.colorLightGreen));
        relativeLayout.setOnClickListener(v -> showUpdateCreditMessage());

        TextView tvMessage =
                UiUtils.createDefaultTextView(mContext, RelativeLayout.ALIGN_PARENT_LEFT, "Valor restante no ticket: ");
        TextView tvRemainingValue =
                UiUtils.createDefaultTextView(mContext, RelativeLayout.ALIGN_PARENT_RIGHT, getReamingCreditAsString(spendCredit));

        relativeLayout.addView(tvRemainingValue);
        relativeLayout.addView(tvMessage);
        runOnUiThread(() -> llStatement.addView(relativeLayout));
    }

    private String getReamingCreditAsString(float spendCredit) {
        float spend = mCredit - spendCredit;
        return getString(R.string.price_symbol, spend);
    }

    private void showRequestCreditAlertMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.title_set_credit_value);

        EditText etCredit = new EditText(mContext);
        etCredit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        builder.setView(etCredit);

        builder.setNeutralButton(getString(R.string.button_text_confirm), ((dialog, which) -> {
            float value = Float.parseFloat(etCredit.getText().toString());
            SharedPreferencesHelper.putFloat(mContext, Constants.KEY_CREDIT_VALUE, value);
            mCredit = value;
            readAllElements();
        }));
        MainActivity.this.runOnUiThread(() -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    private void showAddItemAlertMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.title_add_new_expense);

        EditText inputDescription = new EditText(mContext);
        inputDescription.setInputType(InputType.TYPE_CLASS_TEXT);

        EditText inputPrice = new EditText(mContext);
        inputPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        LinearLayout linearLayout = UiUtils.createLinearLayout(mContext);

        linearLayout.addView(UiUtils.createTextView(mContext, getString(R.string.text_description)));
        linearLayout.addView(inputDescription);
        linearLayout.addView(UiUtils.createTextView(mContext, getString(R.string.text_price)));
        linearLayout.addView(inputPrice);

        builder.setView(linearLayout);

        builder.setPositiveButton(R.string.button_text_confirm, (dialogInterface, i) -> {
            String description = inputDescription.getText().toString();
            float price = Float.parseFloat(inputPrice.getText().toString());
            addItem(description, price);
        });
        builder.setNegativeButton(R.string.button_text_cancel, (dialogInterface, i) -> {});
        MainActivity.this.runOnUiThread(() -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    private void showItemSelectedAlertMessage(int id, String message, String price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.title_purchase_details);
        builder.setMessage(message + "\n\n" + getString(R.string.text_value_specification, price));
        builder.setNeutralButton(R.string.button_text_ok, null);
        builder.setNeutralButton(R.string.button_text_delete, ((dialogInterface, i) -> deleteElementById(id)));
        MainActivity.this.runOnUiThread(() -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    private void showUpdateCreditMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.title_credit);
        String message = getString(R.string.text_confirm_change_credit, mCredit);
        builder.setMessage(message);
        builder.setNegativeButton(R.string.button_text_cancel, null);
        builder.setPositiveButton(R.string.button_text_update, (dialogInterface, i) -> showRequestCreditAlertMessage());
        MainActivity.this.runOnUiThread(() -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @Override
    public void onDatabaseConnected() {
        Log.d(TAG, "onDatabaseConnected()");
        readAllElements();
    }

    @Override
    public void onConnectError() {
        Log.d(TAG, "onConnectError()");
    }

    @Override
    public void onElementInserted() {
        showMessage("Elemento inserido com sucesso!");
        readAllElements();
    }


    @Override
    public void onInsertError() {
        showMessage("Erro ao inserir novos elementos!");
    }

    @Override
    public void onElementRead(List<Purchase> purchases) {
        populateStatement(purchases);
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onReadError() {
        showMessage("Erro lendo elementos salvos!");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onElementDeleted() {
        showMessage("Elemento excluido com sucesso!");
        readAllElements();
    }

    @Override
    public void onDeleteError() {
        showMessage("Erro ao deletar o elemnto!");
    }

    private void showMessage(String message) {
        Log.d(TAG, message);
        //Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}