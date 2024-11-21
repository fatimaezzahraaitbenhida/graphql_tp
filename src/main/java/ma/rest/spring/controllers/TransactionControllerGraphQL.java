package ma.rest.spring.controllers;

import lombok.AllArgsConstructor;
import ma.rest.spring.entities.Compte;
import ma.rest.spring.entities.Transaction;
import ma.rest.spring.entities.TransactionRequest;
import ma.rest.spring.entities.TypeTransaction;
import ma.rest.spring.repositories.CompteRepository;
import ma.rest.spring.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@AllArgsConstructor
public class TransactionControllerGraphQL {

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @MutationMapping
    public Transaction addTransaction(@Argument("transaction") TransactionRequest transactionRequest) {
        if (transactionRequest == null) {
            throw new RuntimeException("Le paramètre transactionRequest est null !");
        }

        // Conversion de la date (au format "yyyy-MM-dd") en objet Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(transactionRequest.getDate());
        } catch (Exception e) {
            throw new RuntimeException("Format de date invalide : " + transactionRequest.getDate());
        }

        Compte compte = compteRepository.findById(transactionRequest.getCompteId())
                .orElseThrow(() -> new RuntimeException("Compte introuvable avec l'ID : " + transactionRequest.getCompteId()));

        Transaction transaction = new Transaction();
        transaction.setCompte(compte);
        transaction.setMontant(transactionRequest.getMontant());
        transaction.setDate(parsedDate);
        transaction.setType(transactionRequest.getType());
        return transactionRepository.save(transaction);
    }


    @QueryMapping
    public Map<String, Object> transactionStats(){
        long count = transactionRepository.count();
        double sumDepots = transactionRepository.sumByType(TypeTransaction.DEPOT);
        double sumRetraits = transactionRepository.sumByType(TypeTransaction.RETRAIT);
        return Map.of(
                "count", count,
                "sumDepots", sumDepots,
                "sumRetraits", sumRetraits
                );
}
}