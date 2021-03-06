package model.blockchain.service;

import model.blockchain.Block;
import model.crypto.Credential;
import model.message.Commit;
import model.message.Prepare;
import model.message.Request;
import repository.BlockRepository;
import repository.Repositories;
import repository.Repository;

import java.util.Base64;

public class BlockGenerator {
    public static final int REQUEST_THRESHOLD = 100;
    public static long start = 0;

    public static void findBlockAndAddBlock(Commit commit, Repositories repositories, Credential credential) {
        Block block = findBlock(commit, repositories);

        if(block == null) return;

        addBlock(block, repositories, credential);
        addRightHeightBlock(block, repositories, credential);
    }

    private static Block findBlock(Commit commit, Repositories repositories) {
        Block block = repositories.tempBlock().find(
                Base64.getEncoder().encodeToString(commit.getBlockHash()));

        if (block == null) {
            System.out.println("ERROR: blockInfo is not exist");
            return null;
        }

        if(repositories.block().getHighest() + 1 != block.getHeight()) {
            return null;
        }

        return block;
    }

    private static void addBlock(Block block, Repositories repositories, Credential credential) {
        repositories.block().add(block.getCurrentHash(), block);
        repositories.tempBlock().delete(block.getCurrentHash());

        for(Request request : block.getDatas()) {
            repositories.request().delete(Base64.getEncoder().encodeToString(request.getDigest()));
        }

        if(block.getHeight() == 99) { {
            System.out.println("dd");
        }}

        if(block.getHeight() == 100) {
            System.out.println(System.currentTimeMillis() - start + "ms");
        }
        System.out.println(credential.primaryNumber + " : block is generated with " + block.getHeight());
    }

    private static void addRightHeightBlock(Block currentBlock, Repositories repositories, Credential credential) {
        Block block = repositories.tempBlock().findBlockWithPreviousHash(currentBlock.getCurrentHash());
        if(block == null) return;
        addBlock(block, repositories, credential);
        addRightHeightBlock(block, repositories, credential);
    }
}
