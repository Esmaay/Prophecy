package me.esmaybe.prophecy.managers.wandmanager.tempblock;

import me.esmaybe.prophecy.VOGWands;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

public class TempBlock {

    public static Map<Block, TempBlock> instances = new ConcurrentHashMap<>();
    public static final PriorityQueue<TempBlock> REVERT_QUEUE = new PriorityQueue<>(100,
            (t1, t2) -> (int) (t1.revertTime - t2.revertTime));
    private final Block block;
    private Material newType;
    private BlockData newData;
    private BlockState state;
    private long revertTime;
    private boolean inRevertQueue;

    public TempBlock(Block block, Material newType, BlockData newData) {
        this.block = block;
        this.newData = newData;
        this.newType = newType;
        if (instances.containsKey(block)) {
            TempBlock temp = instances.get(block);
            if (newType != temp.newType) {
                temp.block.setType(newType);
                temp.newType = newType;
            }
            if (newData != temp.newData) {
                temp.block.setBlockData(newData);
                temp.newData = newData;
            }
            this.state = temp.state;
            instances.put(block, temp);
        } else {
            this.state = block.getState();
            instances.put(block, this);
            block.setType(newType);
            block.setBlockData(newData);
        }
        if (this.state.getType() == Material.FIRE) this.state.setType(Material.AIR);
    }

    public static TempBlock get(Block block) {
        if (isTempBlock(block)) {
            return instances.get(block);
        }
        return null;
    }

    public static boolean isTempBlock(Block block) {
        return block != null && instances.containsKey(block);
    }

    public static boolean isTouchingTempBlock(Block block) {
        BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
        for (BlockFace face : faces) {
            if (instances.containsKey(block.getRelative(face))) {
                return true;
            }
        }
        return false;
    }

    public static void removeAll() {
        for (Block block : instances.keySet()) {
            revertBlock(block, Material.AIR);
        }
        for (TempBlock tempblock : REVERT_QUEUE) {
            tempblock.revertBlock();
        }
    }

    public static void removeBlock(Block block) {
        instances.remove(block);
    }

    public static void revertBlock(Block block, Material defaultType) {
        if (instances.containsKey(block)) instances.get(block).revertBlock();
        else block.setType(defaultType);
    }

    public Block getBlock() {
        return this.block;
    }

    public Location getLocation() {
        return this.block.getLocation();
    }

    public BlockState getState() {
        return this.state;
    }

    public long getRevertTime() {
        return this.revertTime;
    }

    public void setRevertTime(long revertTime) {
        if (this.inRevertQueue) REVERT_QUEUE.remove(this);
        this.inRevertQueue = true;
        this.revertTime = (revertTime + System.currentTimeMillis());
        REVERT_QUEUE.add(this);
    }

    public void revertBlock() {
        this.state.update(true);
        instances.remove(this.block);
    }

    public void setState(BlockState newstate) {
        this.state = newstate;
    }

    public void setType(Material material) {
        setType(material, this.newData);
    }

    public void setType(Material material, BlockData data) {
        this.newType = material;
        this.newData = data;
        this.block.setType(material);
        this.block.setBlockData(data);
    }

    public static void startReversion() {
        new BukkitRunnable() {
            public void run() {
                long currentTime = System.currentTimeMillis();
                while (!TempBlock.REVERT_QUEUE.isEmpty()) {
                    TempBlock tempBlock = TempBlock.REVERT_QUEUE.peek();
                    if (currentTime < tempBlock.revertTime) break;
                    TempBlock.REVERT_QUEUE.poll();
                    tempBlock.revertBlock();
                }
            }
        }.runTaskTimer(VOGWands.getInstance(), 0L, 1L);
    }

}
