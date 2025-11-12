package mini_tracker.mini_tracker.Services;

import lombok.extern.slf4j.Slf4j;
import mini_tracker.mini_tracker.Entities.User;
import mini_tracker.mini_tracker.Exceptions.NotFoundException;
import mini_tracker.mini_tracker.Payloads.UserPayload;
import mini_tracker.mini_tracker.Payloads.UserUpdatePayload;
import mini_tracker.mini_tracker.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcrypt;


    // Recuperare tutti gli Utenti paginati
    public Page<User> getAllUsers(int pageNumber, int pageSize, String sortBy) {
        // Oggetto Pageable per la paginazione
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        // Chiamiamo findAll
        return userRepository.findAll(pageable);
    }

    //Salvataggio Utente
    public User saveNewUser(UserPayload payload){

        User newUser = new User(
                payload.username(),
                payload.email(),
                bcrypt.encode(payload.password()),
                payload.name(),
                payload.surname()
        );

        User savedUser = userRepository.save(newUser);
        log.info("Utente " + savedUser.getSurname() + " salvato correttamente!");
        return savedUser;

    }

    //findByID
    public User findById(UUID userId){

        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException( "utente non trovato" , userId));

    }

    // findByID and Update
    public User findByIdAndUpdate(UUID userId, UserPayload payload){

        User found =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato" , userId ));

        found.setUsername(payload.username());
        found.setEmail(payload.email());
        found.setPassword(payload.password());
        found.setName(payload.name());
        found.setSurname(payload.surname());

        return userRepository.save(found);
    }

    //delete
    public void findByIdAndDelete(UUID userId){

        User found = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato", userId));

        userRepository.delete(found);

        log.info("utente con ID " + userId + " eliminato correttamente.");

    }

    //find by email (Auth)
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con l'email  non è stato trovato"));
    }

    //PUT aggiorna profilo senza password
    public User findByIdAndUpdate(UUID userId, UserUpdatePayload payload) {
        User found = this.findById(userId);

        found.setUsername(payload.username());
        found.setEmail(payload.email());
        found.setName(payload.name());
        found.setSurname(payload.surname());

        return userRepository.save(found);
    }

    // Metodo per bloccare un utente
    public User lockUser(UUID userId) {
        User found = this.findById(userId);
        found.setLocked(true);
        return userRepository.save(found);
    }

    // Metodo per sbloccare un utente
    public User unlockUser(UUID userId) {
        User found = this.findById(userId);
        found.setLocked(false);
        return userRepository.save(found);
    }

    // Metodo per totale utenti
    public long getTotalUserCount() {
        return userRepository.count();
    }

}
