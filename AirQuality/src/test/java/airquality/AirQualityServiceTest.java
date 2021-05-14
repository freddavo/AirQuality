package airquality;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import static org.assertj.core.api.Assertions.assertThat;


//// testes com mockito- testar intereçaões com a cache tal como na Station test

@ExtendWith(MockitoExtension.class)
public class AirQualityServiceTest {

    @Mock (lenient = true)
    private Cache cache;

    @InjectMocks
    private AirQualityService airQualityService;

    ArrayList<String> cidadesdisponiveis = new ArrayList<>();
    AirQuality airquality;
    int miss;
    int hit;

    @BeforeEach
    public void setup() throws Exception {
        //setup

        // lista de cidades disponiveis
        String[] cidades = {"Aveiro","Madrid","Porto","Lille","Barcelona","Roma"};
        for (String city : cidades)
            cidadesdisponiveis.add(city);


        miss = 5;
        hit = 10;


        HashMap<String,HashMap<String,Float>> dados = new HashMap<>();

        HashMap<String,Float> co = new HashMap<>();
        HashMap<String,Float> pm25 = new HashMap<>();

        co.put("v", (float) 2.0);
        pm25.put("v", (float) 17.4);

        dados.put("co",co);
        dados.put("pm25",pm25);

        Dados info = new Dados(20,dados);


        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();


        airquality = new AirQuality("ok",info,timestamp);

        //Mockito
        Mockito.when(cache.getCitiesAvailable()).thenReturn(cidadesdisponiveis);
        Mockito.when(cache.getHitAndMiss()).thenReturn(hit+miss);

        for(int i=0;i<cidadesdisponiveis.size();i++){
            Mockito.when(cache.isValid(cidadesdisponiveis.get(i))).thenReturn(true);
            Mockito.when(cache.getAirQualityByCity(cidadesdisponiveis.get(i))).thenReturn(airquality);
        }

        Mockito.when(cache.getAirQualityByCity("Vigo")).thenReturn(null);
        Mockito.when(cache.isValid("Esgueira")).thenReturn(false);

    }


    @Test
    public void cidade_valida(){ // valiadde de dados registados
        for(int cidade=0;cidade<cidade;cidade++){
            assertThat(cache.isValid(cidadesdisponiveis.get(cidade))).isEqualTo(true);
        }
    }


    @Test
    public void cidade_invalida(){ // verifica que Espinho nao cidade valida

        assertThat(cache.isValid("Alentejo")).isEqualTo(false);
    }


    @Test
    public void cidade_valida_retornar_valores(){ // retornada uma cidade disponivel , os dados dessa mm cidade sao os esorados
        for(int cidade=0;cidade<cidadesdisponiveis.size();cidade++){
            assertThat(cache.getAirQualityByCity(cidadesdisponiveis.get(cidade))).isEqualTo(airquality);
        }
    }


    @Test
    public void cidade_invalida_null(){ // parecido ao teste anterior , só que a cidade nao esta disponivel logo retorna null

        assertThat(cache.getAirQualityByCity("Brasilia")).isEqualTo(null);
    }


    @Test
    public void cidades_registadas(){ //verifica se asidades estao registadas na cache
        for(int cidade=0;cidade<cidadesdisponiveis.size();cidade++){
            assertThat(cache.getCitiesAvailable().contains(cidadesdisponiveis.get(cidade))).isEqualTo(true);
        }
        assertThat(cache.getCitiesAvailable().size()).isEqualTo(6);
    }


    @Test
    public void givenStats_hit_miss(){ //  verifica se os valores de hit e miss estão correctos, através da soma de ambos

        assertThat(cache.getHitAndMiss()).isEqualTo(hit+miss);
    }
}