package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityInteractEvent;
// CraftBukkit end

public class BlockRedstoneOre extends Block {

    public static final BlockStateBoolean a = BlockRedstoneTorch.LIT;

    public BlockRedstoneOre(Block.Info block_info) {
        super(block_info);
        this.o((IBlockData) this.getBlockData().set(BlockRedstoneOre.a, false));
    }

    @Override
    public int a(IBlockData iblockdata) {
        return (Boolean) iblockdata.get(BlockRedstoneOre.a) ? super.a(iblockdata) : 0;
    }

    @Override
    public void attack(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman) {
        interact(iblockdata, world, blockposition, entityhuman); // CraftBukkit - add entityhuman
        super.attack(iblockdata, world, blockposition, entityhuman);
    }

    @Override
    public void stepOn(World world, BlockPosition blockposition, Entity entity) {
        // CraftBukkit start
        // interact(world.getType(blockposition), world, blockposition);
        // super.stepOn(world, blockposition, entity);
        if (entity instanceof EntityHuman) {
            org.bukkit.event.player.PlayerInteractEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityHuman) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
            if (!event.isCancelled()) {
                interact(world.getType(blockposition), world, blockposition, entity); // add entity
                super.stepOn(world, blockposition, entity);
            }
        } else {
            EntityInteractEvent event = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
            world.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                interact(world.getType(blockposition), world, blockposition, entity); // add entity
                super.stepOn(world, blockposition, entity);
            }
        }
        // CraftBukkit end
    }

    @Override
    public boolean interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        interact(iblockdata, world, blockposition, entityhuman); // CraftBukkit - add entityhuman
        return super.interact(iblockdata, world, blockposition, entityhuman, enumhand, movingobjectpositionblock);
    }

    private static void interact(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) { // CraftBukkit - add Entity
        playEffect(world, blockposition);
        if (!(Boolean) iblockdata.get(BlockRedstoneOre.a)) {
            // CraftBukkit start
            if (CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition, iblockdata.set(BlockRedstoneOre.a, true)).isCancelled()) {
                return;
            }
            // CraftBukkit end
            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockRedstoneOre.a, true), 3);
        }

    }

    @Override
    public void tick(IBlockData iblockdata, World world, BlockPosition blockposition, Random random) {
        if ((Boolean) iblockdata.get(BlockRedstoneOre.a)) {
            // CraftBukkit start
            if (CraftEventFactory.callBlockFadeEvent(world, blockposition, iblockdata.set(BlockRedstoneOre.a, false)).isCancelled()) {
                return;
            }
            // CraftBukkit end
            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockRedstoneOre.a, false), 3);
        }

    }

    @Override
    public void dropNaturally(IBlockData iblockdata, World world, BlockPosition blockposition, ItemStack itemstack) {
        super.dropNaturally(iblockdata, world, blockposition, itemstack);
        /* CraftBukkit start - Delegated to getExpDrop
        if (EnchantmentManager.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemstack) == 0) {
            int i = 1 + world.random.nextInt(5);

            this.dropExperience(world, blockposition, i);
        }
        // */

    }

    @Override
    public int getExpDrop(IBlockData iblockdata, World world, BlockPosition blockposition, ItemStack itemstack) {
        if (EnchantmentManager.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemstack) == 0) {
            int i = 1 + world.random.nextInt(5);

            return i;
        }
        return 0;
        // CraftBukkit end
    }

    private static void playEffect(World world, BlockPosition blockposition) {
        double d0 = 0.5625D;
        Random random = world.random;
        EnumDirection[] aenumdirection = EnumDirection.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection[j];
            BlockPosition blockposition1 = blockposition.shift(enumdirection);

            if (!world.getType(blockposition1).g(world, blockposition1)) {
                EnumDirection.EnumAxis enumdirection_enumaxis = enumdirection.k();
                double d1 = enumdirection_enumaxis == EnumDirection.EnumAxis.X ? 0.5D + 0.5625D * (double) enumdirection.getAdjacentX() : (double) random.nextFloat();
                double d2 = enumdirection_enumaxis == EnumDirection.EnumAxis.Y ? 0.5D + 0.5625D * (double) enumdirection.getAdjacentY() : (double) random.nextFloat();
                double d3 = enumdirection_enumaxis == EnumDirection.EnumAxis.Z ? 0.5D + 0.5625D * (double) enumdirection.getAdjacentZ() : (double) random.nextFloat();

                world.addParticle(ParticleParamRedstone.a, (double) blockposition.getX() + d1, (double) blockposition.getY() + d2, (double) blockposition.getZ() + d3, 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockRedstoneOre.a);
    }
}
