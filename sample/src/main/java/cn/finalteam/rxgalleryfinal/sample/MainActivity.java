package cn.finalteam.rxgalleryfinal.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.ui.widget.RecyclerViewFinal;

public class MainActivity extends AppCompatActivity {

    Button mBtnOpen;

    RecyclerViewFinal recyclerViewFinal;

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

        recyclerViewFinal = (RecyclerViewFinal) findViewById(R.id.recycler_view);

        View view = LayoutInflater.from(this).inflate(R.layout.layout_loadmorestyle_head, null);

        recyclerViewFinal.addFooterView(view);

    }
}
