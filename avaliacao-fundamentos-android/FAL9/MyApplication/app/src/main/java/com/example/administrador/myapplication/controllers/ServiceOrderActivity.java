package com.example.administrador.myapplication.controllers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.administrador.myapplication.R;
import com.example.administrador.myapplication.models.entities.ServiceOrder;
import com.example.administrador.myapplication.util.AppUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class ServiceOrderActivity extends BaseAppActivity {

    public static final int REQUEST_CODE_PICK_CONTACT = 3;
    public static final String EXTRA_SERVICE_ORDER = "EXTRA_SERVICE_ORDER";
    public static final String EXTRA_START_BENCHMARK = "EXTRA_START_BENCHMARK";

    private static final String TAG = ServiceOrderActivity.class.getSimpleName();

    private EditText mEditTextClientName, mEditTextClientPhone, mEditTextAddress, mEditTextDate, mEditTextTime, mEditTextValue, mEditTextDescription;
    private Switch mSwitchPaid;
    private ServiceOrder mServiceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order_material);

        this.bindElements();

        final Bundle extras = super.getIntent().getExtras();
        if (extras == null) {
            mServiceOrder = new ServiceOrder();
            final Date dateNow = new Date();
            mEditTextDate.setText(AppUtil.formatDate(dateNow));
            mEditTextTime.setText(AppUtil.formatTime(dateNow));
        } else {
            mServiceOrder = AppUtil.get(extras.getParcelable(EXTRA_SERVICE_ORDER));

            final long startTime = AppUtil.get(extras.getLong(EXTRA_START_BENCHMARK));
            final long endTime = SystemClock.elapsedRealtime();
            Log.d(TAG, String.format("Benchmark Extras (Serializable x Parcelable): %dms", endTime - startTime));

            mEditTextClientName.setText(mServiceOrder.getClient());
            mEditTextClientPhone.setText(mServiceOrder.getPhone());
            mEditTextAddress.setText(mServiceOrder.getAddress());
            mEditTextDate.setText(AppUtil.formatDate(mServiceOrder.getDate()));
            mEditTextTime.setText(AppUtil.formatTime(mServiceOrder.getDate()));
            mEditTextValue.setText(AppUtil.formatDecimal(mServiceOrder.getValue()));
            mSwitchPaid.setChecked(mServiceOrder.isPaid());
            mEditTextDescription.setText(mServiceOrder.getDescription());
        }
        mSwitchPaid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mServiceOrder.setPaid(isChecked);
            }
        });
    }

    private void bindElements() {
        mEditTextClientName = AppUtil.get(this.findViewById(R.id.editTextClientName));
        mEditTextClientPhone = AppUtil.get(this.findViewById(R.id.editTextClientPhone));
        mEditTextAddress = AppUtil.get(this.findViewById(R.id.editTextAddress));
        mEditTextDate = AppUtil.get(this.findViewById(R.id.editTextDate));
        mEditTextTime = AppUtil.get(this.findViewById(R.id.editTextTime));
        mEditTextValue = AppUtil.get(this.findViewById(R.id.editTextValue));
        mSwitchPaid = AppUtil.get(this.findViewById(R.id.switchPaid));
        mEditTextDescription = AppUtil.get(this.findViewById(R.id.editTextDescription));

        //TODO: Explanation 1:
        mEditTextClientName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_edittext_client, 0);
        mEditTextClientName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mEditTextClientName.getRight() - mEditTextClientName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        //TODO: Explanation 2:
                        final Intent goToSOContacts = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        goToSOContacts.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                        startActivityForResult(goToSOContacts, REQUEST_CODE_PICK_CONTACT);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_service_order, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSave:
                saveServiceOrder();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @see <a href="http://developer.android.com/training/basics/intents/result.html">Getting a Result from an Activity</a>
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                //TODO: Explanation 3:
                try {
                    final Uri contactUri = data.getData();
                    final String[] projection = {
                            ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    };
                    final Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                    cursor.moveToFirst();

                    mEditTextClientName.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME)));
                    mEditTextClientPhone.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                    cursor.close();
                } catch (Exception e) {
                    Log.d(TAG, "Unexpected error");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveServiceOrder() {
        final Calendar serviceOrderCalendar = Calendar.getInstance(AppUtil.LOCALE_PT_BR);

        boolean isValid = verifyMandatoryFields(mEditTextClientName, mEditTextClientPhone, mEditTextDate, mEditTextTime, mEditTextValue)
            & verifyDateFields(mEditTextDate)
            & verifyTimeFields(mEditTextTime)
            & verifyValueFields(mEditTextValue);

        if (isValid) {
            mServiceOrder.setClient(mEditTextClientName.getText().toString().trim());
            mServiceOrder.setPhone(mEditTextClientPhone.getText().toString().trim());
            mServiceOrder.setAddress(mEditTextAddress.getText().toString().trim());
            mServiceOrder.setDate(serviceOrderCalendar.getTime());
            mServiceOrder.setValue(Double.valueOf(mEditTextValue.getText().toString().trim()));
            mServiceOrder.setDescription(mEditTextDescription.getText().toString().trim());
            mServiceOrder.save();
            super.setResult(RESULT_OK);
            super.finish();
        }
    }
}
