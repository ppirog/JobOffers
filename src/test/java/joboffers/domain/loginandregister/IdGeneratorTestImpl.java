package joboffers.domain.loginandregister;

class IdGeneratorTestImpl implements IdGenerable{
    @Override
    public String generate() {
        return "test-id";
    }
}
