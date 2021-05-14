package airquality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class AirQualityController {
    //ENDPOINTS
    @Autowired
    private AirQualityService serviceAir;

    @Autowired
    private StationService serviceStation;

    @Autowired
    private RestTemplate restTemplate;

    // CHAVE GERADA DA API

    private String token = "2a4eb591ee0e1ece4c7259035d002d7892bc239e";

    /** Dada uma cidade é retornado o objecto "AirQuality" correspondentes com os valores registados
     * Verifica se o valor já se encontra em Cache. No caso de não se encontrar é feito um pedido à API (refreshCity)
     * Verifica se o valor que se encontra em cache é válido (não foi registado há mais de mais minutos)
     * Se o valor já tiver sido registado há mais de 10 minutos é feita uma chamada à API (refreshCity)
     * Nos dois casos acima é incrementado o número de tentativas sem sucesso (miss)
     * Para o último caso possível,
     * é incrementado o número de sucessos e retornado o objecto AirQuality de uma determinada cidade
     */
    // Consulta os valores de qualidade do ar por cada cidade
    @GetMapping("/air/{city}")
    public AirQuality getAirQuality(@PathVariable String city){
        if(!serviceAir.returnAirQuality().containsKey(city) || System.currentTimeMillis() - serviceAir.returnAirQuality().get(city).getTime() > 600000){
            this.refreshCity(city);
            serviceAir.incrementMiss();
        }

        else {
            serviceAir.incrementHit();
        }

        return serviceAir.returnAirQuality().get(city);
    }


    /**
     * Método para fazer chamadas à API para uma determinada cidade
     */
    private void refreshCity(String city){ //Guardar na cache pelo que não é contabilizado como sendo um request
        AirQuality airQuality = restTemplate.getForObject("https://api.waqi.info/feed/"+city+"/?token="+token,AirQuality.class);
        serviceAir.guardarAirQuality(city,airQuality);
    }

    // no refresh city n conta como pedido , uma vez q guarda na cache



    //lista estações que estao em cache.
    @GetMapping("/stations")
    public Map<Integer,Station> getStations(){

        return serviceStation.returnStation();
    }



    @GetMapping("/requests")
    public String getRequests(){
        return "Requests: " + Cache.countRequests(); } //Retorna TODOS os requests (hits,miss, e acesso a endpoints)
    //O html também acede à API produzida por mim, pelo que também irá incrementar o número de requests



   // vai a cache pedir hits e miss's
    @GetMapping("/stats")
    public String getStats() {

        return "Hits: " + serviceStation.getHit() + "<br>Miss: " + serviceStation.getMiss();
    }




    @GetMapping("/station/{city}")
    public String getSpecificStation(@PathVariable String city){
        Map<Integer,Station> stacidade = serviceStation.returnStation();

        //apenas poderá ser avaliado se o valor esperado está em cache ou não
        for(int s=0;s<stacidade.size();s++){
            if (stacidade.get(s)!=null && stacidade.get(s).getCity().equalsIgnoreCase(city.toLowerCase()) ){
                serviceStation.incrementHit();
                return "Existe estação em  " + city.substring(0, 1).toUpperCase() + city.substring(1)
                        + "!<br>City: " + stacidade.get(s).getCity() + "<br>ID: " + Integer.toString(stacidade.get(s).getID());
            }
        }
        serviceStation.incrementMiss();
        return "Estação nao encontrada";
    }


}