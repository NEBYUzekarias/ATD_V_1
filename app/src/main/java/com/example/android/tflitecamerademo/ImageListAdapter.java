package com.example.android.tflitecamerademo;




import android.content.Context;
import android.net.Uri;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/***
 * The adapter class for the RecyclerView, contains the sports data.
 */
class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder>  {

    // Member variables.
    private List<Image> mDataset;

    private RecyclerViewClickListener mListener;
    public ButtonListener onClickListener;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public ImageView imageView;
        public TextView mTextView;
        public ImageView button ;
        private ImageView delete;
        private RecyclerViewClickListener mListener;

        public ViewHolder(View v  , RecyclerViewClickListener listener ) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.tv_android);
            imageView = (ImageView)v.findViewById(R.id.img_android);
            button =  v.findViewById(R.id.upload);
            delete = v.findViewById(R.id.delete);
            delete.setOnClickListener(v1 -> onClickListener.deleteOnClick(v1, ImageListAdapter.this.mDataset.get(getAdapterPosition())));
            button.setOnClickListener(this);
            imageView.setOnClickListener(this::onClick);
            mListener = listener;
        }

        @Override
        public void onClick(View view ) {
            int id = view.getId();
            mListener.onClick(view, ImageListAdapter.this.mDataset.get(getAdapterPosition()) , id);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ImageListAdapter(RecyclerViewClickListener listener , ButtonListener buttonListener ) {
        this.mListener = (RecyclerViewClickListener) listener;
        this.onClickListener = (ButtonListener) buttonListener;
    }


    public void setDatas(List<Image> datas) {
        mDataset = datas;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ImageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        return new ViewHolder(v , mListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Image selectedData = mDataset.get(position);
        holder.mTextView.setText("stage" + selectedData.stage);
        Uri uri = Uri.parse(selectedData.imagePath);
        Context context = holder.imageView.getContext();
        Picasso.with(context).load(uri).fit().into(holder.imageView);
        // holder.imageView.setImageURI(uri);
        if (selectedData.isUpload) {
            holder.button.setOnClickListener(view -> Toast.makeText(view.getContext(), "Already uploaded", Toast.LENGTH_SHORT)
                    .show());


            holder.button.setImageResource(R.drawable.ic_done);
            holder.delete.setVisibility(View.GONE);

        }
        else{
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setImageResource(R.drawable.ic_delete);
            holder.button.setImageResource(R.drawable.ic_settings_system_daydream_black_24dp);
        }
    }



    public interface ButtonListener {

        void deleteOnClick(View v, Image position);

    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset != null) {
            return mDataset.size();
        }

        return 0;
    }

}
