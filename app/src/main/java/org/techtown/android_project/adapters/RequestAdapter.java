package org.techtown.android_project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.techtown.android_project.FirebaseID;
import org.techtown.android_project.R;
import org.techtown.android_project.models.Post;
import org.techtown.android_project.models.Request;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.PostViewHolder> {
    private final int limit = 3;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private List<Request> datas;
    private Context context_1;
    public RequestAdapter(List<Request> datas, Context mcontext) {

        this.datas = datas;
        context_1 = mcontext;

    }

    @NonNull
    @Override
    public RequestAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new RequestAdapter.PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.request_question,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull final RequestAdapter.PostViewHolder holder, int position) {



        final Request data = datas.get(position); // 위에서 부터 아래까지의 포지션에
        mStore.collection(FirebaseID.user).document(data.getMainrequestID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String mainphoto = String.valueOf(documentSnapshot.getString(FirebaseID.profilephoto));
                String nickname = String.valueOf(documentSnapshot.getString(FirebaseID.nickname));
                holder.TextView_nickname.setText(nickname);

            }

        });

        holder.text_comment.setText(data.getRequest_message());

        Glide.with(this.context_1).
                load(data.getProfileimage()).into(holder.image);
//        Glide.with(this.context_1).load(data.getImage()).override(300,300).into(holder.ImageView_postimage);

        long curTime = System.currentTimeMillis();

        long regTime = data.getTimeMillis();

        long diffTime = (curTime - regTime) / 1000;

        String msg = null;

        if (diffTime < TIME_MAXIMUM.SEC) {

            // sec

            msg = "방금 전";

        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {

            // min

            msg = diffTime + "분 전";

        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {

            // hour

            msg = (diffTime) + "시간 전";

        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {

            // day

            msg = (diffTime) + "일 전";

        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {

            // day

            msg = (diffTime) + "달 전";

        } else {

            msg = (diffTime) + "년 전";

        }
        holder.clock_motion.setText(msg);

    }

    @Override
    public int getItemCount() {
        if(datas.size() > limit){
            return limit;
        }
        else
        {
            return datas.size();
        }

    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView TextView_nickname, text_comment, clock_motion;
        CircleImageView image;

//        private ImageView ImageView_postimage;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            TextView_nickname = itemView.findViewById(R.id.TextView_nickname);
            text_comment = itemView.findViewById(R.id.text_comment);
            image = itemView.findViewById(R.id.image_person_sub);
            clock_motion = itemView.findViewById(R.id.clock_motion);

//            title = itemView.findViewById(R.id.title_simple);
        }

    }

    private static class TIME_MAXIMUM {

        public static final int SEC = 60;

        public static final int MIN = 60;

        public static final int HOUR = 24;

        public static final int DAY = 30;

        public static final int MONTH = 12;

    }

}
