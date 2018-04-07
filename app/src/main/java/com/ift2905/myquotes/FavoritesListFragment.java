package com.ift2905.myquotes;

import android.arch.lifecycle.Lifecycle;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.ift2905.myquotes.R.id.container;

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
    ImageButton btn_delete;
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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3)
            {
                Log.d("@string/debugging", "clicked");
                Toast.makeText(getContext(), "Stop Clicking me: "+position, Toast.LENGTH_SHORT).show();
                AboutFragment fragment = new AboutFragment();
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container_main, fragment, "FRAG_ABOUT");
                ft.commit();
            }
        });

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
            btn_delete = (ImageButton) view.findViewById(R.id.delete_button);
            btn_delete.setTag(i);

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                    adb.setTitle("Deleting quote from Favorites");
                    adb.setMessage("Are you sure you want to delete it?");
                    final int positionToRemove = (int)view.getTag();
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String id = list_favorite_quotes.get(positionToRemove).getId();
                            DBHelper.deleteQuoteFromFavorites(id);
                            list_favorite_quotes.remove(positionToRemove);
                            adapter.notifyDataSetChanged();
                        }});
                    adb.show();
                }
            });

            quote = list_favorite_quotes.get(i);
            tv_quote.setText(quote.getQuote());
            tv_author.setText(quote.getAuthor());

            //String url = films.LineupItems.get(i).ImagePlayerNormalC;

            //Picasso.with(getApplicationContext()).load(url).into(im);
            return view;
        }
    }
}
