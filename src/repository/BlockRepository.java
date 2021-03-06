package repository;

import model.blockchain.Block;

public class BlockRepository extends Repository<Block> {
    String top = "";
    long highest;

    @Override
    public void add(String name, Block value) {
        if (repository.containsKey(name)) {
            return;
        }
        top = name;
        highest++;
        repository.put(name, value);
    }

    public String getTop() {
        return top;
    }

    public long getHighest() {
        return highest;
    }
}
