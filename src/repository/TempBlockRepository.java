package repository;

import model.blockchain.Block;

import java.util.Objects;

public class TempBlockRepository extends Repository<Block> {
    public Block findBlockWithPreviousHash(String blockHash) {
        return repository.values().stream().filter(block -> Objects.equals(block.getPreviousHash(), blockHash)).findAny().orElse(null);
    }
}
