package com.example.noteapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapps.database.NoteDataBase;
import com.example.noteapps.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.regex.Pattern;

public class CreateNoteActivity extends AppCompatActivity {
    ImageView imageBack, imageSave;
    private EditText intputNoteTitle, inputNoteSubTitle, inputNoteText;
    private TextView textDateTime;
    private String selectedNoteColor;
    private View viewSubTitleIndicaColor;
    ImageView imageNote;
    String selectImagePath;
    TextView textWebUrl;
    LinearLayout layoutWebUrl;
    AlertDialog dialogAddUrl;

    private static final int REQUEST_CODE = 200;
    private static final int REQUEST_CODE_SELECT_IMAGE = 200;

    private String[] storagePermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        intputNoteTitle = findViewById(R.id.inputNoteTitle);

        inputNoteSubTitle = findViewById(R.id.inputNoteSubTitle);
        inputNoteText = findViewById(R.id.inputNoteText);
        textDateTime = findViewById(R.id.textDateTime);
        imageBack = findViewById(R.id.imageBack);
        imageSave = findViewById(R.id.imageSave);
        imageNote = findViewById(R.id.imageNote);
        textWebUrl = findViewById(R.id.textWebUrl);
        layoutWebUrl = findViewById(R.id.layoutWebUrl);

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        viewSubTitleIndicaColor = findViewById(R.id.viewSubTitleColor);
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        textDateTime.setText(
                new SimpleDateFormat("EEE ,dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())

        );
        selectedNoteColor = "#333333";
        selectImagePath = "";
        initMissellaneous();
        setSubTitleIndicaColor();

    }
    private void saveNote() {
        if (intputNoteTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, "Nhập tiêu đề note", Toast.LENGTH_SHORT).show();
            return;
        }
        if (inputNoteSubTitle.getText().toString().isEmpty() && inputNoteText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
            return;
        }
        final Note note = new Note();
        note.setTitle(intputNoteTitle.getText().toString());
        note.setSubtitle(inputNoteSubTitle.getText().toString());
        note.setNoteText(inputNoteText.getText().toString());
        note.setDateTime(textDateTime.getText().toString());
        note.setColor(selectedNoteColor);
        note.setImagePath(selectImagePath);
        Toast.makeText(this, selectImagePath, Toast.LENGTH_SHORT).show();
        if(layoutWebUrl.getVisibility()==View.INVISIBLE){
            note.setWebLink(textWebUrl.getText().toString());
        }
        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                NoteDataBase.getNoteDataBase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNoteTask().execute();

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragetPermission() {
        requestPermissions(storagePermission, REQUEST_CODE_SELECT_IMAGE);
    }
    private void initMissellaneous() {
        final LinearLayout layoutMissellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMissellaneous);
        layoutMissellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        final ImageView imageColor1 = layoutMissellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMissellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMissellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMissellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMissellaneous.findViewById(R.id.imageColor5);

        layoutMissellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubTitleIndicaColor();
            }
        });

        layoutMissellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FDBE3B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubTitleIndicaColor();
            }
        });
        layoutMissellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubTitleIndicaColor();
            }
        });
        layoutMissellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#3A52Fc";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);
                setSubTitleIndicaColor();
            }
        });
        layoutMissellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);
                setSubTitleIndicaColor();
            }
        });

        layoutMissellaneous.findViewById(R.id.layoutAddimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateNoteActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                } else {
                    selectImage();
                }
            }
        });
        layoutMissellaneous.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showAddUrlDialog();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    private void setSubTitleIndicaColor() {
        viewSubTitleIndicaColor.setBackgroundColor(Color.parseColor(selectedNoteColor));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        selectImage();
                    } else {
                        Toast.makeText(this, "Mo quyen ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectUri = data.getData();
                if (selectUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        selectImagePath = getPathFromUri(selectUri);
                    } catch (Exception e) {
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getPathFromUri(Uri uri) {
        String filePath;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            filePath = uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    private void showAddUrlDialog() {
        if (dialogAddUrl == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(CreateNoteActivity.this).inflate(R.layout.layout_add_url, (ViewGroup) findViewById(R.id.layoutAddUrlContainer));
            builder.setView(view);
            dialogAddUrl = builder.create();
            if (dialogAddUrl.getWindow() != null) {
                dialogAddUrl.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            final EditText inputUrl = view.findViewById(R.id.inputAddUrl);
            inputUrl.requestFocus();
            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputUrl.getText().toString().trim().isEmpty()) {
                        Toast.makeText(CreateNoteActivity.this, "Bạn phải nhập url", Toast.LENGTH_SHORT).show();

                    } else if (!Patterns.WEB_URL.matcher(inputUrl.getText().toString().trim()).matches()) {
                        Toast.makeText(CreateNoteActivity.this, "Bạn cần nhập đúng định dạng url", Toast.LENGTH_SHORT).show();

                    } else {
                        textWebUrl.setText(inputUrl.getText().toString());
                        layoutWebUrl.setVisibility(View.VISIBLE);
                        dialogAddUrl.dismiss();
                    }
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddUrl.dismiss();
                }
            });
        }
        dialogAddUrl.show();
    }
}
