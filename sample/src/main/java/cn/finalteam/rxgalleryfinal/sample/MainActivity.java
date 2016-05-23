package cn.finalteam.rxgalleryfinal.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.ui.widget.RecyclerViewFinal;

public class MainActivity extends AppCompatActivity {

    Button mBtnOpen;

    RecyclerViewFinal recyclerView;

    private List<String> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnOpen = (Button) findViewById(R.id.btn_open);

        mBtnOpen.setOnClickListener(v -> RxGalleryFinal
                .with(this)
                .image()
                .radio()
                .openGallery());
        mList = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            mList.add("");
        }
        recyclerView = (RecyclerViewFinal) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        View header = LayoutInflater.from(this).inflate(R.layout.auto_fit_header, recyclerView, false);
        header.setOnClickListener(v -> Toast.makeText(v.getContext(), "grid_layout_auto_fit_header", Toast.LENGTH_SHORT)
                .show());

        MyAdapter adapter = new MyAdapter();
//        FooterAdapter footerAdapter = new FooterAdapter(adapter, header);

//        final GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return footerAdapter.isFooter(position) ? manager.getSpanCount() : 1;
//            }
//        });


        recyclerView.setAdapter(adapter);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mList.add("dddd");
                mList.add("dddd");
                mList.add("dddd");
                mList.add("dddd");
                mList.add("dddd");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText("item " + position);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }
}
