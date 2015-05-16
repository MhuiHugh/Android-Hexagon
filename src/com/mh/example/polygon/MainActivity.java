package com.mh.example.polygon;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.mh.widget.HexagonView;


/**
 * @author Mhui
 * 演示自定义六边形点击事件监听
 */
public class MainActivity extends Activity implements HexagonView.OnHexagonViewClickListener{
    private final String TAG="MainActivity";

    HexagonView hexagonHello,hexagonCar; //六边形控件实例

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }


    //---------------------------------------------------------

    /**
     * 事件监听
     */
    public void onClick(View view){
        Log.d(TAG,"onClick()");
        switch (view.getId()){
            case R.id.hexagon_hello:
                Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show();
                break;
            case R.id.hexagon_car:
                Toast.makeText(this,"Car",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    //---------------------------------------------------------

    /**
     * init
     */
    private void init(){
        Log.d(TAG,"init()");
        hexagonHello=(HexagonView)this.findViewById(R.id.hexagon_hello);
        hexagonCar=(HexagonView)this.findViewById(R.id.hexagon_car);
        hexagonHello.setOnHexagonClickListener(this);
        hexagonCar.setOnHexagonClickListener(this);
    }
}
