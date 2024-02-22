package joboffers.domain.loginandregister;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryUserRepositoryTestImpl implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void save(final User user) {
        users.put(user.id(), user);
    }

    @Override
    public boolean existsByUsername(final String username) {
        return users.values().stream().anyMatch(user -> user.username().equals(username));
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return users.values().stream().filter(user -> user.username().equals(username)).findFirst();
    }
}
