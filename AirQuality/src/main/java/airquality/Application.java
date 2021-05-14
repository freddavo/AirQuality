package airquality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;

@SpringBootApplication
public class Application {

    @Autowired
    private StationService service;


    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build(); }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {

        int tqs = 0;
        String [] nomes = {"shanghai","paris","london","lisbon","berlin","tokyo","munchen","denver","helsinki","stockholm","moscow","madrid","beijing","porto","hongkong","barcelona","manchester","braga","liverpool","roma","lille","bern","linz","new-york"};



        ArrayList<String> nomes_cidades = new ArrayList<>();

        for (String c : nomes) {
            nomes_cidades.add(c);
        }


        ArrayList<Station> sta = new ArrayList<>();

        //Guardar objectos station num arrayList para depois guarda-las na Cache
        Station st;
        while (tqs<nomes_cidades.size()){
            st = new Station(tqs,nomes_cidades.get(tqs));
            sta.add(st);
            tqs+=1;
        }

        //Guardar as stations disponíveis para cada cidade, com um id associado
        return args -> {
            for(int i=0;i<sta.size();i++){
                service.saveStation(sta.get(i).getID(),sta.get(i));
            }
        };
    }

}