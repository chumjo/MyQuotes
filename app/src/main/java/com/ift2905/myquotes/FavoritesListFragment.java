package com.ift2905.myquotes;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
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

import java.util.ArrayList;

/**
 * Fragment containing ListView to display the list of Favorite quotes
 */

public class FavoritesListFragment extends Fragment {

    MyAdapter adapter;
    ListView list;
    Quote quote;
    TextView tv_quote;
    TextView tv_author;
    ImageView im_category;
    ImageButton btn_delete;
    Button btn_delete_all;
    LayoutInflater inflater;

    // List of quotes from Favorites Database
    ArrayList<Quote> list_favorite_quotes = DBHelper.getFaroriteQuotes();

    /**
     * Constructor of Favorite Quotes ListView Fragment
     */
    public FavoritesListFragment(){
        super();
    }

    /**
     * Creation of Favorites Quotes ListView Fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;
        final View rootView = inflater.inflate(R.layout.fragment_favorite_listview, container, false);
        list = (ListView) rootView.findViewById(R.id.favorite_list);
        adapter = new MyAdapter();
        list.setAdapter(adapter);

        // Listener for list items
        // if one item (favorite quote) is clicked a ViewPager with the favorite quotes is displayed
        // similar to the MainActivity ViewPager
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3)
            {
                Fragment frag_fav_vp = getFragmentManager().findFragmentByTag("FRAG_FAV_VP");

                if(frag_fav_vp == null)
                    frag_fav_vp = new FavoritesViewPagerFragment();

                // Inform the ViewPager the position of the quote to display
                Bundle bundle=new Bundle();
                bundle.putInt("position_selected",position);
                frag_fav_vp.setArguments(bundle);

                // Replace the ListView Fragment with the ViewPager Fragment
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container_main, frag_fav_vp, "FRAG_FAV_VP");
                ft.commit();
            }
        });

        btn_delete_all = (Button) rootView.findViewById(R.id.btn_delete_all);

        // Listener to the DELETE ALL BUTTON (if we want to delete all the favorite quotes at once)
        btn_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Deletion alert
                AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                if(list_favorite_quotes.size() != 0) {
                    adb.setTitle(R.string.delete_all);
                    adb.setMessage(R.string.delete_all_question);

                    // If cancel, do nothing
                    adb.setNegativeButton(R.string.cancel_btn, null);

                    // If ok, remove element from list
                    adb.setPositiveButton(R.string.ok_btn, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            int fav_liste_size = list_favorite_quotes.size();

                            for(int i=0; i<fav_liste_size; i++) {

                                String id = list_favorite_quotes.get(0).getId();

                                // Remove quote from Favorites database
                                DBHelper.deleteQuoteFromFavorites(id);

                                // Remove quote from list of displayed quotes in ListView
                                list_favorite_quotes.remove(0);

                                adapter.notifyDataSetChanged();

                                // Uncheck Favorite's star if quote displayed in MainActivity ViewPager (mViewPager)
                                ((MainActivity)getActivity()).unCheckFavoriteState(id);
                            }
                        }});

                // If there are no quotes to delete
                } else {
                    adb.setTitle(R.string.delete_all_empty);
                    adb.setMessage(R.string.delete_all_empty_question);
                    adb.setNeutralButton(R.string.back_btn, null);
                }
                adb.show();
            }
        });

        return rootView;
    }

    /**
     * Adapter of Favorite Quote Fragment to Favorite Quotes ListView Fragment
     */
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

            // Create view for Favorite fragment in ListView
            tv_quote = (TextView) view.findViewById(R.id.quote);
            tv_author = (TextView) view.findViewById(R.id.author);
            im_category = (ImageView) view.findViewById(R.id.imgCategory);
            btn_delete = (ImageButton) view.findViewById(R.id.delete_button);
            btn_delete.setTag(i);

            // Delete one single quote (pressing it's delete button)
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Deletion alert
                    AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                    adb.setTitle(R.string.delete_one);
                    adb.setMessage(R.string.delete_one_question);

                    // Get the position of the quote to remove
                    final int positionToRemove = (int)view.getTag();

                    // If cancel, do nothing
                    adb.setNegativeButton(R.string.cancel_btn, null);

                    // If ok, remove element from list
                    adb.setPositiveButton(R.string.ok_btn, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String id = list_favorite_quotes.get(positionToRemove).getId();

                            // Remove quote from Favorites database
                            DBHelper.deleteQuoteFromFavorites(id);

                            // Remove quote from list of displayed quotes in ListView
                            list_favorite_quotes.remove(positionToRemove);

                            adapter.notifyDataSetChanged();

                            // Uncheck Favorite's star if quote displayed in MainActivity ViewPager (mViewPager)
                            ((MainActivity)getActivity()).unCheckFavoriteState(id);
                        }});
                    adb.show();
                }
            });

            quote = list_favorite_quotes.get(i);
            tv_quote.setText(quote.getQuote());
            tv_author.setText(quote.getAuthor());

            // Set the image category icon
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getContext().getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue,true);
            @ColorInt int color = typedValue.data;

            im_category.setImageDrawable(getResources().getDrawable(SettingsResources.getIcon(quote.getCategory())));
            im_category.setColorFilter(color, PorterDuff.Mode.MULTIPLY);

            return view;
        }
    }
}
