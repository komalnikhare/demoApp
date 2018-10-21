package com.arrk.starwars;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arrk.starwars.models.Character;

import java.util.List;


public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.ContactViewHolder>{

    private Context context;
    private List<Character> mCharacterList;
    private LayoutInflater inflater;
    private ItemClickListener listener = null;

    public CharacterListAdapter(Context context, ItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<Character> characters){
        mCharacterList = characters;
        notifyDataSetChanged();
    }


    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row,parent,false);

        ContactViewHolder viewHolder = new ContactViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {

        holder.txtName.setText(mCharacterList.get(position).getName());

        if (listener != null){
            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(mCharacterList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCharacterList == null? 0: mCharacterList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{

        TextView txtName;
        View parentView;
        public ContactViewHolder(View itemView) {
            super(itemView);
            parentView = itemView.findViewById(R.id.parentView);
            txtName = itemView.findViewById(R.id.txt_character_name);
        }
    }

    public interface ItemClickListener {
        void onItemClick(Object object);
    }
}
