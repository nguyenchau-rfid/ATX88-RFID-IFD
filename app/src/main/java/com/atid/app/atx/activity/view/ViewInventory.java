package com.atid.app.atx.activity.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atid.app.atx.R;
import com.atid.app.atx.ReadJson.ApiUnit;
import com.atid.app.atx.ReadJson.ChiTietSP;
import com.atid.app.atx.ReadJson.ChitietSP_Inventories;
import com.atid.app.atx.ReadJson.ListViewAdapter;
import com.atid.app.atx.ReadJson.ListViewPairedAdapter;
import com.atid.app.atx.ReadJson.LoginAdapter;
import com.atid.app.atx.ReadJson.ModelRFIDTagReference;
import com.atid.app.atx.ReadJson.PostTag;
import com.atid.app.atx.ReadJson.RFIDTagReference;
import com.atid.app.atx.ReadJson.RfidToProductCode;
import com.atid.app.atx.ReadJson.TokenAccess;
import com.atid.app.atx.ReadJson.TokenManager;
import com.atid.app.atx.ReadJson.WebApi;
import com.atid.app.atx.activity.MainActivity;
import com.atid.app.atx.adapter.DataListAdapter;
import com.atid.app.atx.adapter.KeyListAdapter;
import com.atid.app.atx.data.Constants;
import com.atid.app.atx.data.GlobalData;
import com.atid.app.atx.dialog.MessageBox;
import com.atid.app.atx.dialog.WaitDialog;
import com.atid.lib.diagnostics.ATException;
import com.atid.lib.module.barcode.types.BarcodeType;
import com.atid.lib.module.rfid.uhf.params.SelectMask6cParam;
import com.atid.lib.module.rfid.uhf.params.TagExtParam;
import com.atid.lib.module.rfid.uhf.types.BankType;
import com.atid.lib.module.rfid.uhf.types.Mask6cAction;
import com.atid.lib.module.rfid.uhf.types.Mask6cTarget;
import com.atid.lib.module.rfid.uhf.types.SelectFlag;
import com.atid.lib.module.rfid.uhf.types.SessionFlag;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.diagnotics.ATLog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import retrofit2.Call;
import retrofit2.Response;


