package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wristtracking.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import database.DatabaseHelper;
import model.DataModel;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private List<DataModel> dataList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, time, activity, rotation;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.tv_date_time);
            time = (TextView) view.findViewById(R.id.tv_time);
            activity = (TextView) view.findViewById(R.id.tv_activityName);
            rotation = (TextView) view.findViewById(R.id.tv_rotation);
        }
    }


    public HistoryAdapter(List<DataModel> dlist, Context mcontext) {

        this.dataList = dlist;
        context = mcontext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_adapter, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.date.setText(dataList.get(position).getDate());
        holder.time.setText(dataList.get(position).getTime());
        holder.activity.setText(dataList.get(position).getActivityName());
        holder.rotation.setText(dataList.get(position).getRotation());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                showAlert(context, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void showAlert(Context mContext, int position) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        Button btn_accept = (Button) view.findViewById(R.id.btn_yes);
        Button btn_reject = (Button) view.findViewById(R.id.btn_no);
        TextView title = view.findViewById(R.id.txt_dialog);
        title.setText("Are you sure you want to delete this activity from history ?");
        btn_accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatabaseHelper helper = new DatabaseHelper(mContext);
                // TODO Auto-generated method stub

                if (helper.deleteRecord(dataList.get(position).getTime())) {
                    Toast.makeText(mContext, "Entry Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "No Entry Deleted", Toast.LENGTH_SHORT).show();
                }
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
                dialog.dismiss();
            }
        });
        btn_reject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });

    }

}

