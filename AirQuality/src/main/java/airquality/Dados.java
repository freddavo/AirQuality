package airquality;

import java.util.HashMap;
public class Dados {

    private int aqi;
    private HashMap<String, HashMap<String, Float>> iaqi; // é assim a estrutura json da api


    public Dados(int aqi, HashMap<String, HashMap<String, Float>> iaqi) {
        this.aqi = aqi;
        this.iaqi = iaqi;
    }


    public int getAqi() {

        return aqi;
    }

    public void setAqi(int aqi) {

        this.aqi = aqi;
    }


    public HashMap<String, HashMap<String, Float>> getIaqi() {

        return iaqi;
    }

    public void setIaqi(HashMap<String, HashMap<String, Float>> iaqi) {

        this.iaqi = iaqi;
    }


}