package com.talhasyed.bidit.controller;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.talhasyed.bidit.R;
import com.talhasyed.bidit.model.ListingModel;
import com.talhasyed.bidit.storage.ListingCRUD;
import com.talhasyed.bidit.textwatcher.SelfClearingEditTextWatcher;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {
    private DateTime closingTimeSet;
    private TextView tvClosingTime;
    private EditText etName, etDesc;
    private TextInputLayout tilName, tilDesc;
    private ListingCRUD listingCRUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        tvClosingTime = (TextView) findViewById(R.id.textViewAddItemClosingTime);
        etDesc = (EditText) findViewById(R.id.editTextAddItemDescription);
        etName = (EditText) findViewById(R.id.editTextAddItemName);
        tilDesc = (TextInputLayout) findViewById(R.id.textInputLayoutAddItemDescription);
        tilName = (TextInputLayout) findViewById(R.id.textInputLayoutAddItemName);

        etName.addTextChangedListener(new SelfClearingEditTextWatcher(tilName));
        etDesc.addTextChangedListener(new SelfClearingEditTextWatcher(tilDesc));

        listingCRUD = new ListingCRUD(getContentResolver());
    }

    public void launchTimePicker(View view) {
        // Initialize
        SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                getString(R.string.label_datetime_dialog),
                getString(R.string.positive_button_datetime_picker),
                getString(R.string.negative_button_datetime_picker)
        );
        if (closingTimeSet != null) {
            dateTimeDialogFragment.setDay(closingTimeSet.getDayOfMonth());
            dateTimeDialogFragment.setHour(closingTimeSet.hourOfDay().get());
            dateTimeDialogFragment.setMinute(closingTimeSet.minuteOfHour().get());
            dateTimeDialogFragment.setMonth(closingTimeSet.monthOfYear().get()-1);
            dateTimeDialogFragment.setYear(closingTimeSet.getYear());
        }
        // Set listener
        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {

                closingTimeSet = new DateTime(date);
                if (closingTimeSet.isBeforeNow()) {
                    Toast.makeText(AddItemActivity.this, "Please select a closing time in the future", Toast.LENGTH_SHORT).show();
                    closingTimeSet = null;
                    tvClosingTime.setText("");
                } else {
                    tvClosingTime.setText(DateTimeFormat.longDateTime().withLocale(Locale.getDefault()).withZone(DateTimeZone.getDefault()).print(closingTimeSet));
                }
                // Date is get on positive button click
                // Do something
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                tvClosingTime.setText("");
                closingTimeSet = null;
                // Date is get on negative button click
            }
        });
        // Show
        dateTimeDialogFragment.show(getSupportFragmentManager(), "dialog_time");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            if (saveEntry()) {
                finish();
                return true;
            } else {
                return false;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean saveEntry() {
        ListingModel listing = collectAndValidate();
        if (listing == null) {
            return false;
        } else {
            if (listingCRUD.insert(listing) == null) {
                Toast.makeText(this, "Error Inserting into DB", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }

        }
    }

    private ListingModel collectAndValidate() {
        ListingModel model = new ListingModel();
        if (TextUtils.isEmpty(etName.getText())) {
            tilName.setErrorEnabled(true);
            tilName.setError(getString(R.string.error_field_required));
            etName.requestFocus();
            return null;
        } else {
            model.setName(etName.getText().toString().trim());
        }


        if (TextUtils.isEmpty(etDesc.getText())) {
            tilDesc.setErrorEnabled(true);
            tilDesc.setError(getString(R.string.error_field_required));
            etDesc.requestFocus();
            return null;
        } else {
            model.setDescription(etDesc.getText().toString().trim());
        }
        if (closingTimeSet == null) {
            Toast.makeText(this, "Please select a closing time", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            if (closingTimeSet.isBeforeNow()) {
                Toast.makeText(AddItemActivity.this, "Please select a closing time in the future", Toast.LENGTH_SHORT).show();
                tvClosingTime.setText("");
                return null;
            } else {
                model.setClosingDate(closingTimeSet);
            }
        }
        model.setStartDate(new DateTime());
        return model;
    }

}
