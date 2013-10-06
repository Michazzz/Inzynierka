package pl.edu.zut.mwojtalewicz.friendLocalizerLibrary;

import java.util.ArrayList;

import pl.edu.zut.mwojtalewicz.friendlocalizer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchFriendsListViewAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private ArrayList<SearchFriendsItem> objects;
	

	private class ViewHolder {
		TextView tvTitle;
	    TextView tvEmail;
	    //ImageView ivProfilePhoto;
	}
	
	public SearchFriendsListViewAdapter(Context context, ArrayList<SearchFriendsItem> objects)
	{
		inflater = LayoutInflater.from(context);
	    this.objects = objects;
	}

	@Override
	public int getCount() {
		return objects.size();
	}
	
	public SearchFriendsItem getItem(int position)
	{
		return objects.get(position);
	}
	
	public long getItemId(int position) 
	{
	    return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder = null;
	    if(convertView == null) {
	    	holder = new ViewHolder();
	        convertView = inflater.inflate(R.layout.activity_search_friends_list_view, null);
	        holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitleListViewItem);
	        holder.tvEmail = (TextView) convertView.findViewById(R.id.tvEmailListViewItem);
	         convertView.setTag(holder);
	      } else {
	         holder = (ViewHolder) convertView.getTag();
	      }
	      holder.tvTitle.setText(objects.get(position).getName()+" "+objects.get(position).getLastname());
	      holder.tvEmail.setText(objects.get(position).getEmail());
	      return convertView;
	}
}
