package ee.taltech.gamecollection.twister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ee.taltech.gamecollection.R;
import ee.taltech.gamecollection.twister.library.LuckyWheelView;
import ee.taltech.gamecollection.twister.library.model.LuckyItem;

public class TwisterActivity extends Activity {
    List<LuckyItem> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_twister);

        final LuckyWheelView luckyWheelView = findViewById(R.id.luckyWheel);

        LuckyItem luckyRHRed = new LuckyItem();
        luckyRHRed.topText = "R HAND";
        luckyRHRed.color = 0xFFFA0000;
        data.add(luckyRHRed);

        LuckyItem luckyRHGreen = new LuckyItem();
        luckyRHGreen.topText = "R HAND";
        luckyRHGreen.color = 0xFF13cf25;
        data.add(luckyRHGreen);

        LuckyItem luckyRHAir = new LuckyItem();
        luckyRHAir.topText = "R HAND";
        luckyRHAir.color = 0xFFB6FAEF;
        data.add(luckyRHAir);

        LuckyItem luckyRHYellow = new LuckyItem();
        luckyRHYellow.topText = "R HAND";
        luckyRHYellow.color = 0xFFF0DD0C;
        data.add(luckyRHYellow);

        LuckyItem luckyRHBlue = new LuckyItem();
        luckyRHBlue.topText = "R HAND";
        luckyRHBlue.color = 0xFF0068FA;
        data.add(luckyRHBlue);

        ////////////

        LuckyItem luckyRFRed = new LuckyItem();
        luckyRFRed.topText = "R FOOT";
        luckyRFRed.color = 0xFFFA0000;
        data.add(luckyRFRed);

        LuckyItem luckyRFGreen = new LuckyItem();
        luckyRFGreen.topText = "R FOOT";
        luckyRFGreen.color = 0xFF13cf25;
        data.add(luckyRFGreen);

        LuckyItem luckyRFAir = new LuckyItem();
        luckyRFAir.topText = "R FOOT";
        luckyRFAir.color = 0xFFB6FAEF;
        data.add(luckyRFAir);

        LuckyItem luckyRFYellow = new LuckyItem();
        luckyRFYellow.topText = "R FOOT";
        luckyRFYellow.color = 0xFFF0DD0C;
        data.add(luckyRFYellow);

        LuckyItem luckyRFBlue = new LuckyItem();
        luckyRFBlue.topText = "R FOOT";
        luckyRFBlue.color = 0xFF0068FA;
        data.add(luckyRFBlue);

        ////////////

        LuckyItem luckyLHRed = new LuckyItem();
        luckyLHRed.topText = "L HAND";
        luckyLHRed.color = 0xFFFA0000;
        data.add(luckyLHRed);

        LuckyItem luckyLHGreen = new LuckyItem();
        luckyLHGreen.topText = "L HAND";
        luckyLHGreen.color = 0xFF13cf25;
        data.add(luckyLHGreen);

        LuckyItem luckyLHAir = new LuckyItem();
        luckyLHAir.topText = "L HAND";
        luckyLHAir.color = 0xFFB6FAEF;
        data.add(luckyLHAir);

        LuckyItem luckyLHYellow = new LuckyItem();
        luckyLHYellow.topText = "L HAND";
        luckyLHYellow.color = 0xFFF0DD0C;
        data.add(luckyLHYellow);

        LuckyItem luckyLHBlue = new LuckyItem();
        luckyLHBlue.topText = "L HAND";
        luckyLHBlue.color = 0xFF0068FA;
        data.add(luckyLHBlue);

        ////////////

        LuckyItem luckyLFRed = new LuckyItem();
        luckyLFRed.topText = "L FOOT";
        luckyLFRed.color = 0xFFFA0000;
        data.add(luckyLFRed);

        LuckyItem luckyLFGreen = new LuckyItem();
        luckyLFGreen.topText = "L FOOT";
        luckyLFGreen.color = 0xFF13cf25;
        data.add(luckyLFGreen);

        LuckyItem luckyLFAir = new LuckyItem();
        luckyLFAir.topText = "L FOOT";
        luckyLFAir.color = 0xFFB6FAEF;
        data.add(luckyLFAir);

        LuckyItem luckyLFYellow = new LuckyItem();
        luckyLFYellow.topText = "L FOOT";
        luckyLFYellow.color = 0xFFF0DD0C;
        data.add(luckyLFYellow);

        LuckyItem luckyLFBlue = new LuckyItem();
        luckyLFBlue.topText = "L FOOT";
        luckyLFBlue.color = 0xFF0068FA;
        data.add(luckyLFBlue);


        luckyWheelView.setData(data);
        luckyWheelView.setRound(5);

        findViewById(R.id.play).setOnClickListener(view -> {
            int index = getRandomIndex();
            luckyWheelView.startLuckyWheelWithTargetIndex(index);
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(index ->
                Toast.makeText(getApplicationContext(), data.get(index).topText, Toast.LENGTH_SHORT).show());
    }

    private int getRandomIndex() {
        Random rand = new Random();
        return rand.nextInt(data.size() - 1);
    }
}
