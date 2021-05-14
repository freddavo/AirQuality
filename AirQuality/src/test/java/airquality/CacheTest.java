package airquality;
import org.junit.Before;
import org.junit.Test;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

public class CacheTest {


    private AirQuality airQuality;
    private Dados info;
    private String status,city;
    private long time;
    private Station station;
    private int id;
    private int before_size;
    private int after_size;
    private int old_req;
    private int new_req;
    private int size1;
    private int size2;
    Cache cache;

    Map<String,AirQuality> airQualityMap = Cache.getAirQuality();
    Map<Integer,Station> stationMap = Cache.getStations();

    @Before
    public void setup() {
        cache = new Cache();
        HashMap<String, HashMap<String,Float>> setup = new HashMap<>();

        HashMap<String,Float> co = new HashMap<>();
        HashMap<String,Float> pm = new HashMap<>();

        co.put("v", (float) 2.0);
        pm.put("v", (float) 17.4);

        setup.put("co",co);
        setup.put("pm25",pm);

        Dados info = new Dados(20,setup);

        time = new Timestamp(System.currentTimeMillis()).getTime();

        airQuality = new AirQuality("ok",info,time);

    }


    @Test
    public void testSetAirQuality(){
        before_size = airQualityMap.size();
        cache.setAirQuality("Lille",airQuality);
        cache.setAirQuality("Madrid",airQuality);
        after_size = airQualityMap.size(); // o tamanho deve aumentar 2 unidades

        //verifcar se o tamnaho é igual depois da inserção
        assertThat(before_size+2==after_size).isEqualTo(true);
        assertThat(before_size==after_size).isEqualTo(false);
        assertThat(before_size+1==after_size).isEqualTo(false);

        //verifcar se os objetos retornados sao iguais
        Map<String,AirQuality> aq = Cache.getAirQuality();
        assertThat(aq.get("Madrid").equals(airQuality)).isEqualTo(true);
        assertThat(aq.get("Lille").equals(airQuality)).isEqualTo(true);


    }


    @Test
    public void testGetAirQuality() { //verifica se um objecto AirQuality retornado é o esperado
        cache.setAirQuality("New-York",airQuality); // guarda um objeto

        old_req = cache.countRequests();
        Map <String,AirQuality> aq = cache.getAirQuality();
        new_req = cache.countRequests();

        //verifica se o numero de pedidos aumenta
        assertThat(old_req==new_req).isEqualTo(false);

        assertThat(old_req+1==new_req).isEqualTo(true);

        assertThat(aq.equals(null)).isEqualTo(false);
        assertThat(airQuality.equals(aq.get("New-York"))).isEqualTo(true);
        assertThat(airQuality.equals(aq.get("Barcelona"))).isEqualTo(false);


    }


    @Test
    public void testSetStation() { //verifica se um objecto Station é guardado correctamente na cache
        station = new Station(500,"Lille");

        before_size = cache.getStations().size();
        cache.setStation(station.getID(),station);
        after_size = cache.getStations().size();

        //Assert Size
        assertThat(before_size+1==after_size).isEqualTo(true);
        assertThat(before_size+2==after_size).isEqualTo(false);
        assertThat(before_size==after_size).isEqualTo(false);

        // o objecto retornado pela cache deve ser igual ao guardado
        Map<Integer,Station> st = cache.getStations();
        assertThat(500==st.get(500).getID()).isEqualTo(true);
        assertThat("Lille".equals(st.get(500).getCity())).isEqualTo(true);

    }


    @Test
    public void testGetStation() { // verifica se um objecto Station retornado é o esperado
        station = new Station(50,"Aveiro"); //  guardar um objecto predifinido e efectua um getStation
        cache.setStation(station.getID(),station);
        old_req = cache.countRequests();
        Map<Integer,Station> st = cache.getStations();
        new_req = cache.countRequests();

        assertThat(st.equals(null)).isEqualTo(false);
        assertThat(station.equals(st.get(50))).isEqualTo(true); //Verifica se o objecto guardado e retornado são iguais
        assertThat(station.equals(st.get(2))).isEqualTo(false);

        assertThat(old_req==new_req).isEqualTo(false);
        assertThat(old_req+1==new_req).isEqualTo(true); // número de requests aumentou uma unidade
    }


