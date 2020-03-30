package com.gold.kiwi.zingzing.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.ToastUtil;
import com.gold.kiwi.zingzing.GPCApp;
import com.gold.kiwi.zingzing.R;
import com.gold.kiwi.zingzing.bean.RoomBean;
import com.gold.kiwi.zingzing.dialog.DialJoinRoom;
import com.gold.kiwi.zingzing.dialog.DialMakeRoom;
import com.gold.kiwi.zingzing.dialog.DialSearchRoom;

import java.util.List;

public class RecyclerRoomAdapter extends RecyclerView.Adapter<RecyclerRoomAdapter.ContactViewHolder>
{
	private String TAG = getClass().getSimpleName();
	private GPCApp app;
	private ToastUtil toast;
	private List<RoomBean> roomList;
	private DialSearchRoom dialSearchRoom;

	public void setRoomList(List<RoomBean> roomList)
	{
		this.roomList = roomList;
	}

	public void setDialSearchRoom(DialSearchRoom dial){this.dialSearchRoom = dial;}

	@NonNull
	@Override
	public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_room, parent, false);

		app = GPCApp.getInstance();
		toast = new ToastUtil(view.getContext());

		return new ContactViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ContactViewHolder holder, int position)
	{
		RoomBean bean = roomList.get(position);

		holder.tv_room_name.setText(bean.getRoom_name());
		holder.tv_room_head.setText(bean.getUser_name());
		holder.tv_sysdate.setText(bean.getSys_date());

		holder.cv_contact.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					toast.showToast("비밀번호 입력" + bean.getRoom_name());

					LOG.d("test",bean.getRoom_name());

					DialJoinRoom builder = new DialJoinRoom(app.getApplicationContext());
					builder.show(bean);


				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});

//		holder.iv_calling.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				try{
//					Context context = app.getApplicationContext();
//					Intent intent = new Intent(Intent.ACTION_DIAL);
//					intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//					intent.setData(Uri.parse("tel:"+ bean.getTelNum()));
//
//					context.startActivity(intent);
//				}catch (Exception e){
//					e.printStackTrace();
//				}
//			}
//		});

//		holder.ll_contents.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v)
//			{
//				Bundle bundle = new Bundle();
//				bundle.putParcelable("contactBean", bean);
//
//				BaseFragment fragment = new FragContactDetail();
//				fragment.setArguments(bundle);
//
//				app.fragAddStack(fragment);
//			}
//		});
	}

	@Override
	public int getItemCount()
	{
		return roomList.size();
	}

	public class ContactViewHolder extends RecyclerView.ViewHolder
	{
		CardView cv_contact;

		TextView tv_room_name;
		TextView tv_room_head;
		TextView tv_sysdate;

		public ContactViewHolder(View itemView)
		{
			super(itemView);

			cv_contact = itemView.findViewById(R.id.cv_contact);

			tv_room_name = itemView.findViewById(R.id.tv_room_name);
			tv_room_head = itemView.findViewById(R.id.tv_room_head);
			tv_sysdate = itemView.findViewById(R.id.tv_sysdate);

		}
	}
}
