package airquality;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AirQualityService {
    //função do AirQualityService é interagir com a Cache


    //Guardar objecto, interagindo com a Cache
    public void guardarAirQuality(String city,AirQuality aq) {
        Cache.setAirQuality(city,aq);
    }

    //Retornar todos os objectos AirQuality
    public Map<String,AirQuality> returnAirQuality(){
        return Cache.getAirQuality();
    }

    //Incrementa o valor de Hits, interagindo com a Cache
    public void incrementHit() {
        Cache.incHit(); }

    //Incrementa o valor de Miss, interagindo com a Cache
    public void incrementMiss() {
        Cache.incMiss(); }

    //Retorna o valor de Hit, interagindo com a Cache
    public int getHit() {
        return Cache.getHit(); }

    //Retorna o valor de Miss, interagindo com a Cache
    public int getMiss() {
        return Cache.getMiss(); }
}
