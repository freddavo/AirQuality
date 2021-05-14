package airquality;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache {
    protected static final Map<String,AirQuality> airQuality = new HashMap<>();
    protected static final Map<Integer,Station> stations = new HashMap<>();
    private static int requests = 0;
    private static int miss = 0;
    private static int hit = 0;


   //Guarda um objecto AirQuality associado a uma cidade

    public static void setAirQuality(String city,AirQuality airq){
        airq.setTime(System.currentTimeMillis());
        airQuality.put(city, airq);
    }

    // Retorna um objecto AirQuality
    public static Map<String,AirQuality> getAirQuality(){
        requests++;
        return airQuality;
    }




    // Guarda um objecto Station associado a um ID (int)
    public static void setStation (int id,Station station) { stations.put(id,station); }

    //Retorna todos os objectos Station guardados previamente

    public static Map<Integer,Station> getStations() {
        requests++;
        return stations;
    }

    //Retorna o número de request
    public static int countRequests() {
        return requests; }

    //Diz-nos o número de tentativas SEM sucesso
    public static int getMiss() {
        return miss; }

    //Incrementa o número de tentativas SEM sucesso
    public static void incMiss() {
        miss++; }

    //Diz-nos número de tentativas COM sucesso
    public static int getHit() {
        return hit; }

    //Incrementa o número de tentativas COM sucesso
    public static void incHit() {
        hit++; }


     //Verifica se os valores de air quality para uma determinada cidade são válidos
     //São válidos se não tiverem passado 10 minutos desde o seu registo

    public boolean isValid(String city){
        if(airQuality.get(city)!=null){
            long time = airQuality.get(city).getTime();
            return System.currentTimeMillis() - time < 600000; // conversao
        }
        return false;
    }


     //Retorna todas as cidades disponíveis

    public List<String> getCitiesAvailable(){
        ArrayList<String> cidades = new ArrayList<>();
        Integer[] ids = stations.keySet().toArray(new Integer[0]);

        for(int i=0;i<ids.length;i++){
            cidades.add(stations.get(ids[i]).getCity());
        }

        return cidades;
    }


     // soma de sucessos e falhas

    public int getHitAndMiss(){

        return getHit()+getMiss();
    }


    //retorna um object station do id correspondente

    public Station getStationByID(int id) {

        return stations.get(id);
    }


     //retorna um object AirQuality da cidade correspondente

    public AirQuality getAirQualityByCity(String city){
        return airQuality.get(city); }

}