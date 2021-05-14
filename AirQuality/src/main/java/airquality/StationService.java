package airquality;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class StationService {
    //interação coma a cache

    //Guardar objecto
    public void saveStation(int id, Station st) {
        Cache.setStation(id, st); }

    //Retorna todas as Stations
    public Map<Integer,Station> returnStation(){

        return Cache.getStations();
    }


    public void incrementHit() {
        Cache.incHit(); }


    public void incrementMiss() {
        Cache.incMiss(); }


    public int getHit() {
        return Cache.getHit(); }


    public int getMiss() {
        return Cache.getMiss(); }

}