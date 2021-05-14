package airquality;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

// testes com mockito- testar intereçaões com a cache
@ExtendWith(MockitoExtension.class)
public class StationServiceTest {


    @Mock (lenient = true)
    private Cache cache;

    @InjectMocks
    private StationService stationService;


    @BeforeEach

    // setup
    public void setup() throws Exception {
        Station st = new Station(1, "Aveiro");
        Mockito.when(cache.getStationByID(st.getID())).thenReturn(st);
        Mockito.when(cache.getStationByID(50)).thenReturn(null);//Wrong ID
    }


    @Test
    public void whenGetStationByID_thenReturnStation(){ // é o objeto que foi guaraddo q vai ser devolvido?
        int id = 1;
        String city = "Aveiro";
        Station aveiro = cache.getStationByID(id);
        assertThat(aveiro.getID()).isEqualTo(id);
        assertThat(aveiro.getCity()).isEqualTo("Aveiro");
    }


    @Test
    public void whenGetStationByWrongID_thenReturnNull(){ // se o id da estaçaõ n existir ou for errado o q vai ser retornado tem q ser null
        int id = 2;
        Station nao_encontrada = cache.getStationByID(id);
        assertThat(nao_encontrada).isNull();
    }


    @Test
    public void whenValidStation_thenDetailsAreCorrect() { // verfica se os atributos da estação estao corretos
        int id = 1;
        String city = "Aveiro";

        assertThat(cache.getStationByID(id).getID()).isEqualTo(id);
        assertThat(cache.getStationByID(id).getCity()).isEqualTo(city);
    }

}