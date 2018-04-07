package com.ift2905.myquotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Jonathan on 2018-04-06.
 */

public class FavoritesListFragment extends Fragment {

    MyAdapter adapter;
    ListView list;
    Quote quote;
    TextView tv_quote;
    TextView tv_author;
    ImageView im_category;
    ArrayList<Quote> list_favorite_quotes = DBHelper.getFaroriteQuotes();
    LayoutInflater inflater;

    public FavoritesListFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;

        View rootView = inflater.inflate(R.layout.fragment_favorite_listview, container, false);
        list = (ListView) rootView.findViewById(R.id.favorite_list);
        adapter = new MyAdapter();
        list.setAdapter(adapter);

        return rootView;
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_favorite_quotes.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view==null){
                view = inflater.inflate(R.layout.listitem_quote,viewGroup,false);
            }

            tv_quote = (TextView) view.findViewById(R.id.quote);
            tv_author = (TextView) view.findViewById(R.id.author);
            im_category = (ImageView) view.findViewById(R.id.imgCategory);

            quote = list_favorite_quotes.get(i);
            tv_quote.setText(quote.getQuote());
            tv_author.setText(quote.getAuthor());

            //String url = films.LineupItems.get(i).ImagePlayerNormalC;

            //Picasso.with(getApplicationContext()).load(url).into(im);
            return view;
        }
    }
}
