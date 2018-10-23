package com.yusufalicezik.wots.Message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yusufalicezik.wots.R;
import com.yusufalicezik.wots.Utils.UniversalImagLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesPersonActivity extends AppCompatActivity {

    private CircleImageView profilResim;
    private TextView isimSoyisim;

    private String mesajGonderilenID;
    private String mesajGonderilenProfilResmi;
    private String mesajGonderilenIsimSoyisim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_person);

        profilResim=findViewById(R.id.circleImageViewMessagesPerson);
        isimSoyisim=findViewById(R.id.tvPersonMessagesIsimSoyisim);
        UniversalInit();

        mesajGonderilenID=getIntent().getStringExtra("id");
        mesajGonderilenProfilResmi=getIntent().getStringExtra("profilresmi");
        mesajGonderilenIsimSoyisim=getIntent().getStringExtra("isim");

        isimSoyisim.setText(mesajGonderilenIsimSoyisim);
        UniversalImagLoader.setImage(mesajGonderilenProfilResmi, profilResim, null, "");


    }

    private void UniversalInit() {
        UniversalImagLoader universalImageLoader = new UniversalImagLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    public void geriTic(View view)
    {
        onBackPressed();
    }
}
