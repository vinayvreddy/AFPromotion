package com.promotion.afpromotion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class PromotionsRecyclerView extends RecyclerView.Adapter<PromotionsRecyclerView.PromotionsViewHolder> {

            ArrayList<Promotions> proArrayList;
            Context mContext;
            String url ="";
            public static class PromotionsViewHolder extends RecyclerView.ViewHolder {

                TextView textView;
                ImageView imageView;
                TextView textView2;
                TextView textView3;
                Button button;
                public PromotionsViewHolder(View itemView) {
                    super(itemView);
                    textView = (TextView) itemView.findViewById(R.id.textView);
                    imageView = (ImageView) itemView.findViewById(R.id.imageView);
                    textView2 = (TextView) itemView.findViewById(R.id.textView2);
                    textView3 = (TextView) itemView.findViewById(R.id.textView3);
                    textView3.setMovementMethod(LinkMovementMethod.getInstance());
                    button = (Button) itemView.findViewById(R.id.button);
                }
            }


         public PromotionsRecyclerView(Context context,ArrayList<Promotions> list){
                    proArrayList = list;
                    mContext = context;
         }

        @Override
        public int getItemCount() {
            return  proArrayList.size();
        }

        @Override
        public PromotionsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
            PromotionsViewHolder pvh = new PromotionsViewHolder(v);
            return pvh;
        }

    @Override
    public void onBindViewHolder(PromotionsViewHolder promotionsViewHolder, int i) {
        url = proArrayList.get(i).getButtonTarget();
        promotionsViewHolder.textView.setText(proArrayList.get(i).getTitle());
        promotionsViewHolder.imageView.setImageBitmap(Util.loadBitmap(Util.getPreferenceFilepath(mContext),proArrayList.get(i).getTitle()));
        promotionsViewHolder.textView2.setText(proArrayList.get(i).getDescription());

        String footer = proArrayList.get(i).getFooter();
        if(!footer.equals("")) {
            String string = footer.substring(0, footer.indexOf("<"));
            String link = footer.substring(footer.indexOf("<"), footer.lastIndexOf(">"));
            promotionsViewHolder.textView3.setText(string + " " + Html.fromHtml(link));
        }
        promotionsViewHolder.button.setText(proArrayList.get(i).getButtonTitle());
        promotionsViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PromotionsWebViewActivity.class);
                intent.putExtra("url",url);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }
        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
}
