package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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
import com.dsquare.hibour.pojos.govtproofs.Datum;
import com.dsquare.hibour.pojos.govtproofs.Proofs;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GovtProof extends AppCompatActivity implements View.OnClickListener
        ,AdapterView.OnItemSelectedListener,ImagePickerDialog.ImageChooserListener{

    private Spinner cardsSpinner,genderSpinner;
    private List<String> cardsList=new ArrayList<>();
    private List<String> genderList = new ArrayList<>();
    private Button continuous;
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
    private ImageView imageUploaded,uploadimage;
    private Gson gson;
    private ArrayAdapter<String> cardsAdapter,genderAdapter;
    private Map<String,String> cardsMap = new LinkedHashMap<>();
    private String cardTypeId="";
    private String cardImageString="a",genderString="";
    private Bitmap bitmap;
    private Hibour application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_proof);
        initializeViews();
        initializeEventListeners();
        getAllProofTypes();
    }

    /*initialize views*/
    private void initializeViews(){
        application = Hibour.getInstance(this);
        continuous = (Button)findViewById(R.id.govt_proof_continue);
        cardsSpinner = (Spinner)findViewById(R.id.govt_cards_spinner);
        cardnum = (EditText)findViewById(R.id.govt_card_number_edittext);
        inputcardnum = (TextInputLayout)findViewById(R.id.govt_cardnumber_inputlayout);
        imageUploaded = (ImageView)findViewById(R.id.proof_uploaded);
        uploadimage = (ImageView)findViewById(R.id.govt_proof_image_upload);
        imageUploadText = (TextView)findViewById(R.id.govt_proof_image_text);
        genderSpinner = (Spinner)findViewById(R.id.govt_gender_spinner);
        avenir = Typeface.createFromAsset(getAssets(),"fonts/AvenirLTStd-Book.otf");
        networkDetector = new NetworkDetector(this);
        accountsClient = new AccountsClient(this);
        gson = new Gson();
        continuous.setTypeface(avenir);
//        previous.setTypeface(avenir);
        cardnum.setTypeface(avenir);
        inputcardnum.setTypeface(avenir);
        prepareCardsList();
        cardsAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_dropdown_item_1line,cardsList);
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_dropdown_item_1line,genderList);
        genderSpinner.setAdapter(genderAdapter);
        cardsSpinner.setAdapter(cardsAdapter);
        cardsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null && parent.getChildAt(0) != null) {
                    String cardType = cardsList.get(position);
                    Log.d("cardtype",cardType);
                    if(!cardType.equals("Select Card")){
                        cardTypeId = cardsMap.get(cardType);
                        Log.d("cardtype",cardType);
                    }
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                            .getColor(R.color.gray));
                    ((TextView) parent.getChildAt(0)).setTypeface(avenir);
                    ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if (parent != null && parent.getChildAt(0) != null) {
                    if(position==1){
                        genderString="0";
                        Log.d("genderString",genderString);
                    }else{
                        genderString = "1";
                        Log.d("genderString",genderString);
                    }

                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                            .getColor(R.color.gray));
                    ((TextView) parent.getChildAt(0)).setTypeface(avenir);
                    ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    /*initialize event listeners*/
    private void initializeEventListeners(){
        continuous.setOnClickListener(this);
        uploadimage.setOnClickListener(this);
        //cardsSpinner.setOnItemSelectedListener(this);
    }
    /*prepare cards list*/
    private void prepareCardsList(){
        cardsList.add("Select Card");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.govt_proof_continue:
                //openSocialPrefActivity();
                validateProofData();
                break;
            case R.id.govt_proof_image_upload:
                openImageChooser();
                break;
        }
    }
    /*validate proof data*/
    private void validateProofData(){
        Log.d("cardtype",cardTypeId);
        Log.d("cardnum",cardnum.getText().toString());
        Log.d("cardimage",cardImageString);
        Log.d("gender",genderString);
        if(!cardTypeId.equals("")&&!cardnum.getText().toString().equals(null)
                &&!cardnum.getText().toString().equals("null")&&
                !cardnum.getText().toString().equals("")&&
                !cardImageString.equals("")&& !genderString.equals("")){
           // Toast.makeText(this,"All fields are required",Toast.LENGTH_LONG).show();
            sendProofsData(application.getUserId(),cardTypeId,cardnum.getText().toString()
                    ,cardImageString);
        }else{
            Log.d("userid",application.getUserId());
            sendProofsData(application.getUserId(),cardTypeId,cardnum.getText().toString()
                    ,cardImageString);
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
    private void openSocialPrefActivity(){
        Intent locationIntent = new Intent(this,SocialCategories.class);
        startActivity(locationIntent);
    }

    /* open signup activity*/
    private void openPreviousActivity(){
//        Intent signUpIntent = new Intent(this,SignUp.class);
//        startActivity(signUpIntent);
//        finish();
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK
                &&  data != null && data.getData() != null) {
            imageUploaded.setVisibility(View.VISIBLE);
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                cardImageString=getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == REQUEST_IMAGE_SELECTOR && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUploaded.setVisibility(View.VISIBLE);
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                cardImageString= getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* validate dat
a*/
    /* send data to server*/
    private void sendProofsData(String userId,String cardType,String cardNumber,String cardImage){
        if(networkDetector.isConnected()){
            uploadProofsDialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.insertProofDetails(userId,cardType,cardNumber,cardImage
                    ,genderString,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseProofDetails(jsonObject);
                    closeProofsDialog();
                    openSocialPrefActivity();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("govt",error.toString());
                    closeProofsDialog();
                    openSocialPrefActivity();
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
        try {
            Proofs proofsData = gson.fromJson(jsonObject.toString(),Proofs.class);
            List<Datum> data = proofsData.getData();
            if(data.size()>0){
                cardsList.clear();
                cardsList.add("Select Card");
                cardsMap.clear();
                for(Datum d:data){
                    cardsMap.put(d.getProofName(),d.getId()+"");
                    cardsList.add(d.getProofName());
                }
                cardsAdapter = new ArrayAdapter<String>(this
                        ,android.R.layout.simple_dropdown_item_1line,cardsList);
                cardsSpinner.setAdapter(cardsAdapter);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
    /* parse insert card types data*/
    private void parseProofDetails(JSONObject jsonObject){
        Log.d("json",jsonObject.toString());

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

    /* convert image to string*/
    public String getStringImage(Bitmap bmp){
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
//        bitmap = BitmapFactory.decodeFile(imgPath,
//                options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
