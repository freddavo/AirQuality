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

        ArrayList<String> cityNames = new ArrayList<>();
        cityNames.add("Shanghai");cityNames.add("Paris");cityNames.add("London");cityNames.add("Lisbon");
        cityNames.add("Berlin");cityNames.add("Tokyo");cityNames.add("Munchen");cityNames.add("Denver");
        cityNames.add("Helsinki");cityNames.add("Stockholm");cityNames.add("Moscow");cityNames.add("Madrid");
        cityNames.add("Beijing"); cityNames.add("Porto");cityNames.add("HongKong");cityNames.add("Barcelona");
        cityNames.add("Manchester"); cityNames.add("Braga");cityNames.add("Liverpool");cityNames.add("Roma");
        cityNames.add("Lille");cityNames.add("Bern"); cityNames.add("Linz"); cityNames.add("New-York");

        ArrayList<Station> sta = new ArrayList<>();

        //Guardar objectos station num arrayList para depois guarda-las na Cache
        Station st;
        while (tqs<cityNames.size()){
            st = new Station(tqs,cityNames.get(tqs));
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