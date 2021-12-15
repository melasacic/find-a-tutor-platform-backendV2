package com.findTutor.findTutor;

import com.findTutor.findTutor.database.tutor.model.DBTutor;
import com.findTutor.findTutor.database.tutor.TutorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TutorConfig {

    @Bean
    CommandLineRunner commandLineRunner(TutorRepository repository){
        return args -> {
           DBTutor mela= new DBTutor(
                    "Mela",
                    "Sacic",
                    "mellsacic",
                    "12345",
                    "mela.sacic@gmail.com",
                    "Sarajevo",
                    "060/319-2739"
            );

            DBTutor nihad= new DBTutor(
                    "Nihad",
                    "Hrnjic",
                    "nhrnjic",
                    "678910",
                    "n.hrnjic@gmail.com",
                    "Hadzici",
                    "060/318-2710"
            );

            repository.saveAll(List.of(mela, nihad));
        };
    }
}
