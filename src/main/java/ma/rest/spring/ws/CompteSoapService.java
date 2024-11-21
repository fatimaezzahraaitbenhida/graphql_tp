package ma.rest.spring.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import ma.rest.spring.entities.Compte;
import ma.rest.spring.entities.TypeCompte;
import ma.rest.spring.repositories.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@WebService(serviceName = "BanqueWS")
public class CompteSoapService {

    @Autowired
    private CompteRepository compteRepository;

    @WebMethod
    public List<Compte> getComptes(){
        return compteRepository.findAll();
    }

    @WebMethod
    public Compte getCompteById(@WebParam(name = "id") Long id){
        return compteRepository.findById(id).orElse(null);
    }

    @WebMethod
    public Compte saveCompte(@WebParam(name = "solde") double solde , @WebParam(name = "type") TypeCompte type){
     Compte c = new Compte(null, solde, null, type, null);
     return compteRepository.save(c);
    }

    @WebMethod
    public Compte updateCompte(@WebParam(name = "id") Long id, @WebParam(name = "solde") double solde , @WebParam(name = "type") TypeCompte type){
        Compte c = compteRepository.findById(id).orElse(null);
        if(c != null){
            c.setSolde(solde);
            c.setType(type);
            return compteRepository.save(c);
        }
        return null;
    }

    @WebMethod
    public boolean deleteCompte(@WebParam(name = "id") Long id){
        if(compteRepository.existsById(id)){
            compteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
