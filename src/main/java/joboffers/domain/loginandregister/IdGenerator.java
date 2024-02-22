package joboffers.domain.loginandregister;

import java.util.UUID;

class IdGenerator implements IdGenerable{
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