public class ViewInventory extends BaseView implements OnClickListener, OnCheckedChangeListener,
        RadioGroup.OnCheckedChangeListener, OnItemLongClickListener, AdapterView.OnItemClickListener {


    private static final int METHOD_RFID = 0;
    private static final int METHOD_BARCODE = 1;
    private static final int METHOD_TRIGGER = 4;
    private static final int MASK_EPC_OFFSET = 16;
    private static final int NIBLE_SIZE = 4;
    private static final int NO_RESTART = 0;
    private static final double TPS_TIME_SECOND = 1000.0;
    private final static int MAX_VOLUME = 10;
    // Chọn loại luồng âm thanh để phát nhạc.
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private static final int MAX_STREAMS = 5;
    // ------------------------------------------------------------------------
    // Member Variable
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // Khai bao cac bien doc json ve
    // ------------------------------------------------------------------------
    LoginAdapter loginadapter = new LoginAdapter();
    ListViewAdapter customListAdapter;
    ListViewPairedAdapter customListPairedAdapter;
    ArrayList<ChiTietSP> inventoriesSP_Array = new ArrayList<>();
    ArrayList<ModelRFIDTagReference> RfidToProductCode_Array = new ArrayList<>();
    ArrayList<ModelRFIDTagReference> RfidPaired_Array = new ArrayList<>();
    ArrayList<ModelRFIDTagReference> Rfid_Not_Paired_Array = new ArrayList<>();
    CheckBox chkFRID, chkRFIDTag;
    Double tempdatacode;
    String productCode;
    TokenManager ngayToken = new TokenManager();
    String keyDay_IFD = "Ngay_IFD";
    String keyDay_Kiot = "Ngay_Kiot";
    String keyToken_IFD = "keytoken_IFD";
    String keyToken_Kiot = "keytoken_Kiot";
    TextView txtSLRFID_KIOT;
    TextView txtSanPham;
    boolean timsp = false;
    int masp_tim;
    boolean SetchkFRID = false;
    private RadioGroup rdMethod;
    private RadioButton rdMethodRfid;
    private RadioButton rdMethodBarcode;
    private RadioButton rdMethodTrigger;
    private ListView lstData;
    private ListView lstTrigger;
    private TextView txtCount;
    private TextView txtTotalCount;
    private TextView txtKeyType;
    private TextView txtKeyState;
    private TextView txtTagSpeed;
    private LinearLayout linearAction;
    private LinearLayout linearCount;
    private LinearLayout linearTriggerState;
    private Button btnAction;
    private Button btnClear;
    private Button btnSetting;
    private Button btnUpload;
    private DataListAdapter adpData;
    private KeyListAdapter adpTrigger;
    private volatile boolean mIsContinuousMode;
    private volatile boolean mIsReportRssi;
    private int mTotalCount;
    private int mMethod;
    private ISetWriteMemoryListener mSetWriteListener;
    private int mRfidTagCount;
    private volatile boolean mIsRfidTagSpeed;
    private long mRfidTagLastTime;
    private int mBarcodeRestartTime;
    private volatile boolean mIsBarcodeRestart;
    private Handler mHandler;
    private WebApi mAPIServiceIFD, mAPIServiceKiot, mAPIService_PostReadTag;
    private String m_Text = "";
    private SoundPool soundPool;
    // private AudioManager audioManager;
    private int soundID;
    private boolean PairedWithTagID = false;
    int tempcodepproduct;
    boolean isCheckTag = false;
    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------
    private Runnable mRestartBarcodeScanProc = new Runnable() {
        @Override
        public void run() {
            ResultCode res = ResultCode.NoError;
            if ((res = getReader().getBarcode().startDecode()) != ResultCode.NoError) {
                ATLog.e(TAG, "ERROR. $mRestartBarcodeScanProc.run() - Failed to start decode [%s]", res);
                MessageBox.show(
                        getActivity(), String.format(Locale.US, "%s. [%s]",
                                getString(R.string.msg_fail_start_decode), res.getMessage()),
                        getString(R.string.title_error));
                getReader().getBarcode().stop();
                return;
            }
        }
    };

    // ------------------------------------------------------------------------
    // Public Methods
    // ------------------------------------------------------------------------

    public ViewInventory() {
        super();
        TAG = ViewInventory.class.getSimpleName();
        mId = VIEW_INVENTORY;
        mTotalCount = 0;
        mMethod = METHOD_RFID;

        mIsContinuousMode = true;
        mIsReportRssi = false;

        mSetWriteListener = null;

        mRfidTagCount = 0;
        mIsRfidTagSpeed = false;
        mRfidTagLastTime = 0;

        mBarcodeRestartTime = 0;
        mIsBarcodeRestart = false;
        mHandler = new Handler();
        mAPIServiceIFD = ApiUnit.getServiceIFD();
        mAPIServiceKiot = ApiUnit.getServicesKiot();

    }

    // ------------------------------------------------------------------------
    // Override Event Methods
    // ------------------------------------------------------------------------

    public void setSetWriteMemoryListener(ISetWriteMemoryListener listener) {
        mSetWriteListener = listener;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        OperationMode mode = OperationMode.Normal;
        String checkId = "";
        switch (checkedId) {
            case R.id.method_rfid:
                if (!rdMethodRfid.isChecked())
                    return;
                if (mMethod == METHOD_RFID)
                    return;
                mMethod = METHOD_RFID;
                mode = OperationMode.Normal;
                checkId = "method_rfid";
                break;
            case R.id.method_barcode:
                if (!rdMethodBarcode.isChecked())
                    return;
                if (mMethod == METHOD_BARCODE)
                    return;
                mMethod = METHOD_BARCODE;
                mode = OperationMode.Barcode;
                checkId = "method_barcode";
                break;
            case R.id.method_trigger_event_only:
                if (!rdMethodTrigger.isChecked())
                    return;
                if (mMethod == METHOD_TRIGGER)
                    return;
                mMethod = METHOD_TRIGGER;
                mode = OperationMode.TriggerEventOnly;
                checkId = "method_trigger";
                break;
            default:
                ATLog.e(TAG, "ERROR. onCheckedChanged(%d) - Failed to unknown check id", checkedId);
                return;
        }
        try {
            getReader().setOperationMode(mode);
        } catch (ATException e) {
            ATLog.e(TAG, "ERROR. onCheckedChanged(%s) - Faield to set operation mode", checkId);
            return;
        }
        displayOperationMode();
        ATLog.i(TAG, INFO, "INFO. onCheckChnaged(%s)", checkId);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.action:
                if (mMethod == METHOD_RFID || mMethod == METHOD_BARCODE) {
                    action();
                    lstData.setAdapter(adpData);
                }
                break;
            case R.id.setting:
                if (mMethod == METHOD_RFID) {
                    showRfidSetting();
                } else if (mMethod == METHOD_BARCODE) {
                    showBarcodeSetting();
                }
                ATLog.i(TAG, INFO, "INFO. onClick(R.id.setting) - Method [%d]", mMethod);
                break;
            case R.id.clear:
                if (mMethod == METHOD_RFID || mMethod == METHOD_BARCODE)
                    clear();
                break;
            case R.id.chkRFID:
                if (mMethod == METHOD_RFID || mMethod == METHOD_BARCODE)

                    break;
            case R.id.btnUpload:
//                if (mMethod == METHOD_RFID)
//                    Toast.makeText(getActivity(), "Upload data to server "
//                            + loginadapter.getmaUserID() + "\n"
//                            + loginadapter.getLongitude() + "--" + loginadapter.getLatitude() + "\n"
//                            + loginadapter.getLocalAddress(), Toast.LENGTH_LONG).show();
                for(int i=0;i<adpData.getCount();i++)
                {
                    Log.d("testCheck","Tag "+ adpData.getData(i));
                    PostReadTag(adpData.getData(i));
                }

                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        parent.showContextMenu();
        return true;
    }
    // ------------------------------------------------------------------------
    // Action Methods
    // ------------------------------------------------------------------------

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hienthiSP(position);
    }

    @SuppressWarnings("incomplete-switch")
    private void action() {
        ResultCode res = ResultCode.NoError;
        enableWidgets(false);

        if (getReader().getAction() == ActionState.Stop) {
            if (mMethod == METHOD_RFID) {
                if ((res = getReader().getRfidUhf().inventory6c()) != ResultCode.NoError) {
                    ATLog.e(TAG, "ERROR. action() - Failed to start inventory [%s]", res);
                    MessageBox.show(
                            getActivity(), String.format(Locale.US, "%s. [%s]",
                                    getString(R.string.msg_fail_start_inventory), res.getMessage()),
                            getString(R.string.title_error));
                    getReader().getRfidUhf().stop();
                    return;
                }
            } else if (mMethod == METHOD_BARCODE) {
                if (mIsBarcodeRestart) {
                    mIsBarcodeRestart = false;
                } else {
                    if ((res = getReader().getBarcode().startDecode()) != ResultCode.NoError) {
                        ATLog.e(TAG, "ERROR. action() - Failed to start decode [%s]", res);
                        MessageBox.show(
                                getActivity(), String.format(Locale.US, "%s. [%s]",
                                        getString(R.string.msg_fail_start_decode), res.getMessage()),
                                getString(R.string.title_error));
                        getReader().getBarcode().stop();
                        return;
                    }

                    if (mBarcodeRestartTime > NO_RESTART) {
                        mIsBarcodeRestart = true;
                    } else {
                        mIsBarcodeRestart = false;
                    }
                }
            }

        } else {

            if (mIsBarcodeRestart) {
                if (mMethod == METHOD_BARCODE) {
                    mHandler.removeCallbacks(mRestartBarcodeScanProc);
                    if ((res = getReader().getBarcode().stop()) != ResultCode.NoError) {
                        ATLog.e(TAG, "ERROR. action() - Failed to stop operation [%s]", res);
                        MessageBox.show(
                                getActivity(), String.format(Locale.US, "%s. [%s]",
                                        getString(R.string.msg_fail_stop_action), res.getMessage()),
                                getString(R.string.title_error));
                        enableWidgets(true);
                        return;
                    }
                }
                mIsBarcodeRestart = false;
            } else {
                switch (getReader().getAction()) {
                    case Decoding:
                        if (mMethod == METHOD_BARCODE) {
                            if ((res = getReader().getBarcode().stop()) != ResultCode.NoError) {
                                ATLog.e(TAG, "ERROR. action() - Failed to stop operation [%s]", res);
                                MessageBox.show(
                                        getActivity(), String.format(Locale.US, "%s. [%s]",
                                                getString(R.string.msg_fail_stop_action), res.getMessage()),
                                        getString(R.string.title_error));
                                enableWidgets(true);
                                return;
                            }
                        }
                        break;
                    case Inventory6c:
                    case InventoryAndDecode:
                        if (mMethod == METHOD_RFID) {
                            if ((res = getReader().getRfidUhf().stop()) != ResultCode.NoError) {
                                ATLog.e(TAG, "ERROR. action() - Failed to stop operation [%s]", res);
                                MessageBox.show(
                                        getActivity(), String.format(Locale.US, "%s. [%s]",
                                                getString(R.string.msg_fail_stop_action), res.getMessage()),
                                        getString(R.string.title_error));
                                enableWidgets(true);
                                return;
                            }
                        }
                        break;
                }
            }
        }
        ATLog.i(TAG, INFO, "INFO. action() - repeat [%s]", mIsBarcodeRestart);
    }

    private void clear() {

        txtKeyType.setText("");
        txtKeyState.setText("");
        mTotalCount = 0;
        adpData.clear();
        PairedWithTagID = false;
        chkFRID.setChecked(false);
        RfidToProductCode_Array.clear();

        inventoriesSP_Array.clear();
        Rfid_Not_Paired_Array.clear();
        RfidPaired_Array.clear();
        txtSanPham.setText("Sản Phẩm");
        txtSLRFID_KIOT.setText("");
        customListAdapter.notifyDataSetChanged();
        if (customListPairedAdapter == null)
            return;
        else
            customListPairedAdapter.notifyDataSetChanged();

        if (btnClear.getText().equals("Quay Lại") || btnClear.getText().equals(getString(R.string.action_clear))) {
            timsp = false;
            btnClear.setText(getString(R.string.action_clear));
            btnAction.setText(getString(R.string.action_start));

        }
        if (chkRFIDTag.getText().equals("Hiển Thị Giày Lẻ Đôi")) {
            PairedWithTagID = false;
            chkRFIDTag.setVisibility(View.GONE);
            chkFRID.setVisibility(View.VISIBLE);

        }
        if (mIsRfidTagSpeed) {
            mRfidTagCount = 0;
            mRfidTagLastTime = 0;
            txtTotalCount.setText(String.format(Locale.US, "0.0 tps"));
        }

        txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
        txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));

        ATLog.i(TAG, INFO, "INFO. clear()");
    }

    // ------------------------------------------------------------------------
    // Override Reader Event Methods
    // ------------------------------------------------------------------------

    private void displayOperationMode() {
        switch (mMethod) {
            case METHOD_RFID:
                lstTrigger.setVisibility(View.GONE);
                lstData.setVisibility(View.VISIBLE);
                linearAction.setVisibility(View.VISIBLE);
                linearCount.setVisibility(View.VISIBLE);
                linearTriggerState.setVisibility(View.GONE);
                if (mIsRfidTagSpeed)
                    txtTagSpeed.setVisibility(View.VISIBLE);
                break;
            case METHOD_BARCODE:
                lstTrigger.setVisibility(View.GONE);
                lstData.setVisibility(View.VISIBLE);
                linearAction.setVisibility(View.VISIBLE);
                linearCount.setVisibility(View.VISIBLE);
                linearTriggerState.setVisibility(View.GONE);
                txtTagSpeed.setVisibility(View.GONE);
                break;
            case METHOD_TRIGGER:
                lstTrigger.setVisibility(View.VISIBLE);
                lstData.setVisibility(View.GONE);
                linearAction.setVisibility(View.INVISIBLE);
                linearCount.setVisibility(View.GONE);
                linearTriggerState.setVisibility(View.VISIBLE);
                txtTagSpeed.setVisibility(View.GONE);
                break;
            default:
                ATLog.e(TAG, "ERROR. changeOperationMode() - Failed to change display operation mode");
                return;
        }

        ATLog.i(TAG, INFO, "INFO. changeOperationMode()");
    }

    @Override
    public void onReaderActionChanged(ATEAReader reader, ResultCode code, ActionState action, Object params) {

        if (code != ResultCode.NoError) {
            ATLog.e(TAG, "ERROR. onReaderActionChanged([%s], %s, %s) - Failed to action changed [%s]", reader, code,
                    action, code);
            enableWidgets(true);
            return;
        }
        if (action == ActionState.Stop) {
            inventoriesSP_Array.clear();
            RfidToProductCode_Array.clear();
            if (chkFRID.isChecked() && SetchkFRID == true) {
                lstData.setAdapter(adpData);
                txtSanPham.setText("Chế độ xem mã RFID");
                txtSLRFID_KIOT.setVisibility(View.INVISIBLE);
                //Log.d("testCheck", SetchkFRID + " if****" +  chkFRID.isChecked() + "--");
            } else {
                txtSanPham.setText("Sản Phẩm");
                txtSLRFID_KIOT.setVisibility(View.VISIBLE);
                //txtSLRFID_KIOT.setText("RFID   | KiotViet");
                //convertRFtoCode("4966640000000000000000AA");
                  ConvertRFtoProductcode();
            }
            Log.d("testCheck", SetchkFRID + " --else--" + chkFRID.isChecked() + "--" + loginadapter.getmaUserID());
            txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
            txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));

            if (mIsRfidTagSpeed) {
                mRfidTagCount = 0;
                mRfidTagLastTime = 0;
            }

            if (mBarcodeRestartTime > NO_RESTART) {
                if (mIsBarcodeRestart) {
                    mHandler.postDelayed(mRestartBarcodeScanProc, mBarcodeRestartTime);
                }
            }

            if (!mIsBarcodeRestart) {
                if (timsp == true) {
                    btnAction.setText("Tìm Sản Phẩm");
                } else
                    btnAction.setText(R.string.action_start);
                enableWidgets(true);
            }

        } else {
            chkRFIDTag.setChecked(false);
            Rfid_Not_Paired_Array.clear();
            btnAction.setText(R.string.action_stop);
            RfidPaired_Array.clear();
            txtSLRFID_KIOT.setText("RFID   | KiotViet");
            enableWidgets(true);
        }


        ATLog.i(TAG, INFO, "EVENT. onReaderActionChanged([%s], %s, %s)", reader, code, action);
    }

    @Override
    public void onReaderReadTag(ATEAReader reader, String tag, Object params) {
        float rssi = 0;
        float phase = 0;
        long tempRFID;
        long time = System.currentTimeMillis();
        double interval = 0.0;
        double tagSpeed = 0.0;
// if (params != null  && tag.equals(masp_tim))
      //  tempRFID = hex2Decimal(tag);
    //    String strLong = Long.toString(tempRFID);
    //    Log.d("CheckRFID", "the RFID : " + tempRFID + " chieudai: " + tag +"**"+strLong);
        // code chay on
//        if (params != null) {
//            TagExtParam param = (TagExtParam) params;
//            rssi = param.getRssi();
//            phase = param.getPhase();
//        }
//        adpData.add(strLong, "", rssi, phase);

        if (params != null) {
            TagExtParam param = (TagExtParam) params;
            rssi = param.getRssi();
            phase = param.getPhase();
        }
//        if (tag == masp_tim && timsp == true) {
//            Log.d("tempRFID", "the RFID : " + timsp + " --- " + rssi + "-- " + tempcodepproduct + "**" + rssi);
//            adpData.updateRssi(tag, "", rssi, phase);
//            setVolume(Math.abs(rssi));
//        } else if (timsp == false) {
//            Log.d("kotimsp", "the RFID : " + rssi + " -- " + phase);
//            adpData.add(tag, "", rssi, phase);
//        }
        adpData.add(tag, "", rssi, phase);
        mTotalCount++;

        txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
        txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));

        if (mIsRfidTagSpeed) {
            mRfidTagCount++;
            if (mRfidTagLastTime == 0) {
                mRfidTagLastTime = time;
                tagSpeed = 0.0;
            } else {
                interval = (double) ((time - mRfidTagLastTime) / TPS_TIME_SECOND);
                tagSpeed = (double) mRfidTagCount / interval;
            }
            txtTagSpeed.setText(String.format(Locale.US, "%.2f tps", tagSpeed));

            ATLog.i(TAG, INFO, "EVENT. onReaderReadTag([%s], [%s], [%.2f, %.2f]) - [%.02f tps]"
                    , reader, tag, rssi, phase, tagSpeed);
        } else
            ATLog.i(TAG, INFO, "EVENT. onReaderReadTag([%s], [%s], [%.2f, %.2f])", reader, tag, rssi, phase);
    }
    public void CovertProductID2EPC()
    {

    }
    public static long hex2Decimal(String a) {
        String s = a.substring(4);
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        long val = 0l;
        int stringLength = s.length();
        Log.d("sendGetANV", "the RFID : " + s + " chieudai: " + s.substring(4));
        int position = 0;
        for (int i = stringLength; i > 0; i--) {
            char c = s.charAt(i - 1);
            int d = digits.indexOf(c);
            val = (long) Math.pow(16, position) * d + val;
            position++;
        }
        return val;
    }

    public void PlaySound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            // Deprecated way of creating a SoundPool before Android API 21.
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
        soundID = soundPool.load(getActivity().getApplicationContext(), R.raw.beep4, 1);
    }

    public void setVolume(double soundVolume) {
        final float volume = ((float) (soundVolume / MAX_VOLUME));
        final float getvol = 1 / volume;
        final float kq = getvol * 3;
        //Log.d("sendGetANV", soundVolume + "-" + volume + "--" + getvol + " * " + kq + " ***");
        //  AudioManager soundPool = (AudioManager) getActivity().getSystemService(AUDIO_SERVICE);
        soundPool.play(soundID, kq, kq, 1, 0, 1);

    }

    @Override
    public void onReaderAccessResult(ATEAReader reader, ResultCode code, ActionState action, String epc, String data,
                                     Object params) {
        float rssi = 0;
        float phase = 0;

        if (params != null) {
            TagExtParam param = (TagExtParam) params;
            rssi = param.getRssi();
            phase = param.getPhase();
        }

        ATLog.i(TAG, INFO, "EVENT. onReaderAccessResult([%s], [%s], %s, [%s], [%s], [%.2f, %.2f])", reader, code,
                action, epc, data, rssi, phase);
    }

    @Override
    public void onReaderReadBarcode(ATEAReader reader, BarcodeType type, String codeId, String barcode, Object params) {
        adpData.add(type, codeId, barcode);
        mTotalCount++;

        txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
        txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));

        ATLog.i(TAG, INFO, "EVENT. onReaderReadBarcode([%s], %s, [%s], [%s])", reader, type, codeId, barcode);
    }

    @Override
    public void onReaderOperationModeChanged(ATEAReader reader, OperationMode mode, Object params) {

        switch (mode) {
            case Normal:
                if (mMethod != METHOD_RFID) {
                    mMethod = METHOD_RFID;
                    rdMethod.check(R.id.method_rfid);
                }
                break;
            case Barcode:
                if (mMethod != METHOD_BARCODE) {
                    mMethod = METHOD_BARCODE;
                    rdMethod.check(R.id.method_barcode);
                }
                break;
            case TriggerEventOnly:
                if (mMethod != METHOD_TRIGGER) {
                    mMethod = METHOD_TRIGGER;
                    rdMethod.check(R.id.method_trigger_event_only);
                }
                break;

            default:
                ATLog.e(TAG, "ERROR. onReaderOperationModeChanged([%s], %s) - Failed to unknown operation mode", reader,
                        mode);
                break;
        }
        displayOperationMode();
        ATLog.i(TAG, INFO, "EVENT. onReaderOperationModeChanged([%s], %s)", reader, mode);
    }

    @Override
    public void onReaderPowerGainChanged(ATEAReader reader, int power, Object params) {

        ATLog.i(TAG, INFO, "EVENT. onReaderPowerGainChanged([%s], %d)", reader, power);
    }

    @Override
    public void onReaderBatteryState(ATEAReader reader, int batteryState, Object params) {

        ATLog.i(TAG, INFO, "EVENT. onReaderBatteryState([%s], %d)", reader, batteryState);
    }

    // ------------------------------------------------------------------------
    // Abstractable Methods
    // ------------------------------------------------------------------------

    @Override
    public void onReaderKeyChanged(ATEAReader reader, KeyType type, KeyState state,
                                   Object params) {

        if (mMethod == METHOD_TRIGGER) {
            adpTrigger.add(type.toString(), state.toString());
            txtKeyType.setText(type.toString());
            txtKeyState.setText(state.toString());
        }

        ATLog.i(TAG, INFO, "EVENT. onReaderKeyChanged([%s], %s, %s)", reader, type, state);
    }

    // Get Inflate Resource Id
    @Override
    protected int getInflateResId() {
        return R.layout.view_inventory;
    }

    // Initialize View
    @Override
    protected void initView() {

        rdMethod = (RadioGroup) mView.findViewById(R.id.method_type);
        rdMethod.setOnCheckedChangeListener(this);

        rdMethodRfid = (RadioButton) mView.findViewById(R.id.method_rfid);
        rdMethodBarcode = (RadioButton) mView.findViewById(R.id.method_barcode);
        rdMethodTrigger = (RadioButton) mView.findViewById(R.id.method_trigger_event_only);

        lstData = (ListView) mView.findViewById(R.id.data_list);
        adpData = new DataListAdapter(getActivity());
        lstData.setAdapter(adpData);
        lstData.setOnItemLongClickListener(this);
        lstData.setOnItemClickListener(this);
        lstTrigger = (ListView) mView.findViewById(R.id.trigger_list);
        adpTrigger = new KeyListAdapter(getActivity());
        lstTrigger.setAdapter(adpTrigger);

        txtCount = (TextView) mView.findViewById(R.id.count);
        txtTotalCount = (TextView) mView.findViewById(R.id.total_count);
        txtKeyType = (TextView) mView.findViewById(R.id.key_type);
        txtKeyState = (TextView) mView.findViewById(R.id.key_state);
        txtTagSpeed = (TextView) mView.findViewById(R.id.tag_speed);
        chkFRID = (CheckBox) mView.findViewById(R.id.chkRFID);
        chkFRID.setOnCheckedChangeListener(this);
        chkRFIDTag = (CheckBox) mView.findViewById(R.id.chkRFIDTag);

        chkRFIDTag.setOnCheckedChangeListener(this);
        if (mIsRfidTagSpeed)
            txtTagSpeed.setVisibility(View.VISIBLE);
        else
            txtTagSpeed.setVisibility(View.GONE);

        linearAction = (LinearLayout) mView.findViewById(R.id.linear_action);
        linearCount = (LinearLayout) mView.findViewById(R.id.linear_count);
        linearTriggerState = (LinearLayout) mView.findViewById(R.id.linear_key);

        if (!isATS100()) {
            rdMethodTrigger.setVisibility(View.GONE);
            lstTrigger.setVisibility(View.GONE);
            linearTriggerState.setVisibility(View.GONE);
        }

        btnAction = (Button) mView.findViewById(R.id.action);
        btnAction.setOnClickListener(this);

        btnClear = (Button) mView.findViewById(R.id.clear);
        btnClear.setOnClickListener(this);

        btnSetting = (Button) mView.findViewById(R.id.setting);
        btnSetting.setOnClickListener(this);
        btnUpload = (Button) mView.findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(this);

        registerForContextMenu(lstData);
        txtSanPham = (TextView) mView.findViewById(R.id.txtSanPham);
        txtSLRFID_KIOT = (TextView) mView.findViewById(R.id.txtSLRFID_KIOT);

        ATLog.i(TAG, INFO, "INFO. initView()");
    }

    // sự kiện nút check Tag RFID
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.chkRFID:
                if (isChecked == true) {
                    inventoriesSP_Array.clear();
                    adpData.clear();
                    adpData.setReportRssi(true);
                    SetchkFRID = true;
                    Toast.makeText(getActivity(), "chkRFID--", Toast.LENGTH_SHORT).show();
                    Log.d("testtimsp", timsp + "---*");
                } else {
                    SetchkFRID = false;
                    adpData.clear();
                    adpData.setReportRssi(false);
                    Toast.makeText(getActivity(), "chkRFID--not check", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chkRFIDTag:
                if (isChecked == true && PairedWithTagID == true) {
                    //  RfidPaired_Array.clear();
                    PairedWithTagID = false;
                    txtSanPham.setText("Chế độ tìm giày lẽ đôi");
                    txtSLRFID_KIOT.setText("   Chi Tiết Đôi Giày");
                    Rfid_Not_Paired_Array.addAll(RfidToProductCode_Array);
                    // Collections.sort(  Rfid_Not_Paired_Array.);
                    Log.d("Rfid_Not_Paired_Array", Rfid_Not_Paired_Array.size() + "--");
                    for (int i = 0; i < Rfid_Not_Paired_Array.size(); i++) {
                        Log.d("Rfid_Not_Paired_Array", i + "(-)" + Rfid_Not_Paired_Array.get(i).getRfidTagID() + "--" + Rfid_Not_Paired_Array.get(i).getShoesIsLeftFoot() + "--");
                        if (getIndexOf(Rfid_Not_Paired_Array.get(i).getRfidTagID()) == 1) {
                            Rfid_Not_Paired_Array.remove(i);
                            i--;
                        }
                    }
                    customListPairedAdapter.notifyDataSetChanged();
                    customListPairedAdapter = new ListViewPairedAdapter(getActivity(), (Rfid_Not_Paired_Array));
                    lstData.setAdapter(customListPairedAdapter);

                } else {
                    PairedWithTagID = true;
                    Toast.makeText(getActivity(), "chkRFIDTag--HIển thị lại các đôi giày", Toast.LENGTH_SHORT).show();
                    Rfid_Not_Paired_Array.clear();
                    txtSanPham.setText("Chế độ tìm giày đồng đôi");
                    customListPairedAdapter.notifyDataSetChanged();
                    customListPairedAdapter = new ListViewPairedAdapter(getActivity(), RfidPaired_Array);
                    lstData.setAdapter(customListPairedAdapter);
                }
                break;
        }
    }
    private int getIndexOf(Double s) {
        for (int i = 0; i < RfidPaired_Array.size(); i++) {
            Log.d("Rfid_Not_Paired_Array", RfidPaired_Array.size() + "**" + RfidPaired_Array.get(i).getShoesPairedWithTagID() + "--");
            if (RfidPaired_Array.get(i).getShoesPairedWithTagID().equals(s)) {
                return 1; //i là vị trí của s
            }
        }
        return -1; // không có trả về -1 vì 0 là vị trí đầu tiên trong mảng
    }

    // Exit View
    @Override
    protected void exitView() {

        ATLog.i(TAG, INFO, "INFO. exitView()");
    }

    // Enabled Widgets
    @Override
    protected void enableWidgets(boolean enabled) {
        super.enableWidgets(enabled);
        rdMethod.setEnabled(mIsEnabled);
        if (getReader() != null) {
            rdMethodRfid.setEnabled(mIsEnabled && getReader().getRfidUhf() != null);
            rdMethodBarcode.setEnabled(mIsEnabled && getReader().getBarcode() != null);
            rdMethodTrigger.setEnabled(mIsEnabled
                    && getReader().getRfidUhf() != null && getReader().getBarcode() != null);
        }
        lstData.setEnabled(enabled);
        lstTrigger.setEnabled(enabled);

        linearAction.setEnabled(mIsEnabled);
        btnAction.setEnabled(enabled);
        btnClear.setEnabled(mIsEnabled);
        btnSetting.setEnabled(mIsEnabled);
        btnUpload.setEnabled(enabled);
        ATLog.i(TAG, INFO, "INFO. enableWidgets(%s)", enabled);
    }

    // Loading Reader Properties
    @Override
    protected boolean loadingProperties() {

        mTotalCount = 0;
        PlaySound();
        // Load Operation Mode
        OperationMode mode;
        try {
            mode = getReader().getOperationMode();
        } catch (ATException e) {
            ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load operation mode");
            return false;
        }
        switch (mode) {
            case Normal:
                mMethod = METHOD_RFID;
                break;
            case Barcode:
                mMethod = METHOD_BARCODE;
                break;
            case TriggerEventOnly:
                mMethod = METHOD_TRIGGER;
                break;
            default:
                ATLog.e(TAG, "ERROR. loadingProperties() - Failed to load unknown operation mode");
                return false;
        }

        try {
            mIsRfidTagSpeed = (Boolean) GlobalData.getConfig(getActivity().getApplicationContext(),
                    getReader().getDeviceType(), getReader().getAddress(), GlobalData.KEY_TAG_SPEED);
        } catch (Exception e1) {
            ATLog.e(TAG, "ERROR. loadingProperties() - Failed to get global data(%s)",
                    GlobalData.KEY_TAG_SPEED);
        }

        try {
            mBarcodeRestartTime = (Integer) GlobalData.getConfig(getActivity().getApplicationContext(),
                    getReader().getDeviceType(), getReader().getAddress(), GlobalData.KEY_RESTART_TIME);
        } catch (Exception e1) {
            ATLog.e(TAG, "ERROR. loadingProperties() - Failed to get global data(%s)",
                    GlobalData.KEY_RESTART_TIME);
        }

        if (getReader().getRfidUhf() == null) {
            // Enabled Use Key Action
            try {
                getReader().setUseActionKey(true);
            } catch (ATException e) {
                ATLog.e(TAG, "ERROR. loadingProperties() - Failed to enabled key action");
                return false;
            }

            return true;
        }

        // Set Continuous Mode
        try {
            getReader().getRfidUhf().setContinuousMode(mIsContinuousMode);
        } catch (ATException e) {
            ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to set continuous mode [%s]",
                    mIsContinuousMode);
            return false;
        }

        // Load Continous Mode
        try {
            mIsContinuousMode = getReader().getRfidUhf().getContinuousMode();
        } catch (ATException e) {
            ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load continuous mode");
            return false;
        }

        // Load Report Rssi
        try {

            // mIsReportRssi=true;
            getReader().getRfidUhf().setReportRssi(true);
            // kiểm tra chkFRID.isCheck
         /*   if(chkFRID.isChecked())
            {mIsReportRssi = getReader().getRfidUhf().getReportRssi();
                Log.d("sendGetANV",mIsReportRssi+"---");
            }
            else
                mIsReportRssi=false;*/
        } catch (ATException e) {
            ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load report rssi");
            return true;
        }

        // Load Selection Mask
        if (!checkMask()) {
            ATLog.e(TAG, "ERROR. loadingProperties() - Failed to check selection mask");
            return false;
        }

        // Enabled Use Key Action
        try {
            getReader().setUseActionKey(true);
        } catch (ATException e) {
            ATLog.e(TAG, "ERROR. loadingProperties() - Failed to enabled key action");
            return false;
        }

        ATLog.i(TAG, INFO, "INFO. loadingProperties()");
        return true;

        // AudioManager soundPooll = getActivity().getSystemService(AUDIO_SERVICE);

    }

    // Loaded Reader Properteis
    @Override
    protected void loadedProperties(boolean isInitialize) {
        if (isInitialize) {
            switch (mMethod) {
                case METHOD_RFID:
                    rdMethod.check(R.id.method_rfid);
                    break;
                case METHOD_BARCODE:
                    rdMethod.check(R.id.method_barcode);
                    break;
                case METHOD_TRIGGER:
                    rdMethod.check(R.id.method_trigger_event_only);
                    break;
            }

            txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
            txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));
            enableWidgets(true);
        } else {
            enableWidgets(false);
        }
        displayOperationMode();

        ATLog.i(TAG, INFO, "INFO. loadedProperties()");
    }

    private void finishSetSelectMask(boolean result) {

        WaitDialog.hide();

        if (result) {
            enableWidgets(true);
        } else {
            finishView();
        }

        ATLog.i(TAG, INFO, "INFO. finishSetSelectMask(%s)", result);
    }

    @Override
    protected void completeSetting(int type, Intent data) {
        // Load Report Rssi
        try {

            mIsReportRssi = getReader().getRfidUhf().getReportRssi();
        } catch (ATException e) {
            ATLog.e(TAG, e, "ERROR. completeSetting() - Failed to load report rssi");
            return;
        }

        // kiểm tra chkFRID.isCheck

        if (chkFRID.isChecked()) {

            adpData.setReportRssi(true);
        } else {

            adpData.setReportRssi(false);
        }
        switch (type) {
            case OPTION_RFID_UHF:
                if (data != null) {
                    mIsRfidTagSpeed = data.getBooleanExtra(Constants.RFID_TAG_SPEED, false);
                    GlobalData.putConfig(
                            getReader().getDeviceType(), getReader().getAddress(), GlobalData.KEY_TAG_SPEED, mIsRfidTagSpeed);
                } else {
                    try {
                        mIsRfidTagSpeed = (Boolean) GlobalData.getConfig(getActivity().getApplicationContext(),
                                getReader().getDeviceType(), getReader().getAddress(), GlobalData.KEY_TAG_SPEED);
                    } catch (Exception e) {
                        ATLog.e(TAG, "ERROR. completeSetting() - Failed to get global data(%s)",
                                GlobalData.KEY_TAG_SPEED);
                    }
                }

                if (mIsRfidTagSpeed)
                    txtTagSpeed.setVisibility(View.VISIBLE);
                else
                    txtTagSpeed.setVisibility(View.GONE);

                break;
            case OPTION_BARCODE:
                if (data != null) {
                    mBarcodeRestartTime = data.getIntExtra(Constants.BARCODE_RESTART_TIME, NO_RESTART);
                    GlobalData.putConfig(getReader().getDeviceType(),
                            getReader().getAddress(), GlobalData.KEY_RESTART_TIME, mBarcodeRestartTime);
                } else {
                    try {
                        mBarcodeRestartTime = (Integer) GlobalData.getConfig(getActivity().getApplicationContext(),
                                getReader().getDeviceType(), getReader().getAddress(), GlobalData.KEY_RESTART_TIME);
                    } catch (Exception e) {
                        ATLog.e(TAG, "ERROR. completeSetting() - Failed to get global data(%s)",
                                GlobalData.KEY_RESTART_TIME);
                    }
                }
                break;
        }

        ATLog.i(TAG, INFO, "INFO. completeSetting()");
    }
    // ------------------------------------------------------------------------
    // Declare Class SetSelectMaskThread
    // ------------------------------------------------------------------------

    public void ConvertRFtoProductcode() {
        int tempdata = adpData.getCount();
        inventoriesSP_Array = new ArrayList<>();
            for (int i = 0; i < tempdata; i++) {
                Log.d("sendGetANV", adpData.getData(i));
                convertRFtoCode(adpData.getData(i));
                //  convertRFtoCode("4966640000000000000000AA");
            }

    }

    public void PostReadTag(String epc) {
        PostTag post = new PostTag();
        post.setRfidTaskID(loginadapter.getmaUserID());
        post.setTrackingGPS_Longitude(loginadapter.getLongitude());
        post.setTrackingGPS_Latitude(loginadapter.getLatitude());
        post.setTrackingGPS_Location(loginadapter.getLocalAddress());
        post.setEpc(epc);
        Log.d("checkRFID", "CheckRFID " + epc );
        //mAPIService_PostReadTag = ApiUnit.getServiceIFD();
        mAPIServiceIFD.PostReadTagTest(post, ngayToken.Laytoken(getActivity(), keyToken_IFD)).enqueue(new retrofit2.Callback<PostTag>() {
            @Override
            public void onResponse(Call<PostTag> call, Response<PostTag> response) {
                if (response.isSuccessful()) {
                    Log.d("checkRFID", "CheckRFID " + response.body().getMessage() );
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject object = null;
                    String messageString = "";
                    try {
                        object = new JSONObject(response.errorBody().string());
                        messageString = object.getString("message") + object.getString("errorMessage")  ;
                        Log.d("checkRFID", "CheckRFID " + messageString );
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(getActivity(), "Loi 401" + messageString, Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(getActivity(), "Loi 404" + messageString, Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(getActivity(), "Loi 500" + messageString, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), "unknown error " + messageString, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                call.cancel();
            }

            @Override
            public void onFailure(Call<PostTag> call, Throwable t) {
                Log.e("postLocation", "Dangnhap " + t.toString());
                call.cancel();
            }
        });
    }
    // ------------------------------------------------------------------------
    // Declare Interface ISetWriteMemoryListener
    // ------------------------------------------------------------------------
    public void CheckTaskID()
    {

    }
    ////////////////////  Đọc dữ liệu từ thẻ rfid kiểm tra json
    public void convertRFtoCode(final String tag) {
        mAPIServiceIFD.getRFtoProduct(tag, ngayToken.Laytoken(getActivity(), keyToken_IFD)).enqueue(new retrofit2.Callback<RFIDTagReference>() {
            @Override
            public void onResponse(Call<RFIDTagReference> call, Response<RFIDTagReference> response) {
                if (response.isSuccessful()) {

                    productCode = response.body().getModel().getProductCode();
                    tempdatacode = response.body().getModel().getKiotVietProductID();
                    boolean shoseleft = response.body().getModel().getShoesIsLeftFoot();
                    boolean  isPairedProduct = response.body().getModel().getIsPairedProduct();
                    Log.d("getShoesIsLeftFoot", response.body().getModel().getShoesIsLeftFoot() + "-----" + tempdatacode);
                    Double PairedWithTag = response.body().getModel().getShoesPairedWithTagID();
                    Double TagRFID = response.body().getModel().getRfidTagID();

                    if (PairedWithTagID == true && isPairedProduct==true ) {

                        ModelRFIDTagReference ArrayRFID = new ModelRFIDTagReference();
                        ArrayRFID.setKiotVietProductID(tempdatacode);
                        ArrayRFID.setShoesIsLeftFoot(shoseleft);
                        ArrayRFID.setShoesPairedWithTagID(PairedWithTag);
                        ArrayRFID.setRfidTagID(TagRFID);
                        RfidToProductCode_Array.add(ArrayRFID);
                        for (int i = 0; i < RfidToProductCode_Array.size(); i++) {
                            for (int j = i + 1; j < RfidToProductCode_Array.size(); j++) {
                                if (RfidToProductCode_Array.get(i).getRfidTagID() == RfidToProductCode_Array.get(j).getShoesPairedWithTagID()) {
                                    ArrayRFID.setShoes_IsRightFoot(!RfidToProductCode_Array.get(i).getShoesIsLeftFoot());
                                    RfidPaired_Array.add(ArrayRFID);
                                    // go bo cac san pham dong doi
                                    RfidToProductCode_Array.remove(j);
                                    j--;
                                }
                            }
                        }
                        customListPairedAdapter = new ListViewPairedAdapter(getActivity(), RfidPaired_Array);
                        lstData.setAdapter(customListPairedAdapter);
                        call.cancel();
                    } else {
                        Log.d("laymakiot", tempdatacode.toString());
                        ChiTietSP invSP = new ChiTietSP();
                        invSP.setId(tempdatacode);
                        invSP.setCodeRFID(productCode);
                        invSP.setDemsoluong(1);
                        inventoriesSP_Array.add(invSP);
                        for (int i = 0; i < inventoriesSP_Array.size(); i++) {
                            for (int j = i + 1; j < inventoriesSP_Array.size(); j++) {
                                if (inventoriesSP_Array.get(i).getId().equals(inventoriesSP_Array.get(j).getId())) {
                                    inventoriesSP_Array.get(i).demsoluong++;
                                    inventoriesSP_Array.remove(j);
                                    j--;
                                }
                            }
                        }
                        customListAdapter = new ListViewAdapter(getActivity(), inventoriesSP_Array);
                        lstData.setAdapter(customListAdapter);

                    }

                } else {
                    JSONObject object = null;
                    String messageString = "";
                    try {
                        object = new JSONObject(response.errorBody().string());
                        System.out.println(object.getString("message"));
                        messageString = object.getString("message");

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(getActivity(), "Loi 401" + messageString, Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(getActivity(), "Loi 404" + messageString, Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(getActivity(), "Loi 500" + messageString, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), "unknown error " + messageString, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(retrofit2.Call<RFIDTagReference> call, Throwable t) {
                call.cancel();
                //  Log.d("sendGetANV", t.toString());
                Toast.makeText(getActivity(), "Lỗi convertRFtoCode " + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.data_list) {
            menu.setHeaderTitle("Tùy Chọn ");
            menu.setHeaderIcon(R.drawable.read_memory_able);
            getActivity().getMenuInflater().inflate(R.menu.menu_user, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals("Send Mail")) {
            Saveexcel();
            ShareViaEmail();
        } else if (item.getTitle().equals("Tồn KiotViet"))  //&& chkFRID.isChecked()
        {
            for (int i = 0; i < inventoriesSP_Array.size(); i++) {
                Log.d("KiotViet", inventoriesSP_Array.get(i).getId() + "**" + inventoriesSP_Array.get(i).getDemsoluong() + "");
                LaySP(inventoriesSP_Array.get(i).getId(), i);
            }

            customListAdapter = new ListViewAdapter(getActivity(), inventoriesSP_Array);
            lstData.setAdapter(customListAdapter);

        } else if (item.getTitle().equals("Tìm Kiếm Sản Phẩm")) {

            FindProduct();
        } else if (item.getTitle().equals("Check Giày Đồng Đôi")) {
            FindPairedWithTagID();
        }
        return true;
    }

    // Tim giay dong doi
    public void FindPairedWithTagID() {
        //  Toast.makeText(getActivity(), "Chế độ tìm giày đồng đôi ", Toast.LENGTH_LONG).show();
        // adpData.clear();
        PairedWithTagID = true;
        SetchkFRID = false;
        chkFRID.setVisibility(View.GONE);
        chkRFIDTag.setVisibility(View.VISIBLE);
        txtSanPham.setText("Chế độ tìm giày đồng đôi");
        txtSLRFID_KIOT.setText("   Chi Tiết Đôi Giày");
        ConvertRFtoProductcode();

    }

    // Tìm Kiếm san pham
    public void FindProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Nhập mã sản phẩm cần tìm kiếm");
        // Set up the input
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String maCodeSP = input.getText().toString();
                mAPIServiceIFD.getFindProduct(Integer.parseInt(maCodeSP)).enqueue(new retrofit2.Callback<RfidToProductCode>() {
                    @Override
                    public void onResponse(Call<RfidToProductCode> call, Response<RfidToProductCode> response) {
                        if (response.isSuccessful()) {
                            timsp = true;
                            tempcodepproduct = response.body().getRFIDTagCode();
                            chkFRID.setVisibility(View.INVISIBLE);
                            adpData.clear();
                            masp_tim = tempcodepproduct;
                            adpData.add(tempcodepproduct + "", "", 0, 0);
                            lstData.setAdapter(adpData);
                            btnAction.setText("Tìm Sản Phẩm");
                            btnClear.setText("Quay Lại");
                            call.cancel();
                        } else {
                            call.cancel();
                            Toast.makeText(getActivity(), "Không tìm thấy thông tin sản phẩm " + masp_tim, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<RfidToProductCode> call, Throwable t) {
                        call.cancel();
                        //  Log.d("sendGetANV", t.toString());
                        Toast.makeText(getActivity(), "Lỗi Find_Product " + t, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    public void LaySP(final Double Productid, final int vitri) {
        Call<ChiTietSP> call4 = mAPIServiceKiot.getChitietSP(ngayToken.getContent(), ngayToken.getRetailer(), ngayToken.Laytoken(getActivity(), keyToken_Kiot), Productid);
        call4.enqueue(new retrofit2.Callback<ChiTietSP>() {
            @Override
            public void onResponse(Call<ChiTietSP> call4, Response<ChiTietSP> response1) {
                if (response1.isSuccessful()) {
                    try {
                        ArrayList<ChitietSP_Inventories> tUser = response1.body().getInv();
                        for (int i = 0; i < tUser.size(); i++) {
                            if (tUser.get(i).getBranchId() == 151537) {
                                Log.d("laytensp", vitri + "-*-" + response1.body().getName() + "-=" + Productid);
                                inventoriesSP_Array.get(vitri).setOnHand(tUser.get(i).getOnHand());
                                inventoriesSP_Array.get(vitri).setName(response1.body().getName());
                                inventoriesSP_Array.get(vitri).setCost(response1.body().getBasePrice());
                                //Toast.makeText(getActivity(), "Lấy ra brandId " + loginadapter.getBrandid(), Toast.LENGTH_LONG).show();
                            }
                        }
                        customListAdapter.notifyDataSetChanged();
                        call4.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ChiTietSP> call4, Throwable t) {
                Log.d("sendGetANV", t.toString());
                Toast.makeText(getActivity(), "Lỗi LaySP ", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void hienthiSP(int vitri) {
        String codesp;
        String tenSP;
        String giaSP;
        String slKiot;
        if (inventoriesSP_Array.size() > 0) {
            codesp = inventoriesSP_Array.get(vitri).getId() + "";
            tenSP = inventoriesSP_Array.get(vitri).getName();
            giaSP = inventoriesSP_Array.get(vitri).getCost() + "";
            slKiot = inventoriesSP_Array.get(vitri).getOnHand() + "";

            MessageBox.show(getActivity(),
                    "Mã Sản Phẩm: " + codesp + "\n" +
                            "Tên Sản Phẩm: " + tenSP + "\n" +
                            "Giá Sản Phẩm: " + giaSP + " VND" + "\n" +
                            "Số lượng tồn KiotViet: " + slKiot + "\n",
                    "Thông Tin Sản Phẩm");
        }
    }

    private void ShareViaEmail() {
        TokenAccess ngayToken = new TokenAccess();
        File file = new File(Environment.getExternalStorageDirectory() + "/" + "Email-Ghap/Emails.txt");
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"nguyenchau0312@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Phiếu Kiểm Kho " + " - " + ngayToken.getDateT());
        email.putExtra(Intent.EXTRA_TEXT, "Phiếu Kiểm Kho " + " - " + ngayToken.getGio());

        email.setType("application/excel");
        Uri uri = null;
        try {
            // File file = new File(this.getExternalFilesDir(null), "samplefile.txt");
            uri = FileProvider.getUriForFile(getActivity(), "send mail", file);
            FileOutputStream osw = new FileOutputStream(file);
            osw.write("Say something".getBytes("UTF-8"));
            osw.close();
            Log.i("File Reading stuff", "success");
            email.putExtra(Intent.EXTRA_STREAM, uri);
        } catch (Exception e) {

        }

        startActivityForResult(Intent.createChooser(email, "Choose an Email client :"), 1);
    }

    public void Saveexcel() {

        File sdCard = new File(Environment.getExternalStorageDirectory().toString() + "/Temp_ATX");
        TokenAccess ngayToken = new TokenAccess();
        String csvFile = "Kiểm Kho " + ngayToken.getGio() + ".xls";
        //String csvFile = "myData.xls";

        //create directory if not exist
        if (!sdCard.isDirectory()) {
            sdCard.mkdirs();
        }
        try {
            //file path
            File file = new File(sdCard, csvFile);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Phiếu Kiểm Kho - " + loginadapter.getmaUserID(), 0);
            sheet.addCell(new Label(0, 0, "ProductCode")); // column and row
            sheet.addCell(new Label(1, 0, "ProductName"));
            sheet.addCell(new Label(2, 0, "RFID")); // column and row
            sheet.addCell(new Label(3, 0, "KiotViet"));
            if (inventoriesSP_Array.size() > 0) {
                for (int i = 0; i < inventoriesSP_Array.size(); i++) {
                    sheet.addCell(new Label(0, i + 1, inventoriesSP_Array.get(i).getId() + ""));
                    sheet.addCell(new Label(1, i + 1, inventoriesSP_Array.get(i).getName()));
                    sheet.addCell(new Label(2, i + 1, inventoriesSP_Array.get(i).getDemsoluong() + ""));
                    sheet.addCell(new Label(3, i + 1, inventoriesSP_Array.get(i).getOnHand() + ""));
                }

            }
            if (chkFRID.isChecked()) {
                sheet.addCell(new Label(0, 0, "Mã thẻ RFID")); // column and row
                sheet.addCell(new Label(1, 0, "Cout"));
                for (int i = 0; i < adpData.getCount(); i++) {

                    sheet.addCell(new Label(0, i + 1, adpData.getData(i) + ""));
                    sheet.addCell(new Label(1, i + 1, adpData.getDataCount(i) + "---" + adpData.getmRSSI(i)));
                }
            }
            workbook.write();
            workbook.close();
            Toast.makeText(getActivity(), "Data Exported in a Excel Sheet in " + sdCard + "\'" + csvFile, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface ISetWriteMemoryListener {
        void onSetWriteMemory(String data);
    }
    private class SetSelectMaskThread extends Thread {

        private String mData;
        private boolean mRes;
        private Runnable mFinish = new Runnable() {

            @Override
            public void run() {
                finishSetSelectMask(mRes);
            }

        };

        public SetSelectMaskThread(String data) {
            super();

            mData = data;
            mRes = true;
        }

        @Override
        public void run() {

            ATLog.i(TAG, INFO, "+++ INFO. SetSelectMaskThread.run()");

            SelectMask6cParam param = new SelectMask6cParam(true, Mask6cTarget.SL, Mask6cAction.AB, BankType.EPC,
                    MASK_EPC_OFFSET, mData, mData.length() * NIBLE_SIZE);

            try {
                getReader().getRfidUhf().setSelectMask6c(0, param);
            } catch (ATException e) {
                ATLog.e(TAG, e, "ERROR. SetSelectMaskThread.run() - Failed to set selection mack 6c [%s]",
                        param.toString());
                mIsUseMask = false;
                mRes = false;
                getActivity().runOnUiThread(mFinish);
                return;
            }
            for (int i = 1; i < MAX_MASK; i++) {
                try {
                    getReader().getRfidUhf().setSelectMask6cEnabled(i, false);
                } catch (ATException e) {
                    ATLog.e(TAG, e, "ERROR. SetSelectMaskThread.run() - Failed to set selection mack 6c disabled [%d]",
                            i);
                    mIsUseMask = false;
                    mRes = false;
                    getActivity().runOnUiThread(mFinish);
                    return;
                }
            }
            try {
                getReader().getRfidUhf().setSelectFlag(SelectFlag.SL);
            } catch (ATException e) {
                ATLog.e(TAG, e, "ERROR. SetSelectMaskThread.run() - Failed to set select flag");
                mIsUseMask = false;
                mRes = false;
                getActivity().runOnUiThread(mFinish);
                return;
            }
            try {
                getReader().getRfidUhf().setSessionFlag(SessionFlag.AB);
            } catch (ATException e) {
                ATLog.e(TAG, e, "ERROR. SetSelectMaskThread.run() - Failed to set session flag");
                mIsUseMask = false;
                mRes = false;
                getActivity().runOnUiThread(mFinish);
                return;
            }
            mIsUseMask = true;
            mRes = true;
            getActivity().runOnUiThread(mFinish);

            ATLog.i(TAG, INFO, "--- INFO. SetSelectMaskThread.run()");
        }
    }

}
