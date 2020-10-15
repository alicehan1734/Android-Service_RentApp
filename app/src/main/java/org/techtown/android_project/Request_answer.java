package org.techtown.android_project;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.techtown.android_project.adapters.RequestAdapter;
import org.techtown.android_project.adapters.RequestFullAdapter;
import org.techtown.android_project.adapters.SimplePostAdapter;
import org.techtown.android_project.models.Post;
import org.techtown.android_project.models.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Request_answer extends AppCompatActivity {
    RecyclerView recyclerview_requestbar;
    Button click_request_button;
    EditText EditText_request;
    private ArrayList<Request> mRequest;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //사용자 가져오기
    String id;
    private RequestFullAdapter RAdapter;
    String currentUser = mAuth.getCurrentUser().getUid();
    String postId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_answer);

        click_request_button = findViewById(R.id.click_request_button);
        EditText_request = findViewById(R.id.EditText_request);
        recyclerview_requestbar = findViewById(R.id.recyclerview_requestbar);

        id = getIntent().getStringExtra("id");
        click_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<String, Object> tags = new HashMap<>();
                tags.put(FirebaseID.mainrequestID, currentUser);
                tags.put(FirebaseID.mainrequestID_message, EditText_request.getText().toString()); //글쓴 날짜
                tags.put(FirebaseID.TimeMillis,System.currentTimeMillis()); //글쓴 날짜
                tags.put(FirebaseID.timestamp, FieldValue.serverTimestamp()); //글쓴 날짜

                mStore.collection(FirebaseID.user).document(currentUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String mainphoto = String.valueOf(documentSnapshot.getString(FirebaseID.profilephoto));
                        postId = mStore.collection(FirebaseID.user).document().getId();
                        tags.put(FirebaseID.profilephoto,mainphoto);
                        mStore.collection(FirebaseID.user).document(id).collection(FirebaseID.request).document(postId).set(tags, SetOptions.merge());

                    }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRequest = new ArrayList<>();

        mStore.collection(FirebaseID.user).document(id).collection(FirebaseID.request).orderBy(FirebaseID.timestamp, Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            mRequest.clear();  //중복이 되므로, 한번 그전에 초기화를 한번 해주기 .

                            for (DocumentSnapshot snap : value.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String mainrequestID = String.valueOf(shot.get(FirebaseID.mainrequestID));
                                String mainrequestID_message = String.valueOf(shot.get(FirebaseID.mainrequestID_message));
                                String TimeMillis = String.valueOf(shot.get(FirebaseID.TimeMillis));

                                long time = Long.parseLong(TimeMillis);
                                String profile = String.valueOf(shot.get(FirebaseID.profilephoto));

                                Request data = new Request(time,mainrequestID,mainrequestID_message,profile);
                                mRequest.add(data);
                            }

                            RAdapter = new RequestFullAdapter(mRequest,Request_answer.this);
                            recyclerview_requestbar.setAdapter(RAdapter);
                        }
                    }
                });


    }
}