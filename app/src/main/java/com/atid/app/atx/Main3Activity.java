package com.atid.app.atx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atid.app.atx.ReadJson.ApiUnit;
import com.atid.app.atx.ReadJson.LoginAdapter;
import com.atid.app.atx.ReadJson.RfidToProductCode;
import com.atid.app.atx.ReadJson.WebApi;
import com.atid.app.atx.activity.view.BaseView;
import com.atid.app.atx.adapter.DataListAdapter;
import com.atid.app.atx.dialog.MessageBox;
import com.atid.lib.module.barcode.types.BarcodeType;
import com.atid.lib.module.rfid.uhf.params.TagExtParam;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.diagnotics.ATLog;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class Main3Activity  extends Activity {



}
