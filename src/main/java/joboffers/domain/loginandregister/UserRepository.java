package joboffers.domain.loginandregister;

import java.util.Optional;

interface UserRepository {

        void save(User user);

        boolean existsByUsername(String username);

        Optional<User> findByUsername(String username);
}
