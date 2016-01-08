package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.dialogs.ImagePickerDialog;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GovtProof extends AppCompatActivity implements View.OnClickListener
        ,AdapterView.OnItemSelectedListener,ImagePickerDialog.ImageChooserListener{

    private Spinner cardsSpinner;
    private List<String> cardsList=new ArrayList<>();
    private Button next,previous;
    private EditText cardnum;
    private TextInputLayout inputcardnum;
    private TextView imageUploadText;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog uploadProofsDialog;
    private Typeface avenir;
    private DialogFragment chooserDialog;
    private static final int REQUEST_IMAGE_SELECTOR=1000;
    private static final int REQUEST_IMAGE_CAPTURE=1001;
    private ImageView imageUploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_proof);
        initializeViews();
        initializeEventListeners();
    }

    /*initialize views*/
    private void initializeViews(){
        next = (Button)findViewById(R.id.govt_next_button);
        cardsSpinner = (Spinner)findViewById(R.id.govt_cards_spinner);
        previous = (Button)findViewById(R.id.govt_prev_button);
        cardsSpinner = (Spinner)findViewById(R.id.govt_cards_spinner);
        cardnum = (EditText)findViewById(R.id.govt_card_number_edittext);
        inputcardnum = (TextInputLayout)findViewById(R.id.govt_cardnumber_inputlayout);
        imageUploaded = (ImageView)findViewById(R.id.proof_uploaded);
        imageUploadText = (TextView)findViewById(R.id.govt_proof_image_text);
        avenir = Typeface.createFromAsset(getAssets(),"fonts/AvenirLTStd-Book.otf");
        networkDetector = new NetworkDetector(this);
        accountsClient = new AccountsClient(this);
        next.setTypeface(avenir);
        previous.setTypeface(avenir);
        cardnum.setTypeface(avenir);
        inputcardnum.setTypeface(avenir);
        prepareCardsList();
        ArrayAdapter<String> cardsAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_dropdown_item_1line,cardsList);
        cardsSpinner.setAdapter(cardsAdapter);
        cardsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null && parent.getChildAt(0) != null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                            .getColor(R.color.gray));
                    ((TextView) parent.getChildAt(0)).setTypeface(avenir);
                    Log.d("padding", parent.getChildAt(0).getPaddingLeft() + "");
                    ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    /*initialize event listeners*/
    private void initializeEventListeners(){
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        imageUploadText.setOnClickListener(this);
        //cardsSpinner.setOnItemSelectedListener(this);
    }
    /*prepare cards list*/
    private void prepareCardsList(){
        cardsList.add("Select Card");
        cardsList.add("Pan Card");
        cardsList.add("Voter Card");
        cardsList.add("Ration Card");
        cardsList.add("Adhar Card");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.govt_next_button:
                openLocationActivity();
                break;
            case R.id.govt_prev_button:
                openPreviousActivity();
                break;
            case R.id.govt_proof_image_text:
                openImageChooser();
                break;
        }
    }
    /* open image chooser dialog*/
    private void openImageChooser(){
        chooserDialog = new ImagePickerDialog();
        chooserDialog.show(getSupportFragmentManager(),"chooser dialog");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /* open location activity*/
    private void openLocationActivity(){
        Intent locationIntent = new Intent(this,ChooseLocation.class);
        startActivity(locationIntent);
    }

    /* open signup activity*/
    private void openPreviousActivity(){
        Intent signUpIntent = new Intent(this,SignUp.class);
        startActivity(signUpIntent);
        finish();
    }

    @Override
    public void onChoose(int choice) {
        switch (choice){
            case 0:
                openGallary();
                break;
            case 1:
                openCamera();
                break;
        }
        chooserDialog.dismiss();
    }
    /* open gallary intent*/
    private void openGallary(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
    }
    /* open camera*/
    private void openCamera(){
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageUploaded.setVisibility(View.VISIBLE);
        }else if(requestCode == REQUEST_IMAGE_SELECTOR && resultCode == RESULT_OK){
            imageUploaded.setVisibility(View.VISIBLE);
        }
    }

    /* validate data*/
    /* send data to server*/
    private void sendProofsData(String userId,String cardType,String cardNumber,String cardImage){
        if(networkDetector.isConnected()){
            uploadProofsDialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.insertProofDetails(userId,cardType,cardNumber,cardImage
                    ,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseProofDetails(jsonObject);
                    closeProofsDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("govt",error.toString());
                    closeProofsDialog();
                }
            });
        }else{
            Toast.makeText(this,"Network connection error",Toast.LENGTH_LONG).show();
        }
    }
    /* get all proof types*/
    private void getAllProofTypes(){
        if(networkDetector.isConnected()){
            uploadProofsDialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.getAllProofTypes(new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseAllProofsTypes(jsonObject);
                    closeProofsDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("govt",error.toString());
                    closeProofsDialog();
                }
            });
        }else{
            Toast.makeText(this,"Network connection error",Toast.LENGTH_LONG).show();
        }
    }
    /* parse proof types*/
    private void parseAllProofsTypes(JSONObject jsonObject){

    }
    /* parse insert card types data*/
    private void parseProofDetails(JSONObject jsonObject){

    }
    /* close proof dialog*/
    private void closeProofsDialog(){
        if(uploadProofsDialog!=null){
            if(uploadProofsDialog.isShowing()){
                uploadProofsDialog.dismiss();
                uploadProofsDialog=null;
            }
        }
    }
}
