package br.com.oiinternet.poidnormalizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.oiinternet.poidnormalizer.service.MoiAssociationService;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private MoiAssociationService moiAssociationService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Change this for aggregate function in mongo
        System.out.println("Starting normalize MOi Association");
        int total = moiAssociationService.findDuplicatedMoiAssociation();
        System.out.println("Finished normalize MOi Association - Total duplicados -> " + total);
    }

}