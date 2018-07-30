package com.nollpointer.wallpapers;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class ImageCardsAdapter extends RecyclerView.Adapter<ImageCardsAdapter.ViewHolder>{
    //Картинки и их состояния
    private ArrayList<Integer> imagesIds;
    private ArrayList<Boolean> favouriteChecked;

    //слушатель для нажатий на картинку или кнопку "избранное"
    private Listener listener;

    public interface Listener{
        void onImageClicked(int source);
        void onFavouriteClicked(int source,boolean state);
    }

    ImageCardsAdapter(Set<Integer> imagesIds, Listener listener) {
        this.imagesIds = new ArrayList<>(imagesIds);
        this.listener = listener;
        initFavouriteImages(false);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView mCardView;
        ViewHolder(CardView c){
            super(c);
            mCardView = c;
        }
    }

    @Override
    public ImageCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.image_preview_layout,parent,false);
        return new ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(ImageCardsAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.mCardView;
        ImageView image = cardView.findViewById(R.id.image_cardview);
        image.setImageResource(imagesIds.get(position));
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImageClicked(imagesIds.get(position));
            }
        });
        final ImageButton button = cardView.findViewById(R.id.favourite_image_preview);
        setImageButtonImage(button,favouriteChecked.get(position));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state = favouriteChecked.get(position);
                state = !state;
                setImageButtonImage(button,state);
                listener.onFavouriteClicked(imagesIds.get(position),state);
                favouriteChecked.set(position,state);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesIds.size();
    }

    private void setImageButtonImage(ImageView image,boolean state){
        if(state)
            image.setImageResource(R.drawable.ic_favorite_picked);
        else
            image.setImageResource(R.drawable.ic_favorite_clear);
    }

    private void initFavouriteImages(boolean state){
        favouriteChecked = new ArrayList<>();
        int size = imagesIds.size();
        for(int i=0;i<size;i++)
            favouriteChecked.add(state);
    }



    //Обновления списков или отдельного элемента


    public void refresh(Set<Integer> imagesIds,Collection<Boolean> favouriteImages){
        this.imagesIds = new ArrayList<>(imagesIds);
        this.favouriteChecked = new ArrayList<>(favouriteImages);
        notifyDataSetChanged();
    }

    public void setOnlyFavouriteImages(Set<Integer> imagesIds){
        this.imagesIds = new ArrayList<>(imagesIds);
        initFavouriteImages(true);
        notifyDataSetChanged();
    }

    public void refreshItem(int source,boolean state){
        int index = imagesIds.indexOf(source);
        favouriteChecked.set(index,state);
        notifyItemChanged(index);
    }
}