    @Test
    public void testCountRequests() { // testa se a contagem do número de requests é incrementa quando é feito um get
        old_req = cache.countRequests();
        cache.getStations();         // estações ou getairquality
        new_req = cache.countRequests();

        assertThat(old_req+1==new_req).isEqualTo(true);
        assertThat(old_req==new_req).isEqualTo(false);
    }


    @Test
    public void testIsValid(){ // verifica se umr egisto airquality é valido ou seja aijda n passou 10 min (600000ms)

        cache.setAirQuality("Aveiro",airQuality);
        cache.getAirQualityByCity("Aveiro").setTime(0); //0ms
        assertThat(cache.isValid("Aveiro")).isEqualTo(false);

        cache.setAirQuality("Porto",airQuality);
        cache.getAirQualityByCity("Porto").setTime(System.currentTimeMillis()); // tempo atual
        assertThat(cache.isValid("Porto")).isEqualTo(true);


    }


    @Test
    public void testGetCitiesAvailable(){ //verifica se as cidades disponíveis são guardadas correctamente
        cache.setStation(1,new Station(6,"Barcelona")); // guardar estação barcelona com id
        cache.setStation(2,new Station(7,"Madrid")); // guardar estaçao madrid com id

        assertThat(cache.getCitiesAvailable().contains("Barcelona")).isEqualTo(true); // lista de cidades disponiveis pela cache contem cidades guradadas'
        assertThat(cache.getCitiesAvailable().contains("Madrid")).isEqualTo(true);

    }


    @Test
    public void testHitAndMiss(){ // TESTAR SE VALOR DE HIT + MISS É CORRETO
        for(int i=0;i<5;i++){ cache.incMiss(); }
        for(int j=0;j<10;j++){ cache.incHit(); }

        assertThat(cache.getHitAndMiss()==15).isEqualTo(true); //soma


        assertThat(cache.getHit()==10).isEqualTo(true);
        assertThat(cache.getMiss()==5).isEqualTo(true);
    }


    @Test
    public void testGetStationByID(){
        cache.setStation(100,new Station(100,"Aveiro")); // station com id 100 e cidade Aveiro

        int id_errado = 999;

        assertThat(cache.getStationByID(100).getCity().equals("Aveiro")).isEqualTo(true);
        assertThat(cache.getStationByID(100).getID()==100).isEqualTo(true);
        assertThat(cache.getStationByID(id_errado)).isEqualTo(null); // n ha nenhum co id 999
    }





    @Test
    public void testGetAirQualityByCity() throws InterruptedException { // verificar se os atributos do objecto retornado são iguais aos do objecto guardado
        HashMap<String, HashMap<String,Float>> dados = new HashMap<>(); // dados

        HashMap<String,Float> co = new HashMap<>(); // airquality
        HashMap<String,Float> pm25 = new HashMap<>(); //airquality

        co.put("v", (float) 18.45);
        pm25.put("v", (float) 64.23);

        dados.put("co",co);
        dados.put("pm25",pm25);

        Dados info = new Dados(56,dados);

        time = new Timestamp(System.currentTimeMillis()).getTime();
        airQuality = new AirQuality("ok",info,time);
        cache.setAirQuality("Aveiro",airQuality);

        AirQuality airQualityReceived = cache.getAirQualityByCity("Aveiro");
        assertThat(airQualityReceived.getTime()>=time).isEqualTo(true);
        assertThat(airQualityReceived.getStatus().equals("ok")).isEqualTo(true);
        assertThat(airQualityReceived.getData().equals(info)).isEqualTo(true);
    }
}