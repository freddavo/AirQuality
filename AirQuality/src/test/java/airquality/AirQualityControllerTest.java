package airquality;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// estes de Integração - Tecnologia utilizada: MockMvc

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AirQualityControllerTest {


    ArrayList<String> cidades_disponiveis = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AirQualityController controller;

    @BeforeEach
    public void setup() throws Exception {

    }

    /**
     * testGetAirQuality - Testa todas as cidades disponíveis e o valor de AirQuality associado
     * Espera um Status "ok" para cada station
     * URL : localhost:8080/api/air/{city}
     */
    @Test
    public void testGetAirQuality() throws Exception {  //testa todas as cidades disponiveis e o valor aiq de cd cidade

        cidades_disponiveis.add("porto");cidades_disponiveis.add("aveiro");cidades_disponiveis.add("londres");
        cidades_disponiveis.add("lisboa");cidades_disponiveis.add("barcelona");cidades_disponiveis.add("madrid");
        cidades_disponiveis.add("manchester");cidades_disponiveis.add("liverpool");cidades_disponiveis.add("new-york");



        for (int cidades=0;cidades<cidades_disponiveis.size();cidades++){
            mockMvc.perform(MockMvcRequestBuilders.get("/api/air/"+cidades_disponiveis.get(cidades)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("ok"));
        }
    }


    @Test
    public void testGetStations() throws Exception { // verfifica se todas as estaçes estao registadas

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/api/stations"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        for(int i=0;i<cidades_disponiveis.size();i++){
            assertThat(result.contains(cidades_disponiveis.get(i))).isEqualTo(true);
        }
    }


    @Test
    public void testGetStats() throws Exception { // Retorna todas as estatisticas e verifica se o valor associado é numério (isNumeric) e se hit e miss nao sao null
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/api/stats"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String[] split = result.split("<br>");
        String hits = split[0];
        String miss = split[1];

        String[] split2 = hits.split(" ");
        assertThat(isNumeric(split2[1])).isEqualTo(true);
        assertThat(!split2[0].isEmpty()).isEqualTo(true);
        assertThat(!split2[0].equals("")).isEqualTo(true);
        assertThat(!split2[0].equals(null)).isEqualTo(true);

        String[] split3 = miss.split(" ");
        assertThat(isNumeric(split3[1])).isEqualTo(true);
        assertThat(!split3[0].isEmpty()).isEqualTo(true);
        assertThat(!split3[0].equals("")).isEqualTo(true);
        assertThat(!split3[0].equals(null)).isEqualTo(true);
    }


    @Test
    public void testGetSpecificStation() throws Exception { // se existe uma station para as cidades disponíveis
        String result,city;
        String[] split;

        cidades_disponiveis.add("porto");cidades_disponiveis.add("aveiro");cidades_disponiveis.add("londres");
        cidades_disponiveis.add("lisboa");cidades_disponiveis.add("barcelona");cidades_disponiveis.add("madrid");
        cidades_disponiveis.add("manchester");cidades_disponiveis.add("liverpool");cidades_disponiveis.add("new-york");


        for (int i=0;i<cidades_disponiveis.size();i++){

            result = mockMvc.perform(MockMvcRequestBuilders.get("/api/station/"+cidades_disponiveis.get(i)))
                    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

            split = result.split("<br>");
            city = split[1].split(" ")[1].trim().toLowerCase();

            assertThat(cidades_disponiveis.contains(city)).isEqualTo(true);
        }
    }


    @Test
    public void testSpecificStationWrongCity() throws Exception{ // Verifica que não é encontrada nenhuma station para uma cidade não definida.
        String result,city;
        String[] split;

        ArrayList<String> wrong_cities = new ArrayList<>();
        wrong_cities.add("LeçadaPalmeira");wrong_cities.add("Arouca");wrong_cities.add("Algarve");

        for (int i=0;i<wrong_cities.size();i++){
            String res = mockMvc.perform(MockMvcRequestBuilders.get("/api/station/"+wrong_cities.get(i)))
                    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
            assertThat(res.equals("Station not found.")).isEqualTo(true);
        }
    }



    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}