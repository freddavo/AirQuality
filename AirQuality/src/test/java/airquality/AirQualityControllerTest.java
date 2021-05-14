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

// Testes de Integração - Tecnologia utilizada: MockMvc

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



    @Test
    public void testGetAirQuality() throws Exception { // testa todas as cidades disponíveis e o valor de AirQuality associado

        String [] disponiveis= {"shanghai","paris","london","lisbon","berlin","tokyo","munchen","denver","helsinki","stockholm","moscow","madrid","beijing","porto","hongkong","barcelona","manchester","braga","liverpool","roma","lille","bern","linz","new-york"};
        for (String c : disponiveis ) {
            cidades_disponiveis.add(c);
        }

        //mockmvc chama o método get("/api/air")  requisição GET para a url do  controller q esta no before no @Before
        for (int cidade=0;cidade<cidades_disponiveis.size();cidade++){
            mockMvc.perform(MockMvcRequestBuilders.get("/api/air/"+cidades_disponiveis.get(cidade))) //  localhost:8080/api/air/{city}
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("ok"));
        }
    }


    @Test
    public void testGetStations() throws Exception { // verfifica se todas as estaçes estao registadas

        String res = mockMvc.perform(MockMvcRequestBuilders.get("/api/stations"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        for(int i=0;i<cidades_disponiveis.size();i++){
            assertThat(res.contains(cidades_disponiveis.get(i))).isEqualTo(true);
        }
    }


    @Test
    public void testGetStats() throws Exception { // Retorna todas as estatisticas  e se hit e miss nao sao null
        String res = mockMvc.perform(MockMvcRequestBuilders.get("/api/stats"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String[] split = res.split("<br>");
        String hits = split[0];
        String miss = split[1];

        String[] split2 = hits.split(" ");

        assertThat(!split2[0].isEmpty()).isEqualTo(true);
        assertThat(!split2[0].equals("")).isEqualTo(true);
        assertThat(!split2[0].equals(null)).isEqualTo(true);

        String[] split3 = miss.split(" ");

        assertThat(!split3[0].isEmpty()).isEqualTo(true);
        assertThat(!split3[0].equals("")).isEqualTo(true);
        assertThat(!split3[0].equals(null)).isEqualTo(true);
    }


    @Test

    public void testGetSpecificStation() throws Exception {
        String res;
        String city;
        String[] split;
        String [] disponiveis= {"shanghai","paris","london","lisbon","berlin","tokyo","munchen","denver","helsinki","stockholm","moscow","madrid","beijing","porto","hongkong","barcelona","manchester","braga","liverpool","roma","lille","bern","linz","new-york"};



        for (String c : disponiveis ) {
            cidades_disponiveis.add(c);
        }
        for (int cidade=0;cidade<cidades_disponiveis.size();cidade++){

            res = mockMvc.perform(MockMvcRequestBuilders.get("/api/station/"+cidades_disponiveis.get(cidade)))
                    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();


            //System.out.println(result); " esxiste estação em Tokyo" Id:5

            split = res.split("<br>"); // lista


            city = split[1].split(" ")[1].trim().toLowerCase();
            //System.out.println(city); //nome das cidades , ta correto



            assertThat(cidades_disponiveis.contains(city)).isEqualTo(true);

        }
    }


    @Test
    public void testSpecificStationWrongCity() throws Exception{ // Verifica que não é encontrada nenhuma station para uma cidade não definida.

        String [] cidades_inexistentes={"Leça","Matosinhos","Faro"};

        ArrayList<String> cidades_ine = new ArrayList<>();

        for (String c : cidades_inexistentes ) {
            cidades_ine.add(c);
        }

        for (int i=0;i<cidades_inexistentes.length;i++){
            String res = mockMvc.perform(MockMvcRequestBuilders.get("/api/station/"+cidades_inexistentes[i])) //http://localhost:8080/api/station/{{city}}
                    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

            assertThat(res.equals("Estação nao encontrada")).isEqualTo(true);
        }
    }




}