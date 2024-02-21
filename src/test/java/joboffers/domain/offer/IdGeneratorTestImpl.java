package joboffers.domain.offer;

class IdGeneratorTestImpl implements IdGenerable {
    @Override
    public String generateId() {
        return "test-id";
    }
}
