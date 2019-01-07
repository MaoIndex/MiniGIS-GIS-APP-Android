package com.example.gcd_chenx.minigis;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.data.Geodatabase;
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable;
import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private MapView mapview;
    private ShapefileFeatureTable shpfiletable;
    private FeatureLayer shplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArcGISRuntimeEnvironment.setLicense(
                "runtimelite,1000,rud9255553260,none,0JMFA0PL40SB003AD009");

        //TODO
        String _msg = "";
        //GHXT003_PY_ZW_14.shp
        mapview = findViewById(R.id.mapView);
        mapview.setAttributionTextVisible(false);
        ArcGISMap _map = new ArcGISMap(Basemap.createTopographic());
        //String _shppath = getSDPath() + "/Download/minigis/test.shp";
        String _gdbpath = getSDPath() + "/Download/minigis/minigis.gdb";
        try {
            Geodatabase gdbfiletable = new Geodatabase(_gdbpath);
            //shpfiletable = new ShapefileFeatureTable(_shppath);
            //shpfiletable.loadAsync();
            _msg =  shpfiletable.getLoadError().toString();

            /*
            shpfiletable.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    GeometryType gt = shpfiletable.getGeometryType();
                    String name = shpfiletable.getTableName();
                    shplayer = new FeatureLayer(shpfiletable);
                    if (shplayer.getFullExtent() != null) {
                        mapview.setViewpointGeometryAsync(shplayer.getFullExtent());
                    } else {
                        shplayer.addDoneLoadingListener(new Runnable() {
                            @Override
                            public void run() {
                                mapview.setViewpointGeometryAsync(shplayer.getFullExtent());
                            }
                        });
                    }
                    _map.getOperationalLayers().add(shplayer);
                }
            });
            */
        } catch (Exception e) {
            _msg = e.getMessage();
        }

        SimpleLineSymbol _linesymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,
                Color.RED, 1.0f);
        SimpleFillSymbol _fillsymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID,
                Color.YELLOW, _linesymbol);
        SimpleRenderer _renderer = new SimpleRenderer(_fillsymbol);
        shplayer.setRenderer(_renderer);

        mapview.setMap(_map);

        new AlertDialog.Builder(this)
                .setTitle("标题")
                .setMessage(_msg)
                .setPositiveButton("确定", null)
                .show();

    }

    protected boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    protected String getSDPath(){
        File _sddir = null;
        boolean _sdcardexist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        if(_sdcardexist){
            _sddir = Environment.getExternalStorageDirectory();
        }
        return _sddir.toString();
    }

    @Override
    protected void onPause(){
        mapview.pause();
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapview.resume();
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        mapview.dispose();
    }

}
