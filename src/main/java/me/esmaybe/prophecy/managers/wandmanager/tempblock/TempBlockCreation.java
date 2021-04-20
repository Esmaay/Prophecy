package me.esmaybe.prophecy.managers.wandmanager.tempblock;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

import java.util.concurrent.ConcurrentHashMap;

public class TempBlockCreation {

    public static ConcurrentHashMap<Block, Long> blocks = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Block, TempBlock> temps = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Block, BlockState> states = new ConcurrentHashMap<>();

    public TempBlockCreation(Block block, Material material, BlockData data, long delay) {
        this(block, material, data, delay, true);
    }

    public TempBlockCreation(Block block, Material material, BlockData data, long delay, boolean temp) {
        if (blocks.containsKey(block)) {
            blocks.replace(block, System.currentTimeMillis() + delay);
            block.setType(material);
            block.setBlockData(data);
        } else {
            blocks.put(block, System.currentTimeMillis() + delay);
            if (TempBlock.isTempBlock(block)) TempBlock.get(block).revertBlock();
            if (temp) {
                TempBlock tb = new TempBlock(block, material, data);
                temps.put(block, tb);
            } else {
                states.put(block, block.getState());
                if (material != null) {
                    block.setType(material);
                    block.setBlockData(data);
                }
            }
        }
    }

    public static void manage() {
        for (Block b : blocks.keySet())
            if (System.currentTimeMillis() >= blocks.get(b)) {
                if (temps.containsKey(b)) {
                    TempBlock tb = temps.get(b);
                    tb.revertBlock();
                    temps.remove(b);
                }
                if (states.containsKey(b)) {
                    BlockState bs = states.get(b);
                    bs.update(true);
                    states.remove(b);
                }
                blocks.remove(b);
            }
    }

    public static void revert(Block block) {
        if (blocks.containsKey(block)) {
            if ((TempBlock.isTempBlock(block)) && (temps.containsKey(block))) {
                TempBlock tb = TempBlock.get(block);
                tb.revertBlock();
                temps.remove(block);
            }
            if (states.containsKey(block)) {
                states.get(block).update(true);
                states.remove(block);
            }
            blocks.remove(block);
        }
    }

    public static void revertAll() {
        for (Block b : blocks.keySet()) {
            if (temps.containsKey(b)) {
                TempBlock tb = temps.get(b);
                tb.revertBlock();
            }
            if (states.containsKey(b)) states.get(b).update(true);
        }
        temps.clear();
        states.clear();
        blocks.clear();
    }

    public static boolean hasBlock(Block block) {
        if (blocks.containsKey(block)) return true;
        return false;
    }

    public static boolean isTempBlock(Block block) {
        if (temps.containsKey(block)) return true;
        return false;
    }

    public static boolean isBlockState(Block block) {
        if (states.containsKey(block)) return true;
        return false;
    }

}
